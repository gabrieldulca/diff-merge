package main.java.com.diffmerge.mapper;

import org.eclipse.emf.compare.Diff;

import main.java.com.diffmerge.dto.DiffDto;

public class DiffMapper {
	
	public static DiffDto toDiffDto(Diff difference) {
		DiffDto diffDto = new DiffDto();
		return diffDto;
	}
}