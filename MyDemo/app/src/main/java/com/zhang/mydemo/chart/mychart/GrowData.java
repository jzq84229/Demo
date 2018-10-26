package com.zhang.mydemo.chart.mychart;

import java.util.Date;

/**
 * Created by zjun on 2015/9/11 0011.
 */
public class GrowData {
    public static final int TYPE_WEIGHT = 0;
    public static final int TYPE_HEIGHT = 1;
    public static final int MAX_WEIGHT = 100;
    public static final int MAX_HEIGHT = 200;
    private int type;
    private Double value;
    private Date createAt;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
