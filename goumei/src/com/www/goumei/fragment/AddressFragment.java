package com.www.goumei.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.model.TreeNode.TreeNodeClickListener;
import com.unnamed.b.atv.view.AndroidTreeView;
import com.www.goumei.R;
import com.www.goumei.activity.MainMyAct;
import com.www.goumei.bean.CityData;
import com.www.goumei.bean.CityDataModels;
import com.www.goumei.bean.DistrictData;
import com.www.goumei.bean.DistrictDataModels;
import com.www.goumei.bean.ProvinceData;
import com.www.goumei.bean.ProvinceDataModels;
import com.www.goumei.http.req.ApiClientC;
import com.www.goumei.tree.CityHolder;
import com.www.goumei.tree.CityHolder.CityTreeItem;
import com.www.goumei.tree.DistrictHolder.DistrictTreeItem;
import com.www.goumei.tree.DistrictHolder;
import com.www.goumei.tree.ProvinceHolder;
import com.www.goumei.tree.ProvinceHolder.ProvinceTreeItem;
import com.www.goumei.utils.Constant;
import com.www.goumei.utils.ProcessUtil;


/**
 * 地址
 * @author json
 *
 */
public class AddressFragment extends BaseFragment  implements   OnClickListener {
	private AndroidTreeView tView;
	private TextView last;
	private TextView edit;
	Activity  pActivity;
	private Intent intent;
	 ViewGroup containerView ;
	 TreeNode root;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View   rootView=inflater.inflate(R.layout.activity_address, container, false);
		last = (TextView) rootView.findViewById(R.id.personaldata_last);
		edit = (TextView) rootView.findViewById(R.id.tv_save_data);
		edit.setVisibility(View.INVISIBLE);
		edit.setOnClickListener(this);
		containerView   = (ViewGroup) rootView.findViewById(R.id.container);

	        root= TreeNode.root();

//	        TreeNode myProfile = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_person, "My Profile")).setViewHolder(new ProfileHolder(getActivity()));
//	        TreeNode bruce = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_person, "Bruce Wayne")).setViewHolder(new ProfileHolder(getActivity()));
//	        TreeNode clark = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_person, "Clark Kent")).setViewHolder(new ProfileHolder(getActivity()));
//	        TreeNode barry = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_person, "Barry Allen")).setViewHolder(new ProfileHolder(getActivity()));
	        ProvinceData data=new ProvinceData();
	        data.setProvinceName("北京");
	        TreeNode pr=new TreeNode(new ProvinceHolder.ProvinceTreeItem(data)).setViewHolder(new ProvinceHolder(getActivity()));
//	       
//	        root.addChildren(pr);
	        tView = new AndroidTreeView(getActivity(), root);
	        tView.setDefaultAnimation(true);
	        tView.setDefaultContainerStyle(R.style.TreeNodeStyleDivided, true);
	       
	        tView.setDefaultNodeClickListener(new TreeNodeClickListener() {
				
				@Override
				public void onClick(TreeNode node, Object value) {
						if(node!=null&&node.getChildren().size()<=0){
							if(value instanceof ProvinceTreeItem){
//								new GeCityTask(((ProvinceTreeItem)value).province,node).execute("");
								getCity(((ProvinceTreeItem)value).province,node);
							}
							if(value instanceof CityTreeItem){
								getDistrict(((CityTreeItem)value).City,node);
							}
							if(value instanceof DistrictTreeItem){
								intent=new Intent();
								DistrictData dd=((DistrictTreeItem)value).District;
								String address=dd.getProvinceName()+" "+dd.getCityName()+" "+dd.getDistrictName();
								intent.putExtra(Constant.ADDRESS, address);
								 ((MainMyAct)pActivity).goBackAndShowPreView(intent);
							}
						}
					
				}
				
			});
	        
	        if(root.getChildren()!=null&&root.getChildren().size()<=0){
	        	new GeProvinceTask().execute("");
	        }
	        
		return rootView;
	}
   @Override
public void onActivityCreated(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	   setListeners();
	super.onActivityCreated(savedInstanceState);
	if (Build.VERSION.SDK_INT >= 11) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads  ().detectDiskWrites().detectNetwork().penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
		}
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
	
	class GeProvinceTask extends AsyncTask<String, String, String> {
		ProvinceDataModels videoList;

		@Override
		protected String doInBackground(String... arg0) {
			videoList = ApiClientC.getProvinceById();

			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (videoList != null) {
				
				if (videoList.getModels() != null
						&& videoList.getModels().size() >0) {
					for(ProvinceData pro:videoList.getModels()){
						root.addChildren(new TreeNode(new ProvinceHolder.ProvinceTreeItem(pro)).setViewHolder(new ProvinceHolder(getActivity())));
					}
					 containerView.addView(tView.getView());
				}else{
					
				}
				
			}

		}
	}
	
	private void getCity(ProvinceData province,TreeNode node){
		CityDataModels videoList;
		videoList = ApiClientC.getCitysById(province.getID()+"");
		if (videoList != null) {
			if (videoList.getModels() != null
					&& videoList.getModels().size() >0) {
				for(CityData pro:videoList.getModels()){
					pro.setProvinceName(province.getProvinceName());
					node.addChild(new TreeNode(new CityHolder.CityTreeItem(pro)).setViewHolder(new CityHolder(getActivity())));
				}
				 
			}else{
				
			}
		}
	}
	private void getDistrict(CityData city,TreeNode node){
		DistrictDataModels videoList;
		videoList = ApiClientC.getDistrictsById(city.getID()+"");
		if (videoList != null) {
			if (videoList.getModels() != null
					&& videoList.getModels().size() >0) {
				for(DistrictData pro:videoList.getModels()){
					pro.setCityName(city.getCityName());
					pro.setProvinceName(city.getProvinceName());
					node.addChild(new TreeNode(new DistrictHolder.DistrictTreeItem(pro)).setViewHolder(new DistrictHolder(getActivity())));
				}
				
			}else{
				
			}
		}
	}
	class GeCityTask extends AsyncTask<String, String, String> {
		CityDataModels videoList;
		TreeNode node;
		ProvinceData province;
		public GeCityTask(ProvinceData province,TreeNode node) {
			this.province=province;
			this.node=node;
		}
		
		@Override
		protected String doInBackground(String... arg0) {
			videoList = ApiClientC.getCitysById(this.province.getID()+"");
			
			return null;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			ProcessUtil.showProgressDialog(pActivity, "获取城市中..."); 
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			ProcessUtil.dismissProgressdialog();
			if (videoList != null) {
				if (videoList.getModels() != null
						&& videoList.getModels().size() >0) {
					ProvinceData data=new ProvinceData();
			        data.setProvinceName("北京");
			        TreeNode pr=new TreeNode(new ProvinceHolder.ProvinceTreeItem(data)).setViewHolder(new ProvinceHolder(getActivity()));
			        node.addChild(pr);
					for(CityData pro:videoList.getModels()){
						pro.setProvinceName(province.getProvinceName());
						node.addChild(new TreeNode(new CityHolder.CityTreeItem(pro)).setViewHolder(new CityHolder(getActivity())));
					}
					 
				}else{
					
				}
				
			}
			
		}
	}
	
}
