package com.zhang.common.lib.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by jun on 2019/2/27.
 */

public class CloseUtils {
    public CloseUtils() {
    }

    public static void closeQuietly(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (IOException var2) {
                var2.printStackTrace();
            }
        }

    }

    public static void closeQuietly(Closeable... closeables) {
        if (null != closeables) {
            Closeable[] var1 = closeables;
            int var2 = closeables.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                Closeable closeable = var1[var3];

                try {
                    if (null != closeable) {
                        closeable.close();
                    }
                } catch (IOException var6) {
                    var6.printStackTrace();
                }
            }
        }

    }
}