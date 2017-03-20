package com.xbc.utils.activity;

/**
 *@author xiaobo.cui 2014年11月26日 上午10:40:15
 *
 */
public class SortToken {
	/** 简拼 */
	public String simpleSpell = "";
	/** 全拼 */
	public String wholeSpell = "";
	/** 中文全名 */
	public String chName = "";
	@Override
	public String toString() {
		return "[simpleSpell=" + simpleSpell + ", wholeSpell=" + wholeSpell + ", chName=" + chName + "]";
	}
}
