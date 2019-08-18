package fr.imt.mines.ales.component.attribute;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;

import dedal.Attribute;
import fr.imt.ales.redoc.type.hierarchy.build.HierarchyBuilder;
import fr.imt.ales.redoc.type.hierarchy.structure.JavaType;
import fr.imt.mines.ales.component.CheckerNot4DedalInterface;
import fr.imt.mines.ales.utils.DiffObjectJson;

public class AttributeSubstuabilityChecker extends CheckerNot4DedalInterface {

	public AttributeSubstuabilityChecker(HierarchyBuilder hierarchyBuilderOld, HierarchyBuilder hierarchyBuilderNew) {
		super(hierarchyBuilderOld, hierarchyBuilderNew);
	}
	
	public DiffObjectJson check(Diff diffObject, DifferenceKind differenceKind) throws ClassNotFoundException {
		ReferenceChange referenceChange = (ReferenceChange)diffObject;
		Attribute attributeObject = (Attribute)referenceChange.getValue();
		
		DiffObjectJson diffObjectJson = new DiffObjectJson();
		diffObjectJson.setDifferenceKind(differenceKind.getName());
		diffObjectJson.setDedalElementId(attributeObject.toString());
		diffObjectJson.setDedalType(referenceChange);
		
		if(differenceKind == DifferenceKind.DELETE) {
			diffObjectJson.setSusbstituability(false);
			diffObjectJson.getViolatedRules().put("A_Delete");
			return diffObjectJson;
		}
		
		if(differenceKind == DifferenceKind.ADD) {
			diffObjectJson.setSusbstituability(true);
			return diffObjectJson;
		}
		
		if (differenceKind == DifferenceKind.CHANGE) {

			JavaType jTypeParamNewClass = getHierarchyBuilderNew().findJavaType(
					((Attribute)diffObject.getMatch().getRight()).getType());
			JavaType jTypeParamOldClass = getHierarchyBuilderOld().findJavaType(
					((Attribute)diffObject.getMatch().getLeft()).getType());

			if(jTypeParamNewClass.isSubtypeOf(jTypeParamOldClass)) {
				diffObjectJson.setSusbstituability(true);
			}else if(jTypeParamOldClass.isSubtypeOf(jTypeParamNewClass)){
				diffObjectJson.setSusbstituability(false);
				diffObjectJson.getViolatedRules().put("ANew >= AOld");
			}else {
				diffObjectJson.setSusbstituability(false);
				diffObjectJson.getViolatedRules().put("ANew || AOld");
			}
			return diffObjectJson;
		}
		return null;
	}
}
