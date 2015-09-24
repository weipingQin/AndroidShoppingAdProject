package com.www.goumei.fragment;

import java.io.File;
import java.util.Observer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.www.goumei.utils.PathComm;

public class CropImageInterface {
	public static final int CROP = 109;
	private static String sys_img_name;

	public static void cropImg(Activity activity, String imgFilePath) {
		sys_img_name = System.currentTimeMillis() + ".jpg";
		Intent intent = new Intent();
		intent.setAction("com.android.camera.action.CROP");
		intent.putExtra("crop", "true");

		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", 600);
		intent.putExtra("outputY", 600);

		intent.setDataAndType(Uri.fromFile(new File(imgFilePath)), "image/*");
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(PathComm.picPath + sys_img_name)));
		activity.startActivityForResult(intent, CROP);
	}

	public static void onActivityResult(int requestCode, int resultCode,
			Intent data, Observer observer) {
		// 发表完成后主页刷新
		if (resultCode == Activity.RESULT_OK && CROP == requestCode) {
			File cropFile = new File(PathComm.picPath + sys_img_name);
			boolean exist = cropFile.exists();
			long length = cropFile.length();
			if (exist && length > 0) {
				observer.update(null, cropFile.getAbsolutePath());
			}
		}
	}
}
