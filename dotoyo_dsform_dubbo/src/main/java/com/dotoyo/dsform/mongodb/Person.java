/**
 * Person.java
 * ��Ȩ����(C) 2012 
 * ����:cuiran 2012-12-12 11:37:16
 */
package com.dotoyo.dsform.mongodb;

import com.dotoyo.dsform.model.AbstractModel;

/**
 * TODO
 * 
 * @author cuiran
 * @version TODO
 */
public class Person extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3617931430808763429L;

	private String id;
	private String name;
	private int age;

	public Person() {
		super();
	}

	public Person(String id, String name, int age) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @param age
	 *            the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * 
	 * @param name
	 * @param age
	 */
	public Person(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}

	public String toString() {
		return "Person[id=" + id + ",name=" + name + ",age=" + age + "]";
	}

}
