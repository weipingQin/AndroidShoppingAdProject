package com.www.goumei.adapter;



import java.util.List;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
/**
 * 
* @ClassName: MyPagerAdapter 
* @Description: TODO
* @author sunyouyi
* @date 2015-6-4 上午8:27:14 
*
 */
public class MyPagerAdapter extends PagerAdapter{

    private List<View> list;
    public MyPagerAdapter(List<View> list){
        this.list = list;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list==null?0:list.size();
    }

	/*@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		if(position<getCount()){
			container.removeViewAt(position);
		}
	}*/

    public List<View> getList() {
        return list;
    }
    public void setList(List<View> list) {
        this.list = list;
    }
    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        Log.i("info_out", "getCount="+getCount()+"arg1="+arg1);
        if (getCount() > arg1) {
            ((ViewPager) arg0).removeView(list.get(arg1));
        }
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        container.removeView(list.get(position));
        container.addView(list.get(position));
        return list.get(position);
    }
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0==arg1;
    }

}

