package main.java.com.diffmerge.mapper;

import org.eclipse.emf.compare.Diff;

import main.java.com.diffmerge.dto.DiffDto;


public abstract class DiffMapper {
	
	public abstract DiffDto toDiffDto(Diff difference);
}