package com.www.goumei.activity;

import com.www.goumei.R;
import com.www.goumei.fragment.HomeFragment;

import android.os.Bundle;

/**
 * 
 * @ClassName: MainHomeAct
 * @Description: 首页
 * @author sunyouyi
 * @date 2015-6-1 上午6:25:01
 * 
 */
public class MainHomeAct extends BaseFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.net_finance_home);
		setiContainerID(R.id.finance_home);

		initShowFragment(new HomeFragment(), "HomeFragment");
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.goBackAndShowPreView(null);

	}
}
