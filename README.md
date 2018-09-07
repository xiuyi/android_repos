# android_repos
android 项目库
说明：BaseLibrary下的app模块是module，不可直接运行
使用方法：
1、添加maven仓库
maven {url "https://raw.githubusercontent.com/xiuyi/android_repos/master"}
2、引入
implementation 'com.chen:baselibrary:1.0'
3、build.gradle android节点下添加，否则会弹出异常
compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
4、Application继承自BaseApplication
   style.xml MyAppTheme parent @style/AppTheme
