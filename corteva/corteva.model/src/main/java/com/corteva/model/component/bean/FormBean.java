package com.corteva.model.component.bean;

/**
 * @author Sapient
 *This is a POJO to hold form fields name and value 
 */
public class FormBean {

	/**
	 * name of form
	 */
	private String name;
	/**
	 * value of form
	 */
	private String value;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	

}
