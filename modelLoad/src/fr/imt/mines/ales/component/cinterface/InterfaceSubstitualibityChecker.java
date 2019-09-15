package fr.imt.mines.ales.component.cinterface;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;

import dedal.DIRECTION;
import dedal.Interface;
import dedal.InterfaceType;
import fr.imt.ales.redoc.type.hierarchy.build.HierarchyBuilder;
import fr.imt.ales.redoc.type.hierarchy.structure.JavaType;
import fr.imt.mines.ales.component.Checker4DedalInterface;

public class InterfaceSubstitualibityChecker extends Checker4DedalInterface {

	public InterfaceSubstitualibityChecker(HierarchyBuilder hierarchyBuilderOld, HierarchyBuilder hierarchyBuilderNew) {
		super(hierarchyBuilderOld, hierarchyBuilderNew);
	}

	public Boolean check(Diff diffObject, DIRECTION direction, DifferenceKind differenceKind) throws ClassNotFoundException {

		if(direction == DIRECTION.PROVIDED) {
			if(differenceKind == DifferenceKind.ADD) {
				return Boolean.TRUE;
			}
			if(differenceKind == DifferenceKind.DELETE) {
				return Boolean.FALSE;
			}
			if(differenceKind == DifferenceKind.CHANGE && 
					diffObject.getMatch().getRight() instanceof Interface && 
					diffObject.getMatch().getLeft() instanceof Interface) {

				InterfaceType interfaceTypeRight = ((Interface)diffObject.getMatch().getRight()).getType();
				InterfaceType interfaceTypeLeft = ((Interface)diffObject.getMatch().getLeft()).getType();

				JavaType jTypeParamNewClass = getHierarchyBuilderNew().findJavaType(interfaceTypeRight.getName().substring(interfaceTypeRight.getName().indexOf("I")+1));
				JavaType jTypeParamOldClass = getHierarchyBuilderOld().findJavaType(interfaceTypeLeft.getName().substring(interfaceTypeLeft.getName().indexOf("I")+1));

				if(jTypeParamNewClass.isSubtypeOf(jTypeParamOldClass)) {
					return Boolean.TRUE;
				}else if(jTypeParamOldClass.isSubtypeOf(jTypeParamNewClass)){
					return Boolean.FALSE;
				}else {
					String nameInterfaceTypeRight = interfaceTypeRight.getName();
					String nameInterfaceTypeLeft  = interfaceTypeLeft.getName();

					if(nameInterfaceTypeLeft == nameInterfaceTypeRight) {
						return Boolean.TRUE;
					}
					return Boolean.FALSE;
				}


			}
		}

		if(direction == DIRECTION.REQUIRED) {
			if(differenceKind == DifferenceKind.ADD) {
				return Boolean.FALSE;
			}
			if(differenceKind == DifferenceKind.DELETE) {
				return Boolean.TRUE;
			}
			if(differenceKind == DifferenceKind.CHANGE && 
					diffObject.getMatch().getRight() instanceof Interface && 
					diffObject.getMatch().getLeft() instanceof Interface) {

				InterfaceType interfaceTypeRight = ((Interface)diffObject.getMatch().getRight()).getType();
				InterfaceType interfaceTypeLeft = ((Interface)diffObject.getMatch().getLeft()).getType();

				JavaType jTypeParamNewClass = getHierarchyBuilderNew().findJavaType(interfaceTypeRight.getName().substring(interfaceTypeRight.getName().indexOf("I")+1));
				JavaType jTypeParamOldClass = getHierarchyBuilderOld().findJavaType(interfaceTypeLeft.getName().substring(interfaceTypeLeft.getName().indexOf("I")+1));

				if(jTypeParamNewClass.isSubtypeOf(jTypeParamOldClass)) {
					return Boolean.TRUE;
				}else if(jTypeParamOldClass.isSubtypeOf(jTypeParamNewClass)){
					return Boolean.FALSE;
				}else {
					String nameInterfaceTypeRight = interfaceTypeRight.getName();
					String nameInterfaceTypeLeft  = interfaceTypeLeft.getName();

					if(nameInterfaceTypeLeft == nameInterfaceTypeRight) {
						return Boolean.TRUE;
					}
					return Boolean.FALSE;			
				}
			}


		}
		return null;
	}
}
