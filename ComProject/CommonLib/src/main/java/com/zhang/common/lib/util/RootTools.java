package com.zhang.common.lib.util;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * root工具类
 * Created by jun on 2019/2/27.
 */

public class RootTools {
    private static final String TAG = "RootTools";
    public static final String LINUX_CMD_SH = "sh";
    public static final String LINUX_CMD_SU = "su";
    public static final String LINUX_CMD_ID = "id\n";
    public static final String LINUX_CMD_EXIT = "exit\n";
    private static final String LINUX_CMD_EXPORT_LIB = "export LD_LIBRARY_PATH=/vendor/lib:/system/lib\n";
    private static final String ID_UID_GID_REGEX = ".*uid=(\\d*).*gid=(\\d*).*";
    private static ProcessBuilder mSuProcessBuilder = null;
    private static Process mSuProcess = null;

    private RootTools() {
    }

    public static boolean isRootAvailable() {
        String[] places = new String[]{"/sbin/", "/system/bin/", "/system/xbin/", "/bin/", "/"};
        String[] var1 = places;
        int var2 = places.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            String where = var1[var3];
            File file = new File(where + "su");
            if(file.exists()) {
                return true;
            }
        }

        return false;
    }

    public static boolean isRootPermission() {
        boolean bsu = requestSuPermission();
        return bsu;
    }

    public static Process checkGlobalSuPermission(boolean needexit) {
        boolean result = false;

        try {
            if(null == mSuProcessBuilder) {
                mSuProcessBuilder = new ProcessBuilder(new String[]{"su"});
            }

            mSuProcessBuilder.redirectErrorStream(true);
            if(null == mSuProcess) {
                mSuProcess = mSuProcessBuilder.start();
                Log.d("AndroidSunlogin", "------ [debug] checkGlobalSuPermission, ProcessBuilder start");
            }

            DataOutputStream e = new DataOutputStream(mSuProcess.getOutputStream());
            InputStreamReader istream = new InputStreamReader(mSuProcess.getInputStream());
            BufferedReader reader = new BufferedReader(istream);
            e.writeBytes("id\n");
            if(needexit) {
                e.writeBytes("exit\n");
            }

            e.flush();
            String line = null;
            int uid = -1;
            int gid = -1;

            while(null != (line = reader.readLine())) {
                if(!TextUtils.isEmpty(line.trim())) {
                    Pattern pattern = Pattern.compile(".*uid=(\\d*).*gid=(\\d*).*");
                    Matcher matcher = pattern.matcher(line);
                    if(matcher.find()) {
                        uid = Integer.parseInt(matcher.group(1));
                        gid = Integer.parseInt(matcher.group(2));
                        break;
                    }
                }
            }

            if(0 == uid && 0 == gid) {
                result = true;
            }
        } catch (Exception var10) {
            var10.printStackTrace();
        }

        if(!result) {
            mSuProcess = null;
        }

        return mSuProcess;
    }

    public static Process checkSuPermission(boolean needexit) {
        boolean result = false;
        Process process = null;

        try {
            ProcessBuilder e = new ProcessBuilder(new String[]{"su"});
            e.redirectErrorStream(true);
            process = e.start();
            DataOutputStream ostream = new DataOutputStream(process.getOutputStream());
            InputStreamReader istream = new InputStreamReader(process.getInputStream());
            BufferedReader reader = new BufferedReader(istream);
            ostream.writeBytes("id\n");
            if(needexit) {
                ostream.writeBytes("exit\n");
            }

            ostream.flush();
            String line = null;
            int uid = -1;
            int gid = -1;

            while(null != (line = reader.readLine())) {
                if(!TextUtils.isEmpty(line.trim())) {
                    Pattern pattern = Pattern.compile(".*uid=(\\d*).*gid=(\\d*).*");
                    Matcher matcher = pattern.matcher(line);
                    if(matcher.find()) {
                        uid = Integer.parseInt(matcher.group(1));
                        gid = Integer.parseInt(matcher.group(2));
                        break;
                    }
                }
            }

            if(0 == uid && 0 == gid) {
                result = true;
            }
        } catch (Exception var12) {
            var12.printStackTrace();
        }

        if(!result) {
            process = null;
        }

        return process;
    }

    public static boolean requestSuPermission() {
        boolean result = false;

        try {
            Process e = checkSuPermission(true);
            if(null != e) {
                result = true;
                e.waitFor();
                e.destroy();
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

        Log.i("AndroidSunlogin", "[root] request su permission, result:" + result);
        return result;
    }

    public static String runSuCommand(String command) {
        Log.d("AndroidSunlogin", "[debug] runSuCommand, start command: " + command);
        Process process = null;
        DataOutputStream ostream = null;
        InputStream istream = null;
        StringBuffer cmdout = new StringBuffer();

        label121: {
            String e;
            try {
                process = checkSuPermission(false);
                if(null != process) {
                    ostream = new DataOutputStream(process.getOutputStream());
                    istream = process.getInputStream();
                    ostream.writeBytes(command + "\n");
                    ostream.writeBytes("exit\n");
                    ostream.flush();
                    BufferedReader e1 = new BufferedReader(new InputStreamReader(istream));
                    String line = null;

                    while((line = e1.readLine()) != null) {
                        cmdout.append(line).append(System.getProperty("line.separator"));
                    }

                    if(!TextUtils.isEmpty(line)) {
                        Log.d("AndroidSunlogin", "[debug] runSuCommand, result: " + line);
                    }

                    process.waitFor();
                    process.destroy();
                    break label121;
                }

                e = "";
            } catch (Exception var16) {
                var16.printStackTrace();
                break label121;
            } finally {
                try {
                    if(ostream != null) {
                        ostream.close();
                    }
                } catch (Exception var15) {
                    var15.printStackTrace();
                }

            }

            return e;
        }

        Log.d("AndroidSunlogin", "[debug] runSuCommand, start command: " + command);
        return cmdout.toString().trim();
    }

    public static String runSuCommandWithGlobalProcess(String command) {
        Process process = null;
        DataOutputStream ostream = null;
        InputStream istream = null;
        StringBuffer cmdout = new StringBuffer();

        String e;
        try {
            process = checkGlobalSuPermission(false);
            if(null != process) {
                ostream = new DataOutputStream(process.getOutputStream());
                istream = process.getInputStream();
                ostream.writeBytes(command + "\n");
                ostream.flush();
                BufferedReader e1 = new BufferedReader(new InputStreamReader(istream));
                String line = null;

                while((line = e1.readLine()) != null) {
                    cmdout.append(line).append(System.getProperty("line.separator"));
                }

                process.waitFor();
                if(!TextUtils.isEmpty(line)) {
                    Log.d("AndroidSunlogin", "[debug] runSuCommand, result: " + line);
                }

                return cmdout.toString().trim();
            }

            e = "";
        } catch (Exception var16) {
            var16.printStackTrace();
            return cmdout.toString().trim();
        } finally {
            try {
                if(ostream != null) {
                    ostream.close();
                }
            } catch (Exception var15) {
                var15.printStackTrace();
            }

        }

        return e;
    }

    public static boolean runCommandWithSu(String command) {
        Log.d("AndroidSunlogin", "[debug] runCommandWithSu, start command: " + command);
        Process process = null;
        DataOutputStream os = null;
        boolean result = false;

        label96: {
            boolean e;
            try {
                process = checkSuPermission(false);
                if(null != process) {
                    os = new DataOutputStream(process.getOutputStream());
                    os.writeBytes(command + "\n");
                    os.writeBytes("exit\n");
                    os.flush();
                    result = true;
                    process.waitFor();
                    process.destroy();
                    break label96;
                }

                e = false;
            } catch (Exception var15) {
                var15.printStackTrace();
                break label96;
            } finally {
                try {
                    if(os != null) {
                        os.close();
                    }
                } catch (Exception var14) {
                    var14.printStackTrace();
                }

            }

            return e;
        }

        Log.d("AndroidSunlogin", "[debug] runCommandWithSu, end command: " + command);
        return result;
    }
}
