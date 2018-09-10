package com.chen.baselibrary.util;

/**
 * @author chen
 * @date 2018/9/10 下午4:27
 * email xiuyi.chen@erinspur.com
 * desc 系统工具类
 */

public class SystemUtils {

    public static final String SYS_EMUI = "emui";
    public static final String SYS_MIUI = "miui";
    public static final String SYS_FLYME = "flyme";
    // 默认 未知
    public static final String SYS_UNKNOW = "";

    /**
     * 获取系统的名称，能够判断小米、华为、魅族系统
     * @return 系统名称
     */
    public static String getSystemName() {
        String SYS = SYS_UNKNOW;

        if (XiaoMiUtils.isMiUi()) {
            //小米
            SYS = SYS_MIUI;
        } else if (HuaWeiUtils.isEmui()) {
            //华为
            SYS = SYS_EMUI;
        } else if (MeiZuUtils.isFlyme()) {
            //魅族
            SYS = SYS_FLYME;
        }

        return SYS;
    }
}
