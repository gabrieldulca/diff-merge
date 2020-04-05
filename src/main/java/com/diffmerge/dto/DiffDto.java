package main.java.com.diffmerge.dto;

public class DiffDto {

	private TypeDto type;

	private String value;

	private KindDto kind;
	
	private ConflictDto conflict;

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
	
	

}
