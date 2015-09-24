package com.www.goumei.fragment;

import com.www.goumei.R;
import com.www.goumei.activity.MainMyAct;
import com.www.goumei.utils.Constant;
import com.www.goumei.utils.SPutil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



public class GeneralFragment extends BaseFragment implements OnClickListener {

	private LinearLayout layout_ave_processed_pictures;
	private LinearLayout layout_wifi_play;
	private TextView last;
	private ImageView picturesBtn;
	private ImageView wifi_playBtn;
	/**是否保存处理过的视频和图片*/
	private boolean isSave=false;
	/**是否wifi网络下自动播放*/
	private boolean isPlay=false;
    Activity   pActivity;
    TextView title,later;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.activity_general, container,false);
		last = (TextView)view. findViewById(R.id.tv_prior);
		title=(TextView)view. findViewById(R.id.tv_title);
		later=(TextView)view. findViewById(R.id.tv_later);
		layout_ave_processed_pictures = (LinearLayout) view.findViewById(R.id.layout_ave_processed_pictures);
		layout_wifi_play = (LinearLayout) view.findViewById(R.id.layout_wifi_play);
		picturesBtn = (ImageView) view.findViewById(R.id.save_processed_picturesBtn);
		wifi_playBtn = (ImageView)view. findViewById(R.id.wifi_playBtn);
		isSave=(Boolean) SPutil.get(getActivity(), Constant.SP_PIC_SAVE, false);
		isPlay=(Boolean) SPutil.get(getActivity(), Constant.SP_WIFI_PLAY, false);
		if(isSave){
			picturesBtn.setBackgroundResource(R.drawable.icon_btn_on);
		}else{
			picturesBtn.setImageResource(R.drawable.icon_btn_off);
		}
		
		if(isPlay){
			wifi_playBtn.setImageResource(R.drawable.icon_btn_on);
		}else{
			wifi_playBtn.setImageResource(R.drawable.icon_btn_off);
		}
		initView();
		return   view ;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setListeners();
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.pActivity=activity;
	}
	
	public void initView() {
		title.setText("通用");
		later.setVisibility(View.INVISIBLE);
		
		
	}


	public void setListeners() {
		last.setOnClickListener(this);
		layout_ave_processed_pictures.setOnClickListener(this);
		layout_wifi_play.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_prior:
			((MainMyAct)pActivity).goBackAndShowPreView(null);
			break;
			
		case R.id.layout_ave_processed_pictures:
			if (isSave) {
				picturesBtn.setImageResource(R.drawable.icon_btn_off);
				isSave=false;
			}else {
				picturesBtn.setImageResource(R.drawable.icon_btn_on);
				isSave=true;
			}
			SPutil.put(getActivity(), Constant.SP_PIC_SAVE, isSave);
			break;
			
		case R.id.layout_wifi_play:
			if (isPlay) {
				wifi_playBtn.setImageResource(R.drawable.icon_btn_off);
				isPlay=false;
			}else {
				wifi_playBtn.setImageResource(R.drawable.icon_btn_on);
				isPlay=true;
			}
			SPutil.put(getActivity(), Constant.SP_WIFI_PLAY, isPlay);
			break;
		default:
			break;
		}
	}

	@Override
	public void onLeave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReLoad(Intent paramIntent) {
		// TODO Auto-generated method stub
		
	}

}
