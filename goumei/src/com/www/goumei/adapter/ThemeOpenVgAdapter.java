package com.www.goumei.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ThemeOpenVgAdapter extends PagerAdapter {
	
	private List<View> views;

	public ThemeOpenVgAdapter(List<View> views) {
		this.views=views;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return views.size();
	}
	
	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO Auto-generated method stub
		((ViewPager)container).removeView(views.get(position));
	}
	
	@Override
	public void finishUpdate(View container) {
		// TODO Auto-generated method stub
		super.finishUpdate(container);
	}
	
	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager)container).addView(views.get(position));
		return views.get(position);
	}
	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0==arg1;
	}
	
	

}
