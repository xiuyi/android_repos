package com.chen.baselibrary.util;
/**
 * @author chen
 * @date 2018/9/6 下午5:38
 * email xiuyi.chen@erinspur.com
 * desc
 */
public class HanziToPinyin {
    private static HanziToPinyin instance;
    private HanziToPinyin(){}
    public static synchronized HanziToPinyin getInstance(){
        if(instance == null) {
            instance = new HanziToPinyin();
        }
        return instance;
    }

    /**
     * 汉字转换位汉语拼音首字母，英文字符不变
     * @param chines 汉字
     * @return 拼音
     */
    public String converterToFirstSpell(String chines){

        return chines;
    }

    /**
     * 汉字转换位汉语拼音，英文字符不变
     * @param chines 汉字
     * @return 拼音
     */
    public String converterToSpell(String chines){

        return chines;
    }
}
