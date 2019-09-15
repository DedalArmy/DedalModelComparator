package fr.imt.mines.ales.structure.impl.diffconfiguration;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.compare.Diff;

import dedal.CompClass;
import fr.imt.mines.ales.comparators.ProjectComparator;
import fr.imt.mines.ales.structure.AbstractDiffComponent;
import fr.imt.mines.ales.structure.DiffAttribute;

public class DiffComponentClass extends AbstractDiffComponent {

	private Set<DiffAttribute> diffAttributes;
	
	public DiffComponentClass(Diff diff) {
		super(diff);
		this.diffAttributes = new HashSet<DiffAttribute>();
	}

	@Override
	public String getName() {
		return (this.getDiffObject() instanceof CompClass)?((CompClass)this.getDiffObject()).getName():null;
	}
	
	public void addDiffAttribute(DiffAttribute diffAttribute) {
		this.diffAttributes.add(diffAttribute);
	}
	
	public DiffAttribute getDiffAttribute(DiffAttribute diffAttribute) {
		for(DiffAttribute da : this.diffAttributes) {
			if(da.equals(diffAttribute))
				return da;
		}
		return null;
	}
	
	public DiffAttribute getDiffAttribute(String name) {
		for(DiffAttribute da : this.diffAttributes) {
			if(name.equals(da.getName()))
				return da;
		}
		return null;
	}
	
	public Set<DiffAttribute> getDiffAttributes() {
		return this.diffAttributes;
	}
	
	public Boolean containsDiffAttribute(DiffAttribute diffAttribute) {
		return (this.getDiffAttribute(diffAttribute)!=null)?Boolean.TRUE:Boolean.FALSE;
	}
	
	public Boolean containsDiffAttribute(String name) {
		return (this.getDiffAttribute(name)!=null)?Boolean.TRUE:Boolean.FALSE;
	}
	
	@Override
	public Boolean checkGlobalSubstitutability(ProjectComparator pc) {
		for(DiffAttribute da : this.diffAttributes) {
			if(Boolean.FALSE.equals(da.checkGlobalSubstitutability(pc))) {
				System.out.println(da + " --> " + da.checkGlobalSubstitutability(pc));
				return Boolean.FALSE;
			}
		}
		return super.checkGlobalSubstitutability(pc);
	}
	
}
