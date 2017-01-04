package net.wit.socket;

import java.io.Serializable;

public class Parameter {


	private String name;

	private String datatype;

	private String value;
	
	public Parameter(){
		this.name = "";
		this.datatype = "";
		this.value = "";
	}
	
	public Parameter(String name,String datatype,String value){
		this.name = name;
		this.datatype = datatype;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	

}
