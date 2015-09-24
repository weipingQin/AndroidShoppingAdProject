package com.www.goumei.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TabHost;
import cn.sharesdk.framework.ShareSDK;

import com.www.goumei.R;
import com.www.goumei.http.req.SP_ID;
import com.www.goumei.utils.Constant;
import com.www.goumei.utils.SPutil;
import com.yixia.camera.demo.ui.record.MediaRecorderActivity;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity implements OnCheckedChangeListener{
	
	private TabHost mTabHost;
	private Intent mAIntent;
	private Intent mBIntent;
//	private Intent mCIntent;
	private Intent mDIntent;
	private Intent mEIntent;
    RadioButton rb_mainhome,rb_attention,rb_video,rb_message,rb_my;
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.net_main_tab_host);
        SP_ID.id=(String) SPutil.get(this, Constant.ONLINE_ID, "1");
        ShareSDK.initSDK(this);
		ShareSDK.setConnTimeout(20000);
		ShareSDK.setReadTimeout(20000);
        this.mAIntent = new Intent(this,AttentionAct.class);
        this.mBIntent = new Intent(this,MainHomeAct.class);
//        this.mCIntent = new Intent(this,MediaRecorderActivity.class);
        this.mDIntent = new Intent(this,MessageAct.class);
        this.mEIntent = new Intent(this,MainMyAct.class);
      
        rb_attention= (RadioButton) findViewById(R.id.rb_attention);
        rb_attention.setOnCheckedChangeListener(this);
        rb_mainhome=(RadioButton) findViewById(R.id.rb_mainhome);
        rb_mainhome.setOnCheckedChangeListener(this);
       
        rb_video=(RadioButton) findViewById(R.id.rb_video);
        rb_video.setOnCheckedChangeListener(this);
        rb_message=(RadioButton) findViewById(R.id.rb_message);
        rb_message.setOnCheckedChangeListener(this);
        rb_my=(RadioButton) findViewById(R.id.rb_my);
		rb_my.setOnCheckedChangeListener(this);
     
        
        setupIntent();
        rb_mainhome.setChecked(true);
        rb_mainhome.setEnabled(true);
    }

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		 rb_attention.setChecked(false);
		 rb_mainhome.setChecked(false);
		 rb_video.setChecked(false);
		 rb_message.setChecked(false);
		 rb_my.setChecked(false);
		if(isChecked){
			switch (buttonView.getId()) {
			case R.id.rb_attention:
				rb_attention.setChecked(true);
				this.mTabHost.setCurrentTabByTag("A_TAB");
				
				break;
			case R.id.rb_mainhome:
				rb_mainhome.setChecked(true);
				this.mTabHost.setCurrentTabByTag("B_TAB");
		         
				break;
			case R.id.rb_video:
				startActivity(new Intent(this, MediaRecorderActivity.class));
				
//				rb_video.setChecked(true);
//				this.mTabHost.setCurrentTabByTag("C_TAB");
				break;
			case R.id.rb_message:
				rb_message.setChecked(true);
				this.mTabHost.setCurrentTabByTag("D_TAB");
				break;
			case R.id.rb_my:
				rb_my.setChecked(true);
				this.mTabHost.setCurrentTabByTag("E_TAB");
				break;
	        default:
	        	break;
			}
		}
		
	}
	
	private void setupIntent() {
		this.mTabHost = getTabHost();
		
		this.mTabHost.addTab(buildTabSpec("B_TAB", R.string.net_string_home,
				R.drawable.net_mainhome_icon_selector, this.mBIntent));
		 this.mTabHost.addTab(buildTabSpec("A_TAB",R.string.net_string_attention,
				R.drawable.net_attention_icon_selector, this.mAIntent));

		 

//		 this.mTabHost.addTab(buildTabSpec("C_TAB",
//				R.string.net_string_video, R.drawable.net_video_icon_selector,
//				this.mCIntent));

		 this.mTabHost.addTab(buildTabSpec("D_TAB", R.string.net_string_message,
				R.drawable.net_message_icon_selector, this.mDIntent));
		 
		 this.mTabHost.addTab(buildTabSpec("E_TAB", R.string.net_string_my,
		 R.drawable.net_mainmy_icon_selector, this.mEIntent));

		

	}
	
	private TabHost.TabSpec buildTabSpec(String tag, int resLabel, int resIcon,
			final Intent content) {
		return this.mTabHost.newTabSpec(tag).setIndicator(getString(resLabel),
				getResources().getDrawable(resIcon)).setContent(content);
	}

	/*public void setMyReceptTabOn(String gid) {
        MainActivity.this.rb_attention.setChecked(false);
        MainActivity.this.rb_mainhome.setChecked(false);
        MainActivity.this.rb_video.setChecked(false);
       // MainActivity.this.rb_goods.setChecked(false);
        MainActivity.this.rb_my.setChecked(false);
       // MainActivity.this.mRb_prePress = MainActivity.this.rb_home_tabrecept;
        MainActivity.this.rb_goods.setChecked(true);
        MainActivity.this.mTabHost.setCurrentTabByTag("C_TAB");
        Activity mCurrentAct = MainActivity.this.getCurrentActivity();
      
        if (mCurrentAct instanceof GoodsAct) {
            ((GoodsAct) mCurrentAct).goHomeView();
            ((GoodsAct) mCurrentAct).gotoGoodsBijia(gid);
        }

    }*/
	/*public void setMainMyTabOn(String headurl) {
		MainActivity.this.rb_mainhome.setChecked(false);
		MainActivity.this.rb_lianka.setChecked(false);
		// MainActivity.this.rb_goods.setChecked(false);
		MainActivity.this.rb_goods.setChecked(false);
		MainActivity.this.rb_my.setChecked(true);
		// MainActivity.this.mRb_prePress = MainActivity.this.rb_home_tabrecept;
		MainActivity.this.mTabHost.setCurrentTabByTag("D_TAB");
		Activity mCurrentAct = MainActivity.this.getCurrentActivity();
		
		if (mCurrentAct instanceof MainMyAct) {
			((MainMyAct) mCurrentAct).goHomeView();
			((MainMyAct) mCurrentAct).gotoMyHome(headurl);
		}
		
	}*/
	/*public void setLianKa(int type) {
		MainActivity.this.rb_mainhome.setChecked(false);
		//MainActivity.this.rb_lianka.setChecked(false);
		 MainActivity.this.rb_goods.setChecked(false);
		MainActivity.this.rb_my.setChecked(false);
		// MainActivity.this.mRb_prePress = MainActivity.this.rb_home_tabrecept;
		MainActivity.this.rb_lianka.setChecked(true);
		MainActivity.this.mTabHost.setCurrentTabByTag("B_TAB");
		Activity mCurrentAct = MainActivity.this.getCurrentActivity();
		if (mCurrentAct instanceof MessageAct) {
			((MessageAct) mCurrentAct).goHomeView();
			((MessageAct) mCurrentAct).gotoLianka(type);
		}
	}
		public void setLianKaHome() {
			MainActivity.this.rb_mainhome.setChecked(false);
			//MainActivity.this.rb_lianka.setChecked(false);
			MainActivity.this.rb_goods.setChecked(false);
			MainActivity.this.rb_my.setChecked(false);
			// MainActivity.this.mRb_prePress = MainActivity.this.rb_home_tabrecept;
			MainActivity.this.rb_lianka.setChecked(true);
			MainActivity.this.mTabHost.setCurrentTabByTag("B_TAB");
			Activity mCurrentAct = MainActivity.this.getCurrentActivity();
			if (mCurrentAct instanceof MessageAct) {
				((MessageAct) mCurrentAct).goHomeView();
				
			}
		}*/
/*			public void setLianKaLogin() {
				MainActivity.this.rb_lianka.setChecked(false);
				MainActivity.this.rb_mainhome.setChecked(false);
				MainActivity.this.rb_goods.setChecked(false);
				MainActivity.this.rb_lianka.setChecked(false);
				MainActivity.this.rb_my.setChecked(true);
				// MainActivity.this.mRb_prePress = MainActivity.this.rb_home_tabrecept;
				MainActivity.this.mTabHost.setCurrentTabByTag("D_TAB");
	}*/

}
