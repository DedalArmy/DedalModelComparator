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
		
		if (differenceKind == DifferenceKind.CHANGE) {

			JavaType jTypeParamNewClass = null;
			JavaType jTypeParamOldClass = null;
		
				EObject eObjectRight = diffObject.getMatch().getRight();
				EObject eObjectLeft = diffObject.getMatch().getLeft();

				Component compoR = ((Component)eObjectRight);
				Component compoL = ((Component)eObjectLeft);
				
				String compoRName = "";
				String compoLName = "";
				
				if(compoR instanceof CompInstance && compoL instanceof CompInstance) {
					compoLName = ((CompInstance)compoL).getInstantiates().getName();
					compoRName = ((CompInstance)compoR).getInstantiates().getName();
					
					jTypeParamNewClass = getHierarchyBuilderNew().findJavaType(
							compoRName);
					jTypeParamOldClass = getHierarchyBuilderOld().findJavaType(
							compoLName);
				}
				
				if(compoR instanceof CompClass && compoL instanceof CompClass) {
					compoLName = ((CompClass)compoL).getName();
					compoRName = ((CompClass)compoR).getName();
					
					jTypeParamNewClass = getHierarchyBuilderNew().findJavaType(
							compoRName);
					jTypeParamOldClass = getHierarchyBuilderOld().findJavaType(
							compoLName);
				}
				
				if(compoR instanceof CompRole && compoL instanceof CompRole) {
					jTypeParamNewClass = getHierarchyBuilderNew().findJavaType(
							((CompClass)compoR).getName().split("_role")[0]);
					jTypeParamOldClass = getHierarchyBuilderOld().findJavaType(
							((CompClass)compoL).getName().split("_role")[0]);
				}
			
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
