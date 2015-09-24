package com.www.goumei.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import android.app.Application;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.www.goumei.R;

public class MyApplication extends Application {
	public DisplayImageOptions options;

	private static MyApplication instance = null;
	private static Handler mMainThreadHandler = null;
	private static Looper mMainThreadLooper = null;
	private static Thread mMainThread = null;
	private static int mMainThreadId;
	private static HandlerCallback mHandlerCallback;

	public interface HandlerCallback {
		public void handleMessage(Message msg);
	}

	@Override
	public void onCreate() {
		// TODO
		this.instance = this;
		this.mMainThreadHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				mHandlerCallback.handleMessage(msg);
			}
		};
		this.mMainThreadLooper = getMainLooper();
		this.mMainThread = Thread.currentThread();
		this.mMainThreadId = android.os.Process.myTid();

		/* 全局捕获异常 */
//		Thread.currentThread().setUncaughtExceptionHandler(new MyExceptionHandler());

		initUilImageLoader();
		super.onCreate();
	}

	public void setHandlerCallback(HandlerCallback handlerCallback) {
		this.mHandlerCallback = handlerCallback;
	}

	public static MyApplication getInstance() {
		return instance;
	}

	public static void setInstance(MyApplication instance) {
		MyApplication.instance = instance;
	}

	public static Handler getMainThreadHandler() {
		return mMainThreadHandler;
	}

	public static void setMainThreadHandler(Handler mMainThreadHandler) {
		MyApplication.mMainThreadHandler = mMainThreadHandler;
	}

	public static Looper getMainThreadLooper() {
		return mMainThreadLooper;
	}

	public static void setMainThreadLooper(Looper mMainThreadLooper) {
		MyApplication.mMainThreadLooper = mMainThreadLooper;
	}

	public static Thread getMainThread() {
		return mMainThread;
	}

	public static void setMainThread(Thread mMainThread) {
		MyApplication.mMainThread = mMainThread;
	}

	public static int getMainThreadId() {
		return mMainThreadId;
	}

	public static void setMainThreadId(int mMainThreadId) {
		MyApplication.mMainThreadId = mMainThreadId;
	}

	private void initUilImageLoader() {
		options = new DisplayImageOptions.Builder()
		// .showStubImage(R.drawable.ic_launcher)// 设置图片在下载期间显示的图片
				.showImageForEmptyUri(R.drawable.ic_launcher)// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.ic_launcher)// 设置图片加载/解码过程中错误时候显示的图片
				.cacheInMemory(true)// 是否緩存都內存中
				.cacheOnDisc(true)// 是否緩存到sd卡上
				// .displayer(new RoundedBitmapDisplayer(20)).build();
				.displayer(new FadeInBitmapDisplayer(200)).build();
		// This configuration tuning is custom. You can tune every option,
		// you may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.threadPriority(Thread.NORM_PRIORITY - 2)// 设置线程的优先级
				.denyCacheImageMultipleSizesInMemory()// 当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
				.discCacheFileNameGenerator(new Md5FileNameGenerator())// 设置缓存文件的名字
				.discCacheFileCount(200)// 缓存文件的最大个数
				.tasksProcessingOrder(QueueProcessingType.LIFO)// 设置图片下载和显示的工作队列排序
				// .enableLogging() // 是否打印日志用于检查错误
				.defaultDisplayImageOptions(options).build();

		// Initialize ImageLoader with configuration
		ImageLoader.getInstance().init(config);
	}

	private class MyExceptionHandler implements Thread.UncaughtExceptionHandler {

		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			try {
				Log.e("TAG","发生了异常被捕获了");
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				ex.printStackTrace(pw);
				File file = new File(Environment.getExternalStorageDirectory(), "error.log");
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(sw.toString().getBytes());
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			/* 结束自我进程,让android系统重新启动自身 */
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

}
