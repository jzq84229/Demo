二维码library
参照:https://github.com/neuyu/android-best-practices

二维码处理流程分为几个步骤：
初始化相机，设置一些相机参数；
绑定SurfaceView，在SurfaceView上显示预览图像；
获取相机的一帧图像；
对图像进行一定的预处理，只保留亮度信息，成为灰度图像；
对灰度图像进行二维码解析，解析成功进入下一步，不成功回到第③步；
返回解析结果并退出。

- CaptureActivity 二维码扫描页面
	- 实现SurfaceHolder.Callback用于相机显示回调