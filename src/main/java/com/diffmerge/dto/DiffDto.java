package main.java.com.diffmerge.dto;

public class DiffDto {

	public TypeDto type;

	public String value;

	public KindDto kind;

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

}
