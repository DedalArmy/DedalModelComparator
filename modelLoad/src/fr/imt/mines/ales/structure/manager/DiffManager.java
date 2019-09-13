package fr.imt.mines.ales.structure.manager;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.FeatureMapChange;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.ResourceLocationChange;
import org.eclipse.emf.ecore.EObject;

import dedal.ArchitectureDescription;
import dedal.Assembly;
import dedal.Attribute;
import dedal.ClassConnection;
import dedal.CompClass;
import dedal.CompInstance;
import dedal.CompRole;
import dedal.CompType;
import dedal.Component;
import dedal.Configuration;
import dedal.Connection;
import dedal.DIRECTION;
import dedal.InstConnection;
import dedal.Interface;
import dedal.InterfaceType;
import dedal.Parameter;
import dedal.RoleConnection;
import dedal.Signature;
import dedal.Specification;
import fr.imt.mines.ales.structure.DiffArchitectureLevel;
import fr.imt.mines.ales.structure.DiffAttribute;
import fr.imt.mines.ales.structure.DiffComponent;
import fr.imt.mines.ales.structure.DiffConnection;
import fr.imt.mines.ales.structure.DiffDedal;
import fr.imt.mines.ales.structure.DiffInterface;
import fr.imt.mines.ales.structure.DiffInterfaceType;
import fr.imt.mines.ales.structure.DiffParameter;
import fr.imt.mines.ales.structure.DiffSignature;
import fr.imt.mines.ales.structure.impl.DiffInterfaceImpl;
import fr.imt.mines.ales.structure.impl.diffassembly.DiffAssembly;
import fr.imt.mines.ales.structure.impl.diffassembly.DiffComponentInstance;
import fr.imt.mines.ales.structure.impl.diffassembly.DiffInstanceConnection;
import fr.imt.mines.ales.structure.impl.diffconfiguration.DiffAttributeImpl;
import fr.imt.mines.ales.structure.impl.diffconfiguration.DiffClassConnection;
import fr.imt.mines.ales.structure.impl.diffconfiguration.DiffComponentClass;
import fr.imt.mines.ales.structure.impl.diffconfiguration.DiffConfiguration;
import fr.imt.mines.ales.structure.impl.diffrepo.DiffComponentType;
import fr.imt.mines.ales.structure.impl.diffrepo.DiffInterfaceTypeImpl;
import fr.imt.mines.ales.structure.impl.diffrepo.DiffParameterImpl;
import fr.imt.mines.ales.structure.impl.diffrepo.DiffSignatureImpl;
import fr.imt.mines.ales.structure.impl.diffspecification.DiffComponentRole;
import fr.imt.mines.ales.structure.impl.diffspecification.DiffRoleConnection;
import fr.imt.mines.ales.structure.impl.diffspecification.DiffSpecification;

public class DiffManager {

	/**
	 * Singleton instance
	 */
	private static DiffManager INSTANCE;

	private List<DiffArchitectureLevel> diffArchitectureLevels;
	private List<DiffComponent> diffComponents;
	private List<DiffInterfaceType> diffInterfaceTypes;
	private List<DiffInterface> diffInterfaces;
	private List<DiffConnection> diffConnections;
	private List<DiffParameter> diffParameters;
	private List<DiffSignature> diffSignatures;
	private List<DiffDedal> diffDedals;
	private List<DiffAttribute> diffAttributes;

	private DiffManager() {}

	public static DiffManager getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new DiffManager();
		}
		return INSTANCE;
	}

	public void init(List<Diff> diffs) {
		this.diffArchitectureLevels = new ArrayList<DiffArchitectureLevel>();
		this.diffComponents = new ArrayList<DiffComponent>();
		this.diffInterfaceTypes = new ArrayList<DiffInterfaceType>();
		this.diffInterfaces = new ArrayList<DiffInterface>();
		this.diffConnections = new ArrayList<DiffConnection>();
		this.diffParameters = new ArrayList<DiffParameter>();
		this.diffSignatures = new ArrayList<DiffSignature>();
		this.diffDedals = new ArrayList<DiffDedal>();
		this.diffAttributes = new ArrayList<DiffAttribute>();

		System.out.println("diffs size : " + diffs.size());
		this.fillSets(diffs);
		this.printSetSizes();
		this.relateDiffsToOneAnother();
		System.out.println("-------------------------------------------------");
		this.printSetSizes();
		System.out.println();
	}

	private void printSetSizes() {
		System.out.println("diffArchitectureLevels size : " + diffArchitectureLevels.size());
		System.out.println("diffComponents size : " + diffComponents.size());
		System.out.println("diffInterfaceType size : " + diffInterfaceTypes.size());
		System.out.println("diffInterfaces size : " + diffInterfaces.size());
		System.out.println("diffConnections size : " + diffConnections.size());
		System.out.println("diffParameters size : " + diffParameters.size());
		System.out.println("diffSignatures size : " + diffSignatures.size());
		System.out.println("diffAttributes size : " + diffAttributes.size());
		System.out.println("diffDedals size : " + diffDedals.size());
	}

	private void fillSets(List<Diff> diffs) {

		for(Diff diff : diffs) {
			Object object = null;

			object = getObject(diff, object);
			
			if(object != null) {
				if (object instanceof Component) {
					Component component = (Component)object;
					if(component instanceof CompClass) {
						DiffComponentClass diffCC = new DiffComponentClass(diff);
						this.diffComponents.add(diffCC);
						this.diffDedals.add(diffCC);
					}
					if(component instanceof CompInstance) {
						DiffComponentInstance diffCI = new DiffComponentInstance(diff);
						this.diffComponents.add(diffCI);
						this.diffDedals.add(diffCI);
					}
					if(component instanceof CompRole) {
						DiffComponentRole diffCR = new DiffComponentRole(diff);
						this.diffComponents.add(diffCR);
						this.diffDedals.add(diffCR);
					}
					if(component instanceof CompType) {
						DiffComponentType diffCT = new DiffComponentType(diff);
						this.diffComponents.add(diffCT);
						this.diffDedals.add(diffCT);
					}
				}

				if(object instanceof Interface) {
					DiffInterfaceImpl diffI = new DiffInterfaceImpl(diff);
					this.diffInterfaces.add(diffI);
					this.diffDedals.add(diffI);
				}

				if(object instanceof Attribute) {
					DiffAttributeImpl diffA = new DiffAttributeImpl(diff);
					this.diffAttributes.add(diffA);
					this.diffDedals.add(diffA);
				}

				if (object instanceof Parameter) {
					DiffParameterImpl diffP = new DiffParameterImpl(diff);
					this.diffParameters.add(diffP);
					this.diffDedals.add(diffP);
				}

				if(object instanceof Signature) {
					DiffSignatureImpl diffS = new DiffSignatureImpl(diff);
					this.diffSignatures.add(diffS);
					this.diffDedals.add(diffS);
				}

				if(object instanceof InterfaceType) {
					DiffInterfaceTypeImpl diffIT = new DiffInterfaceTypeImpl(diff);
					this.diffInterfaceTypes.add(diffIT);
					this.diffDedals.add(diffIT);
				}

				if(object instanceof ArchitectureDescription) {
					if(object instanceof Assembly) {
						DiffAssembly diffA = new DiffAssembly(diff);
						this.diffArchitectureLevels.add(diffA);
						this.diffDedals.add(diffA);
					}
					if(object instanceof Configuration) {
						DiffConfiguration diffC = new DiffConfiguration(diff);
						this.diffArchitectureLevels.add(diffC);
						this.diffDedals.add(diffC);
					}
					if(object instanceof Specification) {
						DiffSpecification diffS = new DiffSpecification(diff);
						this.diffArchitectureLevels.add(diffS);
						this.diffDedals.add(diffS);
					}
				}

				if(object instanceof Connection) {
					if(object instanceof InstConnection) {
						DiffInstanceConnection diffIC = new DiffInstanceConnection(diff);
						this.diffConnections.add(diffIC);
						this.diffDedals.add(diffIC);
					}
					if(object instanceof ClassConnection) {
						DiffClassConnection diffCC = new DiffClassConnection(diff);
						this.diffConnections.add(diffCC);
						this.diffDedals.add(diffCC);
					}
					if(object instanceof RoleConnection) {
						DiffRoleConnection diffRC = new DiffRoleConnection(diff);
						this.diffConnections.add(diffRC);
						this.diffDedals.add(diffRC);
					}
				}
			}
		}
	}

	private Object getObject(Diff diff, Object object) {
		if(diff instanceof AttributeChange) {
			object = ((AttributeChange) diff).getValue();
		} else if (diff instanceof FeatureMapChange) {
			object = ((FeatureMapChange) diff).getValue();
		} else if (diff instanceof ReferenceChange) {
			object = ((ReferenceChange) diff).getValue();
		} else if (diff instanceof ResourceAttachmentChange) {
			System.out.println("RESOURCE ATTACHMENT CHANGE");
		} else if (diff instanceof ResourceLocationChange) {
			System.out.println("RESOURCE LOCATION CHANGE");
		}
		return object;
	}
	
	private void relateDiffsToOneAnother() {
		this.relateDiffParametersToDiffSignatures();
		this.relateDiffSignaturesToDiffInterfaceTypes();
		this.relateDiffInterfacesTypeToDiffInterfaces();
		this.relateDiffInterfacesToDiffComponents();
		this.relateDiffInterfacesToDiffConnections();
		this.relateDiffAttributesToDiffComponents();
		this.relateDiffComponentsToDiffConnections();
		this.relateDiffComponentsToDiffArchitectureLevels();
		this.relateDiffConnectionsToDiffArchitectureLevels();
	}

	private void relateDiffParametersToDiffSignatures() {
		List<DiffParameter> toRemove = new ArrayList<DiffParameter>();
		for(DiffParameter dp : this.diffParameters) {
			Diff diff = dp.getDiff();
			List<Diff> requiredBy = diff.getRequiredBy();
			for(Diff d : requiredBy) {
				DiffDedal dd = this.getDiffDedal(d);
				if(dd != null && dd instanceof DiffSignature) {
					((DiffSignature)dd).addDiffParameter(dp);
					toRemove.add(dp);
				}
			}
			List<Diff> requires = diff.getRequires();
			for(Diff d : requires) {
				DiffDedal dd = this.getDiffDedal(d);
				if(dd != null && dd instanceof DiffSignature) {
					((DiffSignature)dd).addDiffParameter(dp);
					toRemove.add(dp);
				}
			}
		}
		this.diffParameters.removeAll(toRemove);
	}

	private void relateDiffSignaturesToDiffInterfaceTypes() {
		List<DiffSignature> toRemove = new ArrayList<DiffSignature>();
		for(DiffSignature ds : this.diffSignatures) {
			Diff diff = ds.getDiff();
			List<Diff> requiredBy = diff.getRequiredBy();
			for(Diff d : requiredBy) {
				DiffDedal dd = this.getDiffDedal(d);
				if(dd != null && dd instanceof DiffInterfaceType) {
					((DiffInterfaceType)dd).addDiffSignature(ds);
					toRemove.add(ds);
				} else if(dd != null && dd instanceof DiffParameter) {
					toRemove.add(ds);
				} else {
					//TODO
//					System.out.println("PendingDiffSignature");
				}
			}
			List<Diff> requires = diff.getRequires();
			for(Diff d : requires) {
				DiffDedal dd = this.getDiffDedal(d);
				if(dd != null && dd instanceof DiffInterfaceType) {
					((DiffInterfaceType)dd).addDiffSignature(ds);
					toRemove.add(ds);
				} else if(dd != null && dd instanceof DiffParameter) {
					toRemove.add(ds);
				} else {
					//TODO
//					System.out.println("PendingDiffSignature");
				}
			}
		}
		this.diffSignatures.removeAll(toRemove);
		toRemove = new ArrayList<DiffSignature>();
//		this.diffSignatures.forEach(ds -> System.out.println(ds.getDiff()));
		for(DiffSignature ds : this.diffSignatures){
			DiffInterfaceType diffIT = this.getDiffInterfaceTypeByInterfaceType((InterfaceType) ds.getDiffObject().eContainer());
			if(diffIT != null) {
				diffIT.addDiffSignature(ds);
				toRemove.add(ds);
			}
		}
		this.diffSignatures.removeAll(toRemove); 
	}

	private DiffInterfaceType getDiffInterfaceTypeByInterfaceType(InterfaceType interfaceType) {
		for(DiffInterfaceType dit : this.diffInterfaceTypes) {
			EObject diffObject = dit.getDiffObject();
			if(interfaceType != null && interfaceType.equals(diffObject)) {
				return dit;
			}
		}
		return null;
	}

	private void relateDiffInterfacesTypeToDiffInterfaces() {
		List<DiffInterfaceType> toRemove = new ArrayList<DiffInterfaceType>();
		for(DiffInterfaceType dit : this.diffInterfaceTypes) {
			Diff diff = dit.getDiff();
			List<Diff> requiredBy = diff.getRequiredBy();
			for(Diff d : requiredBy) {
				DiffDedal dd = this.getDiffDedal(d);
				if(dd != null && dd instanceof DiffInterface) {
					((DiffInterface)dd).setDiffInterfaceType(dit);
					toRemove.add(dit);
				} else if(dd != null && (dd instanceof DiffParameter || dd instanceof DiffSignature)) {
					toRemove.add(dit);
				} else {
					//TODO
//					System.out.println("PendingDiffInterfaceType");
				}
			}
			List<Diff> requires = diff.getRequires();
			for(Diff d : requires) {
				DiffDedal dd = this.getDiffDedal(d);
				if(dd != null && dd instanceof DiffInterface) {
					((DiffInterface)dd).setDiffInterfaceType(dit);
					toRemove.add(dit);
				} else if(dd != null && (dd instanceof DiffParameter || dd instanceof DiffSignature)) {
					toRemove.add(dit);
				} else {
					//TODO
//					System.out.println("PendingDiffInterfaceType");
				}
			}
		}
		this.diffInterfaceTypes.removeAll(toRemove);
	}

	private void relateDiffInterfacesToDiffConnections() {
		List<DiffInterface> toRemove = new ArrayList<DiffInterface>();
		for(DiffInterface di : this.diffInterfaces) {
			Diff diff = di.getDiff();
			List<Diff> requiredBy = diff.getRequiredBy();
			for(Diff d : requiredBy) {
				DiffDedal dd = this.getDiffDedal(d);
				if(dd != null && dd instanceof DiffConnection) {
					if(((Interface)di.getDiffObject()).getDirection().equals(DIRECTION.PROVIDED))
						((DiffConnection)dd).setServerDiffInterface(di);
					else if(((Interface)di.getDiffObject()).getDirection().equals(DIRECTION.REQUIRED))
						((DiffConnection)dd).setClientDiffInterface(di);
					toRemove.add(di);
				} else if(dd != null && (dd instanceof DiffParameter || dd instanceof DiffSignature 
						|| dd instanceof DiffInterfaceType)) {
					toRemove.add(di);
				} else {
					//TODO
//					System.out.println("PendingDiffInterface");
				}
			}
			List<Diff> requires = diff.getRequires();
			for(Diff d : requires) {
				DiffDedal dd = this.getDiffDedal(d);
				if(dd != null && dd instanceof DiffConnection) {
					if(((Interface)di.getDiffObject()).getDirection().equals(DIRECTION.PROVIDED))
						((DiffConnection)dd).setServerDiffInterface(di);
					else if(((Interface)di.getDiffObject()).getDirection().equals(DIRECTION.REQUIRED))
						((DiffConnection)dd).setClientDiffInterface(di);
					toRemove.add(di);
				} else if(dd != null && (dd instanceof DiffParameter || dd instanceof DiffSignature 
						|| dd instanceof DiffInterfaceType)) {
					toRemove.add(di);
				} else {
					//TODO
//					System.out.println("PendingDiffInterface");
				}
			}
		}
		this.diffInterfaces.removeAll(toRemove);
	}
	
	private void relateDiffInterfacesToDiffComponents() {
		List<DiffInterface> toRemove = new ArrayList<DiffInterface>();
		for(DiffInterface di : this.diffInterfaces) {
			Diff diff = di.getDiff();
			List<Diff> requiredBy = diff.getRequiredBy();
			for(Diff d : requiredBy) {
				DiffDedal dd = this.getDiffDedal(d);
				if(dd != null && dd instanceof DiffComponent) {
					((DiffComponent)dd).addDiffInterface(di);
					toRemove.add(di);
				} else if(dd != null && (dd instanceof DiffParameter || dd instanceof DiffSignature 
						|| dd instanceof DiffInterfaceType)) {
					toRemove.add(di);
				} else {
					//TODO
//					System.out.println("PendingDiffInterface");
				}
			}
			List<Diff> requires = diff.getRequires();
			for(Diff d : requires) {
				DiffDedal dd = this.getDiffDedal(d);
				if(dd != null && dd instanceof DiffComponent) {
					((DiffComponent)dd).addDiffInterface(di);
					toRemove.add(di);
				} else if(dd != null && (dd instanceof DiffParameter || dd instanceof DiffSignature 
						|| dd instanceof DiffInterfaceType)) {
					toRemove.add(di);
				} else {
					//TODO
//					System.out.println("PendingDiffInterface");
				}
			}
		}
		this.diffInterfaces.removeAll(toRemove);
	}

	private void relateDiffAttributesToDiffComponents() {
		List<DiffAttribute> toRemove = new ArrayList<DiffAttribute>();
		for(DiffAttribute da : this.diffAttributes) {
			Diff diff = da.getDiff();
			List<Diff> requiredBy = diff.getRequiredBy();
			for(Diff d : requiredBy) {
				DiffDedal dd = this.getDiffDedal(d);
				if(dd != null && dd instanceof DiffComponentClass) {
					((DiffComponentClass)dd).addDiffAttribute(da);
					toRemove.add(da);
				} else {
					//TODO
//					System.out.println("PendingDiffAttribute");
				}
			}
			List<Diff> requires = diff.getRequires();
			for(Diff d : requires) {
				DiffDedal dd = this.getDiffDedal(d);
				if(dd != null && dd instanceof DiffComponentClass) {
					((DiffComponentClass)dd).addDiffAttribute(da);
					toRemove.add(da);
				} else {
					//TODO
//					System.out.println("PendingDiffAttribute");
				}
			}
		}
		this.diffAttributes.removeAll(toRemove);
	}

	private void relateDiffComponentsToDiffConnections() {
		List<DiffComponent> toRemove = new ArrayList<DiffComponent>();
		for(DiffComponent dc : this.diffComponents) {
			Diff diff = dc.getDiff();
			List<Diff> requiredBy = diff.getRequiredBy();
			for(Diff d : requiredBy) {
				DiffDedal dd = this.getDiffDedal(d);
				if(dd != null && dd instanceof DiffInstanceConnection) {
					InstConnection connection = ((DiffInstanceConnection)dd).getDiffObject() instanceof InstConnection?
							(InstConnection)((DiffConnection)dd).getDiffObject()
							: null;
					if(connection != null && connection.getServerInstElem().equals(dc.getDiffObject())) {
						((DiffConnection)dd).setServerDiffComponent(dc);
						toRemove.add(dc);
					}
					else if(connection != null && connection.getClientInstElem().equals(dc.getDiffObject())) {
						((DiffConnection)dd).setClientDiffComponent(dc);
						toRemove.add(dc);
					}
				} else if(dd != null && dd instanceof DiffClassConnection) {
					ClassConnection connection = ((DiffClassConnection)dd).getDiffObject() instanceof ClassConnection?
							(ClassConnection)((DiffConnection)dd).getDiffObject()
							: null;
					if(connection != null && connection.getServerClassElem().equals(dc.getDiffObject())) {
						((DiffConnection)dd).setServerDiffComponent(dc);
						toRemove.add(dc);
					}
					else if(connection != null && connection.getClientClassElem().equals(dc.getDiffObject())) {
						((DiffConnection)dd).setClientDiffComponent(dc);
						toRemove.add(dc);
					}
				}  else if(dd != null && dd instanceof DiffRoleConnection) {
					RoleConnection connection = ((DiffRoleConnection)dd).getDiffObject() instanceof RoleConnection?
							(RoleConnection)((DiffConnection)dd).getDiffObject()
							: null;
					if(connection != null && connection.getServerCompElem().equals(dc.getDiffObject())) {
						((DiffConnection)dd).setServerDiffComponent(dc);
						toRemove.add(dc);
					}
					else if(connection != null && connection.getClientCompElem().equals(dc.getDiffObject())) {
						((DiffConnection)dd).setClientDiffComponent(dc);
						toRemove.add(dc);
					}
				} else if(dd != null && (dd instanceof DiffParameter || dd instanceof DiffSignature 
						|| dd instanceof DiffInterfaceType || dd instanceof DiffInterface)) {
					toRemove.add(dc);
				} else {
					//TODO
//					System.out.println("PendingDiffInterface");
				}
			}
			List<Diff> requires = diff.getRequires();
			for(Diff d : requires) {
				DiffDedal dd = this.getDiffDedal(d);
				if(dd != null && dd instanceof DiffInstanceConnection) {
					InstConnection connection = ((DiffInstanceConnection)dd).getDiffObject() instanceof InstConnection?
							(InstConnection)((DiffConnection)dd).getDiffObject()
							: null;
					if(connection != null && connection.getServerInstElem().equals(dc.getDiffObject())) {
						((DiffConnection)dd).setServerDiffComponent(dc);
						toRemove.add(dc);
					}
					else if(connection != null && connection.getClientInstElem().equals(dc.getDiffObject())) {
						((DiffConnection)dd).setClientDiffComponent(dc);
						toRemove.add(dc);
					}
				} else if(dd != null && dd instanceof DiffClassConnection) {
					ClassConnection connection = ((DiffClassConnection)dd).getDiffObject() instanceof ClassConnection?
							(ClassConnection)((DiffConnection)dd).getDiffObject()
							: null;
					if(connection != null && connection.getServerClassElem().equals(dc.getDiffObject())) {
						((DiffConnection)dd).setServerDiffComponent(dc);
						toRemove.add(dc);
					}
					else if(connection != null && connection.getClientClassElem().equals(dc.getDiffObject())) {
						((DiffConnection)dd).setClientDiffComponent(dc);
						toRemove.add(dc);
					}
				}  else if(dd != null && dd instanceof DiffRoleConnection) {
					RoleConnection connection = ((DiffRoleConnection)dd).getDiffObject() instanceof RoleConnection?
							(RoleConnection)((DiffConnection)dd).getDiffObject()
							: null;
					if(connection != null && connection.getServerCompElem().equals(dc.getDiffObject())) {
						((DiffConnection)dd).setServerDiffComponent(dc);
						toRemove.add(dc);
					}
					else if(connection != null && connection.getClientCompElem().equals(dc.getDiffObject())) {
						((DiffConnection)dd).setClientDiffComponent(dc);
						toRemove.add(dc);
					}
				} else if(dd != null && (dd instanceof DiffParameter || dd instanceof DiffSignature 
						|| dd instanceof DiffInterfaceType || dd instanceof DiffInterface)) {
					toRemove.add(dc);
				} else {
					//TODO
//					System.out.println("PendingDiffInterface");
				}
			}
		}
		this.diffComponents.removeAll(toRemove);
	}

	private void relateDiffComponentsToDiffArchitectureLevels() {
		// TODO Auto-generated method stub
		
	}

	private void relateDiffConnectionsToDiffArchitectureLevels() {
		// TODO Auto-generated method stub
		
	}

	private DiffDedal getDiffDedal(Diff diff) {
		for(DiffDedal dd : this.diffDedals) {
			if(diff != null && diff.equals(dd.getDiff())) {
				return dd;
			}
		}
		return null;
	}
}
