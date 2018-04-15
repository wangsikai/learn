package com.lanking.cloud.sdk.util;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 利用JAXB转换XML与实体BEAN.
 * 
 * @since 3.9.7
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年4月17日
 */
public class XmlBeanMarshall {
	private static Log logger = LogFactory.getLog(XmlBeanMarshall.class);

	/**
	 * XML字串转实体.
	 * 
	 * @param beanClass
	 *            实体类
	 * @param xml
	 *            XML字串
	 * @return Object 实体类对象
	 */
	@SuppressWarnings("rawtypes")
	public static Object xml2Bean(Class beanClass, String xml) {
		Object obj = null;
		try {
			JAXBContext context = JAXBContext.newInstance(beanClass);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			StringReader reader = new StringReader(xml);
			obj = unmarshaller.unmarshal(reader);
		} catch (JAXBException e) {
			logger.error("xml2Bean转换错误！", e);
		}
		return obj;
	}

	/**
	 * XML文件转实体.
	 * 
	 * @param beanClass
	 *            实体类
	 * @param xmlFile
	 *            XML文件
	 * @return Object 实体类对象
	 */
	@SuppressWarnings("rawtypes")
	public static Object xmlFile2Bean(Class beanClass, File xmlFile) {
		Object obj = null;
		try {
			JAXBContext context = JAXBContext.newInstance(beanClass);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			obj = unmarshaller.unmarshal(xmlFile);
		} catch (JAXBException e) {
			logger.error("xmlFile2Bean转换错误！", e);
		}
		return obj;
	}

	/**
	 * 实体转非格式化的XML字串.
	 * 
	 * @param obj
	 *            实体对象
	 * @param encoding
	 *            编码格式
	 * @param isFragment
	 *            是否省略XML头信息
	 * @return String XML字串
	 */
	public static String bean2Xml(Object obj, String encoding, boolean isFragment) {
		String xmlStr = "";
		try {
			JAXBContext context = JAXBContext.newInstance(obj.getClass());

			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);// 编码格式
			// marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);//
			// 是否格式化生成的XML串
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, isFragment);// 是否省略XML头信息
			StringWriter writer = new StringWriter();
			marshaller.marshal(obj, writer);
			xmlStr = writer.getBuffer().toString();
		} catch (JAXBException e) {
			logger.error("bean2Xml转换错误！", e);
		}
		return xmlStr;
	}
}
