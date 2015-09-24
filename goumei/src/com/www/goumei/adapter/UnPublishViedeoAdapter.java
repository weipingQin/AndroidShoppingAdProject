package com.www.goumei.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.sax.StartElementListener;
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
import com.www.goumei.activity.EditDraftboxActivity;
import com.www.goumei.bean.Videos;
import com.www.goumei.utils.TimeUtils;
import com.www.goumei.utils.Util;
/**
 * 
* @Description:草稿箱
* @author json
* 
*
 */
public class UnPublishViedeoAdapter extends BaseAdapter {
	
	private Context context;
	private List<Videos> list = new ArrayList<Videos>();
	public ImageLoader imageLoader; //用来下载图片的类
	private static DisplayImageOptions options;
	public UnPublishViedeoAdapter(Context context,List<Videos> list2) {
		this.context=context;
		for(int i=0;i<list2.size();i++){
			//idPublish为false的时候；说明此项为草稿箱中的内容
			if(!list2.get(i).isPublish){
				this.list.add(list2.get(i));
			}
		}
		
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
//	public void setData(List<Videos> list3){
//		this.list=list3;
//		
//	}
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
	public View getView(final int position, View view, ViewGroup rg) {
		viewHolder holder = null;
		if (view == null) {
			holder = new viewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.list_item_unpublish, null);
			holder.head=(ImageView) view.findViewById(R.id.head);
			holder.name=(TextView) view.findViewById(R.id.name);
			holder.time=(TextView) view.findViewById(R.id.time);
			holder.createTime=(TextView) view.findViewById(R.id.createTime);
			holder.line=(ImageView) view.findViewById(R.id.fans_item_line);
			view.setTag(holder);
		} else {
			holder=(viewHolder) view.getTag();
		}
		Videos bean = list.get(position);
		if(bean.getCover()!=null){
			imageLoader.displayImage(bean.getCover(), holder.head, options);
		}
		holder.name.setText("编辑");
		holder.name.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(context, EditDraftboxActivity.class);
				Bundle data = new Bundle();
				data.putSerializable("video", list.get(position));
				intent.putExtras(data);
				context.startActivity(intent);
				
			}
		});
		if(bean.getPublishTime()!=null){
			holder.createTime.setText(Util.getDateHHmm(bean.getPublishTime()));
		}
		holder.time.setText(TimeUtils.secToTime(bean.getDuration())+"");
		if (position==(list.size()-1)) {
			holder.line.setVisibility(View.GONE);
		}
		return view;
	}

	private class viewHolder {
		private ImageView head;
		private TextView name;
		private TextView time;
		private TextView createTime;
		private ImageView line;
	}
}
