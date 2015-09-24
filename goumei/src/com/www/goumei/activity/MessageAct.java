package com.www.goumei.activity;








import java.text.MessageFormat;

import com.www.goumei.R;
import com.www.goumei.fragment.MessageFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * 
* @ClassName: MessageAct 
* @Description: 信息
* @author sunyouyi
* @date 2015-6-1 下午9:53:55 
*
 */
public class MessageAct extends BaseFragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.net_finance_lianka);
	     setiContainerID(R.id.finance_lianka);
	    
	     initShowFragment(new MessageFragment() ,"MessageFragment");
	}
	 @Override
	    public void onBackPressed() {
	        super.onBackPressed();
	       
	        this.goBackAndShowPreView(null);
	    }
	
}
