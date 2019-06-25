package com.hzw.shoppingcartdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ShoppingCartAdapter shoppingCartAdapter;
    private ExpandableListView expandableListView;
    private CheckBox cball;
    private TextView mTvmoney;
    private TextView mTvDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        expandableListView = findViewById(R.id.expandableList);
        mTvmoney = findViewById(R.id.tv_money);
        mTvDelete = findViewById(R.id.tv_delete);
        cball = findViewById(R.id.cb_all_election);
        shoppingCartAdapter = new ShoppingCartAdapter(this, expandableListView, getData());
        expandableListView.setAdapter(shoppingCartAdapter);

        cball.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                shoppingCartAdapter.selectAllCheck(isChecked);
            }
        });
        shoppingCartAdapter.setOnMoneyChanged(new ShoppingCartAdapter.OnMoneyChanged() {
            @Override
            public void onMoneyChanged(double money) {
                if (String.valueOf(money).endsWith(".0")) {
                    mTvmoney.setText(String.valueOf(money).replace(".0", ""));
                } else {
                    mTvmoney.setText(String.valueOf(money));
                }
            }
        });

        mTvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoppingCartAdapter.removeItemData();
                if (shoppingCartAdapter.getGroupCount() == 0) {
                    cball.setChecked(false);
                }
            }
        });
        shoppingCartAdapter.notifyDataSetChanged();
    }


    private ArrayList<ShoppingCartModel> getData() {
        int lv0Count = 16;
        int lv1Count = 2;

        ArrayList<ShoppingCartModel> res = new ArrayList<>();
        for (int i = 0; i < lv0Count; i++) {
            List<Goods> goodsList = new ArrayList<>();
            for (int j = 0; j < lv1Count; j++) {
                Goods goods = new Goods("廓形复古牛仔外套", "驼色；XL", "468", "url", 1, false);
                goodsList.add(goods);
            }
            ShoppingCartModel shoppingCartModel = new ShoppingCartModel("小艺", "hello", false, goodsList);
            res.add(shoppingCartModel);
        }
        return res;
    }
}
