package com.dotoyo.ims.dsform.allin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Tar;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.util.StringsUtils;

/**
 * 使用ZIP作打包工具
 * 
 * @author xieshh
 * 
 */
public class ZipUtils {
	private static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(ZipUtils.class));

	private ZipUtils() {

	}

	public static void compress(String srcPathName, String descPathName)
			throws Exception {
		File srcdir = new File(srcPathName);
		if (!srcdir.exists())
			throw new Exception(srcPathName + "不存在！");
		File zipFile = new File(descPathName);

		if (zipFile.exists()) {
			zipFile.delete();
		}
		Project prj = new Project();
		Zip zip = new Zip();
		zip.setProject(prj);
		zip.setDestFile(zipFile);
		FileSet fileSet = new FileSet();
		fileSet.setExcludes("*.java");
		fileSet.setProject(prj);
		fileSet.setDir(srcdir);
		zip.addFileset(fileSet);

		zip.execute();
	}

	public static void compress(String srcPathName, String descPathName,List<String> fileter)
			throws Exception {
		File srcdir = new File(srcPathName);
		if (!srcdir.exists())
			throw new Exception(srcPathName + "不存在！");
		File zipFile = new File(descPathName);

		if (zipFile.exists()) {
			zipFile.delete();
		}
		Project prj = new Project();
		Zip zip = new Zip();
		zip.setProject(prj);
		zip.setDestFile(zipFile);
		FileSet fileSet = new FileSet();
		for(int i=0;i<fileter.size();i++){
			fileSet.setExcludes("*."+fileter.get(i));	
		}
		fileSet.setProject(prj);
		fileSet.setDir(srcdir);
		zip.addFileset(fileSet);

		zip.execute();
	}

	public static void tar(String srcPathName, String descPathName)
			throws Exception {
		File srcdir = new File(srcPathName);
		if (!srcdir.exists())
			throw new Exception(srcPathName + "不存在！");
		File zipFile = new File(descPathName);
		if (zipFile.exists()) {
			zipFile.delete();
		}

		Project prj = new Project();
		Tar tar = new Tar();
		tar.setProject(prj);
		tar.setDestFile(zipFile);
		tar.setBasedir(srcdir);
		tar.execute();
	}

	/**
	 * 把JAR包里的指定文件解压到指定目录下
	 * 
	 * @param dirPath
	 * @return
	 * @throws IOException
	 */
	public static void decompressionWarFile(String fullJarFilePath,
			String srcPath, String descPath) {
		if (!descPath.endsWith(File.separator)) {

			descPath += File.separator;

		}

		JarFile jarFile = null;
		try {
			jarFile = new JarFile(fullJarFilePath);
			JarEntry myJarEntry = jarFile.getJarEntry(srcPath);
			String curFileFullPath = myJarEntry.getName();
			if (curFileFullPath.startsWith(srcPath)) {
				if (!myJarEntry.isDirectory()) {

					String descFullFilePath = descPath + curFileFullPath;
					log.debug("正在解压：" + descFullFilePath);
					File file = new File(descPath + curFileFullPath);
					file = file.getParentFile();
					if (file != null && !file.exists()) {
						file.mkdirs();
					}
					InputStream in = null;
					FileOutputStream fout = null;
					try {
						in = jarFile.getInputStream(myJarEntry);
						fout = new FileOutputStream(descFullFilePath, true);
						int i = 0;
						while ((i = in.read()) != -1) {
							fout.write(i);
						}
						fout.flush();
					} finally {
						if (in != null) {
							try {
								in.close();
							} catch (Throwable e) {
							}
						}
						if (fout != null) {
							try {
								fout.close();
							} catch (Throwable e) {
							}
						}

					}

				}
			}
			// }
		} catch (Throwable e) {
			log.error("", e);
		} finally {

			if (jarFile != null) {
				try {
					jarFile.close();
				} catch (Throwable e) {

				}
			}

		}
	}

	/**
	 * 把指定的路径打成WAR包
	 * 
	 * @param parentPath
	 * @param warPath
	 * @param main
	 * @throws Exception
	 */
	public static void buildWar(String parentPath, String warPath, String main)
			throws Exception {
		File file1 = new File(warPath);

		FileOutputStream fos = null;
		InputStream is = null;
		try {
			fos = new FileOutputStream(file1);
			is = new FileInputStream(main);
			JarOutputStream jos = null;

			try {

				Attributes attrs = null;
				java.util.jar.Manifest fest = new java.util.jar.Manifest(is);
				attrs = fest.getMainAttributes();
				attrs.putValue("Created-By", "dotoyo");
				//
				jos = new JarOutputStream(fos, fest);

				buildWar(parentPath, parentPath, jos);

				jos.finish();
			} finally {

				if (jos != null) {

					try {
						jos.close();
					} catch (Exception e) {
					}
				}
			}

		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Throwable e) {
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (Throwable e) {
				}
			}
		}
	}

	protected static void buildWar(String subPath, String parentPath,
			JarOutputStream jos) throws Exception {
		File srcFile = new File(parentPath);
		if (!srcFile.exists())
			return;
		File files[] = srcFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			String filePath = (new StringBuilder()).append(parentPath).append(
					"/").append(files[i].getName()).toString();
			if (file.isDirectory()) {

				buildWar(subPath, filePath, jos);
				continue;
			} else {

				newJar(subPath, file, jos);

			}
		}

	}

	protected static void newJar(String subPath, File file, JarOutputStream jos)
			throws Exception {

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);

			JarEntry warEntry = null;
			//

			String fileName = StringsUtils.replaceAll(file.getPath(),
					File.separator, "/");

			//
			fileName = fileName.substring(subPath.length() + 1, fileName
					.length());
			warEntry = new JarEntry(fileName);

			jos.putNextEntry(warEntry);
			byte bytes[] = new byte[1024 * 2];
			int len = 0;
			while ((len = fis.read(bytes)) >= 0) {
				jos.write(bytes, 0, len);

			}

		} finally {
			if (jos != null) {
				jos.closeEntry();
			}
			if (fis != null) {
				fis.close();
			}
		}
	}

	public static void main1(String[] args) throws Exception {
		// String srcPathName = "G:\\doc";
		// String descPathName = "G:\\doc.zip";
		// ZipUtils.compress(srcPathName, descPathName);

	}

}
