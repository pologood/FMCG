/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.template.method;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;
import net.wit.util.PhoneticZhCNUtil;

/**
 * 模板方法 - 字符串缩略
 * 
 * @author rsico Team
 * @version 3.0
 */
@Component("phoneticMethod")
public class PhoneticMethod implements TemplateMethodModel {

	/** 中文字符配比 */
	private static final Pattern PATTERN = Pattern.compile("[\\u4e00-\\u9fa5\\ufe30-\\uffa0]+$");

	@SuppressWarnings("rawtypes")
	public Object exec(List arguments) throws TemplateModelException {
		if (arguments != null && !arguments.isEmpty() && arguments.get(0) != null && StringUtils.isNotEmpty(arguments.get(0).toString())) {
			String chinese = arguments.get(0).toString();
			return new SimpleScalar(PhoneticZhCNUtil.getZhCNFirstChar(chinese));
		}
		return null;
	}

}