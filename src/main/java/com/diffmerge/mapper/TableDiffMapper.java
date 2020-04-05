package main.java.com.diffmerge.mapper;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Class;

import main.java.com.diffmerge.dto.AttributeDto;
import main.java.com.diffmerge.dto.DiffDto;
import main.java.com.diffmerge.dto.KindDto;
import main.java.com.diffmerge.dto.ModelElementDto;
import main.java.com.diffmerge.dto.ReferenceTypeDto;
import main.java.com.diffmerge.dto.TypeDto;
import main.java.com.diffmerge.dto.table.TableDiffDto;

public class TableDiffMapper extends DiffMapper {
	
	public TableDiffDto toDiffDto(Diff difference) {
		TableDiffDto diffDto = null;
		if(difference instanceof ReferenceChange) {
			if(((ReferenceChange) difference).getValue() instanceof Property) {
				diffDto = new TableDiffDto();
				diffDto.setType( TypeDto.REFERENCECHANGE);
				Property dprop = (Property) ((ReferenceChange) difference).getValue();
				diffDto.setValue(dprop.getName());
				diffDto.setReferenceType(ReferenceTypeDto.PROPERTY);
				DifferenceKind kind = difference.getKind();
				diffDto.setKind(KindDto.valueOf(kind.getName()));
			}
		} else if(difference instanceof AttributeChange) {
			diffDto = new TableDiffDto();
			String value = (String) ((AttributeChange) difference).getValue();
			String name = (String) ((AttributeChange) difference).getAttribute().getName();
			diffDto.setType(TypeDto.ATTRIBUTECHANGE);
			diffDto.setAttribute(new AttributeDto(name, value));
			DifferenceKind kind = difference.getKind();
			diffDto.setKind(KindDto.valueOf(kind.getName()));
		}
		
		
		return diffDto;
	}
	
	@Override
	public ModelElementDto toModelElementDto(EObject modelElement) {
		ModelElementDto modelElementDto = new ModelElementDto();
		if(modelElement instanceof Class) {
			Class mclass = (Class) modelElement;
			modelElementDto.setName(mclass.getName());
		}
		return modelElementDto;
	}
}