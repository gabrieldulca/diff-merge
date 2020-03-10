package main.java.com.diffmerge.dto;

public class ReferenceDiffDto extends DiffDto{
	
	private ReferenceTypeDto referenceType;
	
	public ReferenceDiffDto() {
		this.type = TypeDto.REFERENCECHANGE;
	}

	public ReferenceTypeDto getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(ReferenceTypeDto referenceType) {
		this.referenceType = referenceType;
	}


	

}
