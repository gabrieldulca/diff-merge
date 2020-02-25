package main.java.com.diffmerge.dto;

import java.util.List;

public class DiffDto {
	
	public TypeDto type;
	
	public String value;
	
	public List<DiffDto> children;
	
	public DiffDto() {}

	public TypeDto getType() {
		return type;
	}

	public void setType(TypeDto type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<DiffDto> getChildren() {
		return children;
	}

	public void setChildren(List<DiffDto> children) {
		this.children = children;
	}
	
	

}
