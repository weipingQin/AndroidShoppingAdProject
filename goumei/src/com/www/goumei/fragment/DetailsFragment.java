package com.www.goumei.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.www.goumei.R;

/**
 * 详情
 * @author Eric.Chen
 *
 */
public class DetailsFragment extends BaseFragment  implements   OnClickListener{

	private TextView last;
	private TextView share;
	private PullToRefreshListView detailsList;
	private RadioGroup detailsRG;
	private RadioButton comment;
	private RadioButton love;
	private RadioButton shoping;
   Activity pActivity;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View   view=inflater.inflate(R.layout.activity_details, container,false);
		last = (TextView) view.findViewById(R.id.detailsLast);
		share = (TextView) view.findViewById(R.id.detailsShare);
		detailsList = (PullToRefreshListView)view. findViewById(R.id.detailsList);
		detailsRG = (RadioGroup)view. findViewById(R.id.detailsRG);
		comment = (RadioButton)view. findViewById(R.id.detailsComment);
		love = (RadioButton) view.findViewById(R.id.detailsLove);
		shoping = (RadioButton)view. findViewById(R.id.detailsShoping);
		return view;
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
	

	
	protected void setListeners() {
		// TODO Auto-generated method stub
		last.setOnClickListener(this);
		share.setOnClickListener(this);
		comment.setOnClickListener(this);
		love.setOnClickListener(this);
		shoping.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//返回
		case R.id.detailsLast:
			
			break;
		//分享
		case R.id.detailsShare:
			
			break;
		//评论
		case R.id.detailsComment:
			
			break;
		//喜欢
		case R.id.detailsLove:
			
			break;
		//购物
		case R.id.detailsShoping:
			
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
