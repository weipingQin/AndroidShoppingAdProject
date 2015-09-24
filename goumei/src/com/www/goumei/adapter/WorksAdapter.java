package com.www.goumei.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.www.goumei.R;
import com.www.goumei.activity.AttentionAct;
import com.www.goumei.activity.VideoDetailActivity;
import com.www.goumei.bean.Videos;
import com.www.goumei.fragment.DetailsCommentFragment;
import com.www.goumei.holder.DisplayHolder;
import com.www.goumei.utils.Constant;
import com.www.goumei.utils.UIHelper;
import com.www.goumei.utils.Util;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
/**
 * 
* @ClassName: VideoAdapter 
* @Description: 
* @author sunyouyi
* @date 2015-6-5 上午7:30:31 
*
 */
public class WorksAdapter extends BaseAdapter {
	
	private Context context;
	private List<Videos> list;
	public ImageLoader imageLoader; //用来下载图片的类
	private static DisplayImageOptions options;
	public WorksAdapter(Context context,List<Videos> list2) {
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
	public void setData(List<Videos> list3){
		this.list=list3;
		
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Videos getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup rg) {
		final Videos item = getItem(position);
		final DisplayHolder holder;
		if (view != null) {
			holder = (DisplayHolder) view.getTag();
		} else {
			view =LayoutInflater.from(context).inflate(R.layout.item_gridview_home_hot, null);
			
			holder = new DisplayHolder(view);
			view.setTag(holder);
		}
		
		//viewHodler.discount.setText(info.getDiscount());
	    long availMemory=  Util.getAvailMemory(context);
	     long totalMemory=  Util.getTotalMemory();
	     if((availMemory/totalMemory) >0.5){
	    	 imageLoader.clearDiskCache();
	    	 imageLoader.clearMemoryCache();
	     }
	    
		imageLoader.displayImage(item.userHeadsculpture, holder.brandIv, options);
		imageLoader.displayImage(item.cover, holder.graphIv, options);
		String descTv=item.title;
		
		if(descTv.length()>8){
			
			holder.descTv.setText(descTv.substring(0, 8)+"......");
		}else{
			
			holder.descTv.setText(descTv);
		}
		holder.favRb.setText(String.valueOf(item.hits));
		holder.favRb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					// TODO 改变网络上数据
					holder.favRb.setText(String.valueOf(++item.hits));
					notifyDataSetChanged();
				} else {
					holder.favRb.setText(String.valueOf(--item.hits));
					notifyDataSetChanged();
				}
			}
		});
		if (item.userCertificationState == Constant.UserCertificationState_V) {
			//TODO 让认证标志  显示 /隐藏
			holder.veriIv.setVisibility(View.VISIBLE);
		}else{
			holder.veriIv.setVisibility(View.INVISIBLE);
		}
		holder.layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent it=new Intent(context,VideoDetailActivity.class);
				it.putExtra(Constant.VIDEO, item);
				context.startActivity(it);
			}
		});
		return view;
	}

	
}
