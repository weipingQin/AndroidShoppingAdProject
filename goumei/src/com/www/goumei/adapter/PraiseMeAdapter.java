package com.www.goumei.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.www.goumei.bean.UsersPraiseMeData;
import com.www.goumei.http.req.SP_ID;
import com.www.goumei.utils.Constant;
import com.www.goumei.views.CircleImageView;
/**
 * 
* @ClassName: PraiseMeAdapter 
* @Description: TODO
* @author 
* @date 2015-6-7 上午12:41:54 
*
 */
public class PraiseMeAdapter extends BaseAdapter {
	
	private Context context;
	private List<UsersPraiseMeData> list;
	public ImageLoader imageLoader; //用来下载图片的类
	private static DisplayImageOptions options;
	public PraiseMeAdapter(Context context,List<UsersPraiseMeData> list2) {
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
	public void setData(List<UsersPraiseMeData> list3){
		this.list=list3;
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public UsersPraiseMeData getItem(int position) {
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
					R.layout.layout_praiseme_item, null);
			holder.icon=(CircleImageView) view.findViewById(R.id.fansIcon);
			holder.name=(TextView) view.findViewById(R.id.fansName);
			holder.time=(TextView) view.findViewById(R.id.fansSex);
			holder.follow=(ImageView) view.findViewById(R.id.isfollow);
			holder.signature=(TextView)view.findViewById(R.id.fansId);
			holder.line=(ImageView) view.findViewById(R.id.fans_item_line);
			view.setTag(holder);
		} else {
			holder=(viewHolder) view.getTag();
		}
		final UsersPraiseMeData bean = list.get(position);
		if(bean.getHeadsculpture()!=null){
			imageLoader.displayImage(bean.getHeadsculpture(), holder.icon, options);
		}
		holder.icon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent homeIntent=new Intent(context,HomePageActivity.class);
				homeIntent.putExtra(Constant.OTHER_ID, bean.getID()+"");
				context.startActivity(homeIntent);
				
			}
		});
		holder.name.setText(bean.getDisplayName());
//		if(bean.getSex()==1){
//			holder.sex.setImageResource(R.drawable.icon_man);
//		}else{
//			holder.sex.setImageResource(R.drawable.icon_female);
//		}
		if(bean.getPraiseTimeDescription()!=null){
			holder.time.setText(bean.getPraiseTimeDescription());
		}
		if(bean.getPraiseVideoCover()!=null){
			imageLoader.displayImage(bean.getPraiseVideoCover(),holder.follow, options);
		}
		if(bean.getPraiseContent()!=null){
			holder.signature.setText(bean.getPraiseContent());
		}
		final String id=SP_ID.id;
		holder.follow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
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
		private TextView signature;
//		private ImageView sex;
		private TextView time;
		private ImageView follow;
		private ImageView line;
	}
	
	
}
