package fr.imt.mines.ales.comparators;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.json.JSONException;

import dedal.Attribute;
import dedal.ClassConnection;
import dedal.CompClass;
import dedal.CompInstance;
import dedal.CompRole;
import dedal.CompType;
import dedal.Component;
import dedal.Connection;
import dedal.DedalPackage;
import dedal.InstConnection;
import dedal.Interface;
import dedal.InterfaceType;
import dedal.Parameter;
import dedal.RoleConnection;
import dedal.Signature;
import dedal.impl.AttributeImpl;
import dedal.impl.ClassConnectionImpl;
import dedal.impl.CompClassImpl;
import dedal.impl.CompInstanceImpl;
import dedal.impl.CompRoleImpl;
import dedal.impl.CompTypeImpl;
import dedal.impl.ComponentImpl;
import dedal.impl.InstConnectionImpl;
import dedal.impl.InterfaceImpl;
import dedal.impl.ParameterImpl;
import dedal.impl.RoleConnectionImpl;
import dedal.impl.SignatureImpl;
import fr.imt.ales.redoc.type.hierarchy.build.HierarchyBuilder;
import fr.imt.ales.redoc.type.hierarchy.build.HierarchyBuilderManager;
import fr.imt.mines.ales.architecture.level.ComponentSubstitualibityChecker;
import fr.imt.mines.ales.architecture.level.ConnectionSubstituabilityChecker;
import fr.imt.mines.ales.component.Checker4DedalInterface;
import fr.imt.mines.ales.component.CheckerNot4DedalInterface;
import fr.imt.mines.ales.component.attribute.AttributeSubstuabilityChecker;
import fr.imt.mines.ales.component.cinterface.InterfaceSubstitualibityChecker;
import fr.imt.mines.ales.component.interfacetype.InterfaceTypeDirectionSubstitualibityChecker;
import fr.imt.mines.ales.component.parameter.ParameterSubstitualibityChecker;
import fr.imt.mines.ales.component.signature.SignatureSubstitualibityChecker;
import fr.imt.mines.ales.structure.DiffDedal;
import fr.imt.mines.ales.structure.manager.DiffManager;
import fr.imt.mines.ales.utils.DiffObjectJson;
import fr.imt.mines.ales.utils.FileUtil;
import fr.imt.mines.ales.utils.JsonWriterSingleton;
import fr.imt.mines.ales.utils.MapPathKeys;

public class ProjectComparator {

	private ResourceSet resourceSetOld;
	private ResourceSet resourceSetNew;
	private Map<MapPathKeys, String> mapPathsOldProject;
	private Map<MapPathKeys, String> mapPathsNewProject;
	private EList<Diff> diffsList;
	private HierarchyBuilder hierarchyBuilderOld;
	private HierarchyBuilder hierarchyBuilderNew;
	private HierarchyBuilderManager hierarchyBuilderManager;
	
	private JsonWriterSingleton jsonWriterSingleton;
	private String directoryToStoreJsonFiles;

	private Map<Class<?>, CheckerNot4DedalInterface> mapCheckerNot4Interface;
	private Map<Class<?>, Checker4DedalInterface> mapChecker4Interface;
	
	public ProjectComparator() {
		DedalPackage.eINSTANCE.eClass();

		resourceSetOld = new ResourceSetImpl();
		resourceSetNew = new ResourceSetImpl();
		
		mapChecker4Interface = new HashMap<>();
		mapCheckerNot4Interface = new HashMap<>();
		
		hierarchyBuilderManager  = HierarchyBuilderManager.getInstance();
		hierarchyBuilderManager.init();
	}

	public void loadResources(String pathOldProject, String pathNewProject, String directoryToStoreJsonFiles) throws IOException {

			mapPathsOldProject = FileUtil.createMapPaths(pathOldProject);
			mapPathsNewProject = FileUtil.createMapPaths(pathNewProject);
			
			jsonWriterSingleton = JsonWriterSingleton.getInstance();
			jsonWriterSingleton.setJsonObject(
				mapPathsOldProject,
					mapPathsNewProject);
			
			System.out.println("Loading hierarchy builders...");
			hierarchyBuilderOld = hierarchyBuilderManager.getHierarchyBuilder(
					mapPathsOldProject.get(MapPathKeys.PROJECT_PATH), 
					mapPathsOldProject.get(MapPathKeys.DEPENDENCIES_PATH)
			);
			
			hierarchyBuilderNew = hierarchyBuilderManager.getHierarchyBuilder(
					mapPathsNewProject.get(MapPathKeys.PROJECT_PATH), 
					mapPathsNewProject.get(MapPathKeys.DEPENDENCIES_PATH)
			);
			
			hierarchyBuilderNew.build();
			hierarchyBuilderOld.build();

		
		
		this.directoryToStoreJsonFiles = directoryToStoreJsonFiles;
		
		//------------ Maps with checkers ------------
		mapCheckerNot4Interface.put(Signature.class, new SignatureSubstitualibityChecker(hierarchyBuilderOld, hierarchyBuilderNew));
		mapCheckerNot4Interface.put(SignatureImpl.class, new SignatureSubstitualibityChecker(hierarchyBuilderOld, hierarchyBuilderNew));
		mapCheckerNot4Interface.put(Parameter.class, new ParameterSubstitualibityChecker(hierarchyBuilderOld, hierarchyBuilderNew));
		mapCheckerNot4Interface.put(ParameterImpl.class, new ParameterSubstitualibityChecker(hierarchyBuilderOld, hierarchyBuilderNew));
		mapCheckerNot4Interface.put(Attribute.class, new AttributeSubstuabilityChecker(hierarchyBuilderOld, hierarchyBuilderNew));
		mapCheckerNot4Interface.put(AttributeImpl.class, new AttributeSubstuabilityChecker(hierarchyBuilderOld, hierarchyBuilderNew));
		mapCheckerNot4Interface.put(Component.class, new ComponentSubstitualibityChecker(hierarchyBuilderOld, hierarchyBuilderNew));
		mapCheckerNot4Interface.put(ComponentImpl.class, new ComponentSubstitualibityChecker(hierarchyBuilderOld, hierarchyBuilderNew));
		mapCheckerNot4Interface.put(CompInstance.class, new ComponentSubstitualibityChecker(hierarchyBuilderOld, hierarchyBuilderNew));
		mapCheckerNot4Interface.put(CompInstanceImpl.class, new ComponentSubstitualibityChecker(hierarchyBuilderOld, hierarchyBuilderNew));;
		mapCheckerNot4Interface.put(CompClass.class, new ComponentSubstitualibityChecker(hierarchyBuilderOld, hierarchyBuilderNew));
		mapCheckerNot4Interface.put(CompClassImpl.class, new ComponentSubstitualibityChecker(hierarchyBuilderOld, hierarchyBuilderNew));;
		mapCheckerNot4Interface.put(CompType.class, new ComponentSubstitualibityChecker(hierarchyBuilderOld, hierarchyBuilderNew));
		mapCheckerNot4Interface.put(CompTypeImpl.class, new ComponentSubstitualibityChecker(hierarchyBuilderOld, hierarchyBuilderNew));
		mapCheckerNot4Interface.put(CompRole.class, new ComponentSubstitualibityChecker(hierarchyBuilderOld, hierarchyBuilderNew));
		mapCheckerNot4Interface.put(CompRoleImpl.class, new ComponentSubstitualibityChecker(hierarchyBuilderOld, hierarchyBuilderNew));
		mapCheckerNot4Interface.put(Connection.class, new ConnectionSubstituabilityChecker(hierarchyBuilderOld, hierarchyBuilderNew));
		mapCheckerNot4Interface.put(InstConnection.class, new ConnectionSubstituabilityChecker(hierarchyBuilderOld, hierarchyBuilderNew));
		mapCheckerNot4Interface.put(InstConnectionImpl.class, new ConnectionSubstituabilityChecker(hierarchyBuilderOld, hierarchyBuilderNew));
		mapCheckerNot4Interface.put(ClassConnection.class, new ConnectionSubstituabilityChecker(hierarchyBuilderOld, hierarchyBuilderNew));
		mapCheckerNot4Interface.put(ClassConnectionImpl.class, new ConnectionSubstituabilityChecker(hierarchyBuilderOld, hierarchyBuilderNew));
		mapCheckerNot4Interface.put(RoleConnection.class, new ConnectionSubstituabilityChecker(hierarchyBuilderOld, hierarchyBuilderNew));
		mapCheckerNot4Interface.put(RoleConnectionImpl.class, new ConnectionSubstituabilityChecker(hierarchyBuilderOld, hierarchyBuilderNew));

		mapChecker4Interface.put(Interface.class, new InterfaceSubstitualibityChecker(hierarchyBuilderOld, hierarchyBuilderNew));
		mapChecker4Interface.put(InterfaceImpl.class, new InterfaceSubstitualibityChecker(hierarchyBuilderOld, hierarchyBuilderNew));
		//---------------------------------------------
		
		System.out.println("Loading hierarchy builders OK");

		System.out.println("Creation URIs for each file to compare...");
		URI uri1 = URI.createFileURI(mapPathsOldProject.get(MapPathKeys.DEDAL_FILE_PATH));
		URI uri2 = URI.createFileURI(mapPathsNewProject.get(MapPathKeys.DEDAL_FILE_PATH));
		System.out.println("Creation URIs OK");

		System.out.println("Get factory instance...");
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("dedal", new XMIResourceFactoryImpl());
		System.out.println("Get factory instance OK");

		System.out.println("Loading resource set...");

		resourceSetOld.getResource(uri1, true);
		resourceSetNew.getResource(uri2, true);

		System.out.println("Loading resource set OK !");
	}
	
	public EList<Diff> getDiffsList() {
		return diffsList;
	}
	
	public ResourceSet getResourceSetOld() {
		return resourceSetOld;
	}
	
	public ResourceSet getResourceSetNew() {
		return resourceSetNew;
	}
	
	public void createDifferences() {
		IComparisonScope scope = new DefaultComparisonScope(resourceSetOld, resourceSetNew, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		diffsList = comparison.getDifferences();

		System.out.println("Comparison  OK !");
	}
	
	public Boolean checkSubstitutability(DiffDedal diffDedal) {
		try {
			if(diffDedal.getDiffObject() instanceof Interface) {
				return this.mapChecker4Interface.get(diffDedal.getDiffObject().getClass()).check(diffDedal.getDiff(), 
						((Interface)diffDedal.getDiffObject()).getDirection(), diffDedal.getDiffKind());
			} else {
				//System.out.println(diffDedal.getDiffObject().getClass());
				if(!(diffDedal.getDiffObject() instanceof InterfaceType))
					return this.mapCheckerNot4Interface.get(diffDedal.getDiffObject().getClass()).check(diffDedal.getDiff(), 
							diffDedal.getDiffKind());
				else
					return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println();
		}
		return Boolean.FALSE;
	}

//	public void createDifferences() {
//		
//		IComparisonScope scope = new DefaultComparisonScope(resourceSetOld, resourceSetNew, null);
//		Comparison comparison = EMFCompare.builder().build().compare(scope);
//
//		diffsList = comparison.getDifferences();
//		
//
//		System.out.println("Comparison  OK !");
//
//		diffsList.forEach(diff -> {
//
//			if(diff instanceof ReferenceChange ) {
//						
//				if (diff.getKind() == DifferenceKind.ADD) {
//					findDedalTypeOfEObject(diff, diff.getKind());
//				}else if (diff.getKind() == DifferenceKind.CHANGE && diff.getMatch().getRight() != null && diff.getMatch().getLeft() != null) {
//					findDedalTypeOfEObject(diff, diff.getKind());
//				}else if (diff.getKind() == DifferenceKind.DELETE) {
//					findDedalTypeOfEObject(diff, diff.getKind());
//				}else {
//
//				}
//			}
//
//		});
//		System.out.println("Wrinting JSON.....");
//
//		jsonWriterSingleton.writeJson(directoryToStoreJsonFiles + 
//				mapPathsOldProject.get(MapPathKeys.PROJECT_NAME) + "---" + mapPathsNewProject.get(MapPathKeys.PROJECT_NAME) + ".json");
//		
//		System.out.println("Wrinting JSON OK");
//	
//		hierarchyBuilderManager.init();
//	}
//	
//	private void findDedalTypeOfEObject(Diff diff, DifferenceKind differenceKind) {
//		ReferenceChange referenceChange = ((ReferenceChange)diff);
//		EObject eObject = referenceChange.getValue();
//
//		if (eObject instanceof Component) {
//			Component component = (Component)eObject;
//			
//			try {
//				if(component instanceof CompClass) {
//					jsonWriterSingleton.addDiffToJsonArray(mapCheckerNot4Interface.get(Component.class).check(diff, differenceKind).toJsonObject());
//				}
//					
//				if(component instanceof CompInstance) {
//					jsonWriterSingleton.addDiffToJsonArray(mapCheckerNot4Interface.get(Component.class).check(diff, differenceKind).toJsonObject());
//	
//				}
//				if(component instanceof CompRole) {
//	
//						jsonWriterSingleton.addDiffToJsonArray(mapCheckerNot4Interface.get(Component.class).check(diff, differenceKind).toJsonObject());
//				}
//			} catch (ClassNotFoundException e) {
//					// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}
//		
//		if(eObject instanceof Connection) {
//			try {
//				jsonWriterSingleton.addDiffToJsonArray(mapCheckerNot4Interface.get(Connection.class).check(diff, differenceKind).toJsonObject());
//			} catch (JSONException | ClassNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	
//		if(eObject instanceof Interface) {
//			Interface interfaceObject = (Interface)eObject;
//			try {
////				DiffObjectJson diffObjectJson = jsonWriterSingleton.addDiffToJsonArray(
////						mapChecker4Interface.get(Interface.class).check(diff, interfaceObject.getDirection(), differenceKind)
////						.toJsonObject());
//				//DiffObjectJson diffObjectJson = mapChecker4Interface.get(Interface.class).check(diff, interfaceObject.getDirection(), differenceKind);
//				List<DiffObjectJson> listDiffObjectJson = mapChecker4Interface.get(Interface.class).check(diff, interfaceObject.getDirection(), differenceKind);
//				
//				for(DiffObjectJson diffObjectJson : listDiffObjectJson) {
//					if (diffObjectJson != null) {
//						jsonWriterSingleton.addDiffToJsonArray(diffObjectJson.toJsonObject());
//					}
//				}
//				
//
//			} catch (ClassNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		if(eObject instanceof Attribute) {
//			try {
//				jsonWriterSingleton.addDiffToJsonArray(mapCheckerNot4Interface.get(Attribute.class).check(diff, differenceKind).toJsonObject());
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (ClassNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		if (eObject instanceof Parameter) {
//			try {
//				jsonWriterSingleton.addDiffToJsonArray(mapCheckerNot4Interface.get(Parameter.class).check(diff, differenceKind).toJsonObject());
//			} catch (JSONException | ClassNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//		if(eObject instanceof Signature) {
//			try {
//				jsonWriterSingleton.addDiffToJsonArray(mapCheckerNot4Interface.get(Signature.class).check(diff, differenceKind).toJsonObject());
//			} catch (ClassNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
	
	public void initHierarchyBuilder() {
		hierarchyBuilderManager.init();
	}
}
