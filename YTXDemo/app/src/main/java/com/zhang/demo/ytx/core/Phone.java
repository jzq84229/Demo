package com.zhang.demo.ytx.core;

import com.zhang.demo.ytx.common.utils.DemoUtils;

/**
 * 手机号信息
 * Created by Administrator on 2016/7/12.
 */
public class Phone {
    public static final int CUSTOM_TYPE = 0;
    public long rawContactId;       // 联系人ID
    public long id;                 // 数据表ID
    public int type;                // 类型 0为自定义类型
    public String customLabel;      // 自定义
    private String phoneNum;        // 手机号码

    public Phone(int type, String phoneNum) {
        super();
        this.type = type;
        this.phoneNum = phoneNum;
    }

    public Phone(String phoneNum, String customLabel) {
        super();
        phoneNum = DemoUtils.filteUnNumber(phoneNum);
        this.type = CUSTOM_TYPE;
        this.phoneNum = phoneNum;
        this.customLabel = customLabel;
    }

    public Phone(long rawContactId, long id, int type, String phoneNum) {
        super();
        phoneNum = DemoUtils.filteUnNumber(phoneNum);
        this.rawContactId = rawContactId;
        this.id = id;
        this.type = type;
        this.phoneNum = phoneNum;
    }

    public Phone(long rawContactId, long id, String phoneNum, String customLabel) {
        super();
        phoneNum = DemoUtils.filteUnNumber(phoneNum);
        this.rawContactId = rawContactId;
        this.id = id;
        this.phoneNum = phoneNum;
        this.customLabel = customLabel;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        phoneNum = DemoUtils.filteUnNumber(phoneNum);
        this.phoneNum = phoneNum;
    }

    public void releaseResources() {
        phoneNum = null;
        customLabel = null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((phoneNum == null) ? 0 : phoneNum.hashCode());
        result = prime * result + type;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        Phone other = (Phone) o;
        if (phoneNum == null) {
            if (other.phoneNum != null) {
                return false;
            }
        } else if (!phoneNum.equals(other.phoneNum)) {
            return false;
        }
        return true;
    }
}
