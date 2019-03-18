package com.zhang.common.lib.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Shell命令工具类
 * Created by jun on 2019/2/27.
 */

public class ShellProcess {
    private static final int Length_ProcStat = 9;
    private static final String TAG = "AndroidSunlogin";

    private ShellProcess() {
    }

    public static HashMap<String, String> execCommand(String command, boolean isroot) {
        return parseShellResult(executeCommand(command, isroot));
    }

    public static HashMap<String, ArrayList<String>> execCommand2(String command, boolean isroot) {
        return parseShellResult2(executeCommand(command, isroot));
    }

    public static String executeCommand(String command, boolean isroot) {
        String output_string = "";
        if(command.isEmpty()) {
            return output_string;
        } else {
            try {
                Process e = null;
                if(isroot) {
                    e = RootTools.checkSuPermission(false);
                } else {
                    e = Runtime.getRuntime().exec("sh");
                }

                if(null == e) {
                    return output_string;
                }

                DataOutputStream ostream = new DataOutputStream(e.getOutputStream());
                InputStream istream = e.getInputStream();
                InputStreamReader inputstreamreader = new InputStreamReader(istream);
                BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
                ostream.writeBytes(command + "\n");
                ostream.writeBytes("exit\n");
                ostream.flush();
                String line = "";
                StringBuilder output_strbuilder = new StringBuilder(line);

                while((line = bufferedreader.readLine()) != null) {
                    output_strbuilder.append(line).append('\n');
                }

                output_string = output_strbuilder.toString();
                e.waitFor();
                e.destroy();
            } catch (Exception var10) {
                var10.printStackTrace();
            }

            return output_string;
        }
    }

    public static String executeCommandNoResult(String command, boolean isroot) {
        String output_string = "";
        InputStream istream = null;
        if(command.isEmpty()) {
            return output_string;
        } else {
            try {
                Process e = null;
                if(isroot) {
                    e = RootTools.checkSuPermission(false);
                } else {
                    e = Runtime.getRuntime().exec("sh");
                }

                if(null == e) {
                    String inputstreamreader1 = output_string;
                    return inputstreamreader1;
                }

                DataOutputStream ostream = new DataOutputStream(e.getOutputStream());
                istream = e.getInputStream();
                InputStreamReader inputstreamreader = new InputStreamReader(istream);
                new BufferedReader(inputstreamreader);
                ostream.writeBytes(command + "\n");
                ostream.writeBytes("exit\n");
                ostream.flush();
                e.waitFor();
                e.destroy();
            } catch (Exception var11) {
                var11.printStackTrace();
            } finally {
                CloseUtils.closeQuietly(istream);
            }

            return output_string;
        }
    }

    public static HashMap<String, String> parseShellResult(String cmdout) {
        String tempString = "";
        String[] rows = null;
        String[] columns = null;
        HashMap processMap = new HashMap();
        if(null != cmdout && !cmdout.isEmpty()) {
            rows = cmdout.split("[\n]+");

            for(int i = 0; i < rows.length; ++i) {
                tempString = rows[i];
                tempString = tempString.trim();
                columns = tempString.split("[ ]+");
                if(columns.length == 9) {
                    processMap.put(columns[1], columns[8]);
                }
            }

            return processMap;
        } else {
            return processMap;
        }
    }

    public static HashMap<String, ArrayList<String>> parseShellResult2(String cmdout) {
        String tempString = "";
        String[] rows = null;
        String[] columns = null;
        HashMap processMap = new HashMap();
        if(null != cmdout && !cmdout.isEmpty()) {
            rows = cmdout.split("[\n]+");

            for(int i = 0; i < rows.length; ++i) {
                tempString = rows[i];
                tempString = tempString.trim();
                columns = tempString.split("[ ]+");
                if(columns.length == 9) {
                    ArrayList array = new ArrayList();
                    array.add(columns[1]);
                    array.add(columns[2]);
                    array.add(columns[8]);
                    processMap.put(columns[1], array);
                }
            }

            return processMap;
        } else {
            return processMap;
        }
    }

    public static HashMap<String, String> execGlobalCommand(String command, boolean isroot) {
        return parseShellResult(executeGlobalCommand(command, isroot));
    }

    public static HashMap<String, ArrayList<String>> execGlobalCommand2(String command, boolean isroot) {
        return parseShellResult2(executeGlobalCommand(command, isroot));
    }

    public static String executeGlobalCommand(String command, boolean isroot) {
        String output_string = "";
        if(command.isEmpty()) {
            return output_string;
        } else {
            try {
                Process e = null;
                if(isroot) {
                    e = RootTools.checkGlobalSuPermission(false);
                } else {
                    e = Runtime.getRuntime().exec("sh");
                }

                if(null == e) {
                    return output_string;
                }

                DataOutputStream ostream = new DataOutputStream(e.getOutputStream());
                InputStream istream = e.getInputStream();
                InputStreamReader inputstreamreader = new InputStreamReader(istream);
                BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
                ostream.writeBytes(command + "\n");
                ostream.flush();
                String line = "";
                StringBuilder output_strbuilder = new StringBuilder(line);

                while((line = bufferedreader.readLine()) != null) {
                    output_strbuilder.append(line).append('\n');
                }

                output_string = output_strbuilder.toString();
            } catch (Exception var10) {
                var10.printStackTrace();
            }

            return output_string;
        }
    }
}
