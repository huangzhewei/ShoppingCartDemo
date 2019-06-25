package com.hzw.shoppingcartdemo;

/**
 * Copyright (C)：  蚁秀App
 *
 * @Author: huangzhewei
 * @CreateDate: 2019/6/25 22:02
 * @Version: 1.0
 * @Description: java类作用描述
 */
public class Goods {
    public String name;
    public String color;
    public String money;
    public String url;
    public boolean isChecked;
    public int goodsCount;

    public Goods(String name, String color, String money, String url, int goodsCount, boolean isChecked) {
        this.name = name;
        this.color = color;
        this.money = money;
        this.url = url;
        this.goodsCount = goodsCount;
        this.isChecked = isChecked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
    }
}
