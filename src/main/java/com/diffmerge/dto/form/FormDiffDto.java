package main.java.com.diffmerge.dto.form;

import java.util.List;

import main.java.com.diffmerge.dto.AttributeDto;
import main.java.com.diffmerge.dto.DiffDto;
import main.java.com.diffmerge.dto.KindDto;
import main.java.com.diffmerge.dto.ReferenceTypeDto;
import main.java.com.diffmerge.dto.TypeDto;

public class FormDiffDto extends DiffDto {

	public AttributeDto attribute;

	
	public ReferenceTypeDto referenceType;
	
	public FormDiffDto() {}

	public AttributeDto getAttribute() {
		return attribute;
	}

	public void setAttribute(AttributeDto attribute) {
		this.attribute = attribute;
	}

	public ReferenceTypeDto getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(ReferenceTypeDto referenceType) {
		this.referenceType = referenceType;
	}

	
	
}
