package com.hzw.shoppingcartdemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Copyright (C)：  蚁秀App
 *
 * @Author: huangzhewei
 * @CreateDate: 2019/6/25 21:58
 * @Version: 1.0
 * @Description: java类作用描述
 */
public class ShoppingCartAdapter extends BaseExpandableListAdapter {
    private List<ShoppingCartModel> expandListDataEntities;
    private Context context;
    private ExpandableListView listView;
    private OnChildCheckedListener onChildCheckedListener;
    private OnGroupChecked onGroupChecked;
    private OnMoneyChanged moneyChanged;

    public ShoppingCartAdapter(Context context, ExpandableListView listView, List<ShoppingCartModel> commentsBeanList) {
        this.expandListDataEntities = commentsBeanList;
        this.context = context;
        this.listView = listView;
        this.listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });// 屏蔽收缩
    }


    /*返回group分组的数量，在当前需求中指代评论的数量。*/
    @Override
    public int getGroupCount() {
        return expandListDataEntities.size();
    }


    /*返回所在group中child的数量，这里指代当前评论对应的回复数目*/
    @Override
    public int getChildrenCount(int i) {
        if (expandListDataEntities.get(i).getList() == null) {
            return 0;
        } else {
            return (expandListDataEntities.get(i).getList().size() > 0) ? expandListDataEntities.get(i).getList().size() : 0;
        }
    }

    /*返回group的实际数据，这里指的是当前评论数据。*/
    @Override
    public Object getGroup(int i) {
        return expandListDataEntities.get(i);
    }

    /*返回group中某个child的实际数据，这里指的是当前评论的某个回复数据。*/
    @Override
    public Object getChild(int i, int i1) {
        return expandListDataEntities.get(i).getList().get(i1);
    }

    /*返回分组的id，一般将当前group的位置传给它。*/
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /*返回分组中某个child的id，一般也将child当前位置传给它，不过为了避免重复，可以使用getCombinedChildId(groupPosition, childPosition);来获取id并返回*/
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return getCombinedChildId(groupPosition, childPosition);
    }

    /*表示分组和子选项是否持有稳定的id，这里返回true即可。*/
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /*即返回group的视图，一般在这里进行一些数据和视图绑定的工作，一般为了复用和高效，可以自定义ViewHolder，用法与listview一样，这里就不多说了*/
    @Override
    public View getGroupView(final int groupPosition, boolean isExpand, View convertView, ViewGroup viewGroup) {
        final ShoppingCartModel infoBean = expandListDataEntities.get(groupPosition);
        GroupHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_shoppingcart, viewGroup, false);
            viewHolder = new GroupHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GroupHolder) convertView.getTag();
        }
        if (infoBean != null) {
            viewHolder.name.setText(infoBean.getName());
            viewHolder.cbname.setChecked(infoBean.isChecked());
            viewHolder.cbname.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (buttonView.isPressed()) {
                        selectAllCheckByGroup(groupPosition, isChecked);
                        if (onGroupChecked != null) {
                            onGroupChecked.onCheck(groupPosition, isChecked);
                        }
                    }
                }
            });
        }

        return convertView;
    }

    /*返回分组中child子项的视图，比较容易理解，第一个参数是当前group所在的位置，第二个参数是当前child所在位置。*/
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
        final ChildHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_shoppingcart_goods, viewGroup, false);
            viewHolder = new ChildHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ChildHolder) convertView.getTag();
        }
        final Goods infoBean = expandListDataEntities.get(groupPosition).getList().get(childPosition);
        viewHolder.name.setText(infoBean.name);
        viewHolder.color.setText(infoBean.color);
        viewHolder.money.setText(infoBean.money);
        viewHolder.cbitem.setChecked(infoBean.isChecked);
        viewHolder.etnumber.setText(String.format("%d", infoBean.goodsCount));
        viewHolder.tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = expandListDataEntities.get(groupPosition).getList().get(childPosition).goodsCount;
                count = count + 1;
                expandListDataEntities.get(groupPosition).getList().get(childPosition).setGoodsCount(count);
                viewHolder.etnumber.setText(Integer.toString(count));
                notifyDataSetChanged();
            }
        });
        viewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count = expandListDataEntities.get(groupPosition).getList().get(childPosition).goodsCount;
                if (count > 1) {
                    count = count - 1;
                    expandListDataEntities.get(groupPosition).getList().get(childPosition).setGoodsCount(count);
                    viewHolder.etnumber.setText(Integer.toString(count));
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "不能再少了", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewHolder.etnumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                View content = View.inflate(context, R.layout.dialog, null);
                dialog.setView(content);
                final EditText edt = content.findViewById(R.id.et_input_count);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int count = Integer.parseInt(edt.getText().toString().trim());
                        if (count >= 1) {
                            expandListDataEntities.get(groupPosition).getList().get(childPosition).setGoodsCount(count);
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, "不能再少了", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });


        viewHolder.cbitem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    expandListDataEntities.get(groupPosition).getList().get(childPosition).setChecked(isChecked);
                    nattilyChiChecked();
                    if (onChildCheckedListener != null) {
                        onChildCheckedListener.onChildChecked(groupPosition, childPosition, isChecked);
                    }
                }
            }
        });
        return convertView;
    }

    /*表示分组中的child是否可以选中，这里返回true。*/
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


    private class GroupHolder {
        private GroupHolder(View viewRoot) {
            cbname = viewRoot.findViewById(R.id.cb_name);
            name = viewRoot.findViewById(R.id.tv_name);
        }

        private TextView name;
        private CheckBox cbname;

    }

    private class ChildHolder {
        private TextView tvDelete;
        private TextView tvAdd;
        private TextView etnumber;
        private ImageView goods;
        private TextView name;
        private TextView color;
        private TextView money;
        private CheckBox cbitem;

        private ChildHolder(View viewRoot) {
            goods = viewRoot.findViewById(R.id.iv_goods);
            name = viewRoot.findViewById(R.id.tv_name);
            color = viewRoot.findViewById(R.id.tv_color);
            money = viewRoot.findViewById(R.id.tv_money);
            cbitem = viewRoot.findViewById(R.id.cb_item);
            tvDelete = viewRoot.findViewById(R.id.tv_delete);
            etnumber = viewRoot.findViewById(R.id.et_number);
            tvAdd = viewRoot.findViewById(R.id.tv_add);
        }
    }


    public void selectAllCheck(boolean checked) {
        for (int i = 0; i < getGroupCount(); i++) {
            for (int j = 0; j < getChildrenCount(i); j++) {
                expandListDataEntities.get(i).getList().get(j).setChecked(checked);
            }
            expandListDataEntities.get(i).setChecked(checked);
        }
        notifyDataSetChanged();
    }

    public double selectAllCheckMoney() {
        double money = 0;
        for (int i = 0; i < getGroupCount(); i++) {
            for (int j = 0; j < getChildrenCount(i); j++) {
                if (expandListDataEntities.get(i).getList().get(j).isChecked) {
                    int goodsCount = expandListDataEntities.get(i).getList().get(j).goodsCount;
                    double countMoney = goodsCount * Double.valueOf(expandListDataEntities.get(i).getList().get(j).getMoney());
                    money = money + countMoney;
                }
            }
        }
        return money;
    }


    private void selectAllCheckByGroup(int groupPosition, boolean checked) {
        for (int j = 0; j < getChildrenCount(groupPosition); j++) {
            expandListDataEntities.get(groupPosition).getList().get(j).setChecked(checked);
        }
        expandListDataEntities.get(groupPosition).setChecked(checked);
        notifyDataSetChanged();
    }

    public void removeItemData() {
        if (getSelectedCount() == 0) {
            Toast.makeText(context, "请选择后再删除", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < expandListDataEntities.size(); i++) {
            Iterator<Goods> it = expandListDataEntities.get(i).getList().iterator();
            while (it.hasNext()) {
                boolean x = it.next().isChecked;
                if (x) {
                    it.remove();
                }
            }
        }

        Iterator<ShoppingCartModel> it = expandListDataEntities.iterator();
        while (it.hasNext()) {
            if (it.next().getList().size() == 0) {
                it.remove();
            }
        }


        notifyDataSetChanged();
    }

    public int getGroupSelectedCount() {
        int count = 0;
        for (int x = 0; x < getGroupCount(); x++) {
            if (expandListDataEntities.get(x).isChecked()) {
                count = count + 1;
            }
        }
        return count;
    }

    private int getSelectedCount() {
        int count = 0;
        for (int i = 0; i < getGroupCount(); i++) {
            for (int x = 0; x < getChildrenCount(i); x++) {
                if (expandListDataEntities.get(i).getList().get(x).isChecked()) {
                    count = count + 1;
                }
            }
        }
        return count;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        for (int i = 0; i < getGroupCount(); i++) {
            listView.expandGroup(i);
        }
        if (moneyChanged != null) {
            moneyChanged.onMoneyChanged(selectAllCheckMoney());
        }
    }

    private void nattilyChiChecked() {
        for (int i = 0; i < getGroupCount(); i++) {
            boolean checked = true;
            int childCount = getChildrenCount(i);
            for (int j = 0; j < childCount; j++) {
                if (!expandListDataEntities.get(i).getList().get(j).isChecked()) {
                    checked = false;
                }
            }
            expandListDataEntities.get(i).setChecked(checked);
        }
        notifyDataSetChanged();
    }

    void setOnGroupChecked(OnGroupChecked onGroupChecked) {
        this.onGroupChecked = onGroupChecked;
    }

    void setOnChildCheckedListener(OnChildCheckedListener onChildCheckedListener) {
        this.onChildCheckedListener = onChildCheckedListener;
    }

    void setOnMoneyChanged(OnMoneyChanged onMoneyChanged) {
        this.moneyChanged = onMoneyChanged;
    }

    public interface OnGroupChecked {
        void onCheck(int GroupPosition, boolean checked);
    }

    public interface OnChildCheckedListener {
        void onChildChecked(int groupPosition, int childPosition, boolean isChecked);
    }

    public interface OnMoneyChanged {
        void onMoneyChanged(double money);
    }

}
