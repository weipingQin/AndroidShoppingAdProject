package com.www.goumei.activity;

import java.util.List;
import java.util.Stack;

import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.www.goumei.fragment.BaseFragment;
import com.www.goumei.utils.ScreenObserver;
import com.www.goumei.utils.UIHelper;

/**
 * 
 * @ClassName: BaseFragmentActivity
 * @author sunyouyi
 * @date 2015-6-1 上午6:22:59
 *
 */
public class BaseFragmentActivity extends FragmentActivity {
	private Stack<BaseFragment> mHistoryFragment;
	private FragmentManager mFragmentManager;
	private int iContainerID;
	private ScreenObserver mScreenObserver;
	public boolean isonGesture = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); // To change body of overridden
											// methods use File | Settings |
											// File Templates.

		this.mHistoryFragment = new Stack<BaseFragment>();
		this.mFragmentManager = getSupportFragmentManager();
		ActivityManager mActivityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

		// 获得当前正在运行的activity
		List<ActivityManager.RunningTaskInfo> appList3 = mActivityManager
				.getRunningTasks(1000);
		/*
		 * for (ActivityManager.RunningTaskInfo running : appList3) {
		 * System.out.println(running.baseActivity.getClassName());
		 * Log.e("running.baseActivity.getClassName()===",
		 * running.baseActivity.getClassName());
		 * if(running.baseActivity.getClassName
		 * ().equals("com.yipai.evencard.activity.MainActivity")){ Intent intent
		 * = new Intent(); intent.setClass(getApplicationContext(),
		 * GestureLockActivity.class); //
		 * intent.putExtra(GestureLockActivity.INTENT_MODE,
		 * GestureLockActivity.GESTURE_MODE_VERIFY);
		 * intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 * startActivity(intent); } }
		 */
	}

	public void goHomeView() {
		FragmentTransaction mFragmentTransaction = this.mFragmentManager
				.beginTransaction();
		while (this.mHistoryFragment.size() > 1)
			mFragmentTransaction.detach((Fragment) this.mHistoryFragment.pop());
		if (this.mHistoryFragment.size() == 1) {
			mFragmentTransaction.show((Fragment) this.mHistoryFragment.peek());
			((BaseFragment) this.mHistoryFragment.peek()).onReLoad(null);
		}
		mFragmentTransaction.commit();
	}

	public void goHomeView(Intent it) {
		FragmentTransaction mFragmentTransaction = this.mFragmentManager
				.beginTransaction();
		while (this.mHistoryFragment.size() > 1)
			mFragmentTransaction.detach((Fragment) this.mHistoryFragment.pop());
		if (this.mHistoryFragment.size() == 1) {
			mFragmentTransaction.show((Fragment) this.mHistoryFragment.peek());
			((BaseFragment) this.mHistoryFragment.peek()).onReLoad(it);
		}
		mFragmentTransaction.commit();
	}

	public void initShowFragment(BaseFragment fragment, String toFrag) {
		FragmentTransaction mFragmentTransaction = this.mFragmentManager
				.beginTransaction();
		mFragmentTransaction.add(this.iContainerID, fragment, toFrag);
		mFragmentTransaction.show(fragment);
		mFragmentTransaction.commit();
		this.mHistoryFragment.push(fragment);
	}

	// public void notifyReconizeCarplate(String carplate,Bitmap bp){
	// ((CarRecptFrag)this.mFragmentManager.findFragmentByTag("CarRecptFrag")).reconizeCarplate(carplate,bp);
	// }
	//
	// public void notifyDrivingLicense(Bitmap bp,int pictureId, int flag){
	// ((CarInfoFrag)this.mFragmentManager.findFragmentByTag("CarInfoFrag")).setDrivingLicenseImg(bp,
	// pictureId, flag);
	// }
	//
	// public void notifyCarImg(Bitmap bp,int pictureId, int flag){
	// ((MemberInfoFrag)this.mFragmentManager.findFragmentByTag("MemberInfoFrag")).setCarImg(bp,
	// pictureId, flag);
	// }
	// public void notifyCheckImg1(int pictureId){
	// ((CheckItemCarFace)this.mFragmentManager.findFragmentByTag("CheckItemCarFace")).setCheckImg(pictureId);
	// }
	// public void notifyCheckImg2(int pictureId){
	// ((CarBabyCheckItemDetails)this.mFragmentManager.findFragmentByTag("CarBabyCheckItemDetails")).setCheckImg(pictureId);
	// }

	public void dumpToNext(BaseFragment fragment, String toFrag) {
		FragmentTransaction mFragmentTransaction = this.mFragmentManager
				.beginTransaction();
		mFragmentTransaction.hide((Fragment) this.mHistoryFragment.peek());
		mFragmentTransaction.add(this.iContainerID, fragment, toFrag);
		mFragmentTransaction.show(fragment);
		mFragmentTransaction.commit();
		((BaseFragment) this.mHistoryFragment.peek()).onLeave();
		this.mHistoryFragment.push(fragment);
	}

	public void dumpBackAndToNext(BaseFragment fragment, String toFrag) {
		FragmentTransaction mFragmentTransaction = this.mFragmentManager
				.beginTransaction();
		mFragmentTransaction.remove((Fragment) this.mHistoryFragment.pop());
		// mFragmentTransaction.hide((Fragment)this.mHistoryFragment.peek());
		mFragmentTransaction.add(this.iContainerID, fragment, toFrag);
		mFragmentTransaction.show(fragment);
		mFragmentTransaction.commit();
		// ((BaseFragment)this.mHistoryFragment.peek()).onLeave();
		this.mHistoryFragment.push(fragment);
	}

	public void dumpBackCountAndToNext(BaseFragment fragment, String toFrag,
			int count) {
		FragmentTransaction mFragmentTransaction = this.mFragmentManager
				.beginTransaction();
		for (int i = 0; i < count; i++) {
			mFragmentTransaction.remove((Fragment) this.mHistoryFragment.pop());
		}

		// mFragmentTransaction.hide((Fragment)this.mHistoryFragment.peek());
		mFragmentTransaction.add(this.iContainerID, fragment, toFrag);
		mFragmentTransaction.show(fragment);
		mFragmentTransaction.commit();
		// ((BaseFragment)this.mHistoryFragment.peek()).onLeave();
		this.mHistoryFragment.push(fragment);
	}

	@Override
	public void onBackPressed() {
		// goBackAndShowPreView(null);

	}

	protected void onPause() {
		super.onPause();
		// MobclickAgent.onPause(this);
	}

	protected void onResume() {
		super.onResume();
		// MobclickAgent.onResume(this);
	}

	public void goBackAndShowPreView(Intent mIntent) {

		if (this.mHistoryFragment.size() > 1) {

			FragmentTransaction mFragmentTransaction = this.mFragmentManager
					.beginTransaction();
			mFragmentTransaction.remove((Fragment) this.mHistoryFragment.pop());
			mFragmentTransaction.show((Fragment) this.mHistoryFragment.peek());
			mFragmentTransaction.commit();
			((BaseFragment) this.mHistoryFragment.peek()).onReLoad(mIntent);
		} else {
			UIHelper.showChose(this, "确认退出？", "确认",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							/*
							 * SharedPreferences
							 * sharedPreferences=getSharedPreferences
							 * (LianKaLoginFragment.SETLIANKALOGIN_STAUS,
							 * MODE_PRIVATE);
							 * sharedPreferences.edit().putInt("login_state",
							 * 0).commit();
							 */
							finish();
						}
					}, "取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
		}

	}

	public void goBackCountAndShowPreView(Intent mIntent, int count) {

		if (this.mHistoryFragment.size() > count) {
			FragmentTransaction mFragmentTransaction = this.mFragmentManager
					.beginTransaction();
			for (int i = 0; i < count; i++) {
				mFragmentTransaction.remove((Fragment) this.mHistoryFragment
						.pop());
			}

			mFragmentTransaction.show((Fragment) this.mHistoryFragment.peek());
			mFragmentTransaction.commit();
			((BaseFragment) this.mHistoryFragment.peek()).onReLoad(mIntent);
		}

	}

	public void goIntentBackAndShow(String fragName, Intent mIntent) {
		if (this.mHistoryFragment.size() > 1) {
			FragmentTransaction mFragmentTransaction = this.mFragmentManager
					.beginTransaction();
			mFragmentTransaction.remove((Fragment) this.mHistoryFragment.pop());
			while (!this.mHistoryFragment.peek().getTag().equals(fragName)) {
				this.mHistoryFragment.pop();
			}

			mFragmentTransaction.show((Fragment) this.mHistoryFragment.peek());
			mFragmentTransaction.commit();
			((BaseFragment) this.mHistoryFragment.peek()).onReLoad(mIntent);
		} else {
			UIHelper.showChose(this, "确认退出？", "确认",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					}, "取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
		}

	}

	public void setiContainerID(int containerID) {
		this.iContainerID = containerID;
	}
	/* *//**
	 * 手势密码锁屏
	 */
	/*
	 * public void lockScreen() { mScreenObserver = new ScreenObserver(this);
	 * mScreenObserver.requestScreenStateUpdate(new ScreenStateListener() {
	 * 
	 * @Override public void onScreenStateChange(boolean isScreenOn) {
	 * SharedPreferences share =
	 * getSharedPreferences(MyPwdManagementFragment.GESTURE_PWD
	 * ,Context.MODE_PRIVATE); String pass = share.getString("gesture_PWD",
	 * null); Boolean is_openpwd=share.getBoolean("is_openpwd", false);
	 * if(is_openpwd &&(pass!=null)){ isonGesture = true; if (isScreenOn) {
	 * doSomethingOnScreenOn();//屏幕开着 } else { doSomethingOnScreenOff();//屏幕关了 }
	 * } } }); } private void doSomethingOnScreenOn() { //
	 * LogUtil.i("Screen is on"); } private void doSomethingOnScreenOff() { //
	 * LogUtil.i("Screen is off"); if(!GestureLockActivity.IS_SHOW){ Intent
	 * intent = new Intent(); intent.setClass(getApplicationContext(),
	 * GestureLockActivity.class); //
	 * intent.putExtra(GestureLockActivity.INTENT_MODE,
	 * GestureLockActivity.GESTURE_MODE_VERIFY);
	 * intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); startActivity(intent); }
	 * }
	 */
}
