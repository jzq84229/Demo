package com.zhang.sophixlib;

import android.app.Application;

import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

/**
 * Created by admin on 2017/8/17.
 */

public class SophixHelper {

    public interface MsgDisplayListener {
        void handle(String msg);
    }

    public static MsgDisplayListener msgDisplayListener = null;
    public static StringBuilder cacheMsg = new StringBuilder();

    private static SophixHelper instance;

    private SophixHelper(){}

    public static SophixHelper getInstance() {
        if (instance == null) {
            synchronized (SophixHelper.class) {
                if (instance == null) {
                    instance = new SophixHelper();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化Sophix热补丁
     */
    public void initSophix(Application app) {
        String appVersion;
        try {
            appVersion = app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName;
        } catch (Exception e) {
            appVersion = "1.0.0";
        }
        String idSecret = "24585963-1";
        String appSecret = "67d3084c33f267e93d7f59e62be09d05";
        String rsaSecret = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCQFLJIv28J6Iini3JJOWS88GK9MBqZgc+jnSGZ9kB9s5Hn289LmDUxhuNBoxJPYBu4ocSDBkKaNacmdfE1BR5iH/bP4SGoCsFSp4U611yTctxDWljSFRPQzqpuJXJX6YkWD9tPswZTaFzw6oW0pP9AmV2jIvB0rLedZ8MkNbuC6I7d1/kZREay7DGkKVrBF9hSdteeL0QzCwtwk/Rf8EiItxUmAv+7SOrHDNm7BzGHyOz8B80DIcDvwH8nwl7XXTDRSC9bE2WVmLw8QaQ6PojFym487HePuS27RkKEjwo3bbDM9s8sHwC8sLBkJIhjJ2tbZsh9CF2V5CLt7esvUW7LAgMBAAECggEAOMr/r69N7KDtZOWRqg1miRdURHC2u3WwzDR+7zpjF2l2BOffIVQg+6HI6LGH4SPoKXISjPclv7WQapeo+ZZH8LPazPxYEO4UYClKWfRUOIbZDe+J/V1zjyY3he1a0g5rC/g6IYNW5f/ISuqWw6saNnN9tnVqRQNj6Tbic95+SE4aR+2Dp7bNHiLnHOLfozwXCPymQ88pwxILpGhRKanVMiJnrxpWY058LJjRlAoUXCe9lvd+QCqSvARh2vetjp1zx5k9F+Re9nYifMpsuA8i7wr3cyF0Vc94hEg2FGRvSgZBaJGbEzUrFthJYM7zmb2mTKZlZ2FxwG3EVUGbK1lYqQKBgQDt3Sqgr3VvngFQ62vsDzv9bdwDkn3Xq/JGcE5pLmgGs5Zp1AyxI9IJPQph9GigEKCZOBY9f4i7R0OZZEWwoWCPpTZMZ7YGmd3ZAP8oCjiPX7c2RmfUHJFqgtSupA3ioXyxvgVJoJX/QlLRUo6Y8WUJU9VSePl8NpSmfs72y4uuZwKBgQCbEP2ecZKetJgPIaAfuPJv06HmwsCt65L9cVzdoiMttL8PN0PRDsojcEEOdLLM0qddEE7HrSGo08DAT53GKPSRqa0tHUTJRw2xyERnDhp/W5JYu9VMt3IaSl0AVXR2nKKQ2mlTJ9H/mttyOnXabcDWK2Up5BSoDiTgUx3NB0d1/QKBgQCIRKEMJxinFiT437YXKW8bs9lMbpsLeGU2t1YkacKMvEuaWzHeNYA5bK/LCEL8GCp5PKl5F9wUNjJi6RAHrZvtDx/5S5z7USDIFgsXPCvE73e9bz5CRQ+Frmf7mN6fjBYsmYjb8lScRLm/U4V/1yMj4dQfCFEdOQOWEqWsu1VNVwKBgGe+WE/W4Cc537nP31cZ32kdh+XhShOR1gel/mFxhmOf12uQp9iIMoi/I2dOgAk0rjA/JtXEaQ6QjRacrYvT/rwtbuUN/eVDmk/lEo54CxZF54ViNXRcDwSz18DPa1mntcN/vJObwo1lx4eGwS5f23HuGoKKS5+bxuQl93wicJbpAoGBAJa7BY2zZaeHrPk+SwoRVKjN3TephPxIsL7LNaycLTXv14ocS/Tmmyu5IAaEPEvQL11A302tmBlMWPfCoHcNCjtP7EejxQzAvZdw0mOI6g+BSgn24j1NSEdVN6omPSJaMQWNmSt0YhVEj6faMKyWfZouADTFyl8gZ/tqJ1Td5pX3";

        SophixManager.getInstance().setContext(app)
                .setAppVersion(appVersion)
                .setAesKey(null)
                .setEnableDebug(true)
                .setSecretMetaData(idSecret, appSecret, rsaSecret)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
//                        // 补丁加载回调通知
//                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
//                            // 表明补丁加载成功
//                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
//                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
//                            // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
//                            SophixManager.getInstance().killProcessSafely();
//                        } else {
//                            // 其它错误信息, 查看PatchStatus类说明
//                        }
                        String msg = new StringBuilder("").append("Mode:").append(mode)
                                .append(" Code:").append(code)
                                .append(" Info:").append(info)
                                .append(" HandlePatchVersion:").append(handlePatchVersion).toString();
                        if (msgDisplayListener != null) {
                            msgDisplayListener.handle(msg);
                        } else {
                            cacheMsg.append("\n").append(msg);
                        }
                    }
                }).initialize();
    }

    /**
     * 查询可用补丁
     */
    public void queryAndLoadNewPatch() {
        // queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中
        SophixManager.getInstance().queryAndLoadNewPatch();
    }
}
