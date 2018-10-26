动态热修复补丁

apk使用的类加载器为PathClassLoader

android 查找Class流程
1. PathClassLoader.findClass -> DexPathList.findClass -> 迭代dexElements并查找Class名称，若clazz != null 返回clazz
2. PathClassLoader中的DexPathList对象在PathClassLoader的父类BaseDexClassLoader构造函数中初始化；
3. DexPathList中dexElements在DexPathList的构造函数中初始化；

5. 将.class文件转为.dex文件
好，现在我们使用class文件生成对应的dex文件。生成dex文件所需要的工具为dx，dx工具位于sdk的build-tools文件夹内.
```
dx --dex [--output=<file>] [<file>.class | <file>.{zip,jar,apk} | <directory>]
```