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
import org.eclipse.emf.ecore.resource.ResourceSet;

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
import dedal.DedalDiagram;
import dedal.InstConnection;
import dedal.Interaction;
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

	private DedalDiagram oldDiagram;	
	private DedalDiagram newDiagram;

	private DiffManager() {}

	public static DiffManager getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new DiffManager();
		}
		return INSTANCE;
	}

	public void init(List<Diff> diffs, ResourceSet resourceSetOld, ResourceSet resourceSetNew) {
		this.diffArchitectureLevels = new ArrayList<>();
		this.diffComponents = new ArrayList<>();
		this.diffInterfaceTypes = new ArrayList<>();
		this.diffInterfaces = new ArrayList<>();
		this.diffConnections = new ArrayList<>();
		this.diffParameters = new ArrayList<>();
		this.diffSignatures = new ArrayList<>();
		this.diffDedals = new ArrayList<>();
		this.diffAttributes = new ArrayList<>();

		this.oldDiagram = (DedalDiagram) resourceSetOld.getResources().get(0).getContents().get(0);
		this.newDiagram = (DedalDiagram) resourceSetNew.getResources().get(0).getContents().get(0);

		System.out.println("diffs size : " + diffs.size());
		this.fillSets(diffs);
		System.out.println("-------------------------------------------------");
		System.out.println("-------------------------------------------------");
		this.printSetSizes();
		this.relateDiffsToOneAnother();
		System.out.println("-------------------------------------------------");
		System.out.println("-------------------------------------------------");
		this.printSetSizes();
		System.out.println("-------------------------------------------------");
		System.out.println("-------------------------------------------------");
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
		this.relateDiffInterfacesToDiffComponentsAndDiffConnections();
		this.relateDiffInterfacesToDiffConnections();
		this.relateDiffAttributesToDiffComponents();
		this.relateDiffComponentsToDiffConnectionsAndDiffArchitectureLevels();
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
				if(dd instanceof DiffSignature) {
					((DiffSignature)dd).addDiffParameter(dp);
					toRemove.add(dp);
				}
			}
			List<Diff> requires = diff.getRequires();
			for(Diff d : requires) {
				DiffDedal dd = this.getDiffDedal(d);
				if(dd instanceof DiffSignature) {
					((DiffSignature)dd).addDiffParameter(dp);
					toRemove.add(dp);
				}
			}
		}
		this.diffParameters.removeAll(toRemove);
		toRemove = new ArrayList<>();
		for(DiffParameter dp : this.diffParameters){
			Signature signature = (Signature) dp.getDiffObject().eContainer();
			DiffSignature diffS = this.getDiffSignatureBySignatureID(signature.getId());
			if(diffS != null) {
				diffS.addDiffParameter(dp);
				toRemove.add(dp);
			} else {
				diffS = new DiffSignatureImpl(null);
				diffS.setDiffObject(signature);
				diffS.addDiffParameter(dp);
				toRemove.add(dp);
				this.diffSignatures.add(diffS);
				this.diffDedals.add(diffS);
			}
		}
		this.diffParameters.removeAll(toRemove);
	}


	private DiffSignature getDiffSignatureBySignatureID(String id) {
		for(DiffSignature ds : this.diffSignatures) {
			if(id != null && id.equals(((Signature)ds.getDiffObject()).getId()))
				return ds;
		}
		return null;
	}


	private void relateDiffSignaturesToDiffInterfaceTypes() {
		List<DiffSignature> toRemove = new ArrayList<>();
		for(DiffSignature ds : this.diffSignatures) {
			Diff diff = ds.getDiff();
			if(diff != null) {
				List<Diff> requiredBy = diff.getRequiredBy();
				for(Diff d : requiredBy) {
					DiffDedal dd = this.getDiffDedal(d);
					if(dd instanceof DiffInterfaceType) {
						((DiffInterfaceType)dd).addDiffSignature(ds);
						toRemove.add(ds);
					} else if(dd instanceof DiffParameter) {
						toRemove.add(ds);
					}
				}
				List<Diff> requires = diff.getRequires();
				for(Diff d : requires) {
					DiffDedal dd = this.getDiffDedal(d);
					if(dd instanceof DiffInterfaceType) {
						((DiffInterfaceType)dd).addDiffSignature(ds);
						toRemove.add(ds);
					} else if(dd instanceof DiffParameter) {
						toRemove.add(ds);
					}
				}
			}
		}
		this.diffSignatures.removeAll(toRemove);
		toRemove = new ArrayList<>();
		for(DiffSignature ds : this.diffSignatures){
			InterfaceType interfaceType = (InterfaceType) ds.getDiffObject().eContainer();
			DiffInterfaceType diffIT = this.getDiffInterfaceTypeByInterfaceType(interfaceType);
			if(diffIT != null) {
				diffIT.addDiffSignature(ds);
				toRemove.add(ds);
			} else {
				diffIT = new DiffInterfaceTypeImpl(null);
				diffIT.setDiffObject(interfaceType);
				diffIT.addDiffSignature(ds);
				toRemove.add(ds);
				this.diffInterfaceTypes.add(diffIT);
				this.diffDedals.add(diffIT);
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
			if(diff != null) {
				List<Diff> requiredBy = diff.getRequiredBy();
				for(Diff d : requiredBy) {
					DiffDedal dd = this.getDiffDedal(d);
					if(dd instanceof DiffInterface) {
						((DiffInterface)dd).setDiffInterfaceType(dit);
						toRemove.add(dit);
					} else if(dd != null && (dd instanceof DiffParameter || dd instanceof DiffSignature)) {
						toRemove.add(dit);
					}
				}
				List<Diff> requires = diff.getRequires();
				for(Diff d : requires) {
					DiffDedal dd = this.getDiffDedal(d);
					if(dd instanceof DiffInterface) {
						((DiffInterface)dd).setDiffInterfaceType(dit);
						toRemove.add(dit);
					} else if(dd != null && (dd instanceof DiffParameter || dd instanceof DiffSignature)) {
						toRemove.add(dit);
					}
				}
			}
		}
		this.diffInterfaceTypes.removeAll(toRemove);
		toRemove = new ArrayList<>();
		for(DiffInterfaceType dit : this.diffInterfaceTypes){
			List<Interface> impactedInterfaces = this.findImpactedInterfaces((InterfaceType) dit.getDiffObject(), this.newDiagram);
			this.putDiffInterfaceTypesInDiffInterfaces(toRemove, dit, impactedInterfaces);
			if(impactedInterfaces == null || impactedInterfaces.isEmpty()) {
				impactedInterfaces = this.findImpactedInterfaces((InterfaceType) dit.getDiffObject(), this.oldDiagram);
				this.putDiffInterfaceTypesInDiffInterfaces(toRemove, dit, impactedInterfaces);
			}
		}
		this.diffInterfaceTypes.removeAll(toRemove);
	}


	private void putDiffInterfaceTypesInDiffInterfaces(List<DiffInterfaceType> toRemove, DiffInterfaceType dit,
			List<Interface> impactedInterfaces) {
		for(Interface iInt : impactedInterfaces) {
			DiffInterface diffI = this.getDiffInterfaceByInterface(iInt);
			if(diffI != null) {
				diffI.setDiffInterfaceType(dit);
				toRemove.add(dit);
				break;
			}  else if(dit.getDiffObject() instanceof InterfaceType) {
				diffI = new DiffInterfaceImpl(null);
				diffI.setDiffObject(iInt);
				diffI.setDiffInterfaceType(dit);
				toRemove.add(dit);
				this.diffInterfaces.add(diffI);
				this.diffDedals.add(diffI);
				break;
			}
		}
	}

	private DiffInterface getDiffInterfaceByInterface(Interface inter) {
		for(DiffInterface diffI : this.diffInterfaces) {
			if(inter != null && inter.equals(diffI.getDiffObject()))
				return diffI;
		}
		return null;
	}

	private List<Interface> findImpactedInterfaces(InterfaceType interfaceType, DedalDiagram dedalDiagram) {
		List<Interface> result = new ArrayList<>();
		for(ArchitectureDescription ad : dedalDiagram.getArchitectureDescriptions()) {
			if(ad instanceof Specification) {
				for(CompRole cr : ((Specification)ad).getSpecComponents()) {
					for(Interaction inter : cr.getCompInterfaces()) {
						if(inter instanceof Interface && 
								((Interface)inter).getType().equals(interfaceType)) {
							result.add((Interface) inter);
						}
					}
				}
			} else if(ad instanceof Configuration) {
				for(CompClass cc : ((Configuration)ad).getConfigComponents()) {
					for(Interaction inter : cc.getCompInterfaces()) {
						if(inter instanceof Interface && 
								((Interface)inter).getType().equals(interfaceType)) {
							result.add((Interface) inter);
						}
					}
				}
				for(CompType ct : ((Configuration)ad).getComptypes()) {
					for(Interaction inter : ct.getCompInterfaces()) {
						if(inter instanceof Interface && 
								((Interface)inter).getType().equals(interfaceType)) {
							result.add((Interface) inter);
						}
					}
				}
			} else if(ad instanceof Assembly) {
				for(CompInstance ci : ((Assembly)ad).getAssmComponents()) {
					for(Interaction inter : ci.getCompInterfaces()) {
						if(inter instanceof Interface && 
								((Interface)inter).getType().equals(interfaceType)) {
							result.add((Interface) inter);
						}
					}
				}
			}
		}
		return result;
	}

	private void relateDiffInterfacesToDiffConnections() {
		List<DiffInterface> toRemove = new ArrayList<DiffInterface>();
		for(DiffInterface di : this.diffInterfaces) {
			Diff diff = di.getDiff();
			if(diff != null) {
				List<Diff> requiredBy = diff.getRequiredBy();
				for(Diff d : requiredBy) {
					DiffDedal dd = this.getDiffDedal(d);
					if(dd instanceof DiffConnection) {
						if(((Interface)di.getDiffObject()).getDirection().equals(DIRECTION.PROVIDED))
							((DiffConnection)dd).setServerDiffInterface(di);
						else if(((Interface)di.getDiffObject()).getDirection().equals(DIRECTION.REQUIRED))
							((DiffConnection)dd).setClientDiffInterface(di);
						toRemove.add(di);
					} else if(dd != null && (dd instanceof DiffParameter || dd instanceof DiffSignature 
							|| dd instanceof DiffInterfaceType)) {
						toRemove.add(di);
					}
				}
				List<Diff> requires = diff.getRequires();
				for(Diff d : requires) {
					DiffDedal dd = this.getDiffDedal(d);
					if(dd instanceof DiffConnection) {
						if(((Interface)di.getDiffObject()).getDirection().equals(DIRECTION.PROVIDED))
							((DiffConnection)dd).setServerDiffInterface(di);
						else if(((Interface)di.getDiffObject()).getDirection().equals(DIRECTION.REQUIRED))
							((DiffConnection)dd).setClientDiffInterface(di);
						toRemove.add(di);
					} else if(dd != null && (dd instanceof DiffParameter || dd instanceof DiffSignature 
							|| dd instanceof DiffInterfaceType)) {
						toRemove.add(di);
					}
				}
			}
		}
		this.diffInterfaces.removeAll(toRemove);
		toRemove = new ArrayList<>();
		for(DiffInterface di : this.diffInterfaces){
			List<Connection> impactedConnections = this.findImpactedConnections((Interface) di.getDiffObject(), this.newDiagram);
			this.putDiffInterfacesInDiffConnections(toRemove, di, impactedConnections);
			if(impactedConnections == null || impactedConnections.isEmpty()) {
				impactedConnections = this.findImpactedConnections((Interface) di.getDiffObject(), this.oldDiagram);
				this.putDiffInterfacesInDiffConnections(toRemove, di, impactedConnections);
			}
		}
		this.diffInterfaces.removeAll(toRemove);
	}


	private void putDiffInterfacesInDiffConnections(List<DiffInterface> toRemove, DiffInterface di,
			List<Connection> impactedConnections) {
		for(Connection c : impactedConnections) {
			DiffConnection diffC = this.getDiffConnectionByConnection(c);
			if(diffC != null) {
				if(di.getDiffObject() instanceof Interface && 
						((Interface)di.getDiffObject()).getDirection().equals(DIRECTION.PROVIDED)) {
					diffC.setServerDiffInterface(di);
					toRemove.add(di);
				}
				else if (di.getDiffObject() instanceof Interface && 
						((Interface)di.getDiffObject()).getDirection().equals(DIRECTION.REQUIRED)) {
					diffC.setClientDiffInterface(di);
					toRemove.add(di);
				}
			} else if(di.getDiffObject() instanceof Interface) {
				Interface inter = (Interface)di.getDiffObject();
				if(inter.eContainer() instanceof CompInstance) {
					diffC = new DiffInstanceConnection(null);
				} else if(inter.eContainer() instanceof CompClass) {
					diffC = new DiffClassConnection(null);
				} else if(inter.eContainer() instanceof CompRole) {
					diffC = new DiffRoleConnection(null);
				}
				if(diffC != null) {
					diffC.setDiffObject(c);
					if(inter.getDirection().equals(DIRECTION.PROVIDED)) {
						diffC.setServerDiffInterface(di);
						toRemove.add(di);
						this.diffConnections.add(diffC);
						this.diffDedals.add(diffC);
					} else if(inter.getDirection().equals(DIRECTION.REQUIRED)) {
						diffC.setClientDiffInterface(di);
						toRemove.add(di);
						this.diffConnections.add(diffC);
						this.diffDedals.add(diffC);
					}
				}
			}
		}
	}

	private DiffConnection getDiffConnectionByConnection(Connection c) {
		for(DiffConnection diffC : this.diffConnections) {
			if(c != null && c.equals(diffC.getDiffObject())) {
				return diffC;
			}
		}
		return null;
	}

	private List<Connection> findImpactedConnections(Interface inter, DedalDiagram dedalDiagram) {
		List<Connection> result = new ArrayList<>();
		for(ArchitectureDescription ad : dedalDiagram.getArchitectureDescriptions()) {
			if(ad instanceof Specification) {
				for(RoleConnection rc : ((Specification)ad).getSpecConnections()) {
					if(inter != null && inter.getDirection().equals(DIRECTION.REQUIRED)) {
						if(inter.equals(rc.getClientIntElem()))
							result.add(rc);
					} else if(inter != null && inter.getDirection().equals(DIRECTION.PROVIDED)) {
						if(inter.equals(rc.getServerIntElem()))
							result.add(rc);
					}
				}
			} else if(ad instanceof Configuration) {
				for(ClassConnection cc : ((Configuration)ad).getConfigConnections()) {
					if(inter != null && inter.getDirection().equals(DIRECTION.REQUIRED)) {
						if(inter.equals(cc.getClientIntElem()))
							result.add(cc);
					} else if(inter != null && inter.getDirection().equals(DIRECTION.PROVIDED)) {
						if(inter.equals(cc.getServerIntElem()))
							result.add(cc);
					}
				}
			} else if(ad instanceof Assembly) {
				for(InstConnection ic : ((Assembly)ad).getAssemblyConnections()) {
					if(inter != null && inter.getDirection().equals(DIRECTION.REQUIRED)) {
						if(inter.equals(ic.getClientIntElem()))
							result.add(ic);
					} else if(inter != null && inter.getDirection().equals(DIRECTION.PROVIDED)) {
						if(inter.equals(ic.getServerIntElem()))
							result.add(ic);
					}
				}
			}
		}
		return result;
	}

	private void relateDiffInterfacesToDiffComponentsAndDiffConnections() {
		List<DiffInterface> toRemove = new ArrayList<DiffInterface>();
		for(DiffInterface di : this.diffInterfaces) {
			Diff diff = di.getDiff();
			if(diff != null) {
				List<Diff> requiredBy = diff.getRequiredBy();
				for(Diff d : requiredBy) {
					DiffDedal dd = this.getDiffDedal(d);
					if(dd instanceof DiffComponent) {
						((DiffComponent)dd).addDiffInterface(di);
						toRemove.add(di);
					} else if(dd != null && (dd instanceof DiffParameter || dd instanceof DiffSignature 
							|| dd instanceof DiffInterfaceType)) {
						toRemove.add(di);
					}
				}
				List<Diff> requires = diff.getRequires();
				for(Diff d : requires) {
					DiffDedal dd = this.getDiffDedal(d);
					if(dd instanceof DiffComponent) {
						((DiffComponent)dd).addDiffInterface(di);
						toRemove.add(di);
					} else if(dd != null && (dd instanceof DiffParameter || dd instanceof DiffSignature 
							|| dd instanceof DiffInterfaceType)) {
						toRemove.add(di);
					}
				}
			}
		}
		this.diffInterfaces.removeAll(toRemove);
		toRemove = new ArrayList<>();
		for(DiffInterface di : this.diffInterfaces){
			List<Component> impactedComponents = this.findImpactedComponents((Interface) di.getDiffObject(), this.newDiagram);
			this.putDiffInterfacesInDiffComponents(toRemove, di, impactedComponents);
			if(impactedComponents == null || impactedComponents.isEmpty()) {
				impactedComponents = this.findImpactedComponents((Interface) di.getDiffObject(), this.oldDiagram);
				this.putDiffInterfacesInDiffComponents(toRemove, di, impactedComponents);
			}
		}
		this.relateDiffComponentsToDiffConnectionsAndDiffArchitectureLevels();
		this.diffInterfaces.removeAll(toRemove);
	}

	private void putDiffInterfacesInDiffComponents(List<DiffInterface> toRemove, DiffInterface di,
			List<Component> impactedComponents) {
		for(Component c : impactedComponents) {
			DiffComponent diffC = this.getDiffComponentByComponent(c);
			if(diffC != null) {
				diffC.addDiffInterface(di);
				toRemove.add(di);
			} else if(di.getDiffObject() instanceof Interface) {
				Interface inter = (Interface)di.getDiffObject();
				if(inter.eContainer() instanceof CompInstance) {
					diffC = new DiffComponentInstance(null);
				} else if(inter.eContainer() instanceof CompClass) {
					diffC = new DiffComponentClass(null);
				} else if(inter.eContainer() instanceof CompRole) {
					diffC = new DiffComponentRole(null);
				}
				if(diffC != null) {
					diffC.setDiffObject(c);
					diffC.addDiffInterface(di);
					toRemove.add(di);
					this.diffComponents.add(diffC);
					this.diffDedals.add(diffC);
				}
			}
		}
	}

	private DiffComponent getDiffComponentByComponent(Component c) {
		for(DiffComponent diffC : this.diffComponents) {
			if(c != null && c.equals(diffC.getDiffObject()))
				return diffC;
		}
		return null;
	}

	private List<Component> findImpactedComponents(Interface inter, DedalDiagram dedalDiagram) {
		List<Component> result = new ArrayList<>();
		for(ArchitectureDescription ad : dedalDiagram.getArchitectureDescriptions()) {
			if(ad instanceof Specification) {
				for(CompRole cr : ((Specification)ad).getSpecComponents()) {
					for(Interaction inter2 : cr.getCompInterfaces()) {
						if(inter2.equals(inter))
							result.add(cr);
					}
				}
			} else if(ad instanceof Configuration) {
				for(CompClass cc : ((Configuration)ad).getConfigComponents()) {
					for(Interaction inter2 : cc.getCompInterfaces()) {
						if(inter2.equals(inter))
							result.add(cc);
					}
				}
			} else if(ad instanceof Assembly) {
				for(CompInstance ci : ((Assembly)ad).getAssmComponents()) {
					for(Interaction inter2 : ci.getCompInterfaces()) {
						if(inter2.equals(inter))
							result.add(ci);
					}
				}
			}
		}
		return result;
	}

	private void relateDiffAttributesToDiffComponents() {
		List<DiffAttribute> toRemove = new ArrayList<DiffAttribute>();
		for(DiffAttribute da : this.diffAttributes) {
			Diff diff = da.getDiff();
			if(diff != null) {
				List<Diff> requiredBy = diff.getRequiredBy();
				for(Diff d : requiredBy) {
					DiffDedal dd = this.getDiffDedal(d);
					if(dd != null && dd instanceof DiffComponentClass) {
						((DiffComponentClass)dd).addDiffAttribute(da);
						toRemove.add(da);
					}
				}
				List<Diff> requires = diff.getRequires();
				for(Diff d : requires) {
					DiffDedal dd = this.getDiffDedal(d);
					if(dd != null && dd instanceof DiffComponentClass) {
						((DiffComponentClass)dd).addDiffAttribute(da);
						toRemove.add(da);
					}
				}
			}
		}
		this.diffAttributes.removeAll(toRemove);
		toRemove = new ArrayList<>();
		for(DiffAttribute da : this.diffAttributes){
			CompClass compClass = (CompClass) da.getDiffObject().eContainer();
			DiffComponentClass diffCC = (DiffComponentClass) this.getDiffComponentByComponent(compClass);
			if(diffCC != null) {
				diffCC.addDiffAttribute(da);
				toRemove.add(da);
			} else {
				diffCC = new DiffComponentClass(null);
				diffCC.setDiffObject(compClass);
				diffCC.addDiffAttribute(da);
				toRemove.add(da);
				this.diffComponents.add(diffCC);
				this.diffDedals.add(diffCC);
			}
		}
		this.diffAttributes.removeAll(toRemove);
	}


	private void relateDiffComponentsToDiffConnectionsAndDiffArchitectureLevels() {
		for(DiffComponent dc : this.diffComponents) {
			Diff diff = dc.getDiff();
			if(diff != null) {
				List<Diff> requiredBy = diff.getRequiredBy();
				for(Diff d : requiredBy) {
					DiffDedal dd = this.getDiffDedal(d);
					if(dd instanceof DiffInstanceConnection) {
						InstConnection connection = ((DiffInstanceConnection)dd).getDiffObject() instanceof InstConnection?
								(InstConnection)((DiffConnection)dd).getDiffObject()
								: null;
								if(connection != null && connection.getServerInstElem().equals(dc.getDiffObject())) {
									((DiffConnection)dd).setServerDiffComponent(dc);
								}
								else if(connection != null && connection.getClientInstElem().equals(dc.getDiffObject())) {
									((DiffConnection)dd).setClientDiffComponent(dc);
								}
					} else if(dd instanceof DiffClassConnection) {
						ClassConnection connection = ((DiffClassConnection)dd).getDiffObject() instanceof ClassConnection?
								(ClassConnection)((DiffConnection)dd).getDiffObject()
								: null;
								if(connection != null && connection.getServerClassElem().equals(dc.getDiffObject())) {
									((DiffConnection)dd).setServerDiffComponent(dc);
								}
								else if(connection != null && connection.getClientClassElem().equals(dc.getDiffObject())) {
									((DiffConnection)dd).setClientDiffComponent(dc);
								}
					}  else if(dd instanceof DiffRoleConnection) {
						RoleConnection connection = ((DiffRoleConnection)dd).getDiffObject() instanceof RoleConnection?
								(RoleConnection)((DiffConnection)dd).getDiffObject()
								: null;
								if(connection != null && connection.getServerCompElem().equals(dc.getDiffObject())) {
									((DiffConnection)dd).setServerDiffComponent(dc);
								}
								else if(connection != null && connection.getClientCompElem().equals(dc.getDiffObject())) {
									((DiffConnection)dd).setClientDiffComponent(dc);
								}
					}
				}
				List<Diff> requires = diff.getRequires();
				for(Diff d : requires) {
					DiffDedal dd = this.getDiffDedal(d);
					if(dd instanceof DiffInstanceConnection) {
						InstConnection connection = ((DiffInstanceConnection)dd).getDiffObject() instanceof InstConnection?
								(InstConnection)((DiffConnection)dd).getDiffObject()
								: null;
								if(connection != null && connection.getServerInstElem().equals(dc.getDiffObject())) {
									((DiffConnection)dd).setServerDiffComponent(dc);
								}
								else if(connection != null && connection.getClientInstElem().equals(dc.getDiffObject())) {
									((DiffConnection)dd).setClientDiffComponent(dc);
								}
					} else if(dd instanceof DiffClassConnection) {
						ClassConnection connection = ((DiffClassConnection)dd).getDiffObject() instanceof ClassConnection?
								(ClassConnection)((DiffConnection)dd).getDiffObject()
								: null;
								if(connection != null && connection.getServerClassElem().equals(dc.getDiffObject())) {
									((DiffConnection)dd).setServerDiffComponent(dc);
								}
								else if(connection != null && connection.getClientClassElem().equals(dc.getDiffObject())) {
									((DiffConnection)dd).setClientDiffComponent(dc);
								}
					}  else if(dd instanceof DiffRoleConnection) {
						RoleConnection connection = ((DiffRoleConnection)dd).getDiffObject() instanceof RoleConnection?
								(RoleConnection)((DiffConnection)dd).getDiffObject()
								: null;
								if(connection != null && connection.getServerCompElem().equals(dc.getDiffObject())) {
									((DiffConnection)dd).setServerDiffComponent(dc);
								}
								else if(connection != null && connection.getClientCompElem().equals(dc.getDiffObject())) {
									((DiffConnection)dd).setClientDiffComponent(dc);
								}
					}
				}
			}
		}
	}

	private void relateDiffComponentsToDiffArchitectureLevels() {
		DiffAssembly diffAssm = null;
		DiffConfiguration diffConfig = null;
		DiffSpecification diffSpec = null;
		List<DiffComponent> toRemove = new ArrayList<>();
		for(DiffComponent diffC : this.diffComponents) {
			if(diffC instanceof DiffComponentInstance) {
				diffAssm = this.findDiffAssembly((DiffComponentInstance) diffC);
				diffAssm.addDiffComponent(diffC);
				toRemove.add(diffC);
			}
			if(diffC instanceof DiffComponentClass) {
				diffConfig = this.findDiffConfiguration((DiffComponentClass) diffC);
				diffConfig.addDiffComponent(diffC);
				toRemove.add(diffC);
			}
			if(diffC instanceof DiffComponentType) {
//				diffConfig = this.findDiffConfiguration((DiffComponentType) diffC);
//				diffConfig.addDiffComponent(diffC);
				toRemove.add(diffC);
			}
			if(diffC instanceof DiffComponentRole) {
				diffSpec = this.findDiffSpecification((DiffComponentRole) diffC);
				diffSpec.addDiffComponent(diffC);
				toRemove.add(diffC);
			}
				
		}
		this.diffComponents.removeAll(toRemove);
	}

	private DiffAssembly findDiffAssembly(DiffComponentInstance diffComp) {
		for(DiffArchitectureLevel ad : this.diffArchitectureLevels) {
			if(diffComp != null && 
					diffComp.getDiffObject().eContainer() instanceof Assembly && 
					ad instanceof DiffAssembly &&
					((Assembly)diffComp.getDiffObject().eContainer()).getName().equals(
							((Assembly)ad.getDiffObject()).getName())) {
				return (DiffAssembly) ad;
			}
		}
		DiffAssembly diffAssm = new DiffAssembly(null);
		diffAssm.setDiffObject(diffComp.getDiffObject().eContainer());
		this.diffArchitectureLevels.add(diffAssm);
		this.diffDedals.add(diffAssm);
		return diffAssm;
	}

	private DiffAssembly findDiffAssembly(DiffInstanceConnection diffConn) {
		for(DiffArchitectureLevel ad : this.diffArchitectureLevels) {
			if(diffConn != null && 
					diffConn.getDiffObject().eContainer() instanceof Assembly &&
					ad instanceof DiffAssembly &&
					((Assembly)diffConn.getDiffObject().eContainer()).getName().equals(
							((Assembly)ad.getDiffObject()).getName())) {
				return (DiffAssembly) ad;
			}
		}
		DiffAssembly diffAssm = new DiffAssembly(null);
		diffAssm.setDiffObject(diffConn.getDiffObject().eContainer());
		this.diffArchitectureLevels.add(diffAssm);
		this.diffDedals.add(diffAssm);
		return diffAssm;
	}

	private DiffConfiguration findDiffConfiguration(DiffComponentClass diffComp) {
		for(DiffArchitectureLevel ad : this.diffArchitectureLevels) {
			if(diffComp != null && 
					diffComp.getDiffObject().eContainer() instanceof Configuration &&
					ad instanceof DiffConfiguration &&
					((Configuration)diffComp.getDiffObject().eContainer()).getName().equals(
							((Configuration)ad.getDiffObject()).getName())) {
				return (DiffConfiguration) ad;
			}
		}
		DiffConfiguration diffConfig = new DiffConfiguration(null);
		diffConfig.setDiffObject(diffComp.getDiffObject().eContainer());
		this.diffArchitectureLevels.add(diffConfig);
		this.diffDedals.add(diffConfig);
		return diffConfig;
	}

	private DiffConfiguration findDiffConfiguration(DiffClassConnection diffConn) {
		for(DiffArchitectureLevel ad : this.diffArchitectureLevels) {
			if(diffConn != null && 
					diffConn.getDiffObject().eContainer() instanceof Configuration &&
					ad instanceof DiffConfiguration &&
					((Configuration)diffConn.getDiffObject().eContainer()).getName().equals(
							((Configuration)ad.getDiffObject()).getName())) {
				return (DiffConfiguration) ad;
			}
		}
		DiffConfiguration diffConfig = new DiffConfiguration(null);
		diffConfig.setDiffObject(diffConn.getDiffObject().eContainer());
		this.diffArchitectureLevels.add(diffConfig);
		this.diffDedals.add(diffConfig);
		return diffConfig;
	}

	private DiffSpecification findDiffSpecification(DiffComponentRole diffComp) {
		for(DiffArchitectureLevel ad : this.diffArchitectureLevels) {
			if(diffComp != null && 
					diffComp.getDiffObject().eContainer() instanceof Specification &&
					ad instanceof DiffSpecification &&
					((Specification)diffComp.getDiffObject().eContainer()).getName().equals(
							((Specification)ad.getDiffObject()).getName())) {
				return (DiffSpecification) ad;
			}
		}
		DiffSpecification diffSpec = new DiffSpecification(null);
		diffSpec.setDiffObject(diffComp.getDiffObject().eContainer());
		this.diffArchitectureLevels.add(diffSpec);
		this.diffDedals.add(diffSpec);
		return diffSpec;
	}

	private DiffSpecification findDiffSpecification(DiffRoleConnection diffConn) {
		for(DiffArchitectureLevel ad : this.diffArchitectureLevels) {
			if(diffConn != null && 
					diffConn.getDiffObject().eContainer() instanceof Specification &&
					ad instanceof DiffSpecification &&
					((Specification)diffConn.getDiffObject().eContainer()).getName().equals(
							((Specification)ad.getDiffObject()).getName())) {
				return (DiffSpecification) ad;
			}
		}
		DiffSpecification diffSpec = new DiffSpecification(null);
		diffSpec.setDiffObject(diffConn.getDiffObject().eContainer());
		this.diffArchitectureLevels.add(diffSpec);
		this.diffDedals.add(diffSpec);
		return diffSpec;
	}

	private void relateDiffConnectionsToDiffArchitectureLevels() {
		DiffAssembly diffAssm = null;
		DiffConfiguration diffConfig = null;
		DiffSpecification diffSpec = null;
		List<DiffConnection> toRemove = new ArrayList<>();
		for(DiffConnection diffC : this.diffConnections) {
			if(diffC instanceof DiffInstanceConnection) {
				diffAssm = this.findDiffAssembly((DiffInstanceConnection) diffC);
				diffAssm.addDiffConnection(diffC);
				toRemove.add(diffC);
			}
			if(diffC instanceof DiffClassConnection) {
				diffConfig = this.findDiffConfiguration((DiffClassConnection) diffC);
				diffConfig.addDiffConnection(diffC);
				toRemove.add(diffC);
			}
			if(diffC instanceof DiffRoleConnection) {
				diffSpec = this.findDiffSpecification((DiffRoleConnection) diffC);
				diffSpec.addDiffConnection(diffC);
				toRemove.add(diffC);
			}
		}
		this.diffConnections.removeAll(toRemove);
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
