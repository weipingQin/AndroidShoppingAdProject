package com.www.goumei.fragment;



import com.www.goumei.R;
import com.www.goumei.activity.MainMyAct;
import com.www.goumei.bean.ShopData;
import com.www.goumei.bean.ShopModel;
import com.www.goumei.bean.ShopResult;
import com.www.goumei.bean.UserData;
import com.www.goumei.bean.UserDataB;
import com.www.goumei.http.req.ApiClientC;
import com.www.goumei.http.req.SP_ID;
import com.www.goumei.http.req.UpdateShopReq;
import com.www.goumei.utils.Constant;
import com.www.goumei.utils.L;
import com.www.goumei.utils.ProcessUtil;
import com.www.goumei.utils.StringUtils;
import com.www.goumei.utils.UIHelper;
import com.yixia.camera.demo.utils.ToastUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 店铺认证
 * 
 * @author Eric.Chen
 * 
 */
public class ShopCertificationFragment extends BaseFragment  implements OnClickListener {

	private TextView last;
	private TextView edit;
	private EditText shopName;
	private EditText contactperson;
	private EditText phone;
	private EditText qq;
	private EditText email;
	private Button authentication;
	Activity  pActivity;
	private int shopStatus=0;
	private int shopID=0;
	private UserData user;
	private ShopData shop;
   @Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	   View   view=inflater.inflate(R.layout.activity_shopcertification, container, false);
	   last = (TextView) view.findViewById(R.id.shopcertfication_last);
		edit = (TextView) view.findViewById(R.id.edit);
		shopName = (EditText) view.findViewById(R.id.shopName);
		contactperson = (EditText) view.findViewById(R.id.Contactperson);
		phone = (EditText) view.findViewById(R.id.phone);
		qq = (EditText)view. findViewById(R.id.qq);
		email = (EditText) view.findViewById(R.id.email);
		authentication = (Button) view.findViewById(R.id.bt_Authentication);
	return view;
}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		initData();
		setListeners();
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.pActivity=activity;
	}


	
	protected void initData() {
		// TODO Auto-generated method stub
		
		Bundle  bundle=getArguments();
		if(bundle!=null ){
			
			shopID = bundle.getInt(Constant.SHOPID, 0);
			user=(UserData) bundle.getSerializable(Constant.USERDATA);
			if(shopID==0){
				changeEdit(true);
				authentication.setBackgroundResource(R.drawable.btn_authentication);
			}else{
				new GetShopInfoTask().execute("");
				changeEdit(false);
				
			}
	
		}
		
	}


	protected void setListeners() {
		last.setOnClickListener(this);
		edit.setOnClickListener(this);
		authentication.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.shopcertfication_last:
			  ((MainMyAct)pActivity).goBackAndShowPreView(null);
			break;
		// 编辑
		case R.id.edit:
			if(edit.getText().toString().equals("编辑")){
				changeEdit(true);
				edit.setText("保存");
				
			}else{
				changeEdit(false);
				edit.setText("编辑");
				//TODO 保存
				if(shopID!=0&&checkMsg()){
					new UpdateShopInfoTask().execute("");
				}
				
			}
			break;
		case R.id.bt_Authentication:
			if(shopID==0&&checkMsg()){
				new CreateShopTask().execute("");
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
	
	private boolean checkMsg(){
		if(StringUtils.isEmpty(shopName.getText().toString())){
			ToastUtils.showToast(pActivity, "请填写店铺");
			return false;
		}
		if(StringUtils.isEmpty(contactperson.getText().toString())){
			ToastUtils.showToast(pActivity, "请填写联系人");
			return false;
		}
		if(StringUtils.isEmpty(phone.getText().toString())){
			ToastUtils.showToast(pActivity, "请填写手机号码");
			return false;
		}
		if(StringUtils.isEmpty(qq.getText().toString())){
			ToastUtils.showToast(pActivity, "请填写QQ");
			return false;
		}
		if(StringUtils.isEmpty(email.getText().toString())){
			ToastUtils.showToast(pActivity, "请填写Email");
			return false;
		}
		if(StringUtils.isEmpty(email.getText().toString())){
			ToastUtils.showToast(pActivity, "请填合法邮箱");
			return false;
		}
		shop=new ShopData();
		shop.setLastChangeUser(SP_ID.id);
		shop.setName(shopName.getText().toString());
		shop.setContacts(contactperson.getText().toString());
		shop.setTelphone(phone.getText().toString());
		shop.setQQ(qq.getText().toString());
		shop.setEmail(email.getText().toString());
		return true;
	}
	private void changeEdit(boolean is){
		shopName.setEnabled(is);
		contactperson.setEnabled(is);
		phone.setEnabled(is);
		qq.setEnabled(is);
		email.setEnabled(is);
	}
	private void addTextToEdit(ShopData shop){
		if(shop==null){
			return;
		}
		changeEdit(false);
		shopName.setText(shop.getName());
		contactperson.setText(shop.getContacts());
		phone.setText(shop.getTelphone());
		qq.setText(shop.getQQ());
		email.setText(shop.getEmail());
		if(user!=null){
			switch (user.getShopAuditStatus()) {
			case 0:
				authentication.setBackgroundResource(R.drawable.btn_authentication);
				break;

			case 1:
				authentication.setBackgroundResource(R.drawable.btn_authentication2);
				break;

			case 2:
				authentication.setBackgroundResource(R.drawable.btn_authentication3);
				break;
			default:
				break;
			}
		}
		
	}
	
	class CreateShopTask extends AsyncTask<String, String, String>{
		ShopResult sr;
		@Override
		protected String doInBackground(String... arg0) {
				ShopResult udb=ApiClientC.createShop(shop);
				if(udb!=null){
					sr=udb;
				}
			
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(sr!=null){
				if(sr.getID()>0){
					shopID=sr.getID();
					user.setShopID(sr.getID());
					addTextToEdit(shop);
//					new UpdateUserTask().execute("");
//					new GetShopInfoTask().execute("");
					ToastUtils.showToast("创建店铺成功");
					((MainMyAct)pActivity).goBackAndShowPreView(null);
					
				}
				
			}
		}
	}
	
	class UpdateUserTask extends AsyncTask<String, String, String>{
		String code;
		@Override
		protected String doInBackground(String... arg0) {
			code=ApiClientC.updateUser(user);
			
			return null;
		}
		
		 @Override
	        protected void onPreExecute() {
	            // TODO Auto-generated method stub
	        	
				}
		 
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			L.e("code:::::::::::::::::::::::::"+code);
			if(code!=null &&code.equals("1")){
			}else{
			}
			
		}
	}
	class GetShopInfoTask extends AsyncTask<String, String, String>{
		ShopModel sm;
		@Override
		protected String doInBackground(String... arg0) {
			sm=ApiClientC.GetShop(shopID+"");
			
			return null;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(sm!=null){
				shop=sm.getModel();
				shopID=shop.getID();
				if(shop!=null){
					addTextToEdit(shop);
				}
			}else{
				ToastUtils.showToast(pActivity, "店铺信息获取失败，请返回重试");
			}
			
		}
	}
	class UpdateShopInfoTask extends AsyncTask<String, String, String>{
		String code;
		@Override
		protected String doInBackground(String... arg0) {
			UpdateShopReq req=new UpdateShopReq();
			req.setID(shopID);
			req.setName(shop.getName());
			req.setContacts(shop.getContacts());
			req.setTelphone(shop.getTelphone());
			req.setQQ(shop.getQQ());
			req.setEmail(shop.getEmail());
			code=ApiClientC.updateShop(req);
			
			return null;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(code!=null&&code.equals("1")){
				ToastUtils.showToast(pActivity, "店铺修改成功");
			}else{
				ToastUtils.showToast(pActivity, "店铺修改失败");
			}
			
		}
	}
}
