package fr.imt.mines.ales.component.signature;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;

import dedal.Signature;
import fr.imt.ales.redoc.type.hierarchy.build.HierarchyBuilder;
import fr.imt.ales.redoc.type.hierarchy.structure.JavaType;
import fr.imt.mines.ales.component.CheckerNot4DedalInterface;
import fr.imt.mines.ales.utils.DiffObjectJson;

public class SignatureSubstitualibityChecker extends CheckerNot4DedalInterface{
	public SignatureSubstitualibityChecker(HierarchyBuilder hierarchyBuilderOld, HierarchyBuilder hierarchyBuilderNew) {
		super(hierarchyBuilderOld, hierarchyBuilderNew);
	}
	
	public DiffObjectJson check(Diff diffObject ,DifferenceKind differenceKind) throws ClassNotFoundException {
		
		ReferenceChange referenceChange = (ReferenceChange)diffObject;
		Signature signatureObject = (Signature)referenceChange.getValue();
		
		DiffObjectJson diffObjectJson = new DiffObjectJson();
		diffObjectJson.setDifferenceKind(differenceKind.getName());
		diffObjectJson.setDedalElementId(signatureObject.getName());
		diffObjectJson.setDedalType(referenceChange);
		
		if(differenceKind == DifferenceKind.DELETE) {
			diffObjectJson.setSusbstituability(false);
			diffObjectJson.getViolatedRules().put("S_Delete");
			return diffObjectJson;
		}
		
		if(differenceKind == DifferenceKind.ADD) {
			diffObjectJson.setSusbstituability(true);
			return diffObjectJson;
		}
		
		if (differenceKind == DifferenceKind.CHANGE) {
			
			JavaType jTypeParamNewClass = getHierarchyBuilderNew().findJavaType(
					((Signature)diffObject.getMatch().getRight()).getType());
			JavaType jTypeParamOldClass = getHierarchyBuilderOld().findJavaType(
					((Signature)diffObject.getMatch().getLeft()).getType());

			if(jTypeParamNewClass.isSubtypeOf(jTypeParamOldClass)) {
				diffObjectJson.setSusbstituability(true);
				return diffObjectJson;
			}else if(jTypeParamOldClass.isSubtypeOf(jTypeParamNewClass)){
				diffObjectJson.setSusbstituability(false);
				diffObjectJson.getViolatedRules().put("SNew >= SOld");
			}else {
				diffObjectJson.setSusbstituability(false);
			}
			return diffObjectJson;

		}
		return null;
	}

}
