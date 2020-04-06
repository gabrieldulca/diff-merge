package main.java.com.diffmerge.provider.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;

import main.java.com.diffmerge.dto.ComparisonDto;
import main.java.com.diffmerge.dto.DiffDto;
import main.java.com.diffmerge.dto.MatchDto;
import main.java.com.diffmerge.exception.InvalidParametersException;
import main.java.com.diffmerge.mapper.DiagramDiffMapper;
import main.java.com.diffmerge.mapper.DiffMapper;
import main.java.com.diffmerge.provider.DiffComponent;

public class DiagramDiffComponent extends DiffComponent {
	
	public DiagramDiffComponent() {
		super.setType("diagram");
		super.setMapper(new DiagramDiffMapper());
	}
	
	@Override
	public ComparisonDto getComparison(String left, String right, String origin) throws InvalidParametersException {
				
		Comparison comparison = compare(left, right, origin);

		
		ComparisonDto comparisonDto = new ComparisonDto();
		
		List<Match> matchList = comparison.getMatches();
		List<MatchDto> matchDtoList = new ArrayList<MatchDto>();
		
		comparisonDto.setThreeWay(comparison.isThreeWay());
		for(Match match:matchList) {
			matchDtoList.add(mapMatch(match, comparison.isThreeWay()));
		}
		
		comparisonDto.setMatches(matchDtoList);
		return comparisonDto;
		
	}
	
	public List<DiffDto> getCurrentDiffs(Match match) {
		List<Diff> diffList = match.getDifferences();
		List<DiffDto> diffDtoList = new ArrayList<DiffDto>();
		for(Diff diff:diffList) {
			DiffDto diffDto = getMapper().toDiffDto(diff);
			if(diffDto != null) {
				diffDtoList.add(diffDto);
			}
		}
		return diffDtoList;
		
	}
	
	public List<MatchDto> getCurrentSubMatches(Match match, boolean threeWay) {
		List<Match> subMatchList = match.getSubmatches();
		List<MatchDto> subMatchDtoList = new ArrayList<MatchDto>();
		for(Match subMatch:subMatchList) {
			MatchDto subMatchDto = mapMatch(subMatch, threeWay);
			if(subMatchDto != null) {
				subMatchDtoList.add(subMatchDto);
			}
		}
		return subMatchDtoList;
		
	}
	
	public MatchDto mapMatch(Match match, boolean threeWay) {
		MatchDto matchDto = new MatchDto();
		matchDto.setLeft(getMapper().toModelElementDto(match.getLeft()));
		matchDto.setRight(getMapper().toModelElementDto(match.getRight()));
		if(threeWay) {
			matchDto.setOrigin(getMapper().toModelElementDto(match.getOrigin()));
		}
		matchDto.setDiffs(getCurrentDiffs(match));
		matchDto.setSubMatches(getCurrentSubMatches(match, threeWay));
		
		return matchDto;
		
	}
	

}
