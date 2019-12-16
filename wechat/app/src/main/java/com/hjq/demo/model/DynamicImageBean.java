package com.hjq.demo.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author GF
 * @des 图片
 * @date 2019/11/15
 */
public class DynamicImageBean implements MultiItemEntity {

    private String path;

    public int w;
    public int h;

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private int itemType=1;

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
