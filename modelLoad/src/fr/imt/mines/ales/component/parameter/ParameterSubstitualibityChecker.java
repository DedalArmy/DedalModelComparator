package fr.imt.mines.ales.component.parameter;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;

import dedal.Parameter;
import fr.imt.ales.redoc.type.hierarchy.build.HierarchyBuilder;
import fr.imt.ales.redoc.type.hierarchy.structure.JavaType;
import fr.imt.mines.ales.component.CheckerNot4DedalInterface;
import fr.imt.mines.ales.utils.DiffObjectJson;

public class ParameterSubstitualibityChecker extends CheckerNot4DedalInterface{
	public ParameterSubstitualibityChecker(HierarchyBuilder hierarchyBuilderOld, HierarchyBuilder hierarchyBuilderNew) {
		super(hierarchyBuilderOld, hierarchyBuilderNew);
	}

	public DiffObjectJson check(Diff diffObject, DifferenceKind differenceKind) throws ClassNotFoundException {
		
		ReferenceChange referenceChange = (ReferenceChange)diffObject;
		Parameter parameterObject = (Parameter)referenceChange.getValue();

		
		DiffObjectJson diffObjectJson = new DiffObjectJson();
		diffObjectJson.setDifferenceKind(differenceKind.getName());
		diffObjectJson.setDedalElementId(parameterObject.getType() + " " + parameterObject.getId());
		diffObjectJson.setDedalType(referenceChange);
		
		if(differenceKind == DifferenceKind.DELETE) {
			diffObjectJson.setSusbstituability(true);
			return diffObjectJson;
		}
		
		if(differenceKind == DifferenceKind.ADD) {
			diffObjectJson.setSusbstituability(false);
			diffObjectJson.getViolatedRules().put("P_Addition");
			return diffObjectJson;
		}
		
		if (differenceKind == DifferenceKind.CHANGE) {

			JavaType jTypeParamNewClass = getHierarchyBuilderNew().findJavaType(
					((Parameter)diffObject.getMatch().getRight()).getType());
			JavaType jTypeParamOldClass = getHierarchyBuilderOld().findJavaType(
					((Parameter)diffObject.getMatch().getLeft()).getType());
			
			if(jTypeParamOldClass.isSubtypeOf(jTypeParamNewClass)) {
				diffObjectJson.setSusbstituability(true);
			}else if(jTypeParamNewClass.isSubtypeOf(jTypeParamOldClass)){
				diffObjectJson.setSusbstituability(false);
				diffObjectJson.getViolatedRules().put("PNew <= POld");
			}else {
				diffObjectJson.setSusbstituability(false);
				diffObjectJson.getViolatedRules().put("PNew || POld");
			}
			return diffObjectJson;
		}

		return null;
	}
}
