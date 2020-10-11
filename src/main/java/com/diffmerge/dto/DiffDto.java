package main.java.com.diffmerge.dto;

public class DiffDto {

	private TypeDto type;

	private String value;

	private KindDto kind;
	
	private ConflictDto conflict;
	
	private ReferenceDto referenceDto;

	public DiffDto() {
	}

	public KindDto getKind() {
		return kind;
	}

	public void setKind(KindDto kind) {
		this.kind = kind;
	}

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

	public ConflictDto getConflict() {
		return conflict;
	}

	public void setConflict(ConflictDto conflict) {
		this.conflict = conflict;
	}

	public ReferenceDto getReferenceDto() {
		return referenceDto;
	}

	public void setReferenceDto(ReferenceDto referenceDto) {
		this.referenceDto = referenceDto;
	}

	@Override
	public String toString() {
		return "DiffDto [type=" + type + ", value=" + value + ", kind=" + kind + ", conflict=" + conflict
				+ ", referenceDto=" + referenceDto + "]";
	}
	
	

}
