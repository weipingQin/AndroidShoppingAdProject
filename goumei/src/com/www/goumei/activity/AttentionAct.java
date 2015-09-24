package com.www.goumei.activity;


import com.www.goumei.R;
import com.www.goumei.fragment.AttentionFragment;

import android.os.Bundle;


/**
 * 
* @ClassName: AttentionAct 
* @Description: TODO
* @author sunyouyi
* @date 2015-6-1 下午9:17:52 
*
 */
public class AttentionAct extends BaseFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.net_finance_goods);
	     setiContainerID(R.id.finance_goods);
	     initShowFragment(new AttentionFragment(),"GoodsFragment");
	     //lockScreen();//手势密码锁屏
	}
	 @Override
	    public void onBackPressed() {
	        super.onBackPressed();
	        this.goBackAndShowPreView(null);
	    }
	/*  public void gotoGoodsBijia(String gid ){
	        GoodsParityFragment goodsParityFragment = new GoodsParityFragment();
	        Bundle bundle = new Bundle();
	        bundle.putString("gid", gid);
	        bundle.putString("from", "MyCollectionFragment");
	        goodsParityFragment.setArguments(bundle);
	        dumpToNext(goodsParityFragment, "GoodsParityFragment");

	    }*/
}
