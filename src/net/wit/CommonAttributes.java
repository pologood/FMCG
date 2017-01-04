/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit;

/**
 * 公共参数
 * 
 * @author rsico Team
 * @version 3.0
 */
public final class CommonAttributes {

	/** 日期格式配比 */
	public static final String[] DATE_PATTERNS = new String[] { "yyyy", "yyyy-MM", "yyyyMM", "yyyy/MM", "yyyy-MM-dd", "yyyyMMdd", "yyyy/MM/dd", "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss", "yyyy/MM/dd HH:mm:ss" };

	/** wit.xml文件路径 */
	public static final String WIT_XML_PATH = "/wit.xml";

	/** wit.properties文件路径 */
	public static final String WIT_PROPERTIES_PATH = "/wit.properties";

	/**
	 * 不可实例化
	 */
	private CommonAttributes() {
	}

}