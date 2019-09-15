package fr.imt.mines.ales.structure;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.compare.Diff;

import dedal.ArchitectureDescription;
import fr.imt.mines.ales.comparators.ProjectComparator;

public abstract class AbstractDiffArchitectureLevel extends AbstractDiffDedal implements DiffArchitectureLevel {

	Set<DiffComponent> diffComponents;
	Set<DiffConnection> diffConnections;
	
	public AbstractDiffArchitectureLevel(Diff diff) {
		super(diff);
		this.diffComponents = new HashSet<DiffComponent>();
		this.diffConnections = new HashSet<DiffConnection>(); 
	}
	
	protected abstract Boolean hasGoodType(DiffComponent diffComponent);

	@Override
	public void addDiffComponent(DiffComponent diffComponent) {
		if(this.hasGoodType(diffComponent))
			this.diffComponents.add(diffComponent);
	}

	@Override
	public DiffComponent getDiffComponent(DiffComponent diffComponent) {
		for(DiffComponent dc : this.diffComponents) {
			if(dc.equals(diffComponent))
				return dc;
		}
		return null;
	}

	@Override
	public DiffComponent getDiffComponent(String componentName) {
		for(DiffComponent dc : this.diffComponents) {
			if(componentName.equals(dc.getName()))
				return dc;
		}
		return null;
	}

	@Override
	public Set<DiffComponent> getDiffComponents() {
		return this.diffComponents;
	}

	@Override
	public void addDiffConnection(DiffConnection diffConnection) {
		this.diffConnections.add(diffConnection);
	}

	@Override
	public DiffConnection getDiffConnection(DiffConnection diffConnection) {
		for(DiffConnection dc : this.diffConnections) {
			if(dc.equals(diffConnection))
				return dc;
		}
		return null;
	}

	@Override
	public DiffConnection getDiffConnection(String refID) {
		for(DiffConnection dc : this.diffConnections) {
			if(refID.equals(dc.getName()))
				return dc;
		}
		return null;
	}

	@Override
	public Set<DiffConnection> getDiffConnections() {
		return this.diffConnections;
	}
	
	@Override
	public String getName() {
		return (this.getDiffObject() instanceof ArchitectureDescription)?((ArchitectureDescription) this.getDiffObject()).getName():null;
	}
	
	@Override
	public Boolean checkGlobalSubstitutability(ProjectComparator pc) {
		try {
		for(DiffComponent dc : this.diffComponents) {
			if(Boolean.FALSE.equals(dc.checkGlobalSubstitutability(pc))) {
				System.out.println(dc + " --> " + dc.checkGlobalSubstitutability(pc));
				return Boolean.FALSE;
			}
		}
		for(DiffConnection dc : this.diffConnections) {
			if(Boolean.FALSE.equals(dc.checkGlobalSubstitutability(pc)))
				return Boolean.FALSE;
		}
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println();
		}
		return Boolean.TRUE;
	}

}
