package fr.imt.mines.ales.component.cinterface;

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
import fr.imt.mines.ales.component.Checker4DedalInterface;
import fr.imt.mines.ales.component.interfacetype.InterfaceTypeDirectionSubstitualibityChecker;
import fr.imt.mines.ales.component.parameter.ParameterSubstitualibityChecker;
import fr.imt.mines.ales.component.signature.SignatureSubstitualibityChecker;
import fr.imt.mines.ales.utils.DiffObjectJson;

public class InterfaceSubstitualibityChecker extends Checker4DedalInterface {

	public InterfaceSubstitualibityChecker(HierarchyBuilder hierarchyBuilderOld, HierarchyBuilder hierarchyBuilderNew) {
		super(hierarchyBuilderOld, hierarchyBuilderNew);
	}

	public DiffObjectJson check(Diff diffObject, DIRECTION direction, DifferenceKind differenceKind) throws ClassNotFoundException {
	
		ReferenceChange referenceChange = (ReferenceChange)diffObject;
		Interface interfaceObject = (Interface)referenceChange.getValue();
		
		DiffObjectJson diffObjectJson = new DiffObjectJson();
		diffObjectJson.setDifferenceKind(differenceKind.getName());
		diffObjectJson.setDedalElementId(interfaceObject.getName() + " " + direction + " " + interfaceObject.eContainer().getClass().getName());
		diffObjectJson.setDedalType(referenceChange);

		if(interfaceObject.eContainer() instanceof Component) {
			diffObjectJson.setParent(((Component)interfaceObject.eContainer()).getName());
		}else {
			diffObjectJson.setParent("ERROR PARENT NOT A COMPONENT");
		}
		diffObjectJson.setParentType(interfaceObject.eContainer().getClass().getName());

		
		if(direction == DIRECTION.PROVIDED) {
			if(differenceKind == DifferenceKind.ADD) {
				diffObjectJson.setSusbstituability(true);
				return diffObjectJson;
			}
			if(differenceKind == DifferenceKind.DELETE) {
				diffObjectJson.setSusbstituability(false);
				diffObjectJson.getViolatedRules().put("I_Delete");
				return diffObjectJson;
			}
			if(differenceKind == DifferenceKind.CHANGE) {

				try {
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
							for(Diff diff : diffObject.getRequires()) {
								ReferenceChange refChange = (ReferenceChange)diff;
								EObject eObject = refChange.getValue();
								if(eObject instanceof Interface) {
									Interface interfaceRequired = (Interface)eObject;
									DiffObjectJson diffObjectJsonRequire = this.check(diff, interfaceRequired.getDirection(), diff.getKind());
									if(!diffObjectJsonRequire.getSusbstituability()) {
										diffObjectJson.setSusbstituability(false);
										diffObjectJson.getViolatedRules().put("INew || IOld");
										return diffObjectJson;
									}
								}
								
								if(eObject instanceof Interface) {
									DiffObjectJson diffObjectJsonRequire = new InterfaceTypeDirectionSubstitualibityChecker(this.getHierarchyBuilderOld(), this.getHierarchyBuilderNew()) .check(diff, direction, diff.getKind());
									if(!diffObjectJsonRequire.getSusbstituability()) {
										diffObjectJson.setSusbstituability(false);
										diffObjectJson.getViolatedRules().put("INew || IOld");
										return diffObjectJson;
									}
								}
								
								if(eObject instanceof Signature) {
									DiffObjectJson diffObjectJsonRequire = new SignatureSubstitualibityChecker(this.getHierarchyBuilderOld(), this.getHierarchyBuilderNew()).check(diff, diff.getKind());
									if(!diffObjectJsonRequire.getSusbstituability()) {
										diffObjectJson.setSusbstituability(false);
										diffObjectJson.getViolatedRules().put("INew || IOld");
										return diffObjectJson;
									}
								}
								
								if(eObject instanceof Parameter) {
									DiffObjectJson diffObjectJsonRequire = new ParameterSubstitualibityChecker(this.getHierarchyBuilderOld(), this.getHierarchyBuilderNew()).check(diff, diff.getKind());
									if(!diffObjectJsonRequire.getSusbstituability()) {
										diffObjectJson.setSusbstituability(false);
										diffObjectJson.getViolatedRules().put("INew || IOld");
										return diffObjectJson;
									}
								}
								
							}
							diffObjectJson.setSusbstituability(true);
							return diffObjectJson;

						}
						diffObjectJson.setSusbstituability(false);
						diffObjectJson.getViolatedRules().put("INew || IOld");
						return diffObjectJson;				
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
				return diffObjectJson;
			}
			if(differenceKind == DifferenceKind.DELETE) {
				diffObjectJson.setSusbstituability(true);
				return diffObjectJson;
			}
			if(differenceKind == DifferenceKind.CHANGE) {
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
						for(Diff diff : diffObject.getRequires()) {
							ReferenceChange refChange = (ReferenceChange)diff;
							EObject eObject = refChange.getValue();
							if(eObject instanceof Interface) {
								Interface interfaceRequired = (Interface)eObject;
								DiffObjectJson diffObjectJsonRequire = this.check(diff, interfaceRequired.getDirection(), diff.getKind());
								if(!diffObjectJsonRequire.getSusbstituability()) {
									diffObjectJson.setSusbstituability(false);
									diffObjectJson.getViolatedRules().put("INew || IOld");
									return diffObjectJson;
								}
							}
							
							if(eObject instanceof Signature) {
								DiffObjectJson diffObjectJsonRequire = new SignatureSubstitualibityChecker(this.getHierarchyBuilderOld(), this.getHierarchyBuilderNew()).check(diff, diff.getKind());
								if(!diffObjectJsonRequire.getSusbstituability()) {
									diffObjectJson.setSusbstituability(false);
									diffObjectJson.getViolatedRules().put("INew || IOld");
									return diffObjectJson;
								}
							}
							
							if(eObject instanceof Parameter) {
								DiffObjectJson diffObjectJsonRequire = new ParameterSubstitualibityChecker(this.getHierarchyBuilderOld(), this.getHierarchyBuilderNew()).check(diff, diff.getKind());
								if(!diffObjectJsonRequire.getSusbstituability()) {
									diffObjectJson.setSusbstituability(false);
									diffObjectJson.getViolatedRules().put("INew || IOld");
									return diffObjectJson;
								}
							}
							
						}
						diffObjectJson.setSusbstituability(true);
						return diffObjectJson;

					}
					diffObjectJson.setSusbstituability(false);
					diffObjectJson.getViolatedRules().put("INew || IOld");
					return diffObjectJson;				
				}
			}
		}
		return null;
	}
}
