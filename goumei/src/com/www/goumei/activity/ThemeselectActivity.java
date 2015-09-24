package com.www.goumei.activity;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.www.goumei.R;
import com.www.goumei.bean.SavePicResult;
import com.www.goumei.http.ApiClient;
import com.www.goumei.utils.ProcessUtil;
import com.www.goumei.utils.UIHelper;
import com.yixia.camera.demo.utils.ToastUtils;
import com.yixia.camera.util.StringUtils;

/**
 * 封面选择
 *
 */
public class ThemeselectActivity extends Activity {
	// 返回按键

	private TextView textback,textfinish, texttheme;
	private ImageView ivw_to2;
	private Bitmap myBitmap;
	private Context context = this;
	private byte[] mContent; 
	private TextView select_picture;
	private TextView cover_text;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_cover_select);
		// setContentView(R.layout.activity_main);
		textback = (TextView) findViewById(R.id.tv_prior);
		texttheme = (TextView) findViewById(R.id.tv_title);
		texttheme.setText("选择封面");
		textfinish = (TextView) findViewById(R.id.tv_later);
		textfinish.setText("上传");
		ivw_to2 = (ImageView) findViewById(R.id.ivw_to2);
		select_picture = (TextView) findViewById(R.id.select_picture);
		cover_text = (TextView) findViewById(R.id.cover_text);
		
		textback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		select_picture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final CharSequence[] items = { "进入相册" };

				AlertDialog dlg = new AlertDialog.Builder(
						ThemeselectActivity.this).setItems(items, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								// 这里item是根据选择的方式，
								// 在items数组里面定义了两种方式，拍照的下标为1所以就调用拍照方法
								if (which == 1) {
									Intent getImageByCamera = new Intent(
											"android.media.action.IMAGE_CAPTURE");
									startActivityForResult(getImageByCamera, 1);
								} else {
									Intent getImage = new Intent(
											Intent.ACTION_GET_CONTENT);
									getImage.addCategory(Intent.CATEGORY_OPENABLE);
									getImage.setType("image/jpeg");
									startActivityForResult(getImage, 0);
								}

							}
						}).create();
				dlg.show();
			}
		});
		
		textfinish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(StringUtils.isEmpty(picturePath)){
					ToastUtils.showLongToast("请选择图片！");
				}else{
					DialogHelper dialogHelper = new DialogHelper();
					dialogHelper.execute(SAVE_PIC);
				}
				
			}
		});

	}
	/** 保存为图片*/
	private static final int SAVE_PIC = 0;
	/** 上传图片后返回结果*/
	private SavePicResult savePicResult;
	/** 本地图片路径*/ 
	private String picturePath;

	public class DialogHelper extends AsyncTask<Integer, Void, Integer> {
		public DialogHelper() {
		}

		@Override
		protected void onPreExecute() {
			ProcessUtil.showProgressDialog(ThemeselectActivity.this, "封面上传中...");
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			switch (params[0]) {
			case SAVE_PIC:
				savePicResult = ApiClient.SavePicture(picturePath);
				return SAVE_PIC;
			default:
				return -1;
			}

		}

		@Override
		protected void onPostExecute(Integer result) {
			switch (result) {
			case SAVE_PIC:
				ProcessUtil.dismissProgressdialog();
				if (savePicResult != null) {
					String savePicPath = savePicResult.getSavePath();
					if (!StringUtils.isEmpty(savePicPath)) {
						
						UIHelper.showMsg(ThemeselectActivity.this, "封面上传成功");
						Intent intent = new Intent(ThemeselectActivity.this, EditActivity.class);
						intent.putExtra("savePicPath", savePicPath);
						intent.putExtra("localPicPath", picturePath);
						
						setResult(004, intent);
						ThemeselectActivity.this.finish();
					} else {
						UIHelper.showMsg(ThemeselectActivity.this, "封面上传失败");
					}
				} else {
					UIHelper.showMsg(ThemeselectActivity.this, "封面上传失败");
				}
				break;

			default:
				break;
			}
			super.onPostExecute(result);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		cover_text.setVisibility(View.GONE);
		if (requestCode == 0) {
			try {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				picturePath = cursor.getString(columnIndex);
				cursor.close();
				ivw_to2.setImageBitmap(BitmapFactory.decodeFile(picturePath));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		} else if (requestCode == 1) {
			try {
				Bundle extras = data.getExtras();
				myBitmap = (Bitmap) extras.get("data");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				mContent = baos.toByteArray();
			} catch (Exception e) {
				e.printStackTrace();
			}
			ivw_to2.setImageBitmap(myBitmap);
		}

	}

}
