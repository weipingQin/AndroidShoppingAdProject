package com.www.goumei.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.www.goumei.R;
import com.www.goumei.bean.SaveVideoResult;
import com.www.goumei.http.ApiClient;
import com.www.goumei.utils.ProcessUtil;
import com.www.goumei.utils.StringUtils;
import com.www.goumei.utils.UIHelper;
import com.yixia.camera.demo.ui.record.MediaRecorderActivity;

public class addVideoFragment extends BaseFragment {
	Button btn_shexiang;
	Activity pActivity;
	private static final int SAVE_VIDEO = 0;
	String video_filepathString = "";
	SaveVideoResult saveVideoResult;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_shexiang, container,
				false);
		btn_shexiang = (Button) view.findViewById(R.id.btn_shexiang);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		initListener();

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.pActivity = activity;
	}

	private void initListener() {
		// TODO Auto-generated method stub
		btn_shexiang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				startActivity(new Intent(getActivity(),MediaRecorderActivity.class));
				
				
//				Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//				startActivityForResult(intent, 1);
			}
		});
	}

	@Override
	public void onLeave() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReLoad(Intent paramIntent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("test", "onActivityResult() requestCode:" + requestCode
				+ ",resultCode:" + resultCode + ",data:" + data);

		if (resultCode == pActivity.RESULT_OK) {
			if (null != data) {
				
				Uri uri = data.getData();
				if (uri == null) {
					return;
				} else {

					// 视频捕获并保存到指定的fileUri意图
					/*
					 * Toast.makeText(pActivity, "Video saved to:\n" +
					 * data.getData(), Toast.LENGTH_LONG).show();
					 */
					Cursor c = pActivity.getContentResolver().query(uri,
							new String[] { MediaStore.MediaColumns.DATA },
							null, null, null);
					if (c != null && c.moveToFirst()) {
						// tv.setText("上传中请等待......");
						// pb.setVisibility(View.VISIBLE);
						video_filepathString = c.getString(0);
						// new Upload(filPath).start();
						if (!StringUtils.isEmpty(video_filepathString)) {
							DialogHelper dialogHelper = new DialogHelper();
							dialogHelper.execute(SAVE_VIDEO);

							/*
							 * byte[] a=FileUtil.getBytes(video_filepathString);
							 * UIHelper.showMsg(pActivity, a.toString());
							 */
						}
					}

				}
			}

		} else if (resultCode == pActivity.RESULT_CANCELED) {
			// 用户取消了视频捕捉
			Toast.makeText(pActivity, "你取消视频录制", Toast.LENGTH_LONG).show();
		} else {
			// 视频捕捉失败,建议用户
			Toast.makeText(pActivity, "视频录制失败请重新录制", Toast.LENGTH_LONG).show();
		}

	}

	public class DialogHelper extends AsyncTask<Integer, Void, Integer> {
		public DialogHelper() {
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

			ProcessUtil.showProgressDialog(pActivity, "上传视频中...");
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			switch (params[0]) {
			case SAVE_VIDEO:
				saveVideoResult = ApiClient.SaveVideo(video_filepathString);
				return SAVE_VIDEO;
			default:
				return -1;
			}

		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			switch (result) {
			case SAVE_VIDEO:
				ProcessUtil.dismissProgressdialog();
				if (saveVideoResult != null) {
					String savePath = saveVideoResult.getSavePath();
					if (!StringUtils.isEmpty(savePath)) {

						UIHelper.showMsg(pActivity, "上传视频 成功");
					} else {
						UIHelper.showMsg(pActivity, "上传视频失败");
					}
				} else {
					UIHelper.showMsg(pActivity, "上传视频失败");
				}
				break;

			default:
				break;
			}
			super.onPostExecute(result);
		}
	}
}
