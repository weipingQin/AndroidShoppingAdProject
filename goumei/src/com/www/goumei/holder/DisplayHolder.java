package com.www.goumei.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.www.goumei.R;
import com.www.goumei.views.CircleImageView;
public class DisplayHolder {
	public ImageView graphIv;
	public com.www.goumei.emoji.EmoticonsTextView descTv;
	public RadioButton favRb;
	public CircleImageView brandIv;
	public ImageView veriIv;
	public RelativeLayout layout;
	
	public DisplayHolder(View v) {
		graphIv = (ImageView) v.findViewById(R.id.iv_graph);
		descTv = (com.www.goumei.emoji.EmoticonsTextView) v.findViewById(R.id.tv_desc);
		favRb = (RadioButton) v.findViewById(R.id.rb_rate);
		brandIv = (CircleImageView) v.findViewById(R.id.iv_brand);
		veriIv = (ImageView) v.findViewById(R.id.iv_verified);
		layout=(RelativeLayout) v.findViewById(R.id.layout_item_gridview_home_hot);
	}
	
}
