package com.dotoyo.ims.dsform.allin;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * 为了保证查询速度以及性能考虑,索引不能太大,所以采用根据机构id或公司id划分索引目录.
 * ConcurrentHashMap中key为机构id或公司id.
 * 
 * 为了保证查询结果的实时更新,所以使用两个索引目录.
 * delayIndexWirter,在每天的凌晨3点更新,并把索引2全部删除.(这个IndexReader不用关闭,每次用一个,提高性能)
 * hurryIndexWirter,在数据库每添加或更新数据后,重新创建索引.(这个indexReader必须每次重新打开,达到时时更新的目的)
 * 
 * 查询的结果,就是两个索引的结果合并.
 */

/*  索引管理类
 *  主要实现的功能:优化重量级的indexWriter indexReader,可以实时索引,根据业务切割索引,释放资源,判断机构权限,高亮,优化排序
 *  
 */
public class IndexManager {
	private static IndexManager instance = null;
	
	//延迟的indexWirter,key为机构id或公司id
	private Map<String,IndexWriter> delayIndexWriterMap = new ConcurrentHashMap<String,IndexWriter>();
	//刷新的indexWirter,key为机构id或公司id
	private Map<String,IndexWriter> hurryIndexWriterMap = new ConcurrentHashMap<String,IndexWriter>();
	private Map<String,Directory> delayWriterDirectoryMap = new ConcurrentHashMap<String,Directory>();
	private Map<String,Directory> hurryWriterDirectoryMap = new ConcurrentHashMap<String,Directory>();
	
	private Map<String,IndexReader> delayIndexReaderMap = new ConcurrentHashMap<String,IndexReader>();
	private Map<String,IndexReader> hurryIndexReaderMap = new ConcurrentHashMap<String,IndexReader>();
	private Map<String,IndexSearcher> delayIndexSearcherMap = new ConcurrentHashMap<String,IndexSearcher>();
	private Map<String,IndexSearcher> hurryIndexSearcherMap = new ConcurrentHashMap<String,IndexSearcher>();
	private Map<String,Directory> delayReaderDirectoryMap = new ConcurrentHashMap<String,Directory>();
	private Map<String,Directory> hurryReaderDirectoryMap = new ConcurrentHashMap<String,Directory>();
	

	
	private IndexManager(){
		
	}
	
	public static IndexManager getInstance(){
		if(instance == null){
			newInstance();
		}
		return instance;
	}
	
	private synchronized  static void newInstance() {
		if (instance == null) {
			instance = new IndexManager();
		}
	}
	
	public String getDelayIndexDir(String orgId) throws Exception{
		return IndexConfig.getInstance().getConfig(IndexConfig.INDEX_DIR1) + File.separator + orgId;
	}
	
	public String getHurryIndexDir(String orgId) throws Exception{
		return IndexConfig.getInstance().getConfig(IndexConfig.INDEX_DIR2)+ File.separator + orgId;
	}
	
	//(rv)
	public IndexWriter getDelayIndexWriter(String orgId) throws Exception{
		IndexWriter indexWriter = delayIndexWriterMap.get(orgId);
		if(indexWriter == null){
			indexWriter = newDelayIndexWriter(orgId);
		}
        return indexWriter; 
	}
	
	//(rv)
	public IndexWriter getHurryIndexWriter(String orgId) throws Exception{
		IndexWriter indexWriter = hurryIndexWriterMap.get(orgId);
		if(indexWriter == null){
			indexWriter = newHurryIndexWriter(orgId);
		}
        return indexWriter; 
	}
	
	//延迟查询(rv)
	public IndexSearcher getDelayIndexSearch(String orgId) throws Exception{
		IndexSearcher indexSearcher = delayIndexSearcherMap.get(orgId);
		if(indexSearcher == null){
			File file = new File(getDelayIndexDir(orgId));
			if(!file.exists()){
				return null;
			}
			Directory writerDir = FSDirectory.open(file);
			delayReaderDirectoryMap.put(orgId, writerDir);
			IndexReader indexReader = DirectoryReader.open(writerDir);
			delayIndexReaderMap.put(orgId, indexReader);
			indexSearcher = new IndexSearcher(indexReader);
			delayIndexSearcherMap.put(orgId, indexSearcher);
		}
        return indexSearcher; 
	}
	
	/*
	 * 快速刷新查询(rv)
	 */
	public IndexSearcher getHurryIndexSearch(String orgId) throws Exception{
		IndexSearcher indexSearcher = hurryIndexSearcherMap.get(orgId);
		//第一次为空
		if(indexSearcher == null){
			File file = new File(getHurryIndexDir(orgId));
			if(!file.exists()){
				return null;
			}
			Directory readDir = FSDirectory.open(new File(getHurryIndexDir(orgId)));
			hurryReaderDirectoryMap.put(orgId, readDir);
			IndexReader indexReader = DirectoryReader.open(readDir);
			hurryIndexReaderMap.put(orgId, indexReader);
			indexSearcher = new IndexSearcher(indexReader);
			hurryIndexSearcherMap.put(orgId, indexSearcher);
			return indexSearcher;
		}
		
		//第二次以及以后每次都要判断IndexReader是否有更新
		Directory writerDir = FSDirectory.open(new File(getHurryIndexDir(orgId)));
		IndexReader indexReader = DirectoryReader.open(writerDir);
		//判断是否有更新
		IndexReader newReader = DirectoryReader.openIfChanged((DirectoryReader)indexReader);  // 读入新增加的增量索引内容，满足实时索引需求
        if (newReader != null) {
        	indexReader.close();//这个需要关闭
        	hurryIndexReaderMap.put(orgId, newReader);
        	indexSearcher = new IndexSearcher(indexReader);
        	hurryIndexSearcherMap.put(orgId, indexSearcher);
        }
        return indexSearcher; 
	}
	
	//每年凌晨3点需要执行的合并索引
	public void mergeIndex() throws Exception{
		for(Entry<String, IndexWriter> entry : delayIndexWriterMap.entrySet()){
			String orgId = entry.getKey();
			IndexWriter writer = entry.getValue();
			
			if(delayReaderDirectoryMap.get(orgId) != null){
				Directory dir =  delayReaderDirectoryMap.get(orgId);
				writer.addIndexes(new Directory[]{dir});
				writer.commit();
			}
		}
	}
	
	//每天凌晨3点合并索引结束后调用(rv)
	public void flushDelayIndexSearch() throws Exception{
		for(Entry<String,IndexReader> entry : delayIndexReaderMap.entrySet()){
			String orgId = entry.getKey();
			IndexReader oldIndexReader = entry.getValue();
			
			//判断是否有改变
			IndexReader newReader = DirectoryReader.openIfChanged((DirectoryReader)oldIndexReader);
			if(newReader != null){
				delayIndexReaderMap.put(orgId, newReader);
				IndexSearcher indexSearcher = new IndexSearcher(oldIndexReader);
				delayIndexSearcherMap.put(orgId, indexSearcher);
				if(oldIndexReader != null){
					oldIndexReader.close();
				}
			}
		}
	}
	
	//每天凌晨3点合并索引结束后清空历史数据
	public void cleanHurryIndex() throws Exception{
		for(Entry<String, IndexWriter> entry : hurryIndexWriterMap.entrySet()){
			IndexWriter writer = entry.getValue();
			writer.deleteAll();
			writer.close();
		}
		
		for(Entry<String,IndexReader> entry : hurryIndexReaderMap.entrySet()){
			IndexReader reader = entry.getValue();
			reader.close();
		}
		
		for(Entry<String,Directory> entry : hurryWriterDirectoryMap.entrySet()){
			Directory dir = entry.getValue();
			dir.close();
		}
		
		for(Entry<String,Directory> entry : hurryReaderDirectoryMap.entrySet()){
			Directory dir = entry.getValue();
			dir.close();
		}
		hurryIndexWriterMap.clear();
		hurryIndexReaderMap.clear();
		hurryWriterDirectoryMap.clear();
		hurryReaderDirectoryMap.clear();
		hurryIndexSearcherMap.clear();
		
	}
	
	//每天凌晨3点更新(rv)
	private synchronized IndexWriter newDelayIndexWriter(String orgId) throws Exception {
		IndexWriter indexWriter = delayIndexWriterMap.get(orgId);
		if (indexWriter == null) {
			//指明要索引文件夹的位置
			File fileDir = new  File(getDelayIndexDir(orgId));   
	        if(!fileDir.exists()){
	        	fileDir.mkdirs();
	        }
	        Directory writerDire = FSDirectory.open(fileDir);  
	        delayWriterDirectoryMap.put(orgId, writerDire);
	        
	        //IKAnalyzer 智能切分
	        Analyzer analyzer = new IKAnalyzer(true);   
	        //引入了IndexWriterConfig对象，封装了早期版本的一大堆参数  
	        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, analyzer);  
	        indexWriter = new IndexWriter(writerDire, config);
	        delayIndexWriterMap.put(orgId, indexWriter);
		}
		return indexWriter;
	}
	
	//实时更新索引(rv)
	private synchronized IndexWriter newHurryIndexWriter(String orgId) throws Exception {
		IndexWriter indexWriter = hurryIndexWriterMap.get(orgId);
		if (indexWriter == null) {
			//指明要索引文件夹的位置
			File fileDir = new  File(getHurryIndexDir(orgId));   
	        if(!fileDir.exists()){
	        	fileDir.mkdirs();
	        }
	        Directory writerDire = FSDirectory.open(fileDir);
	        hurryWriterDirectoryMap.put(orgId, writerDire);
	        
	        //IKAnalyzer 智能切分
	        Analyzer analyzer = new IKAnalyzer(true);   
	        //引入了IndexWriterConfig对象，封装了早期版本的一大堆参数  
	        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, analyzer);  
	        indexWriter = new IndexWriter(writerDire, config);
	        hurryIndexWriterMap.put(orgId, indexWriter);
		}
		return indexWriter;
	}

	//web容器关闭时,需要关闭(rv)
	public void closeResource() throws Exception{
		for(Entry<String,IndexWriter> entry : delayIndexWriterMap.entrySet()){
			IndexWriter indexWriter = entry.getValue();
			if(indexWriter != null){
				indexWriter.close();
			}
		}
		
		for(Entry<String,IndexWriter> entry : hurryIndexWriterMap.entrySet()){
			IndexWriter indexWriter = entry.getValue();
			if(indexWriter != null){
				indexWriter.close();
			}
		}
		
		for(Entry<String,IndexReader> entry : delayIndexReaderMap.entrySet()){
			IndexReader indexReader = entry.getValue();
			if(indexReader != null){
				indexReader.close();
			}
		}
		
		for(Entry<String,IndexReader> entry : hurryIndexReaderMap.entrySet()){
			IndexReader indexReader = entry.getValue();
			if(indexReader != null){
				indexReader.close();
			}
		}
		
		for(Entry<String,Directory> entry : delayReaderDirectoryMap.entrySet()){
			Directory dir = entry.getValue();
			if(dir != null){
				dir.close();
			}
		}
		
		for(Entry<String,Directory> entry : hurryReaderDirectoryMap.entrySet()){
			Directory dir = entry.getValue();
			if(dir != null){
				dir.close();
			}
		}
		
		for(Entry<String,Directory> entry : delayWriterDirectoryMap.entrySet()){
			Directory dir = entry.getValue();
			if(dir != null){
				dir.close();
			}
		}
		
		for(Entry<String,Directory> entry : hurryWriterDirectoryMap.entrySet()){
			Directory dir = entry.getValue();
			if(dir != null){
				dir.close();
			}
		}
	}
}
