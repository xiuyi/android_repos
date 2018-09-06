package com.chen.baselibrary.util;


import com.chen.baselibrary.widget.InitialLetter;

import java.util.Comparator;

/**
 * 字母排序
 * @author chenxiuyi
 *
 */
public class LetterComparator implements Comparator<InitialLetter> {
	String letter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	/**
	 * 排列规则：字母在最前面，数字在末尾但并不是按照从小到大排列，其他字符在数字之后
	 */
	@Override
	public int compare(InitialLetter curr, InitialLetter other) {
			if(curr.getIntialLetter()==null || curr.getIntialLetter().length()==0)
				return -1;
			if(other.getIntialLetter()==null || other.getIntialLetter().length()==0)
				return 1;
			char currFirst = curr.getIntialLetter().toUpperCase().charAt(0);
			char anotherFirst = other.getIntialLetter().toUpperCase().charAt(0);
			//两者都是数字
			if('#'==currFirst && '#'==anotherFirst){
				return currFirst-anotherFirst;
			}
			//curr是数字
			else if('#' == currFirst){
				return 1;
			}
			//other是数字
			else if('#' == anotherFirst){
				return -1;
			}
			else{
				int currIndex = letter.indexOf(currFirst);
				int anotherIndex = letter.indexOf(anotherFirst);
				return currIndex - anotherIndex;
			}
	}

}
