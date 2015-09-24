package com.www.goumei.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.www.goumei.R;
import com.www.goumei.bean.ThemeBean;
import com.www.goumei.http.ApiClient;
import com.www.goumei.utils.ProcessUtil;

/**
 * 设置主题
 * 
 */
@SuppressLint("HandlerLeak")
public class ThemeActivity extends Activity implements OnClickListener {

	// 工具栏按钮
	private TextView textback, textfinish, texttheme;
	private int[] check = new int[9];// check=0表示未按 =1表示已按
	/** 主题 */
	List<ThemeBean> MyTheme;
	/** 主题列表 */
	private ListView myThemeListView;
	private ThemeTextAdapter adapter;
	/** 发现-数据加载完成 */
	private static final int LOAD_COMPLETE = 1002;
	/** 主题名称 */
	private String themeName = null;
	/** 主题ID */
	private int themeID = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.edit_cover_theme);
		// MyTheme = ApiClient.getTheme();
		initview();

	}

	private void initview() {
		for (int i = 0; i < check.length; i++) {
			check[i] = 0;
		}
		textback = (TextView) findViewById(R.id.tv_prior);
		texttheme = (TextView) findViewById(R.id.tv_title);
		texttheme.setText("请选择一个主题");
		textfinish = (TextView) findViewById(R.id.tv_later);
		textfinish.setText("完成");
		myThemeListView = (ListView) findViewById(R.id.myThemeListView);

		textback.setOnClickListener(this);
		textfinish.setOnClickListener(this);

		DialogHelper dialogHelper = new DialogHelper();
		dialogHelper.execute();

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case LOAD_COMPLETE:
				ProcessUtil.dismissProgressdialog();
				if (null != MyTheme) {
					adapter = new ThemeTextAdapter(ThemeActivity.this, MyTheme);
					myThemeListView.setAdapter(adapter);

				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {

		case R.id.tv_prior:
			finish();
			break;

		case R.id.tv_later:
			// 传递返回值回去
			Intent intent = new Intent(ThemeActivity.this, EditActivity.class);
			// themeID!=0 &&
			if (themeName != null) {
				intent.putExtra("themeID", themeID);
				intent.putExtra("themeName", themeName);
			}
			setResult(001, intent);
			finish();
			break;

		}
	}

	public class DialogHelper extends AsyncTask<Integer, Void, Integer> {

		public DialogHelper() {
		}

		@Override
		protected void onPreExecute() {
			ProcessUtil.showProgressDialog(ThemeActivity.this, "获取数据中...");
		}

		@Override
		protected Integer doInBackground(Integer... arg0) {
			MyTheme = ApiClient.getTheme();
			handler.sendEmptyMessage(LOAD_COMPLETE);
			return null;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			ProcessUtil.dismissProgressdialog();
		}

	}

	class ThemeTextAdapter extends BaseAdapter {

		private Context context;
		private List<ThemeBean> list;

		public ThemeTextAdapter(Context context, List<ThemeBean> list) {
			this.context = context;
			this.list = list;
		}

		@Override
		public int getCount() {
			int count = 0;
			if (list.size() % 2 == 0) {
				count = list.size() / 2;
			} else if (list.size() % 2 == 1) {
				count = list.size() / 2 + 1;
			}
			return count;
		}

		@Override
		public Object getItem(int position) {
			return list.get((position - 1) / 3);
		}

		@Override
		public long getItemId(int position) {
			return (position - 1) / 3;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(final int position, View view, ViewGroup rg) {
			viewHolder holder = null;
			if (view == null) {
				holder = new viewHolder();
				view = LayoutInflater.from(context).inflate(
						R.layout.item_theme, null);
				holder.t1 = (TextView) view.findViewById(R.id.theme1);
				holder.t2 = (TextView) view.findViewById(R.id.theme2);
				view.setTag(holder);
			} else {
				holder = (viewHolder) view.getTag();
			}
			holder.t1.setText(list.get(position * 2).getName());

			holder.t1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(context, list.get(position * 2).getName(),
							Toast.LENGTH_SHORT).show();
					themeName = list.get(position * 2).getName();
					themeID = list.get(position * 2).getThemeiD();
					texttheme.setText(themeName);
				}
			});
			if((position * 2 + 1)<list.size()){
				holder.t2.setText(list.get(position * 2 + 1).getName());
				holder.t2.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Toast.makeText(context,
								list.get(position * 2 + 1).getName(),
								Toast.LENGTH_SHORT).show();
						themeName = list.get(position * 2 + 1).getName();
						themeID = list.get(position * 2 + 1).getThemeiD();
						texttheme.setText(themeName);
					}
				});
			}

			return view;
		}

		private class viewHolder {
			private TextView t1;
			private TextView t2;
		}

	}

}
