package com.www.goumei.utils;

import android.R.integer;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import org.apache.http.message.BasicNameValuePair;

import com.www.goumei.R;







import java.util.List;

public class UIHelper {

	public static void show(){
		
	}


	public static void reSetListViewHeight(ListView listView) {

		ListAdapter listAdapter = listView.getAdapter();

		if (listAdapter == null) {

		// pre-condition

		return;

		}

		int totalHeight = 0;

		for (int i = 0; i < listAdapter.getCount(); i++) {

		View listItem = listAdapter.getView(i, null, listView);

		listItem.measure(0, 0);

		totalHeight += listItem.getMeasuredHeight();

		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();

		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

		listView.setLayoutParams(params);

	}
	public static void showMsg(Activity aty,String msg){
		LayoutInflater inflater3 =(LayoutInflater) aty.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater3.inflate(R.layout.my_toast,null);
    	TextView textView = (TextView) view.findViewById(R.id.text);
    	textView.setText(msg);
    	Toast toast=null;
    	if(aty.getParent()==null){
    		toast = new Toast(aty);
    	}else{
    		toast = new Toast(aty.getParent());
    	}
		toast.setView(view);
		toast.setDuration(1000);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	/**
	 * 清除缓存成功
	 * @param aty
	 */
	public static void showClearMsg(Activity aty){
		ImageView iv=new ImageView(aty);
		iv.setImageResource(R.drawable.icon_cleared);
		Toast toast=null;
		if(aty.getParent()==null){
			toast = new Toast(aty);
		}else{
			toast = new Toast(aty.getParent());
		}
		toast.setView(iv);
		toast.setDuration(1000);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}




    public static void showChose(Context context, String msg, String posStr,
                                 DialogInterface.OnClickListener posClickListen,
                                 String negStr, DialogInterface.OnClickListener negClick
                                )
    {

        AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
        localBuilder.setMessage(msg);
        localBuilder.setPositiveButton(posStr,posClickListen);
        localBuilder.setNegativeButton(negStr,negClick);
        Dialog dialog = localBuilder.show();
        dialog.setCanceledOnTouchOutside(true);
    }

   /* public static String showEditChose(Context context, String title, String posStr,
                                 DialogInterface.OnClickListener posClickListen,
                                 String neuStr, DialogInterface.OnClickListener neuClick,
                                 DialogInterface.OnCancelListener cancelClick)
    {
        LayoutInflater factory = LayoutInflater.from(context);
        final View textEntryView = factory.inflate(R.layout.alert_dialog_text_entry1, null);
        EditText et = (EditText) textEntryView.findViewById(R.id.et_input);
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
        localBuilder.setTitle(title);
        localBuilder.setView(textEntryView);
        localBuilder.setPositiveButton(posStr, posClickListen);
        localBuilder.setNeutralButton(neuStr, neuClick);
        if (cancelClick != null)
            localBuilder.setOnCancelListener(cancelClick);
        localBuilder.show();
        return et.getText().toString().trim();
    }
*/

}
