package com.lanking.cloud.sdk.bean;

/**
 * 泛型对象转换器泛型抽象类
 * 
 * @since 3.9.7
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年4月13日
 * @param <D>
 *            目标对象
 * @param <S>
 *            源对象
 * @param <ID>
 *            对象标识
 */
public abstract class GenericConverter<D, S, ID> extends Converter<D, S, ID> {

	private Class<D> genericClass;

	public GenericConverter() {
		super();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public GenericConverter(Class genericClass) {
		super();
		this.genericClass = genericClass;
	}

	public Class<D> getGenericClass() {
		return genericClass;
	}

	public void setGenericClass(Class<D> genericClass) {
		this.genericClass = genericClass;
	}

}
