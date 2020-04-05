package main.java.com.diffmerge.dto;

import java.util.List;

public class ConflictDto {

	private TypeDto type;

	private List<DiffDto> diffs;
	
	private List<DiffDto> leftDiffs;
	
	private List<DiffDto> rightDiffs;
	
	private KindDto kind;
	
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

	public List<DiffDto> getDiffs() {
		return diffs;
	}

	public void setDiffs(List<DiffDto> diffs) {
		this.diffs = diffs;
	}

	public List<DiffDto> getLeftDiffs() {
		return leftDiffs;
	}

	public void setLeftDiffs(List<DiffDto> leftDiffs) {
		this.leftDiffs = leftDiffs;
	}

	public List<DiffDto> getRightDiffs() {
		return rightDiffs;
	}

	public void setRightDiffs(List<DiffDto> rightDiffs) {
		this.rightDiffs = rightDiffs;
	}

	

}
