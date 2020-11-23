package main.java.com.diffmerge.provider.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IBatchMerger;
import org.eclipse.emf.compare.merge.IMerger;

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
	public ComparisonDto getMerge(String left, String right, String origin) throws InvalidParametersException, IOException {
				
		Comparison comparison = merge(left, right, origin);
		
		ComparisonDto comparisonDto = new ComparisonDto();
		
		List<Match> matchList = comparison.getMatches();
		List<MatchDto> matchDtoList = new ArrayList<MatchDto>();
		
		comparisonDto.setThreeWay(comparison.isThreeWay());
		for(Match match:matchList) {
			 MatchDto matchDto = mapMatch(match, comparison.isThreeWay());
			 if(matchDto != null) {
				 if(matchDto.getSubMatches()!=null) {
					 matchDtoList.add(matchDto);
				 }
			 }
		}
		
		comparisonDto.setMatches(matchDtoList);
		System.out.println(comparisonDto);
		return comparisonDto;
		
	}


	@Override
	public ComparisonDto getComparison(String left, String right, String origin) throws InvalidParametersException, IOException {
				
		Comparison comparison = compare(left, right, origin);

		
		ComparisonDto comparisonDto = new ComparisonDto();
		
		List<Match> matchList = comparison.getMatches();
		List<MatchDto> matchDtoList = new ArrayList<MatchDto>();
		
		comparisonDto.setThreeWay(comparison.isThreeWay());
		for(Match match:matchList) {
			 MatchDto matchDto = mapMatch(match, comparison.isThreeWay());
			 if(matchDto != null) {
				 if(matchDto.getSubMatches()!=null) {
					 matchDtoList.add(matchDto);
				 }
			 }
		}
		
		comparisonDto.setMatches(matchDtoList);
		System.out.println(comparisonDto);
		return comparisonDto;
		
	}
	
	public List<DiffDto> getCurrentDiffs(Match match) {
		List<Diff> diffList = match.getDifferences();
		List<DiffDto> diffDtoList = new ArrayList<DiffDto>();
		for(Diff diff:diffList) {
			if(diff.getKind().getName().equals("DELETE")) {
				if(diff.getSource().getName().equals("LEFT") && match.getLeft() != null) {
					continue;
				} else if(diff.getSource().getName().equals("RIGHT") && match.getRight() != null) {
					continue;
				}
			} else if(diff.getKind().getName().equals("MOVE")) {
				continue;
			}
			DiffDto diffDto = getMapper().toDiffDto(diff);
			if(diffDto != null) {
				diffDtoList.add(diffDto);
			}
		}
		return diffDtoList.size() > 0 ? diffDtoList : null;
		
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
		if(match.getLeft() != null) {
			matchDto.setLeft(getMapper().toModelElementDto(match.getLeft()));
		}
		if(match.getRight() != null) {
			matchDto.setRight(getMapper().toModelElementDto(match.getRight()));
		}
		if(threeWay) {
			matchDto.setOrigin(getMapper().toModelElementDto(match.getOrigin()));
		}
		
		if(matchDto.getLeft() == null && matchDto.getRight() == null) {
			return null;
		}
		matchDto.setDiffs(getCurrentDiffs(match));
		List<MatchDto> currentSubMatches = getCurrentSubMatches(match, threeWay);
		if(currentSubMatches.size()>0) {
			matchDto.setSubMatches(currentSubMatches);
		}
		
		return matchDto;
		
	}
	

}
