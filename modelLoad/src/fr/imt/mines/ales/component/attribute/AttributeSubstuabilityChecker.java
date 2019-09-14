package fr.imt.mines.ales.component.attribute;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;

import dedal.Attribute;
import fr.imt.ales.redoc.type.hierarchy.build.HierarchyBuilder;
import fr.imt.ales.redoc.type.hierarchy.structure.JavaType;
import fr.imt.mines.ales.component.CheckerNot4DedalInterface;

public class AttributeSubstuabilityChecker extends CheckerNot4DedalInterface {

	public AttributeSubstuabilityChecker(HierarchyBuilder hierarchyBuilderOld, HierarchyBuilder hierarchyBuilderNew) {
		super(hierarchyBuilderOld, hierarchyBuilderNew);
	}
	
	public Boolean check(Diff diffObject, DifferenceKind differenceKind) throws ClassNotFoundException {

		if(differenceKind == DifferenceKind.DELETE) {
			return Boolean.FALSE;
		}
		
		if(differenceKind == DifferenceKind.ADD) {
			return Boolean.TRUE;
		}
		
		if (differenceKind == DifferenceKind.CHANGE) {

			JavaType jTypeParamNewClass = getHierarchyBuilderNew().findJavaType(
					((Attribute)diffObject.getMatch().getRight()).getType());
			JavaType jTypeParamOldClass = getHierarchyBuilderOld().findJavaType(
					((Attribute)diffObject.getMatch().getLeft()).getType());

			if(jTypeParamNewClass.isSubtypeOf(jTypeParamOldClass)) {
				return Boolean.TRUE;
			}else if(jTypeParamOldClass.isSubtypeOf(jTypeParamNewClass)){
				return Boolean.FALSE;
			}else {
				return Boolean.FALSE;
			}
		}
		return null;
	}
}
