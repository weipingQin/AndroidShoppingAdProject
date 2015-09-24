/*
 * Copyright 2013 - learnNcode (learnncode@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */


package com.learnncode.mediachooser.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.learnncode.mediachooser.Datalib;
import com.learnncode.mediachooser.MediaChooser;
import com.learnncode.mediachooser.MediaChooserConstants;
import com.learnncode.mediachooser.R;
import com.learnncode.mediachooser.fragment.BucketImageFragment;
import com.learnncode.mediachooser.fragment.BucketVideoFragment;

public class BucketHomeFragmentActivity extends FragmentActivity {

	private FragmentTabHost mTabHost;
	private TextView headerBarTitle;
	private ImageView headerBarCamera;
	private ImageView headerBarBack;
	private ImageView headerBarDone;

	private static Uri fileUri;
	private ArrayList<String> mSelectedVideo = new ArrayList<String>();
	private ArrayList<String> mSelectedImage = new ArrayList<String>();
	private final Handler handler = new Handler();
  //  private Datalib datalib;
    String pathdata;
    private SharedPreferences pre;
 	private Editor editor;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_home_media_chooser);

		 pre=getSharedPreferences("SP", MODE_PRIVATE);
		 editor = pre.edit();
		
		
		//datalib=(Datalib)getApplication();
		
		headerBarTitle  = (TextView)findViewById(R.id.titleTextViewFromMediaChooserHeaderBar);
		headerBarCamera = (ImageView)findViewById(R.id.cameraImageViewFromMediaChooserHeaderBar);
		headerBarBack   = (ImageView)findViewById(R.id.backArrowImageViewFromMediaChooserHeaderView);
		headerBarDone   = (ImageView)findViewById(R.id.doneImageViewFromMediaChooserHeaderView);
		mTabHost        = (FragmentTabHost) findViewById(android.R.id.tabhost);

		headerBarTitle.setText(getResources().getString(R.string.video));
		
		headerBarCamera.setBackgroundResource(R.drawable.ic_video_white);
		headerBarCamera.setTag(getResources().getString(R.string.video));

		headerBarBack.setOnClickListener(clickListener);
		headerBarCamera.setOnClickListener(clickListener);
		headerBarDone.setOnClickListener(clickListener);

		if(! MediaChooserConstants.showCameraVideo){
			headerBarCamera.setVisibility(View.GONE);
		}

		mTabHost.setup(this, getSupportFragmentManager(), R.id.realTabcontent);



		if(MediaChooserConstants.showVideo){
			mTabHost.addTab(
					mTabHost.newTabSpec("tab2").setIndicator("Video"),
					BucketVideoFragment.class, null);
			View view = mTabHost.getChildAt(0);   
			  // view.getLayoutParams().height = 2; //tabWidget.getChildAt(i)
			 //   final TextView tv = (TextView) view.findViewById(android.R.id.title);
			  //  tv.setTextSize(1);
			
			
			
		}

		if(MediaChooserConstants.showImage){
			mTabHost.addTab(
					mTabHost.newTabSpec("tab1").setIndicator("Image"),
					BucketImageFragment.class, null);
		}

		for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {

			TextView textView = (TextView) mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
			//textView.getLayoutParams().height=5;
			if(textView.getLayoutParams() instanceof RelativeLayout.LayoutParams){

				RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) textView.getLayoutParams();
				params.addRule(RelativeLayout.CENTER_HORIZONTAL);
				params.addRule(RelativeLayout.CENTER_VERTICAL);
				params.height = RelativeLayout.LayoutParams.MATCH_PARENT;
				params.height =2;
				params.width  = RelativeLayout.LayoutParams.WRAP_CONTENT;
				mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title).setLayoutParams(params);

			}else if(textView.getLayoutParams() instanceof LinearLayout.LayoutParams){
				LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) textView.getLayoutParams();
				params.height=2;
				params.gravity = Gravity.CENTER;
				mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title).setLayoutParams(params);
			}

		}

		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {

				android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
				BucketImageFragment imageFragment  = (BucketImageFragment) fragmentManager.findFragmentByTag("tab1");
				BucketVideoFragment videoFragment  = (BucketVideoFragment) fragmentManager.findFragmentByTag("tab2");
				android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


				/*if(tabId.equalsIgnoreCase("tab1")){
					if(imageFragment != null){   
						if(videoFragment != null){
							fragmentTransaction.hide(videoFragment);
						}
						fragmentTransaction.show(imageFragment); 
					}
				}else{ 
					if(videoFragment != null){
						if(videoFragment != null){
							fragmentTransaction.hide(imageFragment);
						}
						fragmentTransaction.show(videoFragment);   
					}
				}*/

				if(tabId.equalsIgnoreCase("tab1")){

					headerBarTitle.setText(getResources().getString(R.string.image));
					headerBarCamera.setBackgroundResource(R.drawable.selector_camera_button);
					headerBarCamera.setTag(getResources().getString(R.string.image));

					if(imageFragment == null){   
						BucketImageFragment newImageFragment = new BucketImageFragment();
						fragmentTransaction.add(R.id.realTabcontent, newImageFragment, "tab1"); 

					}else{

						if(videoFragment != null){
							fragmentTransaction.hide(videoFragment);
						}

						fragmentTransaction.show(imageFragment); 

					}
				}else{ 
					headerBarTitle.setText(getResources().getString(R.string.video));
					headerBarCamera.setBackgroundResource(R.drawable.selector_video_button);
					headerBarCamera.setTag(getResources().getString(R.string.video));

					if(videoFragment == null){

						final BucketVideoFragment newVideoFragment = new BucketVideoFragment();
						fragmentTransaction.add(R.id.realTabcontent, newVideoFragment, "tab2");  

					}else{

						if(imageFragment != null){
							fragmentTransaction.hide(imageFragment);
						}

						fragmentTransaction.show(videoFragment);   
					}
				}
				fragmentTransaction.commit();         
			}
		});

	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			if(view == headerBarCamera){

				if(view.getTag().toString().equals(getResources().getString(R.string.video))){
					Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
					fileUri = getOutputMediaFileUri(MediaChooserConstants.MEDIA_TYPE_VIDEO); // create a file to save the image
					intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

					// start the image capture Intent
					startActivityForResult(intent, MediaChooserConstants.CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);

				}else{
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					fileUri = getOutputMediaFileUri(MediaChooserConstants.MEDIA_TYPE_IMAGE); // create a file to save the image
					intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

					// start the image capture Intent
					startActivityForResult(intent, MediaChooserConstants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
				}

			}else if(view == headerBarDone){

				if(mSelectedImage.size() == 0 && mSelectedVideo.size() == 0){
					Toast.makeText(BucketHomeFragmentActivity.this, getString(R.string.plaese_select_file), Toast.LENGTH_SHORT).show();

				}else{

					if(mSelectedVideo.size() > 0){
						Intent videoIntent = new Intent();
						videoIntent.setAction(MediaChooser.VIDEO_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
						videoIntent.putStringArrayListExtra("list", mSelectedVideo);
						sendBroadcast(videoIntent);
						
					}

					if(mSelectedImage.size() > 0){
						Intent imageIntent = new Intent();
						imageIntent.setAction(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
						imageIntent.putStringArrayListExtra("list", mSelectedImage);
						sendBroadcast(imageIntent);
					}
					    pathdata=pre.getString("path", "");
					    //pathdata=datalib.getlib_path();
					    Intent intent=new Intent();  
			            intent.putExtra("back", pathdata);  
			            setResult(101, intent);  
					
					
					finish();
				}

			}else if(view == headerBarBack){
				
				   Intent intent=new Intent();  
		            intent.putExtra("back", "");  
		            setResult(102, intent);  
				finish();
				 
			}
		}
	};

	/** Create a file Uri for saving an image or video */
	private Uri getOutputMediaFileUri(int type){
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type){

		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), MediaChooserConstants.folderName);
		if (! mediaStorageDir.exists()){
			if (! mediaStorageDir.mkdirs()){
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile;
		if (type == MediaChooserConstants.MEDIA_TYPE_IMAGE){
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
		} else if(type == MediaChooserConstants.MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode,  data);

		if(resultCode ==  Activity.RESULT_OK){

			if (requestCode == MediaChooserConstants.BUCKET_SELECT_IMAGE_CODE){
				addMedia(mSelectedImage, data.getStringArrayListExtra("list"));

			}else if(requestCode == MediaChooserConstants.BUCKET_SELECT_VIDEO_CODE){
				addMedia(mSelectedVideo, data.getStringArrayListExtra("list"));

			}else if (requestCode == MediaChooserConstants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){

				sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, fileUri));
				final AlertDialog alertDialog = MediaChooserConstants.getDialog(BucketHomeFragmentActivity.this).create();
				alertDialog.show();

				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						//Do something after 2000ms
						String fileUriString = fileUri.toString().replaceFirst("file:///", "/").trim();
						android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
						BucketImageFragment bucketImageFragment = (BucketImageFragment) fragmentManager.findFragmentByTag("tab1");
						if(bucketImageFragment != null){   
							bucketImageFragment.getAdapter().addLatestEntry(fileUriString);
							bucketImageFragment.getAdapter().notifyDataSetChanged();
						}
						alertDialog.dismiss();
					}
				}, 5000);

			}else if (requestCode == MediaChooserConstants.CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE){


				sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, fileUri));

				final AlertDialog alertDialog = MediaChooserConstants.getDialog(BucketHomeFragmentActivity.this).create();
				alertDialog.show();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						//Do something after 2000ms
						String fileUriString = fileUri.toString().replaceFirst("file:///", "/").trim();
						android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
						BucketVideoFragment bucketVideoFragment = (BucketVideoFragment) fragmentManager.findFragmentByTag("tab2");
						if(bucketVideoFragment != null){   
							bucketVideoFragment.getAdapter().addLatestEntry(fileUriString);
							bucketVideoFragment.getAdapter().notifyDataSetChanged();

						}
						alertDialog.dismiss();
					}
				}, 5000);
			}
		}
	}

	private void addMedia(ArrayList<String> list, ArrayList<String> input ) {
		for (String string : input) {
			list.add(string);
		}
	}

}
