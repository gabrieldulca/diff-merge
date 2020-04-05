package main.java.com.diffmerge.provider.impl;

import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;

import main.java.com.diffmerge.dto.ComparisonDto;
import main.java.com.diffmerge.dto.DiffDto;
import main.java.com.diffmerge.exception.InvalidParametersException;
import main.java.com.diffmerge.mapper.DiagramDiffMapper;
import main.java.com.diffmerge.mapper.DiffMapper;
import main.java.com.diffmerge.provider.DiffComponent;

public class DiagramDiffComponent extends DiffComponent {
	
	public DiagramDiffComponent() {
		super.setType("diagram");
	}
	
	@Override
	public ComparisonDto getComparison(String model1, String model2, String model3) throws InvalidParametersException {
				
		Comparison comparison = compare(model1, model2, model3);

		List<Diff> diffList = comparison.getDifferences();
		ComparisonDto comparisonDto = new ComparisonDto();
		
		DiffMapper mapper = new DiagramDiffMapper();
		
		for(Diff diff:diffList) {
			DiffDto diffDto = mapper.toDiffDto(diff);
			if(diffDto != null) {
				comparisonDto.addDifference(diffDto);
			}
		}
		return comparisonDto;
		
	}
	

}
