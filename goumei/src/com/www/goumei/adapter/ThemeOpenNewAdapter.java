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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.www.goumei.R;
import com.www.goumei.activity.VideoDetailActivity;
import com.www.goumei.bean.Videos;
import com.www.goumei.holder.DisplayHolder;
import com.www.goumei.utils.Constant;
import com.www.goumei.utils.Util;

public class ThemeOpenNewAdapter extends BaseAdapter {

	private List<Videos> list;
	private LayoutInflater inflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	//private Activity mActivity;
   Context  context;
	public ThemeOpenNewAdapter(Context ct, List<Videos> list) {
		this.list = list;
		this.context = ct;
		inflater = LayoutInflater.from(context);
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.test)
				.showImageForEmptyUri(R.drawable.test)
				.showImageOnFail(R.drawable.test).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Videos themeDetails = list.get(position);
		final DisplayHolder holder;
		if (convertView != null) {
			holder = (DisplayHolder) convertView.getTag();
		} else {
			convertView = inflater.inflate(R.layout.item_gridview_home_hot,
					null);

			holder = new DisplayHolder(convertView);
			convertView.setTag(holder);
		}
		long availMemory = Util.getAvailMemory(context);
		long totalMemory = Util.getTotalMemory();
		if ((availMemory / totalMemory) > 0.5) {
			imageLoader.clearDiskCache();
			imageLoader.clearMemoryCache();
		}
		imageLoader.displayImage(themeDetails.getUserHeadsculpture(),
				holder.brandIv, options);
		imageLoader.displayImage(themeDetails.getCover(), holder.graphIv,
				options);
		String descTv = themeDetails.getTitle();
		if (descTv.length() > 8) {
			holder.descTv.setText(descTv.substring(0, 8) + "......");
		} else {
			holder.descTv.setText(descTv);
		}
		holder.favRb.setText(String.valueOf(themeDetails.getHits()));
		holder.favRb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				int hits = themeDetails.getHits();
				// TODO 改变网络上数据
				if (isChecked) {
					holder.favRb.setText(String.valueOf(++hits));
					notifyDataSetChanged();
				}else {
					holder.favRb.setText(String.valueOf(--hits));
					notifyDataSetChanged();
				}
			}
		});
		if (themeDetails.userCertificationState == Constant.UserCertificationState_V) {
			//TODO 让认证标志  显示 /隐藏
			holder.veriIv.setVisibility(View.VISIBLE);
		}else{
			holder.veriIv.setVisibility(View.INVISIBLE);
		}
		holder.layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				DetailsCommentFragment detailsFragment=new DetailsCommentFragment();
//				Bundle bundle=new Bundle();
//				//得到当前点击的条目的对象
//				Videos themeDetail = list.get(position);
//				bundle.putSerializable("Bean", themeDetail);
//				detailsFragment.setArguments(bundle);
//				((MainHomeAct)context).dumpToNext(detailsFragment, "DetailsCommentFragment");
			//	Intent it=new Intent(context,SurfaceViewTestActivity.class);
				
				Intent it=new Intent(context,VideoDetailActivity.class);
				it.putExtra(Constant.VIDEO, themeDetails);
				context.startActivity(it);
			}
		});
		return convertView;
	}

}
