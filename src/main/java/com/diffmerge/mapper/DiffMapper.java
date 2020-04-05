package main.java.com.diffmerge.mapper;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.ecore.EObject;

import main.java.com.diffmerge.dto.DiffDto;
import main.java.com.diffmerge.dto.ModelElementDto;


public abstract class DiffMapper {
	
	public abstract DiffDto toDiffDto(Diff difference);

	public abstract ModelElementDto toModelElementDto(EObject left);
}