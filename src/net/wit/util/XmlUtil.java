/**
 * 版权所有：厦门市巨龙软件工程有限公司
 * Copyright 2010 Xiamen Dragon Software Eng. Co. Ltd.
 * All right reserved. 
 *====================================================
 * 文件名称: XmlUtil.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2010-12-25			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 */
package net.wit.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * DOM文档解析
 * @ClassName:XmlUtil
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author:chenxy
 * @date 2011-8-24 上午9:55:41
 * @since JDK Version 1.5
 */
public class XmlUtil {

	/**
	 * 通过XML正文内容获得文档
	 * @param content
	 * @return
	 * @throws DocumentException
	 * @author:chenxy
	 */
	public static Document getDocumentHelper(String content) throws DocumentException {
		Document document = DocumentHelper.parseText(content);
		return document;
	}

	/**
	 * 通过XML正文内容获得文档
	 * @param content
	 * @return
	 * @throws DocumentException
	 * @author:chenxy
	 */
	public static Document getDocumentBySAX(String content) throws DocumentException {
		StringReader is = new StringReader(content);
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(is);
		return document;
	}

	/**
	 * 通过XML正文内容获得根节点.
	 * @param content XML正文.
	 * @return
	 * @throws DocumentException
	 */
	public static Element getRootFromContent(String content) throws DocumentException {
		Document document = getDocumentBySAX(content);
		return document.getRootElement();
	}

	/**
	 * 获得指定XML文件的根节点.
	 * @param xmlFilePath 文件路径.
	 * @return
	 */
	public static Element getRootElement(String xmlFilePath) {
		File xmlFile = new File(xmlFilePath);
		SAXReader saxReader = new SAXReader();
		Document document;
		try {
			document = saxReader.read(xmlFile);
		} catch (DocumentException e) {
			throw new java.lang.IllegalStateException("解析XML文件[" + xmlFilePath + "]出错!");
		}
		return document.getRootElement();
	}

	/**
	 * 格式化输出Document文档
	 * @param document
	 * @return
	 * @author:chenxy
	 */
	public static String docToString(Document document) {
		String s = "";
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			OutputFormat format = new OutputFormat("	", true, "UTF-8");
			XMLWriter writer = new XMLWriter(out, format);
			writer.write(document);
			s = out.toString("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 生成xml文件
	 * @param document
	 * @param filename 文件名
	 * @return
	 * @author:chenxy
	 */
	public static boolean docToXmlFile(Document document, String filename) {
		boolean flag = true;
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");
			XMLWriter writer = new XMLWriter(new FileWriter(new File(filename)), format);
			writer.write(document);
			writer.close();
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 生成xml文件
	 * @param document
	 * @param filename 文件名
	 * @return
	 * @author:chenxy
	 */
	public static boolean stringToXmlFile(String str, String filename) {
		boolean flag = true;
		try {
			Document doc = DocumentHelper.parseText(str);
			flag = docToXmlFile(doc, filename);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 读取XML文件
	 * @param filename
	 * @return
	 * @author:chenxy
	 */
	public static Document load(File file) {
		Document document = null;
		try {
			SAXReader saxReader = new SAXReader();
			saxReader.setEncoding("UTF-8");
			document = saxReader.read(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}

	/**
	 * 读取XML文件
	 * @param filename
	 * @return
	 * @author:chenxy
	 */
	public static Document load(String filename) {
		return load(new File(filename));
	}

	/**
	 * 读取XML输入流
	 * @param is
	 * @return
	 * @author:chenxy
	 */
	public static Document load(InputStream is) {
		Document document = null;
		try {
			SAXReader saxReader = new SAXReader();
			saxReader.setEncoding("UTF-8");
			document = saxReader.read(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}

	/**
	 * 指定编码获取Document文档
	 * @param is
	 * @param encode
	 * @return
	 * @author:chenxy
	 */
	public static Document load(InputStream is, String encode) {
		Document document = null;
		try {
			SAXReader saxReader = new SAXReader();
			saxReader.setEncoding(encode);
			document = saxReader.read(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}

	/**
	 * 文档转换
	 * @param document
	 * @param stylesheet
	 * @return
	 * @throws Exception
	 * @author:chenxy
	 */
	public static Document styleDocument(Document document, String stylesheet) throws Exception {
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer(new StreamSource(stylesheet));

		DocumentSource source = new DocumentSource(document);
		DocumentResult result = new DocumentResult();
		transformer.transform(source, result);

		Document transformedDoc = result.getDocument();
		return transformedDoc;
	}

	/**
	 * 获得该节点的XML文件.
	 * @param node
	 * @return
	 */
	public static String getXmlPathByNode(Element node) {
		if (node.getDocument().getName() == null) {
			return "未知名XML";
		} else {
			return "[" + node.getDocument().getName() + "]";
		}
	}

	/**
	 * 获得节点的属性值.
	 * @param node 节点.
	 * @param attributeName 属性名.
	 * @return
	 */
	public static String getAttributeValue(Element node, String attributeName) {
		return node.attributeValue(attributeName);
	}

	/**
	 * 获得节点的属性值.不允许为空
	 * @param node 节点.
	 * @param attributeName 属性名.
	 * @return
	 */
	public static String getMustAttributeValue(Element node, String attributeName) {
		String result = node.attributeValue(attributeName);
		if (StringUtils.isBlank(result)) {
			throw new java.lang.IllegalArgumentException("XML中的[" + node.getPath() + "]节点上没有[" + attributeName + "]属性");
		}
		return result;
	}

	/**
	 * 获得节点的属性值,如果为空,则返回默认值.
	 * @param node 节点.
	 * @param attributeName 属性名.
	 * @param defaultValue 默认值.
	 * @return
	 */
	public static String getAttributeValue(Element node, String attributeName, String defaultValue) {
		String result = node.attributeValue(attributeName);
		if (StringUtils.isNotBlank(result)) {
			return result;
		}
		return defaultValue;
	}

	/**
	 * 获取节点的TEXT值
	 * @param node
	 * @return
	 * @author:chenxy
	 */
	public static String getTextValue(Element node) {
		return node.getText();
	}

	/**
	 * 获取节点的TEXT值
	 * @param node
	 * @return
	 * @author:chenxy
	 */
	public static String getMustTextValue(Element node) {
		String result = node.getText();
		if (StringUtils.isBlank(result)) {
			throw new java.lang.IllegalArgumentException("XML中的[" + node.getPath() + "]节点上没有数据...");
		}
		return result;
	}

}
