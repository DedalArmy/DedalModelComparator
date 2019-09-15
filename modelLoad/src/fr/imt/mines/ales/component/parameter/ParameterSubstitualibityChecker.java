package fr.imt.mines.ales.component.parameter;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;

import dedal.Parameter;
import fr.imt.ales.redoc.type.hierarchy.build.HierarchyBuilder;
import fr.imt.ales.redoc.type.hierarchy.structure.JavaType;
import fr.imt.mines.ales.component.CheckerNot4DedalInterface;

public class ParameterSubstitualibityChecker extends CheckerNot4DedalInterface{
	public ParameterSubstitualibityChecker(HierarchyBuilder hierarchyBuilderOld, HierarchyBuilder hierarchyBuilderNew) {
		super(hierarchyBuilderOld, hierarchyBuilderNew);
	}

	public Boolean check(Diff diffObject, DifferenceKind differenceKind) throws ClassNotFoundException {
		
		if(differenceKind == DifferenceKind.DELETE) {
			return Boolean.TRUE;
		}
		
		if(differenceKind == DifferenceKind.ADD) {
			return Boolean.FALSE;
		}
		
		if (differenceKind == DifferenceKind.CHANGE &&
				diffObject.getMatch().getRight() instanceof Parameter && 
				diffObject.getMatch().getLeft() instanceof Parameter) {

			JavaType jTypeParamNewClass = getHierarchyBuilderNew().findJavaType(
					((Parameter)diffObject.getMatch().getRight()).getType());
			JavaType jTypeParamOldClass = getHierarchyBuilderOld().findJavaType(
					((Parameter)diffObject.getMatch().getLeft()).getType());
			
			if(jTypeParamOldClass.isSubtypeOf(jTypeParamNewClass)) {
				return Boolean.TRUE;
			}else if(jTypeParamNewClass.isSubtypeOf(jTypeParamOldClass)){
				return Boolean.FALSE;
			}else {
				return Boolean.FALSE;
			}
		}
		return null;
	}
}
