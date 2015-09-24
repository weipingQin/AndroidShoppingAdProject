package com.www.goumei.activity;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Window;

import com.www.goumei.R;
import com.www.goumei.fragment.addVideoFragment;
import com.www.goumei.utils.StringUtils;



public class VideoAct extends BaseFragmentActivity {
	public ProgressDialog mProgressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.net_finance_goods);
	     setiContainerID(R.id.finance_goods);
	     initShowFragment(new addVideoFragment(),"MediaRecorderFragment");
	    
	}
	 @Override
	    public void onBackPressed() {
	        super.onBackPressed();
	        this.goBackAndShowPreView(null);
	    }

		public ProgressDialog showProgress(String title, String message) {
			return showProgress(title, message, -1);
		}

		public ProgressDialog showProgress(String title, String message, int theme) {
			if (mProgressDialog == null) {
				if (theme > 0)
					mProgressDialog = new ProgressDialog(this, theme);
				else
					mProgressDialog = new ProgressDialog(this);
				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				mProgressDialog.setCanceledOnTouchOutside(false);// 不能取消
				mProgressDialog.setIndeterminate(true);// 设置进度条是否不明确
			}

			if (!StringUtils.isEmpty(title))
				mProgressDialog.setTitle(title);
			mProgressDialog.setMessage(message);
			mProgressDialog.show();
			return mProgressDialog;
		}

		public void hideProgress() {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}
		}
}
