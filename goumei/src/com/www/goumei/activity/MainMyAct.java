package com.www.goumei.activity;








import com.www.goumei.R;
import com.www.goumei.fragment.MeFragment;

import android.app.Activity;
import android.os.Bundle;

/**
 * 
* @ClassName: MainMyAct 
* @Description: 我的
* @author sunyouyi
* @date 2015-6-1 下午9:12:52 
*
 */
public class MainMyAct extends BaseFragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.net_finance_my);
	     setiContainerID(R.id.finance_my);
	     initShowFragment(new MeFragment(),"MeFragment");
	}
	 @Override
	    public void onBackPressed() {
	        super.onBackPressed();
	        this.goBackAndShowPreView(null);
	    }
	
	
}
