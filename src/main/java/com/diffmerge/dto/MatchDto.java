package main.java.com.diffmerge.dto;

import java.util.List;

public class MatchDto {
		
	// sub matches
	private List<MatchDto> subMatches;
	
	private ModelElementDto left;
	
	private ModelElementDto right;
	
	private ModelElementDto origin;
	
	private List<DiffDto> diffs;
	
	public MatchDto() {}

	public ModelElementDto getLeft() {
		return left;
	}

	public void setLeft(ModelElementDto left) {
		this.left = left;
	}

	public ModelElementDto getRight() {
		return right;
	}

	public void setRight(ModelElementDto right) {
		this.right = right;
	}

	public ModelElementDto getOrigin() {
		return origin;
	}

	public void setOrigin(ModelElementDto origin) {
		this.origin = origin;
	}

	public List<MatchDto> getSubMatches() {
		return subMatches;
	}

	public void setSubMatches(List<MatchDto> subMatches) {
		this.subMatches = subMatches;
	}

	public List<DiffDto> getDiffs() {
		return diffs;
	}

	public void setDiffs(List<DiffDto> diffs) {
		this.diffs = diffs;
	}

	@Override
	public String toString() {
		return "MatchDto [subMatches=" + subMatches + ", left=" + left + ", right=" + right + ", origin=" + origin
				+ ", diffs=" + diffs + "]";
	}

	public String getAvailableMatchId() {
		if(this.origin != null && this.origin.getId() != null) {
			return this.origin.getId();
		} else if(this.left != null && this.left.getId() != null) {
			return this.left.getId();
		} else {
			return this.right.getId();
		}
	}

}
