package fr.imt.mines.ales.component.interfacetype;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;

import dedal.DIRECTION;
import dedal.InterfaceType;
import fr.imt.ales.redoc.type.hierarchy.build.HierarchyBuilder;
import fr.imt.ales.redoc.type.hierarchy.structure.JavaType;
import fr.imt.mines.ales.component.Checker4DedalInterface;

public class InterfaceTypeDirectionSubstitualibityChecker extends Checker4DedalInterface{
	public InterfaceTypeDirectionSubstitualibityChecker(HierarchyBuilder hierarchyBuilderOld, HierarchyBuilder hierarchyBuilderNew) {
		super(hierarchyBuilderOld, hierarchyBuilderNew);
	}
	
	public Boolean check(Diff diffObject, DIRECTION direction, DifferenceKind differenceKind) {
		
		if (differenceKind == DifferenceKind.CHANGE && 
				diffObject.getMatch().getRight() instanceof InterfaceType &&
				diffObject.getMatch().getLeft() instanceof InterfaceType) {

			JavaType jTypeParamNewClass = getHierarchyBuilderNew().findJavaType(
					((InterfaceType)diffObject.getMatch().getRight()).getName());
			JavaType jTypeParamOldClass = getHierarchyBuilderOld().findJavaType(
					((InterfaceType)diffObject.getMatch().getLeft()).getName());

			
			if(direction == DIRECTION.REQUIRED) {
				if(jTypeParamOldClass.isSubtypeOf(jTypeParamNewClass)) {
					return Boolean.TRUE;
				}
				return Boolean.FALSE;
			}
			
			if(direction == DIRECTION.PROVIDED) {
				if(jTypeParamNewClass.isSubtypeOf(jTypeParamOldClass)) {
					return Boolean.TRUE;
				}
				return Boolean.FALSE;
			}
			
		}
		return null;
	}
}
