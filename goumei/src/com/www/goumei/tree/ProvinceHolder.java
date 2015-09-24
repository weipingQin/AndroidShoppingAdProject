package com.www.goumei.tree;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.sample.R;
import com.unnamed.b.atv.sample.holder.IconTreeItemHolder;
import com.unnamed.b.atv.sample.holder.IconTreeItemHolder.IconTreeItem;
import com.www.goumei.bean.ProvinceData;

/**
 * Created by Bogdan Melnychuk on 2/13/15.
 */
public class ProvinceHolder extends TreeNode.BaseNodeViewHolder<ProvinceHolder.ProvinceTreeItem > {

    private PrintView arrowView;

    public ProvinceHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, ProvinceHolder.ProvinceTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_header_node, null, false);
        TextView tvValue = (TextView) view.findViewById(R.id.node_value);
        tvValue.setText(value.province.getProvinceName());

        final PrintView iconView = (PrintView) view.findViewById(R.id.icon);
//        iconView.setIconText(context.getResources().getString(value.icon));
//        iconView.setVisibility(View.GONE);

        arrowView = (PrintView) view.findViewById(R.id.arrow_icon);
//        if (node.isLeaf()) {
//            arrowView.setVisibility(View.INVISIBLE);
//        }

        return view;
    }

    @Override
    public void toggle(boolean active) {
        arrowView.setIconText(context.getResources().getString(active ? R.string.ic_keyboard_arrow_down : R.string.ic_keyboard_arrow_right));
    }
    public static class ProvinceTreeItem {
        public int icon;
        public ProvinceData province;

        public ProvinceTreeItem( ProvinceData province) {
            
            this.province = province;
        }
    }
    @Override
    public int getContainerStyle() {
        return R.style.TreeNodeStyleCustom;
    }

}
