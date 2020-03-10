package main.java.com.diffmerge.dto;

import java.util.ArrayList;
import java.util.List;

public class ComparisonDto {
	
	private List<DiffDto> differences;
	
	public ComparisonDto() {
		differences = new ArrayList();
	}

	public List<DiffDto> getDifferencess() {
		return differences;
	}

	public void setDifferences(List<DiffDto> differences) {
		this.differences = differences;
	}
	
	public void addDifference(DiffDto difference) {
		differences.add(difference);
	}
	
}