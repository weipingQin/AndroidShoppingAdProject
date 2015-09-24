package com.www.goumei.adapter;

import java.util.List;

import org.w3c.dom.Text;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.www.goumei.R;
import com.www.goumei.bean.FansBean;
import com.www.goumei.bean.FriendData;
import com.www.goumei.bean.PushBean;
import com.www.goumei.bean.Videos;
import com.www.goumei.holder.DisplayFriendHolder;
import com.www.goumei.holder.DisplayHolder;
import com.www.goumei.http.req.ApiClientC;
import com.www.goumei.http.req.SP_ID;
import com.www.goumei.utils.Constant;
import com.www.goumei.utils.L;
import com.www.goumei.utils.StringUtils;
import com.www.goumei.utils.Util;
import com.www.goumei.views.CircleImageView;
import com.yixia.camera.demo.utils.ToastUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
/**
 * 
* @ClassName: FriendAdapter 
* @Description: TODO
* @author sunyouyi
* @date 2015-6-7 上午12:41:54 
*
 */
public class PushDetailAdapter extends BaseAdapter {
	
	private Context context;
	private List<PushBean> list;
	public ImageLoader imageLoader; //用来下载图片的类
	private static DisplayImageOptions options;
	public PushDetailAdapter(Context context,List<PushBean> list2) {
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
	public void setData(List<PushBean> list3){
		this.list=list3;
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public PushBean getItem(int position) {
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
					R.layout.push_detail_list_item, null);
			holder.icon=(CircleImageView) view.findViewById(R.id.fansIcon);
//			holder.name=(TextView) view.findViewById(R.id.fansName);
			holder.follow=(TextView) view.findViewById(R.id.isfollow);
			holder.id=(TextView)view.findViewById(R.id.tv_content);
			holder.line=(ImageView) view.findViewById(R.id.fans_item_line);
			holder.ll=(LinearLayout)view.findViewById(R.id.ll);
			view.setTag(holder);
		} else {
			holder=(viewHolder) view.getTag();
		}
		final PushBean bean = list.get(position);
		if(bean.getPushUserHeadHeadsculpture()!=null){
			imageLoader.displayImage(bean.getPushUserHeadHeadsculpture(), holder.icon, options);
		}
//		holder.name.setText(bean.getName());
		holder.id.setText(bean.getContent());
		if (position==(list.size()-1)) {
			holder.line.setVisibility(View.GONE);
		}
		holder.follow.setText(Util.getDateHHmm(bean.getCreateTime()));
		holder.ll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 try {
						Intent intent = new Intent();
						intent.setAction("android.intent.action.VIEW");
						Uri content_url = Uri.parse(bean.getLink());
						intent.setData(content_url);
						context.startActivity(intent);
					} catch (Exception e) {
						// TODO: handle exception
						ToastUtils.showToast(context, "link不地址错误");
					}
			}
		});
		
		return view;
	}

	private class viewHolder {
		private CircleImageView icon;
		private TextView name;
		private TextView id;
		private ImageView sex;
		private TextView follow;
		private ImageView line;
		private LinearLayout ll;
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
				intent.putExtra(Constant.INTENT_INDEX,index);
				intent.putExtra(Constant.INTENT_STATUS,Constant.INTENT_STATUS_TRUE);
				context.sendBroadcast(intent);
			}
			
		}
	}
}
