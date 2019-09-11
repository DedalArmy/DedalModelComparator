package fr.imt.mines.ales.architecture.level;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.ecore.EObject;

import dedal.Assembly;
import dedal.CompClass;
import dedal.CompInstance;
import dedal.CompRole;
import dedal.Component;
import dedal.Configuration;
import dedal.Specification;
import fr.imt.ales.redoc.type.hierarchy.build.HierarchyBuilder;
import fr.imt.ales.redoc.type.hierarchy.structure.JavaType;
import fr.imt.mines.ales.component.CheckerNot4DedalInterface;
import fr.imt.mines.ales.utils.DiffObjectJson;

public class ComponentSubstitualibityChecker extends CheckerNot4DedalInterface {
	public ComponentSubstitualibityChecker(HierarchyBuilder hierarchyBuilderOld, HierarchyBuilder hierarchyBuilderNew) {
		super(hierarchyBuilderOld, hierarchyBuilderNew);
	}
	
	public DiffObjectJson check(Diff diffObject, DifferenceKind differenceKind) {
		
		ReferenceChange referenceChange = (ReferenceChange)diffObject;
		Component componentObject = (Component)referenceChange.getValue();
		
		DiffObjectJson diffObjectJson = new DiffObjectJson();
		diffObjectJson.setDifferenceKind(differenceKind.getName());
		diffObjectJson.setDedalElementId(componentObject.getId());
		diffObjectJson.setDedalType(referenceChange);
		
//		if(componentObject.eContainer() != null && componentObject.eContainer() instanceof Assembly || componentObject.eContainer() instanceof Configuration || componentObject.eContainer() instanceof Specification) {
//			diffObjectJson.setParent(((Component)componentObject.eContainer()).getName());
//			diffObjectJson.setParentType(componentObject.eContainer().getClass().getName());
//		}else {
//			diffObjectJson.setParent("ERROR PARENT NOT AN ARCHITECTURE DESCRIPTION");
//		}

		if(differenceKind == DifferenceKind.DELETE) {
			diffObjectJson.setSusbstituability(false);
			diffObjectJson.getViolatedRules().put("C_Delete");
			return diffObjectJson;
		}
		
		if(differenceKind == DifferenceKind.ADD) {
			diffObjectJson.setSusbstituability(true);
			return diffObjectJson;
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
				diffObjectJson.setSusbstituability(true);
			}else if(jTypeParamOldClass.isSubtypeOf(jTypeParamNewClass)){
				diffObjectJson.setSusbstituability(false);
				diffObjectJson.getViolatedRules().put("CNew >= COld");
			}else {
				System.out.println("Compo equals ?????????");
				System.out.println(diffObject.getRequires().toString());
				System.out.println("");
				diffObjectJson.setSusbstituability(false);
				diffObjectJson.getViolatedRules().put("CNew || COld");
			}
			return diffObjectJson;
		}

		return null;
	}	
}
