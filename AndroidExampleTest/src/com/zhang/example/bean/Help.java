package com.zhang.example.bean;

/** 
 * @Description: TODO(添加描述) 
 * @author sz.jun.zhang@gmail.com
 * @date 2013-7-11 上午7:48:40 
 * @version V1.0 
 */
public class Help {
	
	private String name;
	private String descr;
	
	public Help() {}
	public Help(String name, String descr){
		this.name = name;
		this.descr = descr;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}

}
