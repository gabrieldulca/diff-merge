package main.java.com.diffmerge.mapper;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Class;

import main.java.com.diffmerge.dto.AttributeDto;
import main.java.com.diffmerge.dto.DiffDto;
import main.java.com.diffmerge.dto.KindDto;
import main.java.com.diffmerge.dto.ModelElementDto;
import main.java.com.diffmerge.dto.ReferenceDto;
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
				ReferenceDto referenceDto = new ReferenceDto();
				referenceDto.setName(((ReferenceChange) difference).getReference().getName());
				referenceDto.setLowerBound(((ReferenceChange) difference).getReference().getLowerBound());
				referenceDto.setUpperBound(((ReferenceChange) difference).getReference().getUpperBound());
				diffDto.setReferenceDto(referenceDto);
				diffDto.setReferenceType(ReferenceTypeDto.CLASS);
			} else if(((ReferenceChange) difference).getValue() instanceof EReference) {
				EReference dreference = (EReference) ((ReferenceChange) difference).getValue();
				DiagramDiffDto parent = new DiagramDiffDto();
				parent.setValue(dreference.getName());
				diffDto.setParent(parent);
				ReferenceDto referenceDto = new ReferenceDto();
				referenceDto.setName(dreference.getName());
				referenceDto.setLowerBound(dreference.getLowerBound());
				referenceDto.setUpperBound(dreference.getUpperBound());
				diffDto.setReferenceDto(referenceDto);
				diffDto.setReferenceType(ReferenceTypeDto.REFERENCE);
			} else if(((ReferenceChange) difference).getValue() instanceof EEnumLiteral) {
				EEnumLiteral denum = (EEnumLiteral) ((ReferenceChange) difference).getValue();
				DiagramDiffDto parent = new DiagramDiffDto();
				parent.setValue(denum.getName());
				diffDto.setParent(parent);
				diffDto.setReferenceType(ReferenceTypeDto.ENUM);
			} else if(((ReferenceChange) difference).getValue() instanceof EAttribute) {
				EAttribute dattr = (EAttribute) ((ReferenceChange) difference).getValue();
				DiagramDiffDto parent = new DiagramDiffDto();
				parent.setValue(dattr.getName());
				diffDto.setParent(parent);
				ReferenceDto referenceDto = new ReferenceDto();
				referenceDto.setName(dattr.getName());
				referenceDto.setLowerBound(dattr.getLowerBound());
				referenceDto.setUpperBound(dattr.getUpperBound());
				diffDto.setReferenceDto(referenceDto);
				diffDto.setReferenceType(ReferenceTypeDto.ATTRIBUTE);
			} else if(((ReferenceChange) difference).getValue() instanceof EOperation) {
				EOperation dop = (EOperation) ((ReferenceChange) difference).getValue();
				DiagramDiffDto parent = new DiagramDiffDto();
				parent.setValue(dop.getName());
				diffDto.setParent(parent);
				ReferenceDto referenceDto = new ReferenceDto();
				referenceDto.setName(dop.getName());
				referenceDto.setLowerBound(dop.getLowerBound());
				referenceDto.setUpperBound(dop.getUpperBound());
				diffDto.setReferenceDto(referenceDto);
				diffDto.setReferenceType(ReferenceTypeDto.OPERATION);
			} else if(((ReferenceChange) difference).getValue() instanceof EParameter) {
				EParameter dparam = (EParameter) ((ReferenceChange) difference).getValue();
				DiagramDiffDto parent = new DiagramDiffDto();
				parent.setValue(dparam.getName());
				diffDto.setParent(parent);
				ReferenceDto referenceDto = new ReferenceDto();
				referenceDto.setName(dparam.getName());
				referenceDto.setLowerBound(dparam.getLowerBound());
				referenceDto.setUpperBound(dparam.getUpperBound());
				diffDto.setReferenceDto(referenceDto);
				diffDto.setReferenceType(ReferenceTypeDto.PARAMETER);
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
		} else if(modelElement instanceof EReference) {
			EReference mclass = (EReference) modelElement;
			modelElementDto.setName(mclass.getName());
			modelElementDto.setTarget(mclass.getEType().getName());
		} else if(modelElement instanceof EEnum) {
			EEnum mclass = (EEnum) modelElement;
			modelElementDto.setName(mclass.getName());
		} else if(modelElement instanceof EOperation) {
			EOperation mclass = (EOperation) modelElement;
			modelElementDto.setName(mclass.getName());
		} else if(modelElement instanceof EAttribute) {
			EAttribute mclass = (EAttribute) modelElement;
			modelElementDto.setName(mclass.getName());
		} else if(modelElement instanceof EParameter) {
			EParameter mclass = (EParameter) modelElement;
			modelElementDto.setName(mclass.getName());
		}
		return modelElementDto;
	}
}