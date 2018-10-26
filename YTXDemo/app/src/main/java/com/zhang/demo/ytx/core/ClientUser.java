package com.zhang.demo.ytx.core;

import android.os.Parcel;
import android.os.Parcelable;

import com.yuntongxun.ecsdk.ECInitParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/7/12.
 */
public class ClientUser implements Parcelable {

    private String userId;          //用户注册V账号
    private String userName;        //用户昵称
    private String appKey;          //用户注册Appkey
    private String appToken;        //用户注册Token
    private String password;
    private int sex;                //性别 1男
    private long birth;
    private int pVersion;
    private String signature;
    private ECInitParams.LoginAuthType loginAuthType;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public long getBirth() {
        return birth;
    }

    public void setBirth(long birth) {
        this.birth = birth;
    }

    public int getpVersion() {
        return pVersion;
    }

    public void setpVersion(int pVersion) {
        this.pVersion = pVersion;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public ECInitParams.LoginAuthType getLoginAuthType() {
        return loginAuthType;
    }

    public void setLoginAuthType(ECInitParams.LoginAuthType loginAuthType) {
        this.loginAuthType = loginAuthType;
    }

    public ClientUser(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId" , userId);
            jsonObject.put("userName" , userName);
            jsonObject.put("appKey" , appKey);
            jsonObject.put("appToken" , appToken);
            jsonObject.put("inviteCode" , password);
            jsonObject.put("sex" , sex);
            jsonObject.put("personSign" , signature);
            jsonObject.put("birth" , birth);
            jsonObject.put("pVersion" , pVersion);
            jsonObject.put("loginAuthType" , loginAuthType.getAuthTypeValue());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "ClientUser{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", appKey='" + appKey + '\'' +
                ", appToken='" + appToken + '\'' +
                ", inviteCode='" + password + '\'' +
                '}';
    }

    public ClientUser from(String input) {
        JSONObject object = null;
        try {
            object = new JSONObject(input);
            if(object.has("userId")) {
                this.userId = object.getString("userId");
            }
            if(object.has("userName")) {
                this.userName = object.getString("userName");
            }
            if(object.has("appKey")) {
                this.appKey = object.getString("appKey");
            }
            if(object.has("appToken")) {
                this.appToken = object.getString("appToken");
            }
            if(object.has("inviteCode")) {
                this.password = object.getString("inviteCode");
            }
            if(object.has("sex")) {
                this.sex = object.getInt("sex");
            }
            if(object.has("birth")) {
                this.birth = object.getLong("birth");
            }
            if(object.has("personSign")) {
                this.signature = object.getString("personSign");
            }
            if(object.has("pVersion")) {
                this.pVersion = object.getInt("pVersion");
            }
            if(object.has("loginAuthType")) {
                this.loginAuthType = ECInitParams.LoginAuthType.fromId(object.getInt("loginAuthType"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    protected ClientUser(Parcel in) {
        userId = in.readString();
        userName = in.readString();
        appKey = in.readString();
        appToken = in.readString();
        password = in.readString();
        sex = in.readInt();
        birth = in.readLong();
        pVersion = in.readInt();
        signature = in.readString();
        loginAuthType = ECInitParams.LoginAuthType.fromId(in.readInt());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(appKey);
        dest.writeString(appToken);
        dest.writeString(password);
        dest.writeInt(sex);
        dest.writeLong(birth);
        dest.writeInt(pVersion);
        dest.writeString(signature);
        dest.writeInt(loginAuthType.getAuthTypeValue());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ClientUser> CREATOR = new Creator<ClientUser>() {
        @Override
        public ClientUser createFromParcel(Parcel in) {
            return new ClientUser(in);
        }

        @Override
        public ClientUser[] newArray(int size) {
            return new ClientUser[size];
        }
    };

}
