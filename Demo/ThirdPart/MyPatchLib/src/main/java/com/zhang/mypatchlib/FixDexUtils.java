package com.zhang.mypatchlib;

import android.content.Context;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashSet;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * 热修复工具类
 * 其核心原理就是通过更改含有bug的dex文件的加载顺序。在dex的加载中，若以找到方法则不会继续查找，
 * 所以如果能让修复之后的方法在含有bug的方法之前加载就能达到修复bug的目的。
 * Created by admin on 2017/8/22.
 */

public class FixDexUtils {

    private static HashSet<File> loadedDex = new HashSet<>();

    static {
        loadedDex.clear();
    }

    /**
     * 加载修复Dex文件
     * @param context
     */
    public static void loadFixedDex(Context context) {
        if (context == null) {
            return;
        }
        loadedDex.clear();

        //遍历所有修复的Dex
        File fileDir = context.getDir("odex", Context.MODE_PRIVATE);
        File[] fileList = fileDir.listFiles();
        for (File file : fileList) {
            if (file.getName().startsWith("patch") && file.getName().endsWith(".dex")) {
                loadedDex.add(file);
            }
        }

        //和apk中的Dex文件合并
        doDexInject(context, fileDir, loadedDex);
    }

    /**
     * 和apk中的Dex文件合并
     */
    private static void doDexInject(Context context, File fileDir, HashSet<File> loadedDex) {
        String optimizeDir = fileDir.getAbsolutePath() + File.separator + "opt_dex";
        File fopt = new File(optimizeDir);
        if (!fopt.exists()) {
            fopt.mkdirs();
        }

        try {
            //1.加载应用程序的dex
            //2.拿到系统的dex
            PathClassLoader pathClassLoader = (PathClassLoader) context.getClassLoader();
            //3.拿到插件的dex
            for (File dex : loadedDex) {
                DexClassLoader dexClassLoader = new DexClassLoader(
                        dex.getAbsolutePath(),  //String
                        optimizeDir,            //String optimizedDirectory
                        null,                   //String librarySearchPath
                        pathClassLoader);       //ClassLoader parent

                /**
                 * BaseDexClassLoader ---> DexPathList
                 * DexPathList ---> Element[] dexElements
                 * 把Element[] dexElements合并
                 * {@link BaseDexClassLoader}
                 */
                Object pathObj = getPathList(pathClassLoader);
                Object dexObj = getPathList(dexClassLoader);

                Object pathDexElements = getDexElements(pathObj);
                Object dexDexElements = getDexElements(dexObj);

                // 合并apk中DexElements和dex中DexEmlements
                Object dexElements = combineArray(dexDexElements, pathDexElements);
                //需要重写赋值给DexPathList.dexElements
                Object pathList = getPathList(pathClassLoader);
                setField(pathList, pathList.getClass(), "dexElements", dexElements);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取BaseDexClassLoader中DexPathList变量
     * @param obj
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static Object getPathList(Object obj) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        return getField(obj, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
    }

    /**
     * 获取DexPathList中dexElements变量
     * @param pathObj
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static Object getDexElements(Object pathObj) throws NoSuchFieldException, IllegalAccessException {
        return getField(pathObj, pathObj.getClass(), "dexElements");
    }

    /**
     * 获取类中的成员变量
     * @param obj
     * @param clz
     * @param fieldName
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static Object getField(Object obj, Class clz, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = clz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

    /**
     * 设置成员变量的值
     * @param obj
     * @param clz
     * @param fieldName
     * @param value
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static void setField(Object obj, Class clz, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = clz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    /**
     * 合并数组
     * @param arrayLhs
     * @param arrayRhs
     * @return
     */
    private static Object combineArray(Object arrayLhs, Object arrayRhs) {
        Class componentType = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i + Array.getLength(arrayRhs);

        Object result = Array.newInstance(componentType, j);
        for (int k = 0; k < j; k++) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayLhs, k));
            } else {
                Array.set(result, k, Array.get(arrayRhs, k - i));
            }
        }
        return result;
    }
}
