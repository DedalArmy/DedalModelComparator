package fr.imt.mines.ales.architecture.level;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.ecore.EObject;

import dedal.CompClass;
import dedal.CompInstance;
import dedal.CompRole;
import dedal.Component;
import fr.imt.ales.redoc.type.hierarchy.build.HierarchyBuilder;
import fr.imt.ales.redoc.type.hierarchy.structure.JavaType;
import fr.imt.mines.ales.component.CheckerNot4DedalInterface;

public class ComponentSubstitualibityChecker extends CheckerNot4DedalInterface {
	public ComponentSubstitualibityChecker(HierarchyBuilder hierarchyBuilderOld, HierarchyBuilder hierarchyBuilderNew) {
		super(hierarchyBuilderOld, hierarchyBuilderNew);
	}

	public Boolean check(Diff diffObject, DifferenceKind differenceKind) {

		ReferenceChange referenceChange = (ReferenceChange)diffObject;

		if(differenceKind == DifferenceKind.DELETE) {
			return Boolean.FALSE;
		}

		if(differenceKind == DifferenceKind.ADD) {
			return Boolean.TRUE;
		}

		if (differenceKind == DifferenceKind.CHANGE && 
				((referenceChange.getValue() instanceof CompInstance)||
						(referenceChange.getValue() instanceof CompRole)||
						(referenceChange.getValue() instanceof CompClass))) {

			JavaType jTypeParamNewClass = null;
			JavaType jTypeParamOldClass = null;

			EObject eObjectRight = diffObject.getMatch().getRight();
			EObject eObjectLeft = diffObject.getMatch().getLeft();

			String compoRName = "";
			String compoLName = "";

			if(eObjectRight instanceof CompInstance && eObjectLeft instanceof CompInstance) {
				compoLName = ((CompInstance)eObjectLeft).getInstantiates().getName();
				compoRName = ((CompInstance)eObjectRight).getInstantiates().getName();

				jTypeParamNewClass = getHierarchyBuilderNew().findJavaType(
						compoRName);
				jTypeParamOldClass = getHierarchyBuilderOld().findJavaType(
						compoLName);
			}

			if(eObjectRight instanceof CompClass && eObjectLeft instanceof CompClass) {
				compoLName = ((CompClass)eObjectLeft).getName();
				compoRName = ((CompClass)eObjectRight).getName();

				jTypeParamNewClass = getHierarchyBuilderNew().findJavaType(
						compoRName);
				jTypeParamOldClass = getHierarchyBuilderOld().findJavaType(
						compoLName);
			}

			if(eObjectRight instanceof CompRole && eObjectLeft instanceof CompRole) {
				jTypeParamNewClass = getHierarchyBuilderNew().findJavaType(
						((CompClass)eObjectRight).getName().split("_role")[0]);
				jTypeParamOldClass = getHierarchyBuilderOld().findJavaType(
						((CompClass)eObjectLeft).getName().split("_role")[0]);
			}

			if(jTypeParamNewClass != null && jTypeParamOldClass != null && jTypeParamNewClass.isSubtypeOf(jTypeParamOldClass)) {
				return Boolean.TRUE;
			}
			if(jTypeParamNewClass != null && jTypeParamOldClass != null && jTypeParamOldClass.isSubtypeOf(jTypeParamNewClass)){
				return Boolean.FALSE;
			}
			return null;
		}

		return null;
	}	
}
