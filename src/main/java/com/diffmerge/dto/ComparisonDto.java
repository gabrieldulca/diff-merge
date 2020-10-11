package main.java.com.diffmerge.dto;

import java.util.ArrayList;
import java.util.List;

public class ComparisonDto {
	
	private List<MatchDto> matches;
	private boolean threeWay;

	public boolean isThreeWay() {
		return threeWay;
	}

	public void setThreeWay(boolean threeWay) {
		this.threeWay = threeWay;
	}
	
	public ComparisonDto() {
		matches = new ArrayList();
	}

	public List<MatchDto> getMatches() {
		return matches;
	}

	public void setMatches(List<MatchDto> matches) {
		this.matches = matches;
	}

	@Override
	public String toString() {
		return "ComparisonDto [matches=" + matches + ", threeWay=" + threeWay + "]";
	}

	
	
}