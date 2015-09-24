package com.www.goumei.fragment;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.www.goumei.R;
import com.www.goumei.activity.MainMyAct;
import com.www.goumei.bean.UserData;
import com.www.goumei.bean.UserDataB;
import com.www.goumei.http.req.ApiClientC;
import com.www.goumei.http.req.SP_ID;
import com.www.goumei.utils.Constant;
import com.www.goumei.utils.StringUtils;
import com.www.goumei.views.CircleImageView;
import com.yixia.camera.demo.utils.ToastUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * 修改签名
 * @author Eric.Chen
 *
 */
public class SignFragment extends BaseFragment  implements   OnClickListener {

	private TextView last;
	private TextView edit;
	private UserData user;
	private EditText mSign;
	Activity  pActivity;
	private Intent intent;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View   view=inflater.inflate(R.layout.activity_sign, container, false);
		last = (TextView) view.findViewById(R.id.personaldata_last);
		edit = (TextView) view.findViewById(R.id.tv_save_data);
		edit.setOnClickListener(this);
		mSign=(EditText)view.findViewById(R.id.et_sign);
		 initData();
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
		edit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.personaldata_last:
		   ((MainMyAct)pActivity).goBackAndShowPreView(null);
			break;
		//保存
		case R.id.tv_save_data:
			if(StringUtils.isEmpty(mSign.getText().toString().trim())){
				ToastUtils.showToast(getActivity(), "请输入个性签名");
				return;
			}
			intent=new Intent();
			user.setIndividualitySignature(mSign.getText().toString());
			intent.putExtra(Constant.USERDATA, user);
			
			((MainMyAct)pActivity).goBackAndShowPreView(intent);
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
	private void initData(){
		user=(UserData) getArguments().get(Constant.USERDATA);
		if(user!=null){
			mSign.setText(user.getIndividualitySignature());
			
		}
	}
	
}
