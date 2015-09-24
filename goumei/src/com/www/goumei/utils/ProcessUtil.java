package com.www.goumei.utils;



import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Window;

public class ProcessUtil {
	
	private static ProgressDialog mProgressDialog = null;
	
	private static boolean isShow;
	public static ProgressDialog showProgressDialog(Activity context, String info) {
		
		isShow=true;
		if (mProgressDialog == null) {
			ProgressDialog dialog = new ProgressDialog(context);
			// dialog.setTitle(R.string.login_dialog_title);
			
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			mProgressDialog = dialog;
		
		}
		mProgressDialog.setMessage(info);
		
		if(!context.isFinishing()&&isShow)mProgressDialog.show();
		 mProgressDialog.setCancelable(false);
		return mProgressDialog;
	}

	public static void dismissProgressdialog() {
		isShow=false;
		if (mProgressDialog != null) {
			
			mProgressDialog.dismiss();
			mProgressDialog=null;
		}
	}
	
	
	
}
