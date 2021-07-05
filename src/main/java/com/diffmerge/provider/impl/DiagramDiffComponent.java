package main.java.com.diffmerge.provider.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
import main.java.com.diffmerge.exception.InvalidRepresentationException;
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
			//TODO remove if position changes need to be displayed
			if(diff.getKind().getName().equals("DELETE")) {
				if(diff.getSource().getName().equals("LEFT") && match.getLeft() != null) {
					continue;
				} else if(diff.getSource().getName().equals("RIGHT") && match.getRight() != null) {
					continue;
				}
			} else if(diff.getKind().getName().equals("MOVE")) {
				continue;
			} else if(diff.getKind().getName().equals("ADD")) {
				if(diff.getSource().getName().equals("LEFT") && match.getRight() != null) {
					continue;
				} else if(diff.getSource().getName().equals("RIGHT") && match.getLeft() != null) {
					continue;
				}
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
		// only regard changes in children for elements that are not added or deleted
		if(match.getLeft() != null && match.getRight() != null) {
			if(currentSubMatches.size()>0) {
				List<DiffDto> smDiffList = new ArrayList<DiffDto>();
				if(matchDto.getDiffs()!= null) {
					smDiffList.addAll(matchDto.getDiffs());
				}
				for(int i= 0; i < currentSubMatches.size(); i++) {
					if(currentSubMatches.get(i).getLeft() != null && currentSubMatches.get(i).getRight() != null) {
						if(currentSubMatches.get(i).getDiffs()!= null) {
							smDiffList.addAll(currentSubMatches.get(i).getDiffs());
							if(!currentSubMatches.get(i).getLeft().getType().startsWith("task") 
									&& !currentSubMatches.get(i).getLeft().getType().startsWith("edge")) {
								currentSubMatches.get(i).setDiffs(null);
							}
						}
					}
				}
				if(!smDiffList.isEmpty()) {
					// set the diffs of submatches to the parrent
					matchDto.setDiffs(smDiffList);
				}
				matchDto.setSubMatches(currentSubMatches);
			}
		
		}
		return matchDto;
		
	}

	@Override
	public ComparisonDto getMergeSingleChange(String left, String right, String origin, String elem, boolean revert) {
		Comparison comparison = null;
		try {
			comparison = mergeSingleChange(left, right, origin, elem, revert);
		} catch (InvalidParametersException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	

}
