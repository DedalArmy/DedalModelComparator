package fr.imt.mines.ales.component.interfacetype;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;

import dedal.DIRECTION;
import dedal.InterfaceType;
import fr.imt.ales.redoc.type.hierarchy.build.HierarchyBuilder;
import fr.imt.ales.redoc.type.hierarchy.structure.JavaType;
import fr.imt.mines.ales.component.Checker4DedalInterface;
import fr.imt.mines.ales.utils.DiffObjectJson;

public class InterfaceTypeDirectionSubstitualibityChecker extends Checker4DedalInterface{
	public InterfaceTypeDirectionSubstitualibityChecker(HierarchyBuilder hierarchyBuilderOld, HierarchyBuilder hierarchyBuilderNew) {
		super(hierarchyBuilderOld, hierarchyBuilderNew);
	}
	
	public DiffObjectJson check(Diff diffObject, DIRECTION direction, DifferenceKind differenceKind) {
		ReferenceChange referenceChange = (ReferenceChange)diffObject;
		InterfaceType interfaceType = (InterfaceType)referenceChange.getValue();
		
		DiffObjectJson diffObjectJson = new DiffObjectJson();
		diffObjectJson.setDifferenceKind(differenceKind.getName());
		diffObjectJson.setDedalElementId(interfaceType.toString());
		diffObjectJson.setDedalType(referenceChange);
		
		if (differenceKind == DifferenceKind.CHANGE) {

			JavaType jTypeParamNewClass = getHierarchyBuilderNew().findJavaType(
					((InterfaceType)diffObject.getMatch().getRight()).getName());
			JavaType jTypeParamOldClass = getHierarchyBuilderOld().findJavaType(
					((InterfaceType)diffObject.getMatch().getLeft()).getName());

			
			if(direction == DIRECTION.REQUIRED) {
				if(jTypeParamOldClass.isSubtypeOf(jTypeParamNewClass)) {
					diffObjectJson.setSusbstituability(true);
				}
				diffObjectJson.setSusbstituability(false);
			}
			
			if(direction == DIRECTION.PROVIDED) {
				if(jTypeParamNewClass.isSubtypeOf(jTypeParamOldClass)) {
					diffObjectJson.setSusbstituability(true);
				}
				diffObjectJson.setSusbstituability(false);
			}
			
		}
		return diffObjectJson;
	}
}
