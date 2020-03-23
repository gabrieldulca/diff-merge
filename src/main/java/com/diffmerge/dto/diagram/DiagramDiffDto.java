package main.java.com.diffmerge.dto.diagram;

import java.util.List;

import main.java.com.diffmerge.dto.AttributeDto;
import main.java.com.diffmerge.dto.DiffDto;
import main.java.com.diffmerge.dto.KindDto;
import main.java.com.diffmerge.dto.ReferenceTypeDto;
import main.java.com.diffmerge.dto.TypeDto;

public class DiagramDiffDto extends DiffDto {
	
	private AttributeDto attribute;
	
	private DiagramDiffDto parent;

	public ReferenceTypeDto referenceType;
	
	public DiagramDiffDto() {}

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

	public DiagramDiffDto getParent() {
		return parent;
	}

	public void setParent(DiagramDiffDto parent) {
		this.parent = parent;
	}

	
	

}
