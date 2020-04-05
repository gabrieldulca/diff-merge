package main.java.com.diffmerge.mapper;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Class;

import main.java.com.diffmerge.dto.AttributeDto;
import main.java.com.diffmerge.dto.DiffDto;
import main.java.com.diffmerge.dto.KindDto;
import main.java.com.diffmerge.dto.ModelElementDto;
import main.java.com.diffmerge.dto.ReferenceTypeDto;
import main.java.com.diffmerge.dto.TypeDto;
import main.java.com.diffmerge.dto.diagram.DiagramDiffDto;

public class DiagramDiffMapper extends DiffMapper {
	
	public DiagramDiffDto toDiffDto(Diff difference) {
		DiagramDiffDto diffDto = new DiagramDiffDto();
		
		if(difference instanceof ResourceAttachmentChange) {
			diffDto.setType(TypeDto.RESOURCEATTACHMENTCHANGE);
			Class dclass = (Class) ((ReferenceChange) difference).getValue();
			DiagramDiffDto parent = new DiagramDiffDto();
			parent.setValue(dclass.getName());
			diffDto.setParent(parent);
			diffDto.setReferenceType(ReferenceTypeDto.CLASS);
		} else if(difference instanceof ReferenceChange) {
			diffDto.setType(TypeDto.REFERENCECHANGE);
			if(((ReferenceChange) difference).getValue() instanceof Property) {
				Property dprop = (Property) ((ReferenceChange) difference).getValue();
				diffDto.setValue(dprop.getName());
				diffDto.setReferenceType(ReferenceTypeDto.PROPERTY);
			} else if(((ReferenceChange) difference).getValue() instanceof EClass) {
				EClass dclass = (EClass) ((ReferenceChange) difference).getValue();
				DiagramDiffDto parent = new DiagramDiffDto();
				parent.setValue(dclass.getName());
				diffDto.setParent(parent);
				diffDto.setReferenceType(ReferenceTypeDto.CLASS);
			}
		} else if(difference instanceof AttributeChange) {
			String value = (String) ((AttributeChange) difference).getValue().toString();
			String name = (String) ((AttributeChange) difference).getAttribute().getName();
			diffDto.setType(TypeDto.ATTRIBUTECHANGE);
			diffDto.setAttribute(new AttributeDto(name, value));
		}
		DifferenceKind kind = difference.getKind();
		diffDto.setKind(KindDto.valueOf(kind.getName()));
		
		return diffDto;
	}

	@Override
	public ModelElementDto toModelElementDto(EObject modelElement) {
		ModelElementDto modelElementDto = new ModelElementDto();
		if(modelElement instanceof EClass) {
			EClass mclass = (EClass) modelElement;
			modelElementDto.setName(mclass.getName());
		} else if(modelElement instanceof EPackage) {
			EPackage mclass = (EPackage) modelElement;
			modelElementDto.setName(mclass.getName());
		}
		return modelElementDto;
	}
}