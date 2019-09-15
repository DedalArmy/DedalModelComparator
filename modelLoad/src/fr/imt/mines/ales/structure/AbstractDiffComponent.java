package fr.imt.mines.ales.structure;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.compare.Diff;

import fr.imt.mines.ales.comparators.ProjectComparator;

public abstract class AbstractDiffComponent extends AbstractDiffDedal implements DiffComponent {

	private Set<DiffInterface> diffInterfaces;
	
	public AbstractDiffComponent(Diff diff) {
		super(diff);
		this.diffInterfaces = new HashSet<DiffInterface>();
	}

	@Override
	public void addDiffInterface(DiffInterface diffInterface) {
		this.diffInterfaces.add(diffInterface);
	}

	@Override
	public DiffInterface getDiffInterface(DiffInterface diffInterface) {
		for(DiffInterface di : this.diffInterfaces) {
			if(di.equals(diffInterface))
				return di;
		}
		return null;
	}

	@Override
	public DiffInterface getDiffInterface(String name) {
		for(DiffInterface di : this.diffInterfaces) {
			if(name.equals(di.getName()))
				return di;
		}
		return null;
	}

	@Override
	public Set<DiffInterface> getDiffInterfaces() {
		return this.diffInterfaces;
	}
	
	@Override
	public Boolean containsDiffInterface(DiffInterface diffInterface) {
		return (this.getDiffInterface(diffInterface) != null)?Boolean.TRUE:Boolean.FALSE;
	}
	
	@Override
	public Boolean containsDiffInterface(String name) {
		return (this.getDiffInterface(name) != null)?Boolean.TRUE:Boolean.FALSE;
	}
	
	@Override
	public Boolean checkGlobalSubstitutability(ProjectComparator pc) {
		for(DiffInterface di : this.getDiffInterfaces()) {
			if(Boolean.FALSE.equals(di.checkGlobalSubstitutability(pc))) {
				System.out.println(di + " --> " + di.checkGlobalSubstitutability(pc));
				return Boolean.FALSE;
			}
		}
		return this.isSubstitutable(pc);
	}

}
