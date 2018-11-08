package com.zhang.modapk;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String toolPath = "./src/main/resources/apktool/apktool";
        String apkPath = "src/main/resources/sample.apk";
        String packagePath = "output/app";
        String tempApkPath = "output/tempApk.apk";
        String keystorePath = "keyStore.jks";
        String storePass = "storepass";
        String alias = "alias";
        String keyPass = "keypass";

        depackageApk(toolPath, apkPath, packagePath);
        modifyData(packagePath);
        packateApk(toolPath, packagePath, tempApkPath);
        singApk(tempApkPath, keystorePath, storePass, alias, keyPass);
        System.out.println("system end");
    }

    /**
     * 反编译apk文件
     * @param toolPath
     * @param apkPath
     * @param outputPath
     */
    private static void depackageApk(String toolPath, String apkPath, String outputPath){
        File outFile = new File(outputPath);
        deleteDirectory(outFile.getParentFile());
        try {
            String cmdStr = toolPath + " d " + apkPath + " -o " + outputPath;
            System.out.println(cmdStr);
            Process process = Runtime.getRuntime().exec(cmdStr);
            if (process.waitFor() != 0) {
                System.out.println("解压失败。。。！");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * apk打包
     */
    private static void packateApk(String toolPath, String srcPath, String apkPath) {
        try {
            String cmdStr = toolPath + " b " + srcPath + " -o " + apkPath;
            Process process = Runtime.getRuntime().exec(cmdStr);
            if (process.waitFor() != 0) {
                System.out.println("打包失败...!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * apk签名
     */
    private static void singApk(String tempApkPath, String keyStorePath, String storePass, String alias, String keypass) {
        try {
            String cmdStr = "jarsigner -verbose -keystore " + keyStorePath + " -storepass " + storePass + " -keypass " + keypass + " -signedjar output/signedApk.apk -digestalg SHA1 -sigalg MD5withRSA " + tempApkPath + " " + alias;
            Process process = Runtime.getRuntime().exec(cmdStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改AndroidManifest.xml文件内容
     * @param path
     * @return
     */
    private static boolean modifyData(String path) {
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("文件夹不存在...");
            return false;
        }
        String xmlPath = path + "/AndroidManifest.xml";
//        Document document
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(xmlPath);
            Element root = document.getRootElement();
            System.out.println("root ele:" + root.getName());
            Element eleApplication = root.element("application");
            List eleList = eleApplication.elements("meta-data");
            Iterator iterator = eleApplication.elementIterator("meta-data");
            while (iterator.hasNext()) {
                Element eleMetaData = (Element) iterator.next();
                System.out.println("eleName: " + eleMetaData.getName());

                String attrName = eleMetaData.attributeValue("name");

                if ("arg1".equals(attrName)) {
                    eleMetaData.attribute("value").setValue("newArgValue1");
                } else if ("arg2".equals(attrName)) {
                    eleMetaData.attribute("value").setValue("newArgValue2");
                } else if ("arg3".equals(attrName)) {
                    eleMetaData.attribute("value").setValue("newArgValue3");
                }
            }

            FileWriter fileWriter = new FileWriter(xmlPath);
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(fileWriter, format);
            writer.write( document );
            writer.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 删除目录
     * @param file
     */
    private static boolean deleteDirectory(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                for (File listFile : file.listFiles()) {
                    deleteDirectory(listFile);
                }
            }
            if (!file.delete()) {
                return false;
            }
        }
        return true;
    }




}
