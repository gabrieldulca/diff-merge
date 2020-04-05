package main.java.com.diffmerge.dto;

import java.util.ArrayList;
import java.util.List;

public class ComparisonDto {
	
	private List<MatchDto> matches;
	
	public ComparisonDto() {
		matches = new ArrayList();
	}

	public List<MatchDto> getMatches() {
		return matches;
	}

	public void setMatches(List<MatchDto> matches) {
		this.matches = matches;
	}

	
	
}