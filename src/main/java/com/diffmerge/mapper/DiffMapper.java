package main.java.com.diffmerge.mapper;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Class;


import main.java.com.diffmerge.dto.DiffDto;
import main.java.com.diffmerge.dto.KindDto;
import main.java.com.diffmerge.dto.ReferenceDiffDto;
import main.java.com.diffmerge.dto.ReferenceTypeDto;
import main.java.com.diffmerge.dto.ResourceAttachmentDiffDto;
import main.java.com.diffmerge.dto.TypeDto;

public class DiffMapper {
	
	public static DiffDto toDiffDto(Diff difference) {
		DiffDto diffDto = new DiffDto();
		
		if(difference instanceof ResourceAttachmentChange) {
			diffDto = new ResourceAttachmentDiffDto();
		} else if(difference instanceof ReferenceChange) {
			diffDto = new ReferenceDiffDto();
			if(((ReferenceChange) difference).getValue() instanceof Property) {
				Property dprop = (Property) ((ReferenceChange) difference).getValue();
				diffDto.setValue(dprop.getName());
				((ReferenceDiffDto) diffDto).setReferenceType(ReferenceTypeDto.PROPERTY);
			} else if(((ReferenceChange) difference).getValue() instanceof Class) {
				Class dclass = (Class) ((ReferenceChange) difference).getValue();
				diffDto.setValue(dclass.getName());
				((ReferenceDiffDto) diffDto).setReferenceType(ReferenceTypeDto.CLASS);
			}
		} else if(difference instanceof AttributeChange) {
			String value = (String) ((AttributeChange) difference).getValue();
			diffDto.setType(TypeDto.ATTRIBUTECHANGE);
			diffDto.setValue(value);
		}
		DifferenceKind kind = difference.getKind();
		diffDto.setKind(KindDto.valueOf(kind.getName()));
		
		return diffDto;
	}
}