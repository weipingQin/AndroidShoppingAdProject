package com.www.goumei.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.www.goumei.R;
import com.www.goumei.bean.Categories;

@SuppressLint("ViewHolder")
public class CateoryAdapter extends BaseAdapter {
	
	private Context context;
	private List<Categories> list;
	private ImageLoader imageLoader;
	public CateoryAdapter(Context context,List<Categories> list2) {
		this.context=context;
		this.list=list2;
		
	}
	public void setData(List<Categories> list3){
		this.list=list3;
		
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Categories getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup rg) {
		final Categories item = getItem(position);
		view=LayoutInflater.from(context).inflate(R.layout.list_item_cateory, null);
		TextView tv=(TextView) view.findViewById(R.id.tv_cateory);
		ImageView iv=(ImageView)view.findViewById(R.id.iv_cateory);
		tv.setText(item.getName());
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		imageLoader.displayImage(list.get(position).getIcon(), iv);
		return view;
	}

	
}
