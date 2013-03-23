package com.vektor.ncpu;

public class GovParameter {
	private String key;
	private String val;
	public GovParameter(String key, String val){
		super();
		this.key=key;
		this.val=val;
	} 
	public String getKey(){
		return key;
	}
	public String getVal(){
		return val;
	}
}
