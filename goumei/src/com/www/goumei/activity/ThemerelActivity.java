package com.www.goumei.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.www.goumei.R;

/**
 * 添加相关
 * 
 */
public class ThemerelActivity extends Activity implements OnClickListener {

	// 工具栏按钮
	private TextView textback, textfinish, texttheme;
	private EditText themerel_relation_web;
	private EditText themerel_description;
	/** 关联网址 */
	private String webSite;
	/** 对应描述 */
	private String description;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_themerel);
		initview();
	}

	private void initview() {
		// TODO 自动生成的方法存根
		textback = (TextView) findViewById(R.id.tv_prior);
		texttheme = (TextView) findViewById(R.id.tv_title);
		texttheme.setText("关联产品");
		textfinish = (TextView) findViewById(R.id.tv_later);
		textfinish.setText("完成");
		themerel_relation_web = (EditText) findViewById(R.id.themerel_relation_web);
		themerel_description = (EditText) findViewById(R.id.themerel_description);

		textback.setOnClickListener(this);
		textfinish.setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		// TODO 自动生成的方法存根
		switch (arg0.getId()) {

		case R.id.tv_prior:
			finish();
			break;

		case R.id.tv_later:

			webSite = themerel_relation_web.getText().toString().trim();
			description = themerel_description.getText().toString().trim();

			if (webSite == null) {
				new AlertDialog.Builder(this).setMessage("请输入关联网址")
						.setPositiveButton("确定", null);
				return;
			}

			// 传递返回值回去
			Intent intent = new Intent(ThemerelActivity.this, EditActivity.class);
			intent.putExtra("webSite", webSite);
			intent.putExtra("description", description);
			setResult(003, intent);
			finish();

			break;
		}
	}
}