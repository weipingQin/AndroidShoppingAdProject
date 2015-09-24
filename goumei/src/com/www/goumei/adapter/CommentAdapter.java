package com.www.goumei.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.www.goumei.R;
import com.www.goumei.activity.HomePageActivity;
import com.www.goumei.activity.VideoDetailActivity;
import com.www.goumei.bean.CommentData;
import com.www.goumei.bean.Videos;
import com.www.goumei.bean.VideosDataList;
import com.www.goumei.http.req.ApiClientC;
import com.www.goumei.http.req.SP_ID;
import com.www.goumei.utils.Constant;
import com.www.goumei.utils.L;
import com.www.goumei.utils.ProcessUtil;
import com.www.goumei.views.CircleImageView;
import com.yixia.camera.demo.utils.ToastUtils;
/**
 * 
* @ClassName: PraiseMeAdapter 
* @Description: TODO
* @author 
* @date 2015-6-7 上午12:41:54 
*
 */
public class CommentAdapter extends BaseAdapter {
	private int selectedIndex=-1;
	private Activity context;
	private List<CommentData> list;
	public ImageLoader imageLoader; //用来下载图片的类
	private static DisplayImageOptions options;
	public CommentAdapter(Activity context,List<CommentData> list2) {
		this.context=context;
		this.list=list2;
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		
		 options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.test)
		.showImageForEmptyUri(R.drawable.test)
		.showImageOnFail(R.drawable.test)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
	}
	public void setData(List<CommentData> list3){
		this.list=list3;
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public CommentData getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup rg) {
		viewHolder holder = null;
		if (view == null) {
			holder = new viewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.layout_comment_item, null);
			holder.icon=(CircleImageView) view.findViewById(R.id.fansIcon);
			holder.name=(TextView) view.findViewById(R.id.fansName);
			holder.time=(TextView) view.findViewById(R.id.fansSex);
			holder.follow=(ImageView) view.findViewById(R.id.isfollow);
			holder.signature=(TextView)view.findViewById(R.id.fansId);
			holder.ll_comment_op=(LinearLayout)view.findViewById(R.id.ll_comment_op);
			holder.rl_comment_top=(RelativeLayout)view.findViewById(R.id.rl_comment_top);
			holder.reply=(RelativeLayout)view.findViewById(R.id.rl_reply);
			holder.detail=(RelativeLayout)view.findViewById(R.id.rl_detail);
			holder.delete=(RelativeLayout)view.findViewById(R.id.rl_delete);
			holder.line=(ImageView) view.findViewById(R.id.fans_item_line);
			view.setTag(holder);
		} else {
			holder=(viewHolder) view.getTag();
		}
		final CommentData bean = list.get(position);
		holder.icon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent homeIntent=new Intent(context,HomePageActivity.class);
				homeIntent.putExtra(Constant.OTHER_ID, ""+bean.getReplyUserID());
				context.startActivity(homeIntent);
				
			}
		});
		holder.rl_comment_top.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				selectedIndex=position;
				notifyDataSetChanged();
			}
		});
		if(selectedIndex==position){
			holder.ll_comment_op.setVisibility(View.VISIBLE);
		}else{
			holder.ll_comment_op.setVisibility(View.GONE);
		}
//		if(bean.getHeadsculpture()!=null){
//			imageLoader.displayImage(bean.getHeadsculpture(), holder.icon, options);
//		}
		
//		holder.name.setText(bean.getDisplayName());
		if(bean.getCommonetTime()!=null){
			holder.time.setText(bean.getCommonetTime());
		}
//		if(bean.getPraiseVideoCover()!=null){
//			imageLoader.displayImage(bean.getPraiseVideoCover(),holder.follow, options);
//		}
		if(bean.getContent()!=null){
			holder.signature.setText(bean.getContent());
		}
		final String id=SP_ID.id;
		holder.follow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			}
		});
		holder.reply.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new GetDetailVideosTask(context, bean.getVideosID(),bean.getReplyUserID()).execute("");
			}
		});
		holder.detail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new GetDetailVideosTask(context, bean.getVideosID(),0).execute("");
			}
		});
		holder.delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ToastUtils.showLongToast(context, "删除");
				new DeleteCommentTask(bean.getId()+"",SP_ID.id , position).execute("");
			}
		});
		if (position==(list.size()-1)) {
			holder.line.setVisibility(View.GONE);
		}
		return view;
	}

	private class viewHolder {
		private CircleImageView icon;
		private TextView name;
		private TextView signature;
//		private ImageView sex;
		private TextView time;
		private ImageView follow;
		private ImageView line;
		private RelativeLayout rl_comment_top;
		private LinearLayout ll_comment_op;
		private RelativeLayout reply;
		private RelativeLayout detail;
		private RelativeLayout delete;
	}
	
	class DeleteCommentTask extends AsyncTask<String, String, String>{
		String code;
		String id;
		String createId;
		int index;
		public DeleteCommentTask(String id,String createId,int index){
			this.id=id;
			this.createId=createId;
			this.index=index;
		}
		@Override
		protected String doInBackground(String... arg0) {
			code=ApiClientC.DeleteComment(new String[]{id}, createId);
			
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			L.e("code:::::::::::::::::::::::::"+code);
			if(code!=null &&code.equals("1")){
				
				Intent intent = new Intent();
				intent.setAction(Constant.ACTION_COMMENT_DELETE);
				intent.putExtra(Constant.INTENT_INDEX,index);
				intent.putExtra(Constant.INTENT_STATUS,Constant.INTENT_STATUS_FALSE);
				context.sendBroadcast(intent);
			}
			
		}
	}
	
	
	class GetDetailVideosTask extends AsyncTask<String, String, String> {
		VideosDataList videoList;
		int videoId;
		Activity pActivity;
		int repID;
		public GetDetailVideosTask(Activity pActivity,int id,int repID){
			this.videoId=id;
			this.pActivity=pActivity;
			this.repID=repID;
		}
		@Override
		protected String doInBackground(String... arg0) {
			videoList = ApiClientC.getVideosDetailById(videoId+"",1);
			
			return null;
		}
		
		@Override
		protected void onPreExecute() {
			ProcessUtil.showProgressDialog(pActivity, "详情加载中...");
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			ProcessUtil.dismissProgressdialog();
			if (videoList != null) {
				if (videoList.getModels() != null
						&& videoList.getModels().size()>0) {
					Videos videosDetail=videoList.getModels().get(0);
					//Intent it = new Intent(pActivity, SurfaceViewTestActivity.class);
					Intent it = new Intent(pActivity, VideoDetailActivity.class);
					it.putExtra(Constant.VIDEO, videosDetail);
					if(repID!=0){
						it.putExtra(Constant.REPLYUSERID, repID);
					}
					pActivity.startActivity(it);
				}else{
					ToastUtils.showToast(pActivity, R.string.net_failure);
				}
			}else{
				ToastUtils.showToast(pActivity, R.string.net_failure);
			}
			
		}
	}
	
}
