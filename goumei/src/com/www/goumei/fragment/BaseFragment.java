package com.www.goumei.fragment;



import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * 
* @ClassName: BaseFragment 
* @Description: TODO
* @author sunyouyi
* @date 2015-1-19 下午5:28:05 
*
 */
public abstract class BaseFragment extends Fragment{



    public abstract void onLeave();

    public abstract void onReLoad(Intent paramIntent);





   
 
    
}

