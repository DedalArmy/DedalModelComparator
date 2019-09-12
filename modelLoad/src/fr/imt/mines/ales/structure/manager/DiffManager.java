package fr.imt.mines.ales.structure.manager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.ecore.EObject;
import org.json.JSONException;

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
import fr.imt.mines.ales.utils.DiffObjectJson;

public class DiffManager {

	/**
	 * Singleton instance
	 */
	private static DiffManager INSTANCE;

	private Set<DiffArchitectureLevel> diffArchitectureLevels;
	private Set<DiffComponent> diffComponents;
	private Set<DiffInterfaceType> diffInterfaceType;
	private Set<DiffInterface> diffInterfaces;
	private Set<DiffConnection> diffConnections;
	private Set<DiffParameter> diffParameters;
	private Set<DiffSignature> diffSignatures;
	private Set<DiffDedal> diffDedals;
	private Set<DiffAttribute> diffAttributes;
	
	private DiffManager() {}
	
	public static DiffManager getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new DiffManager();
		}
		return INSTANCE;
	}
	
	public void init(List<Diff> diffs) {
		this.diffArchitectureLevels = new HashSet<DiffArchitectureLevel>();
		this.diffComponents = new HashSet<DiffComponent>();
		this.diffInterfaceType = new HashSet<DiffInterfaceType>();
		this.diffInterfaces = new HashSet<DiffInterface>();
		this.diffConnections = new HashSet<DiffConnection>();
		this.diffParameters = new HashSet<DiffParameter>();
		this.diffSignatures = new HashSet<DiffSignature>();
		
		this.fillSets(diffs);
		System.out.println();
	}

	private void fillSets(List<Diff> diffs) {

		for(Diff diff : diffs) {
			ReferenceChange referenceChange = ((ReferenceChange)diff);
			EObject eObject = referenceChange.getValue();

			if (eObject instanceof Component) {
				Component component = (Component)eObject;
				if(component instanceof CompClass) {
					this.diffComponents.add(new DiffComponentClass(diff));
				}
					
				if(component instanceof CompInstance) {
					this.diffComponents.add(new DiffComponentInstance(diff));
				}
				if(component instanceof CompRole) {
					this.diffComponents.add(new DiffComponentRole(diff));
				}
				if(component instanceof CompType) {
					this.diffComponents.add(new DiffComponentType(diff));
				}
			}
		
			if(eObject instanceof Interface) {
				this.diffInterfaces.add(new DiffInterfaceImpl(diff));
			}
			
			if(eObject instanceof Attribute) {
				this.diffAttributes.add(new DiffAttributeImpl(diff));
			}
			
			if (eObject instanceof Parameter) {
				this.diffParameters.add(new DiffParameterImpl(diff));
			}

			if(eObject instanceof Signature) {
				this.diffSignatures.add(new DiffSignatureImpl(diff));
			}

			if(eObject instanceof InterfaceType) {
				this.diffInterfaceType.add(new DiffInterfaceTypeImpl(diff));
			}

			if(eObject instanceof ArchitectureDescription) {
				if(eObject instanceof Assembly)
					this.diffArchitectureLevels.add(new DiffAssembly(diff));
				if(eObject instanceof Configuration)
					this.diffArchitectureLevels.add(new DiffConfiguration(diff));
				if(eObject instanceof Specification)
					this.diffArchitectureLevels.add(new DiffSpecification(diff));
			}
			
			if(eObject instanceof Connection) {
				if(eObject instanceof InstConnection)
					this.diffConnections.add(new DiffInstanceConnection(diff));
				if(eObject instanceof ClassConnection)
					this.diffConnections.add(new DiffClassConnection(diff));
				if(eObject instanceof RoleConnection)
					this.diffConnections.add(new DiffRoleConnection(diff));
			}
			
		}
		
	}
}
