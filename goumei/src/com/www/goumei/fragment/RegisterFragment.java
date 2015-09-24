package com.www.goumei.fragment;

import com.www.goumei.R;
import com.www.goumei.activity.MainMyAct;
import com.www.goumei.http.ApiClient;
import com.www.goumei.utils.UIHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterFragment extends BaseFragment implements OnClickListener{
	private final static int GETCODE_TAG=1501;
	private final static int GETCODE_OK=1502;
	private TextView tv_prior;
	private TextView tv_title;
	private TextView tv_later;
	private EditText tel_num;
	private EditText pwd;
	private Intent intent;
	private String numString;
    Activity  pActivity;
	private AlertDialog builder;
	private String pass;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View   view=inflater.inflate(R.layout.fragment_resetpwd , container,false);
		tv_prior = (TextView) view.findViewById(R.id.tv_prior);
		tv_title = (TextView)  view.findViewById(R.id.tv_title);
		tv_later = (TextView) view. findViewById(R.id.tv_later);
		tel_num = (EditText) view. findViewById(R.id.tel_num);
		pwd = (EditText) view. findViewById(R.id.pwd);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	
		initData();
		setlistener();
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.pActivity=activity;
	}
	
	
	/**
	 * 初始化数据
	 */
	private void initData() {
		tv_title.setText("设置帐号和密码");
		tv_later.setText("下一步");
	}
	
	private void setlistener() {
		tv_prior.setOnClickListener(this);
		tv_later.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_prior:
			((MainMyAct)pActivity).goBackAndShowPreView(null);
			break;

		case R.id.tv_later:
			numString = tel_num.getText().toString().trim();
			pass = pwd.getText().toString().trim();
			if (TextUtils.isEmpty(numString) && pass.length() >5) {
				UIHelper.showMsg(pActivity, "手机号或者密码不符合规范，请重新输入!");
				
				return ;
			}else {
				Log.e("TAG", numString);
				View view = LayoutInflater.from(pActivity).inflate(
						R.layout.dialog_sendto_code, null);
				builder = new AlertDialog.Builder(pActivity).create();
				TextView number=(TextView) view.findViewById(R.id.dialog_number);
				TextView cancel=(TextView) view.findViewById(R.id.diglog_cancel);
				TextView ok=(TextView) view.findViewById(R.id.dialog_determine);
				number.setText("+86  " + numString);
				cancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						builder.dismiss();
					}
				});
				ok.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						builder.dismiss();
						getCodeTask codeTask=new getCodeTask();
						codeTask.execute(GETCODE_TAG);
					}
				});
				builder.show();
				// 得到属性
				WindowManager.LayoutParams params = builder.getWindow()
						.getAttributes();
				params.gravity = Gravity.CENTER;// 显示在中间
				// 设置对话框的宽度为手机屏幕的0.8
				params.width = (int) (pActivity.getWindowManager()
						.getDefaultDisplay().getWidth() * 0.8);
				// 设置对话框的高度为手机屏幕的0.27
				params.height = (int) (pActivity.getWindowManager()
						.getDefaultDisplay().getHeight() * 0.29);
				builder.getWindow().setAttributes(params);// 設置屬性
				builder.getWindow().setContentView(view);// 把自定義view加上去							
			}
			break;
		default:
			break;
		}
	}
	
	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case GETCODE_OK:
				String code=(String) msg.obj;
				ValidatePhoneFragment  validatePhoneFragment=new ValidatePhoneFragment();
			    Bundle  bundle=new Bundle();
			    bundle.putString("number", numString);
				bundle.putString("pass", pass);
				bundle.putString("code", code);
				validatePhoneFragment.setArguments(bundle);
				((MainMyAct)pActivity).dumpToNext(validatePhoneFragment, "ValidatePhoneFragment");
				break;

			default:
				break;
			}
		}
	};
	
	public class getCodeTask extends AsyncTask<Integer, Void, Integer>{

		@Override
		protected Integer doInBackground(Integer... params) {
			switch (params[0]) {
			case GETCODE_TAG:
				//获取验证码
				String code = ApiClient.GetShortMessageCode(numString);
				Log.e("TAG","///"+ code);
				Message message=new Message();
				message.what=GETCODE_OK;
				message.obj=code;
				handler.sendMessage(message);
				return GETCODE_TAG;

			default:
				return -1;
			}
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
