package com.hzw.shoppingcartdemo;

import java.util.List;

/**
 * Copyright (C)：  蚁秀App
 *
 * @Author: huangzhewei
 * @CreateDate: 2019/6/25 22:00
 * @Version: 1.0
 * @Description: java类作用描述
 */
public class ShoppingCartModel {
    private String name;
    private String content;
    private boolean isChecked;
    private List<Goods> list;

    public ShoppingCartModel(String name, String content, boolean isChecked, List<Goods> list) {
        this.name = name;
        this.content = content;
        this.isChecked = isChecked;
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public List<Goods> getList() {
        return list;
    }

    public void setList(List<Goods> list) {
        this.list = list;
    }
}
