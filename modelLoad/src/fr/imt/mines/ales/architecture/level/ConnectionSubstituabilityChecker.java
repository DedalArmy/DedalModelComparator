package fr.imt.mines.ales.architecture.level;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;

import dedal.Connection;
import dedal.Interface;
import dedal.InterfaceType;
import fr.imt.ales.redoc.type.hierarchy.build.HierarchyBuilder;
import fr.imt.ales.redoc.type.hierarchy.structure.JavaType;
import fr.imt.mines.ales.component.CheckerNot4DedalInterface;
import fr.imt.mines.ales.utils.DiffObjectJson;

public class ConnectionSubstituabilityChecker extends CheckerNot4DedalInterface{

	public ConnectionSubstituabilityChecker(HierarchyBuilder hierarchyBuilderOld, HierarchyBuilder hierarchyBuilderNew) {
		super(hierarchyBuilderOld, hierarchyBuilderNew);
	}

	@Override
	public Boolean check(Diff diffObject, DifferenceKind differenceKind) throws ClassNotFoundException {

		ReferenceChange referenceChange = (ReferenceChange)diffObject;
		Connection connectionObject = referenceChange.getValue() instanceof Connection?
				(Connection)referenceChange.getValue() :
					null;

				DiffObjectJson diffObjectJson = new DiffObjectJson();
				diffObjectJson.setDifferenceKind(differenceKind.getName());
				diffObjectJson.setDedalElementId(connectionObject.getRefID());
				diffObjectJson.setDedalType(referenceChange);

				if(differenceKind == DifferenceKind.DELETE) {
					return Boolean.FALSE;
				}

				if(differenceKind == DifferenceKind.ADD) {
					return Boolean.TRUE;
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
						return Boolean.TRUE;
					}else if(!jTypeServNew.isSubtypeOf(jTypeServOld)){
						return Boolean.FALSE;
					}else {
						return Boolean.FALSE;
					}
				}
				return null;
	}
}
