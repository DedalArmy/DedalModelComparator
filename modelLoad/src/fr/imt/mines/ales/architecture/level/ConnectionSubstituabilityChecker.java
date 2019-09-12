package fr.imt.mines.ales.architecture.level;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.ecore.EObject;

import dedal.CompClass;
import dedal.CompInstance;
import dedal.Component;
import dedal.Connection;
import dedal.DIRECTION;
import dedal.Interface;
import dedal.InterfaceType;
import dedal.Parameter;
import fr.imt.ales.redoc.type.hierarchy.build.HierarchyBuilder;
import fr.imt.ales.redoc.type.hierarchy.structure.JavaType;
import fr.imt.mines.ales.component.CheckerNot4DedalInterface;
import fr.imt.mines.ales.utils.DiffObjectJson;

public class ConnectionSubstituabilityChecker extends CheckerNot4DedalInterface{

	public ConnectionSubstituabilityChecker(HierarchyBuilder hierarchyBuilderOld, HierarchyBuilder hierarchyBuilderNew) {
		super(hierarchyBuilderOld, hierarchyBuilderNew);
	}

	@Override
	public DiffObjectJson check(Diff diffObject, DifferenceKind differenceKind) throws ClassNotFoundException {
		
		ReferenceChange referenceChange = (ReferenceChange)diffObject;
		Connection connectionObject = (Connection)referenceChange.getValue();
		
		DiffObjectJson diffObjectJson = new DiffObjectJson();
		diffObjectJson.setDifferenceKind(differenceKind.getName());
		diffObjectJson.setDedalElementId(connectionObject.getRefID());
		diffObjectJson.setDedalType(referenceChange);
		
		if(differenceKind == DifferenceKind.DELETE) {
			diffObjectJson.setSusbstituability(false);
			diffObjectJson.getViolatedRules().put("Conn_Delete");
			return diffObjectJson;
		}
		
		if(differenceKind == DifferenceKind.ADD) {
			diffObjectJson.setSusbstituability(true);
			return diffObjectJson;
		}
		
		if(differenceKind == DifferenceKind.CHANGE) {

			Connection connectionNew = (Connection)diffObject.getMatch().getRight();
			Connection connectionOld = (Connection)diffObject.getMatch().getLeft();
			
			InterfaceType interfaceType_servNew = ((Interface)connectionNew.getServerIntElem()).getType();
			InterfaceType interfaceType_servOld = ((Interface)connectionOld.getServerIntElem()).getType();
			
			InterfaceType interfaceType_clientNew = ((Interface)connectionNew.getClientIntElem()).getType();
			InterfaceType interfaceType_clientOld = ((Interface)connectionOld.getClientIntElem()).getType();
			
			JavaType jTypeServNew = getHierarchyBuilderNew().findJavaType(interfaceType_servNew.getName());
			JavaType jTypeServOld = getHierarchyBuilderNew().findJavaType(interfaceType_servOld.getName());
			
			JavaType jTypeClientNew = getHierarchyBuilderNew().findJavaType(interfaceType_clientNew.getName());
			JavaType jTypeClientOld = getHierarchyBuilderNew().findJavaType(interfaceType_clientOld.getName());

			
			if(jTypeServNew.isSubtypeOf(jTypeServOld) && jTypeClientOld.isSubtypeOf(jTypeClientNew) ) {
				diffObjectJson.setSusbstituability(true);
			}else if(!jTypeServNew.isSubtypeOf(jTypeServOld)){
				diffObjectJson.setSusbstituability(false);
				diffObjectJson.getViolatedRules().put("Int_Serv_new <= Int_Serv_old");
			}else {
				diffObjectJson.setSusbstituability(false);
				diffObjectJson.getViolatedRules().put("Int_Client_new >= Int_Client_old");
			}
			return diffObjectJson;
		}
		
		return null;
	}
	
}
