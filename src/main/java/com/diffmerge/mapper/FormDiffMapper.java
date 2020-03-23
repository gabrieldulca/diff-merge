package main.java.com.diffmerge.mapper;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Class;

import main.java.com.diffmerge.dto.AttributeDto;
import main.java.com.diffmerge.dto.DiffDto;
import main.java.com.diffmerge.dto.KindDto;
import main.java.com.diffmerge.dto.ReferenceTypeDto;
import main.java.com.diffmerge.dto.TypeDto;
import main.java.com.diffmerge.dto.form.FormDiffDto;

public class FormDiffMapper extends DiffMapper {
	
	public FormDiffDto toDiffDto(Diff difference) {
		FormDiffDto diffDto = new FormDiffDto();
		if(difference instanceof ReferenceChange) {
			diffDto.setType( TypeDto.REFERENCECHANGE);
			if(((ReferenceChange) difference).getValue() instanceof Property) {
				Property dprop = (Property) ((ReferenceChange) difference).getValue();
				diffDto.setValue(dprop.getName());
				diffDto.setReferenceType(ReferenceTypeDto.PROPERTY);
			} else if(((ReferenceChange) difference).getValue() instanceof Class) {
				Class dclass = (Class) ((ReferenceChange) difference).getValue();
				diffDto.setValue(dclass.getName());
				diffDto.setReferenceType(ReferenceTypeDto.CLASS);
			}
		} else if(difference instanceof AttributeChange) {
			String value = (String) ((AttributeChange) difference).getValue();
			String name = (String) ((AttributeChange) difference).getAttribute().getName();
			diffDto.setType(TypeDto.ATTRIBUTECHANGE);
			diffDto.setAttribute(new AttributeDto(name, value));
		}
		DifferenceKind kind = difference.getKind();
		diffDto.setKind(KindDto.valueOf(kind.getName()));
		
		return diffDto;
	}
}