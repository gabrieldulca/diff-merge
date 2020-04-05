package main.java.com.diffmerge.dto;

import java.util.List;

public class MatchDto {
	
	private boolean threeWay;
	
	// sub matches
	private List<MatchDto> subDiffs;
	
	private ModelElementDto left;
	
	private ModelElementDto right;
	
	private ModelElementDto origin;
	
	private List<DiffDto> diffs;
	
	public MatchDto() {}

	public boolean isThreeWay() {
		return threeWay;
	}

	public void setThreeWay(boolean threeWay) {
		this.threeWay = threeWay;
	}

	public List<MatchDto> getSubDiffs() {
		return subDiffs;
	}

	public void setSubDiffs(List<MatchDto> subDiffs) {
		this.subDiffs = subDiffs;
	}

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

	

}
