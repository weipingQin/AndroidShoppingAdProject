package com.www.goumei.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.www.goumei.R;
import com.www.goumei.activity.HomePageActivity;
import com.www.goumei.bean.FriendData;
import com.www.goumei.fragment.UpdatePersonaldataFragment;
import com.www.goumei.http.req.ApiClientC;
import com.www.goumei.http.req.SP_ID;
import com.www.goumei.utils.Constant;
import com.www.goumei.utils.L;
import com.www.goumei.views.CircleImageView;
/**
 * 
* @ClassName: FriendAdapter 
* @Description: TODO
* @author sunyouyi
* @date 2015-6-7 上午12:41:54 
*
 */
public class PersonalFriendAdapter extends BaseAdapter {
	
	private Context context;
	private List<FriendData> list;
	public ImageLoader imageLoader; //用来下载图片的类
	private static DisplayImageOptions options;
	public PersonalFriendAdapter(Context context,List<FriendData> list2) {
		this.context=context;
		this.list=list2;
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		
		 options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.test)
		.showImageForEmptyUri(R.drawable.test)
		.showImageOnFail(R.drawable.test)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
	}
	public void setData(List<FriendData> list3){
		this.list=list3;
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public FriendData getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup rg) {
		viewHolder holder = null;
		if (view == null) {
			holder = new viewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.personal_friend_list_item, null);
			holder.icon=(CircleImageView) view.findViewById(R.id.fansIcon);
			holder.name=(TextView) view.findViewById(R.id.fansName);
			holder.sex=(ImageView) view.findViewById(R.id.fansSex);
			holder.follow=(ImageView) view.findViewById(R.id.isfollow);
			
			holder.line=(ImageView) view.findViewById(R.id.fans_item_line);
			view.setTag(holder);
		} else {
			holder=(viewHolder) view.getTag();
		}
		final FriendData bean = list.get(position);
		if(bean.getHeadsculpture()!=null){
			imageLoader.displayImage(bean.getHeadsculpture(), holder.icon, options);
		}
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent homeIntent=new Intent(context,HomePageActivity.class);
				homeIntent.putExtra(Constant.OTHER_ID, bean.getID()+"");
				context.startActivity(homeIntent);
			}
		});
		holder.icon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent homeIntent=new Intent(context,HomePageActivity.class);
				homeIntent.putExtra(Constant.OTHER_ID, bean.getID()+"");
				context.startActivity(homeIntent);
				
			}
		});
		
		holder.name.setText(bean.getDisplayName());
		if(bean.getSex()==1){
			holder.sex.setImageResource(R.drawable.icon_man);
		}else{
			holder.sex.setImageResource(R.drawable.icon_female);
		}
		if(bean.getIsAttention()==1){
			holder.follow.setImageResource(R.drawable.icon_right);
		}else{
			holder.follow.setImageResource(R.drawable.isfollow3);
		}
		final String id=SP_ID.id;
		final int fansId=bean.getID();
		holder.follow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				new AddAttentionTask(id, fansId+"",position).execute("");
			}
		});
		
		if (position==(list.size()-1)) {
			holder.line.setVisibility(View.GONE);
		}
		return view;
	}

	private class viewHolder {
		private CircleImageView icon;
		private TextView name;
		private ImageView sex;
		private ImageView follow;
		private ImageView line;
	}
	
	class AddAttentionTask extends AsyncTask<String, String, String>{
		String code;
		String id;
		String fansId;
		int index;
		public AddAttentionTask(String id,String fansId,int index){
			this.id=id;
			this.fansId=fansId;
			this.index=index;
		}
		@Override
		protected String doInBackground(String... arg0) {
			code=ApiClientC.addAttention(id, fansId);
			
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			L.e("code:::::::::::::::::::::::::"+code);
			if(code!=null &&code.equals("1")){
				
				Intent intent = new Intent();
				intent.setAction(Constant.ACTION_ATTENTION_STATUS);
				intent.setAction(Constant.ACTION_ADD_FANS);
				intent.putExtra(Constant.INTENT_INDEX,index);
				intent.putExtra(Constant.INTENT_STATUS,Constant.INTENT_STATUS_TRUE);
				context.sendBroadcast(intent);
			}
			
		}
	}
}
