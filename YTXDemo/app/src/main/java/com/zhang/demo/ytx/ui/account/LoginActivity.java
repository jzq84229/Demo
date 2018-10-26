package com.zhang.demo.ytx.ui.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yuntongxun.ecsdk.ECInitParams;
import com.zhang.demo.ytx.R;
import com.zhang.demo.ytx.common.CCPAppManager;
import com.zhang.demo.ytx.common.base.CCPFormInputView;
import com.zhang.demo.ytx.common.dialog.ECProgressDialog;
import com.zhang.demo.ytx.common.utils.FileAccessor;
import com.zhang.demo.ytx.common.utils.ToastUtil;
import com.zhang.demo.ytx.core.ClientUser;
import com.zhang.demo.ytx.ui.ECSuperActivity;
import com.zhang.demo.ytx.ui.SDKCoreHelper;

public class LoginActivity extends ECSuperActivity implements View.OnClickListener, View.OnLongClickListener {

    private EditText ipEt;
    private EditText portEt;
    private EditText appkeyEt;
    private EditText tokenEt;
    private EditText mobileEt;
    private EditText mVoipEt;
    private Button signBtn;
    private CCPFormInputView mFormInputView;
    private CCPFormInputView mFormInputViewPassword;
    private ECProgressDialog mPostiondialog;
    ECInitParams.LoginAuthType mLoginAuthType = ECInitParams.LoginAuthType.NORMAL_AUTH;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initResourceRefs();
        getTopBarView().setTopBarToStatus(1, -1, R.drawable.btn_style_green, null, getString(R.string.app_title_switch),
                getString(R.string.app_name), null, this);
        getTopBarView().getMiddleButton().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                startActivity(new Intent(LoginActivity.this,));
                return false;
            }
        });

        registerReceiver(new String[]{SDKCoreHelper.ACTION_SDK_CONNECT});
    }

    private void initResourceRefs() {
        ipEt = (EditText) findViewById(R.id.ip);
        portEt = (EditText) findViewById(R.id.port);
        appkeyEt = (EditText) findViewById(R.id.appkey);
        tokenEt = (EditText) findViewById(R.id.token);
        mFormInputView = (CCPFormInputView) findViewById(R.id.mobile);
        mobileEt = mFormInputView.getFormInputEditView();
//		mobileEt.setInputType(InputType.TYPE_CLASS_PHONE);
        mFormInputViewPassword = (CCPFormInputView) findViewById(R.id.VoIP_mode);
        mVoipEt = mFormInputViewPassword.getFormInputEditView();
        // mVoipEt.setInputType(InputType.TYPE_CLASS_PHONE);
        mobileEt.requestFocus();
        // mobileEt.setText(ECSDKUtils.getLine1Number(this));
        signBtn = (Button) findViewById(R.id.sign_in_button);
        findViewById(R.id.server_config).setOnLongClickListener(this);
        signBtn.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initConfig();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initConfig() {
        String appkey = FileAccessor.getAppKey();
        String token = FileAccessor.getAppToken();
        appkeyEt.setText(appkey);
        tokenEt.setText(token);

        if (TextUtils.isEmpty(appkey) || TextUtils.isEmpty(token)) {
            signBtn.setEnabled(false);
            ToastUtil.showMessage(R.string.app_server_config_error_tips);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                hideSoftKeyBoard();
                String mobile = mobileEt.getText().toString().trim();
                String pass = mVoipEt.getText().toString().trim();
                if (mLoginAuthType == ECInitParams.LoginAuthType.NORMAL_AUTH
                        && TextUtils.isEmpty(mobile)) {
                    ToastUtil.showMessage(R.string.input_mobile_error);
                    return;
                } else if (mLoginAuthType == ECInitParams.LoginAuthType.PASSWORD_AUTH) {
                    if (TextUtils.isEmpty(mobile) || TextUtils.isEmpty(pass)) {
                        ToastUtil.showMessage(R.string.app_input_paras_error);
                        return;
                    }
                }
                String appKey = appkeyEt.getText().toString().trim();
                String token = tokenEt.getText().toString().trim();
                ClientUser clientUser = new ClientUser(mobile);
                clientUser.setAppKey(appKey);
                clientUser.setAppToken(token);
                clientUser.setLoginAuthType(mLoginAuthType);
                clientUser.setPassword(pass);
                CCPAppManager.setClientUser(clientUser);

                mPostiondialog = new ECProgressDialog(this, R.string.login_posting);
                mPostiondialog.show();

                SDKCoreHelper.init(this, ECInitParams.LoginMode.FORCE_LOGIN);
                break;
            case R.id.text_right:
                swithAccountInput();
                break;
            case R.id.text_left:
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    private void swithAccountInput() {
        if (mLoginAuthType == ECInitParams.LoginAuthType.NORMAL_AUTH) {
            //普通登陆模式
            mLoginAuthType = ECInitParams.LoginAuthType.PASSWORD_AUTH;
            mFormInputView.setInputTitle(getString(R.string.login_prompt_VoIP_account));
            mobileEt.setHint(R.string.login_prompt_VoIP_account_tips);
            mFormInputViewPassword.setVisibility(View.VISIBLE);
        } else {
            //密码登陆模式
            mLoginAuthType = ECInitParams.LoginAuthType.NORMAL_AUTH;
            mFormInputView.setInputTitle(getString(R.string.login_prompt_mobile));
            mobileEt.setHint(R.string.login_prompt_mobile_hint);
            mFormInputViewPassword.setVisibility(View.GONE);
        }
    }
}
