# android_repos
android 项目库<br/>
说明：BaseLibrary下的app模块是module，不可直接运行<br/>
使用方法：<br/>
1、添加maven仓库<br/>
maven {url "https://raw.githubusercontent.com/xiuyi/android_repos/master"}<br/>
2、引入<br/>
implementation 'com.chen:baselibrary:1.0'<br/>
3、build.gradle android节点下添加，否则会弹出异常<br/>
    compileOptions {<br/>
        sourceCompatibility JavaVersion.VERSION_1_8<br/>
        targetCompatibility JavaVersion.VERSION_1_8<br/>
    }<br/>
    packagingOptions {<br/>
        exclude 'META-INF/rxjava.properties'<br/>
    }<br/>
4、Application继承自BaseApplication<br/>
   style.xml MyAppTheme parent @style/AppTheme<br/>

5、使用黄油刀<br/>
在Project的build.gradle中添加<br/>
classpath 'com.jakewharton:butterknife-gradle-plugin:8.8.1'<br/>
在app的build.gradle中添加<br/>
apply plugin: 'com.jakewharton.butterknife'<br/>

6、dubug-db 默认端口号8081
buildTypes {
        debug {
            signingConfig signingConfigs.debug
            resValue("string", "PORT_NUMBER", "8081") //修改端口号
        }
    }
