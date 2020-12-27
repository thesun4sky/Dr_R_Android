package com.coawesome.hosea.dr_r.dao;

/**
 * Created by Hosea on 2016-11-09.
 */
public class ResponseVO {
    private Object[] Items;
    private int Count;
    private int ScannedCount;

    public ResponseVO(Object[] items, int count, int scannedCount) {
        Items = items;
        Count = count;
        ScannedCount = scannedCount;
    }

    public Object[] getItems() {
        return Items;
    }

    public void setItems(Object[] items) {
        Items = items;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public int getScannedCount() {
        return ScannedCount;
    }

    public void setScannedCount(int scannedCount) {
        ScannedCount = scannedCount;
    }
}
