package com.zhang.demo.ytx.ui.contact;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.zhang.demo.ytx.core.ClientUser;
import com.zhang.demo.ytx.core.Phone;
import com.zhang.demo.ytx.storage.AbstractSQLManager;
import com.zhang.demo.ytx.storage.ContactSqlManager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 联系人信息
 * Created by Administrator on 2016/7/12.
 */
public class ECContacts implements Parcelable {

    private long id;
    private String contactid;               //联系人账号
    private String nickname;                //联系人昵称
    private int type;                       //联系人类型
    private String token;                   //联系人账号Token
    private String subAccount;              //联系人子账号
    private String subToken;                //联系人子账号Token
    private String remark;                  //备注(头像)
    private List<Phone> phoneList;
    // Other
    private String[] qpName;                //用于分隔全拼数组
    private String jpNumber;                //简拼对应的拨号键盘的数字
    private String jpName;                  //简拼
    private String qpNameStr;               //全拼对应的拨号盘数字
    private String[] qpNumber;              //保存拼音对应的拨号键盘的数字
    private long photoId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContactid() {
        return contactid;
    }

    public void setContactid(String contactid) {
        this.contactid = contactid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSubAccount() {
        return subAccount;
    }

    public void setSubAccount(String subAccount) {
        this.subAccount = subAccount;
    }

    public String getSubToken() {
        return subToken;
    }

    public void setSubToken(String subToken) {
        this.subToken = subToken;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Phone> getPhoneList() {
        return phoneList;
    }

    public String[] getQpName() {
        return qpName;
    }

    public void setQpName(String[] qpName) {
        this.qpName = qpName;
    }

    public String getJpNumber() {
        return jpNumber;
    }

    public void setJpNumber(String jpNumber) {
        this.jpNumber = jpNumber;
    }

    public String getJpName() {
        return jpName;
    }

    public void setJpName(String jpName) {
        this.jpName = jpName;
    }

    public String getQpNameStr() {
        return qpNameStr;
    }

    public void setQpNameStr(String qpNameStr) {
        this.qpNameStr = qpNameStr;
    }

    public String[] getQpNumber() {
        return qpNumber;
    }

    public void setQpNumber(String[] qpNumber) {
        this.qpNumber = qpNumber;
    }

    public long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(long photoId) {
        this.photoId = photoId;
    }

    public ECContacts() {

    }

    public ECContacts(String contactid) {
        this.contactid = contactid;
    }

    /**
     * 获取排序字母，默认值为"#"
     */
    public String getSortKey() {
        if (jpName == null || jpName.trim().length() <= 0) {
            return "#";
        }
        String c = jpName.substring(0, 1);
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c).matches()) {
            return c.toUpperCase();
        } else {
            return "#";
        }
    }

    public void addPhoneList(List<Phone> phoneList) {
        if (this.phoneList == null) {
            this.phoneList = new ArrayList<>();
        }
        this.phoneList.addAll(phoneList);
        phoneList.clear();
    }

    /**
     * 给联系人添加手机号
     * @param phone     手机号信息
     */
    public void addPhone(Phone phone) {
        if (this.phoneList == null) {
            this.phoneList = new ArrayList<>();
            //将联系人ID设置为手机号码
            setContactid(phone.getPhoneNum());
        }
        this.phoneList.add(phone);
    }

    public ContentValues buildContentValues() {
        ContentValues values = new ContentValues();
        values.put(AbstractSQLManager.ContactsColumn.CONTACT_ID, this.contactid);
        values.put(AbstractSQLManager.ContactsColumn.TYPE, this.type);
        values.put(AbstractSQLManager.ContactsColumn.USERNAME, this.nickname );
        values.put(AbstractSQLManager.ContactsColumn.SUBACCOUNT, this.subAccount );
        values.put(AbstractSQLManager.ContactsColumn.SUBTOKEN, this.subToken );
        values.put(AbstractSQLManager.ContactsColumn.TOKEN, this.token );
        values.put(AbstractSQLManager.ContactsColumn.REMARK, this.remark);
        return values;
    }

    public void setClientUser(ClientUser user) {
        setContactid(user.getUserId());
        setNickname(user.getUserName());
        setRemark(ContactLogic.CONVER_PHONTO[ContactSqlManager.getIntRandom(4, 0)]);
    }

    protected ECContacts(Parcel in) {
        id = in.readLong();
        contactid = in.readString();
        nickname = in.readString();
        type = in.readInt();
        token = in.readString();
        subAccount = in.readString();
        subToken = in.readString();
        remark = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(contactid);
        dest.writeString(nickname);
        dest.writeInt(type);
        dest.writeString(token);
        dest.writeString(subAccount);
        dest.writeString(subToken);
        dest.writeString(remark);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ECContacts> CREATOR = new Creator<ECContacts>() {
        @Override
        public ECContacts createFromParcel(Parcel in) {
            return new ECContacts(in);
        }

        @Override
        public ECContacts[] newArray(int size) {
            return new ECContacts[size];
        }
    };
}
