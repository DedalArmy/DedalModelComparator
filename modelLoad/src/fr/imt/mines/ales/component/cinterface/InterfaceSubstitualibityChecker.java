package fr.imt.mines.ales.component.cinterface;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.ecore.EObject;

import dedal.Component;
import dedal.Connection;
import dedal.DIRECTION;
import dedal.Interface;
import dedal.InterfaceType;
import dedal.Parameter;
import dedal.Signature;
import fr.imt.ales.redoc.type.hierarchy.build.HierarchyBuilder;
import fr.imt.ales.redoc.type.hierarchy.structure.JavaType;
import fr.imt.mines.ales.architecture.level.ConnectionSubstituabilityChecker;
import fr.imt.mines.ales.component.Checker4DedalInterface;
import fr.imt.mines.ales.component.interfacetype.InterfaceTypeDirectionSubstitualibityChecker;
import fr.imt.mines.ales.component.parameter.ParameterSubstitualibityChecker;
import fr.imt.mines.ales.component.signature.SignatureSubstitualibityChecker;
import fr.imt.mines.ales.utils.DiffObjectJson;

public class InterfaceSubstitualibityChecker extends Checker4DedalInterface {

	private ConnectionSubstituabilityChecker connectionSubstituabilityChecker;
	
	public InterfaceSubstitualibityChecker(HierarchyBuilder hierarchyBuilderOld, HierarchyBuilder hierarchyBuilderNew) {
		super(hierarchyBuilderOld, hierarchyBuilderNew);
		connectionSubstituabilityChecker = new ConnectionSubstituabilityChecker(hierarchyBuilderOld, hierarchyBuilderNew);
	}

	public List<DiffObjectJson> check(Diff diffObject, DIRECTION direction, DifferenceKind differenceKind) throws ClassNotFoundException {
	
		List<DiffObjectJson> listDiffObjectJson = new ArrayList<>();
		
		ReferenceChange referenceChange = (ReferenceChange)diffObject;
		Interface interfaceObject = (Interface)referenceChange.getValue();
		
		DiffObjectJson diffObjectJson = new DiffObjectJson();
		diffObjectJson.setDifferenceKind(differenceKind.getName());
		diffObjectJson.setDedalElementId(interfaceObject.getName() + " " + direction + " " + interfaceObject.eContainer().getClass().getName());
		diffObjectJson.setDedalType(referenceChange);

		if(interfaceObject.eContainer() != null && interfaceObject.eContainer() instanceof Component) {
			diffObjectJson.setParent(((Component)interfaceObject.eContainer()).getName());
			diffObjectJson.setParentType(interfaceObject.eContainer().getClass().getName());
		}else {
			diffObjectJson.setParent("ERROR PARENT NOT A COMPONENT");
		}

		
		if(direction == DIRECTION.PROVIDED) {
			if(differenceKind == DifferenceKind.ADD) {
				diffObjectJson.setSusbstituability(true);
				listDiffObjectJson.add(diffObjectJson);
				return listDiffObjectJson;
			}
			if(differenceKind == DifferenceKind.DELETE) {
				diffObjectJson.setSusbstituability(false);
				diffObjectJson.getViolatedRules().put("I_Delete");
				listDiffObjectJson.add(diffObjectJson);
				return listDiffObjectJson;
			}
			if(differenceKind == DifferenceKind.CHANGE) {

				try {
					if(diffObject.getMatch().getRight() instanceof Interface && diffObject.getMatch().getRight() instanceof Interface){

					InterfaceType interfaceTypeRight = ((Interface)diffObject.getMatch().getRight()).getType();
					InterfaceType interfaceTypeLeft = ((Interface)diffObject.getMatch().getLeft()).getType();
														
					JavaType jTypeParamNewClass = getHierarchyBuilderNew().findJavaType(interfaceTypeRight.getName().substring(interfaceTypeRight.getName().indexOf("I")+1));
					JavaType jTypeParamOldClass = getHierarchyBuilderOld().findJavaType(interfaceTypeLeft.getName().substring(interfaceTypeLeft.getName().indexOf("I")+1));

					if(jTypeParamNewClass.isSubtypeOf(jTypeParamOldClass)) {
						diffObjectJson.setSusbstituability(true);
					}else if(jTypeParamOldClass.isSubtypeOf(jTypeParamNewClass)){
						diffObjectJson.setSusbstituability(false);
						diffObjectJson.getViolatedRules().put("INew >= IOld");
					}else {
						String nameInterfaceTypeRight = interfaceTypeRight.getName();
						String nameInterfaceTypeLeft  = interfaceTypeLeft.getName();
						
						if(nameInterfaceTypeLeft == nameInterfaceTypeRight) {
							
							diffObjectJson.setSusbstituability(true);
							listDiffObjectJson.add(diffObjectJson);
							return listDiffObjectJson;
						}
						diffObjectJson.setSusbstituability(false);
						diffObjectJson.getViolatedRules().put("INew || IOld");
						listDiffObjectJson.add(diffObjectJson);
						return listDiffObjectJson;
						
					}
					}
					if(diffObject.getMatch().getRight() instanceof Connection && diffObject.getMatch().getRight() instanceof Connection) {
						listDiffObjectJson.add(connectionSubstituabilityChecker.check(diffObject, differenceKind));
						return listDiffObjectJson;
					}
				} catch (ClassCastException e) {
					System.out.println(e.getMessage());
					System.out.println("=====> ERROR INTERFACE !!!!!!!!!!!!!!!!");
				}
				
			}
		}
		
		if(direction == DIRECTION.REQUIRED) {
			if(differenceKind == DifferenceKind.ADD) {
				diffObjectJson.setSusbstituability(false);
				diffObjectJson.getViolatedRules().put("I_Add");
				listDiffObjectJson.add(diffObjectJson);
				return listDiffObjectJson;
				
			}
			if(differenceKind == DifferenceKind.DELETE) {
				diffObjectJson.setSusbstituability(true);
				listDiffObjectJson.add(diffObjectJson);
				return listDiffObjectJson;
			}
			if(differenceKind == DifferenceKind.CHANGE) {
				try {
					if(diffObject.getMatch().getRight() instanceof Interface && diffObject.getMatch().getRight() instanceof Interface){
							
						InterfaceType interfaceTypeRight = ((Interface)diffObject.getMatch().getRight()).getType();
						InterfaceType interfaceTypeLeft = ((Interface)diffObject.getMatch().getLeft()).getType();
										
						JavaType jTypeParamNewClass = getHierarchyBuilderNew().findJavaType(interfaceTypeRight.getName().substring(interfaceTypeRight.getName().indexOf("I")+1));
						JavaType jTypeParamOldClass = getHierarchyBuilderOld().findJavaType(interfaceTypeLeft.getName().substring(interfaceTypeLeft.getName().indexOf("I")+1));
		
						if(jTypeParamNewClass.isSubtypeOf(jTypeParamOldClass)) {
							diffObjectJson.setSusbstituability(true);
						}else if(jTypeParamOldClass.isSubtypeOf(jTypeParamNewClass)){
							diffObjectJson.setSusbstituability(false);
							diffObjectJson.getViolatedRules().put("INew >= IOld");
						}else {
							String nameInterfaceTypeRight = interfaceTypeRight.getName();
							String nameInterfaceTypeLeft  = interfaceTypeLeft.getName();
							
							if(nameInterfaceTypeLeft == nameInterfaceTypeRight) {
								
								diffObjectJson.setSusbstituability(true);
								listDiffObjectJson.add(diffObjectJson);
								return listDiffObjectJson;
		
							}
							diffObjectJson.setSusbstituability(false);
							diffObjectJson.getViolatedRules().put("INew || IOld");
							listDiffObjectJson.add(diffObjectJson);
							return listDiffObjectJson;				
						}
					}
					if(diffObject.getMatch().getRight() instanceof Connection && diffObject.getMatch().getRight() instanceof Connection) {
						listDiffObjectJson.add(connectionSubstituabilityChecker.check(diffObject, differenceKind));
						return listDiffObjectJson;
					}
			} catch (ClassCastException e) {
				System.out.println(e.getMessage());
				System.out.println("=====> ERROR INTERFACE !!!!!!!!!!!!!!!!");
			}
				
			}
		}
		return null;
	}
}
