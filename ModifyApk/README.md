# java修改APK文件并重新打包签名

本项目为java项目  
##### 使用工具：
- apktool
- dom4j
- jarsigner

##### 实现过程：

1. 使用apktool反编译apk  
    ```
    apktool d apkPath -o outputPath
    ```
2. 使用dom4j解析AndroidMainifest.xml
    ```
    SAXReader reader = new SAXReader();
    Document document = reader.read(xmlPath);
    Element root = document.getRootElement();
    ```
3. 修改文件内容，并写回文件
    ```
    FileWriter fileWriter = new FileWriter(xmlPath);
    OutputFormat format = OutputFormat.createPrettyPrint();
    XMLWriter writer = new XMLWriter(fileWriter, format);
    writer.write( document );
    writer.close();
    ```
4. 使用apktool重新打包apk
    ```
    apktool b srcPath -o tempApkPath;
    ```

5. 使用jarsigner签名apk
    ```
    jarsigner -verbose -keystore keyStorePath -storepass storePass -keypass keypass -signedjar output/signedApk.apk -digestalg SHA1 -sigalg MD5withRSA tempApkPath alias           
    ```
