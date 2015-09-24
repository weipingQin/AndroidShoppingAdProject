package com.www.goumei.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class FileUtil {
	 // 获取扩展存储设备的文件目录
	public static final String SDFile = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/";
	public static final String plugInstallPath = SDFile + "plug/installplug/";
	public static final String jingpin_plugInstallPath = SDFile + "jingpin/"+"plug/installplug/";

	 private final String SD_PATH = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
	    File file;
	    public static String sDStateString = android.os.Environment.getExternalStorageState();
	   
	   // File SDFile = android.os.Environment.getExternalStorageDirectory();
	/**
	 * 在SD卡上创建文件
	 * 
	 * @throws IOException
	 */
	public File creatSDFile(String fileName) {
		File file = new File(fileName);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirName
	 */
	public File creatSDDir(String dirName) {
		File dir = new File(dirName);
		if(!dir.exists()){
			dir.mkdirs();
			
		}
		return dir;
	}

	/**
	 * 判断SD卡上的文件是否存在
	 */
	public boolean isFileExist(String fileName) {
		File file = new File(SDFile + fileName);
		return file.exists();
	}

	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 * 
	 * @param path
	 *            存储位置
	 * @param fileName
	 *            文件名
	 * @param InputStream
	 *            输出流
	 */
	public File writeToSDfromInput(String path, String fileName,
			InputStream inputStream) {
		// createSDDir(path);
		File file = new File(path + fileName);
		OutputStream outStream = null;
		try {
			outStream = new FileOutputStream(file);
			byte[] buffer = new byte[4 * 1024];
			int length;
			while ((length = (inputStream.read(buffer))) > 0) {
				outStream.write(buffer, 0, length);
			}
			outStream.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 读取文本文件到字符串
	 * 
	 * @param filename
	 * @return
	 */
	public static String fileRead(String filename) {
		String fileName = filename;
		// 也可以用String fileName = "mnt/sdcard/Y.txt";
		String res = "";
		try {
			FileInputStream fin = new FileInputStream(fileName);

			InputStreamReader inputStreamReader = new InputStreamReader(fin,
					"UTF-8");
			BufferedReader in = new BufferedReader(inputStreamReader);
			res = getStrings(in);
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	private static String getStrings(BufferedReader in) {
		BufferedReader reader = new BufferedReader(in);
		StringBuffer sb = new StringBuffer("");
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				byte[] stringbom = line.getBytes();
				if (stringbom.length > 2) {
					if (-17 == stringbom[0] && -69 == stringbom[1]
							&& -65 == stringbom[2]) {
						line = new String(stringbom, 3, stringbom.length - 3,
								"UTF-8");
					} else {
						line = new String(stringbom, "UTF-8");
					}
				}
				sb.append(line);
				Log.v("str", "-" + line.toString());
				sb.append("\n");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return sb.toString();
	}

	/**
	 * 删除文件目录下所有文件
	 */
	public static void DeleteFile() {
		/*
		 * String FilePath = Environment.getExternalStorageDirectory()
		 * .getAbsolutePath() + "/plug/installplug/";
		 */
		// 得到该路径文件夹下所有的文件
		File mfile = new File(plugInstallPath);
		File[] files = mfile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 逐条删除
			files[i].delete();
		}
	}

	/**
	 * 判断文件类型
	 *//*
	public static int FileType() {
		// 得到该路径文件夹下所有的文件
		File mfile = new File(plugInstallPath);
		File[] files = mfile.listFiles();
		for (int i = 0; i < files.length; i++) {

		}
		return 0;
	}*/

	/**
	 * 获取文件后缀名
	 */
	public static String getFileSuffix(String fName) {
		// 获取扩展名
		String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
				fName.length()).toLowerCase();
		return FileEnd;
	}

	/**
	 * 安装插件
	 *//*
	public static void InstallPlug(String plugName, Context context) {
		// 1.验证是否存在plug.html、xml、图片
		// 2.读取xml文件
		// 3.将信息写入数据库
		// 4.删除 zip 文件
		// 拷贝一个文件
		String plugPath = SDFile + "/plug/" + plugName;
		// 插件安装后的目录

		String pluginstallPath = plugInstallPath + plugName + ".zip";
		try {
			// 解压缩文件到
			// ZipUtil.UnZipFolder(pluginstallPath, plugPath);
			ZipUtil.upZipFile(new File(pluginstallPath), plugPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 看有没有配置文件 和 索引文件
		int filenum = 0;
		if (new FileUtils().isFileExist(plugPath + "/" + "info.xml")) {
			filenum++;
		}

		if (new FileUtils().isFileExist(plugPath + "/" + "plug.html")) {
			filenum++;
		}
		DOMParse(context, plugPath + "/info.xml");
		
	}*/

	/**
	 * 复制文件
	 * 
	 * @param FilePath
	 * @param newFilePath
	 */
	public static void copyTo(String FilePath, String newFilePath) {
		File file = new File(FilePath);
		File newfile = new File(newFilePath);
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(file);
			fo = new FileOutputStream(newfile);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (Exception e) {
			Log.e("TAG", "文件复制失败!");
			e.printStackTrace();
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	/** 
     * 获得指定文件的byte数组 
     */  
    public static byte[] getBytes(String filePath){  
        byte[] buffer = null;  
        try {  
            File file = new File(filePath);  
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
            byte[] b = new byte[1000];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return buffer;  
    }  

	
}
