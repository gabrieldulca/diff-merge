package main.java.com.diffmerge.dto.tree;

import java.util.List;

import main.java.com.diffmerge.dto.AttributeDto;
import main.java.com.diffmerge.dto.DiffDto;
import main.java.com.diffmerge.dto.KindDto;
import main.java.com.diffmerge.dto.ReferenceTypeDto;
import main.java.com.diffmerge.dto.TypeDto;

public class TreeDiffDto extends DiffDto {
	
	private AttributeDto attribute;
	
	private TreeDiffDto parent;
	
	public ReferenceTypeDto referenceType;
	
	public TreeDiffDto() {}

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

	public TreeDiffDto getParent() {
		return parent;
	}

	public void setParent(TreeDiffDto parent) {
		this.parent = parent;
	}

	
	

}
