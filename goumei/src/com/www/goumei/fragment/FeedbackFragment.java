package com.www.goumei.fragment;

import com.www.goumei.R;
import com.www.goumei.activity.MainMyAct;
import com.www.goumei.http.req.ApiClientC;
import com.www.goumei.utils.Constant;
import com.www.goumei.utils.L;
import com.www.goumei.utils.ProcessUtil;
import com.www.goumei.utils.UIHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



/**
 * 意见反馈
 * 
 * @author Eric.Chen
 * 
 */
public class FeedbackFragment extends BaseFragment  implements OnClickListener {

	private TextView last;
	private TextView sendOut;
	private EditText information;
	private EditText content;
    private  Activity   pActivity;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View  view =inflater.inflate(R.layout.activity_feedback, container, false);
		last = (TextView) view.findViewById(R.id.feedback_last);
		sendOut = (TextView) view. findViewById(R.id.feedback_sendOut);
		content = (EditText)  view.findViewById(R.id.feedback_content);
		information = (EditText)  view.findViewById(R.id.feedback_contact_information);
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
		last.setOnClickListener(this);
		sendOut.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.feedback_last:
			((MainMyAct)pActivity).goBackAndShowPreView(null);
			break;

		case R.id.feedback_sendOut:
			String text_content = content.getText().toString().trim();
			String contact = information.getText().toString().trim();
			if (text_content.equals("") || contact.equals("")) {
				UIHelper.showMsg(pActivity, "意见或者联系方式为空，请检查输入！");
			}else{
				//提交意见
				new AddOPTask().execute("");
			}
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
	
	class AddOPTask extends AsyncTask<String, String, String>{
		String code;
		@Override
		protected String doInBackground(String... arg0) {
			code=ApiClientC.addOpinions(content.getText().toString().trim(), information.getText().toString().trim());
			
			return null;
		}
		
		 @Override
	        protected void onPreExecute() {
	            // TODO Auto-generated method stub
	        	
				
					ProcessUtil.showProgressDialog(pActivity, "提交意见中..."); 
				}
		 
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			ProcessUtil.dismissProgressdialog();
			L.e("code:::::::::::::::::::::::::"+code);
			if(code!=null &&code.equals("1")){
				
				 UIHelper.showMsg(pActivity, "意见提交成功");
				 content.setText("");
				 information.setText("");
			}else{
				 UIHelper.showMsg(pActivity, "意见提交失败");
			}
			
		}
	}
}
