package com.dotoyo.ims.dsform.allin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.util.StringsUtils;

public class FileUtils {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FileUtils.class));

	private FileUtils() {

	}

	/**
	 * 全拷贝
	 * 
	 * @param srcPath
	 * @param descPath
	 */
	public static void copy(String srcPath, String destPath) throws Exception {
		File srcFile = new File(srcPath);
		if (!srcFile.exists()) {
			return;
		}

		if (srcFile.isDirectory()) {
			File destFile = new File(destPath);
			if (!destFile.exists()) {

				destFile.mkdirs();

			}
			copyDirectiory(srcPath, destPath);

		} else if (srcFile.isFile()) {
			File destFile = new File(destPath);
			if (!destFile.exists()) {
				File pFile = destFile.getParentFile();
				if (!destFile.exists()) {
					pFile.mkdirs();
				}

			}
			copyFile(srcFile, destFile);
		}

	}

	private static void copyDirectiory(String srcPath, String destPath)
			throws Exception {
		File srcFile = new File(srcPath);
		if (!srcFile.exists()) {
			return;
		}
		File destFile = new File(destPath);
		if (!destFile.exists()) {
			destFile.mkdirs();
		}
		File[] files = srcFile.listFiles();

		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.isDirectory()) {
				copyDirectiory(srcPath + File.separator + file.getName(),
						destPath + File.separator + file.getName());
				continue;
			}
			File newFile = new File(destPath + File.separator + file.getName());
			if (newFile.exists()) {
				continue;
			}
			log.debug("copy " + srcPath + File.separator + file.getName()
					+ " to " + newFile.getName());
			copyFile(file, newFile);
		}

	}

	private static void copyFile(File srcFile, File destFile) throws Exception {
		if (!srcFile.exists()) {
			return;
		}
		if (srcFile.getAbsolutePath().equals(destFile.getAbsolutePath())) {
			return;
		}
		if (!destFile.exists()) {
			File pFile = destFile.getParentFile();
			if (!pFile.exists()) {
				pFile.mkdirs();
			}
		} else {
			destFile.delete();
		}

		FileInputStream is = null;
		BufferedInputStream bis = null;
		FileOutputStream os = null;
		BufferedOutputStream bos = null;
		try {
			is = new FileInputStream(srcFile);
			bis = new BufferedInputStream(is);
			os = new FileOutputStream(destFile);
			bos = new BufferedOutputStream(os);
			byte[] b = new byte[1024 * 1];
			int len;
			while ((len = bis.read(b)) != -1) {
				bos.write(b, 0, len);
			}
			bos.flush();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (Throwable e) {

				}
				bis = null;
			}
			if (is != null) {
				try {
					is.close();
				} catch (Throwable e) {

				}
				is = null;
			}
			if (bos != null) {

				try {
					bos.close();
				} catch (Throwable e) {

				}
				bos = null;
			}
			if (os != null) {
				try {
					os.close();
				} catch (Throwable e) {

				}
				os = null;
			}

		}

	}

	/**
	 * 全拷贝
	 * 
	 * @param srcPath
	 * @param descPath
	 */
	public static void copy(String srcPath, String destPath,
			Map<String, String> filter) throws Exception {
		File srcFile = new File(srcPath);
		if (!srcFile.exists()) {
			return;
		}

		if (srcFile.isDirectory()) {
			File destFile = new File(destPath);
			if (!destFile.exists()) {

				destFile.mkdirs();

			}
			copyDirectiory(srcPath, destPath, filter);

		} else if (srcFile.isFile()) {
			File destFile = new File(destPath);
			if (!destFile.exists()) {
				File pFile = destFile.getParentFile();
				if (!destFile.exists()) {
					pFile.mkdirs();
				}

			}
			String subName = StringsUtils
					.getFileSubNameFromFullFileName(destFile.getName());
			if (!filter.containsKey(subName)) {
				copyFile(srcFile, destFile, "utf-8");
			}

		}

	}

	private static void copyDirectiory(String srcPath, String destPath,
			Map<String, String> filter) throws Exception {
		File srcFile = new File(srcPath);
		if (!srcFile.exists()) {
			return;
		}
		File destFile = new File(destPath);
		if (!destFile.exists()) {
			destFile.mkdirs();
		}
		File[] files = srcFile.listFiles();

		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.isDirectory()) {
				copyDirectiory(srcPath + File.separator + file.getName(),
						destPath + File.separator + file.getName(), filter);
				continue;
			}
			File newFile = new File(destPath + File.separator + file.getName());
			if (newFile.exists()) {
				continue;
			}
			String subName = StringsUtils
					.getFileSubNameFromFullFileName(destFile.getName());
			if (!filter.containsKey(subName)) {
				log.debug("copy " + srcPath + File.separator + file.getName()
						+ " to " + newFile.getName());
				copyFile(file, newFile, "utf-8");
			}

		}

	}

	private static void copyFile(File srcFile, File destFile, String encode)
			throws Exception {
		if (!srcFile.exists()) {
			return;
		}
		if (!destFile.exists()) {
			File pFile = destFile.getParentFile();
			if (!pFile.exists()) {
				pFile.mkdirs();
			}
		} else {
			destFile.delete();
		}
		FileReader is = null;
		BufferedReader bis = null;
		FileOutputStream os = null;
		BufferedOutputStream bos = null;
		try {
			is = new FileReader(srcFile);
			bis = new BufferedReader(is);
			os = new FileOutputStream(destFile);
			bos = new BufferedOutputStream(os);
			String line = "";
			StringBuilder sb = new StringBuilder();
			while (line != null) {
				// line=new String(line.getBytes(),"gbk");
				// bos.write(line);
				// bos.write("\n");
				sb.append(line);
				sb.append("\n");
				line = bis.readLine();
				log.debug(line);
			}
			String str = sb.toString();
			byte b[] = str.getBytes(encode);
			bos.write(b);
			bos.flush();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (Throwable e) {

				}
				bis = null;
			}
			if (is != null) {
				try {
					is.close();
				} catch (Throwable e) {

				}
				is = null;
			}
			if (bos != null) {

				try {
					bos.close();
				} catch (Throwable e) {

				}
				bos = null;
			}
			if (os != null) {
				try {
					os.close();
				} catch (Throwable e) {

				}
				os = null;
			}

		}

	}

	/**
	 * 删除文件
	 * 
	 * @param filePath
	 * @throws Exception
	 */
	public static void deleteFile(String filePath) throws Exception {
		File srcFile = new File(filePath);
		if (!srcFile.exists()) {
			return;
		}
		srcFile.delete();
	}

	/**
	 * 删除文件
	 * 
	 * @param filePath
	 * @throws Exception
	 */
	public static void renameFile(String srcPath, String destPath)
			throws Exception {
		File srcFile = new File(srcPath);
		if (!srcFile.exists()) {
			return;
		}
		File destFile = new File(destPath);
		if (!destFile.exists()) {
			File pFile = destFile.getParentFile();
			if (!pFile.exists()) {
				pFile.mkdirs();
			}
		} else {
			destFile.delete();
		}
		srcFile.renameTo(destFile);
	}

	/**
	 * 删除所有文件，包括子目录
	 * 
	 * @param folderPath
	 * @throws IOException
	 */
	public static void deleteFolder(String folderPath) throws Exception {
		File srcFile = new File(folderPath);
		if (!srcFile.exists()) {
			return;
		}
		File[] file = srcFile.listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isDirectory()) {
				deleteFolder(folderPath + File.separator + file[i].getName());
				file[i].delete();
			} else {
				file[i].delete();
			}

		}
	}

	/**
	 * 删除目录下指定的文件
	 * 
	 * @param folderPath
	 * @throws IOException
	 */
	public static void deleteFile(String srcPath, List<String> fileList) {
		File srcFile = new File(srcPath);
		if (!srcFile.exists()) {
			return;
		}
		File[] file = srcFile.listFiles();
		for (int i = 0; i < file.length; i++) {
			String fileName = file[i].getName();
			if (file[i].isDirectory()) {
				deleteFile(srcPath + File.separator + file[i].getName(),
						fileList);
			} else {
				for (String fName : fileList) {
					if (fileName.equals(fName)) {
						file[i].delete();
					}
				}
			}
		}
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main1(String[] args) throws Exception {
		String destPath = "g:\\temp";
		String srcPath = "G:\\dotoyo\\btplugin\\src";
		FileUtils.copyDirectiory(srcPath, destPath);
	}

}
