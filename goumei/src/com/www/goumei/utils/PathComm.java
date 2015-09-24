package com.www.goumei.utils;

import java.io.File;

import android.os.Environment;

public class PathComm {
	// app应用的文件路径
	public final static String appFolderPath = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ "Goumei";
	// 应用的图片路径
	public final static String picPath = appFolderPath + File.separator
			+ "Images" + File.separator;

	
}
