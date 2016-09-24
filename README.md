# MultiTaskThreadDownloader

----------


# 一、设计思想 #
- 利用 “建筑者”设计模式搭建的一款下载器框架。屏蔽底层的具体实现，向上层提供接口调用底层的功能。
- 面向接口编程，接口与实现分离，类与类之间通过接口产生依赖。实现松散耦合的系统，便于以后的升级和拓展。

# 二、系统框图 #
![](https://github.com/zengfanyu/MultiTaskThreadDownloader/blob/master/info/%E7%B3%BB%E7%BB%9F%E8%AE%BE%E8%AE%A1%E6%A1%86%E5%9B%BE.png?raw=true)

# 三、Director与BuilderImpl的关系 #
![](https://github.com/zengfanyu/MultiTaskThreadDownloader/blob/master/info/DownloaderImpl%20onListener%20IDownloadTask%20and%20IConnctTask%20.png?raw=true)

# 四、Director的具体实现 #
![](https://github.com/zengfanyu/MultiTaskThreadDownloader/blob/master/info/DownloaderImpl%20implement%20IDownloader%20IDownloadTask.onDownloadListener%20%20IConnctTask.onConnectListener%20.png?raw=true)

# 五、参考资料 #
- [慕课网XRay_Chen老师的视频---Android-Service系列之多线程断点续传下载](http://www.imooc.com/learn/376)



