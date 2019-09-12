package fr.imt.mines.ales.structure.impl.diffrepo;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.compare.Diff;

import dedal.InterfaceType;
import fr.imt.mines.ales.structure.AbstractDiffDedal;
import fr.imt.mines.ales.structure.DiffInterfaceType;
import fr.imt.mines.ales.structure.DiffSignature;

public class DiffInterfaceTypeImpl extends AbstractDiffDedal implements DiffInterfaceType {

	private Set<DiffSignature> diffSignatures;
	
	public DiffInterfaceTypeImpl(Diff diff) {
		super(diff);
		this.diffSignatures = new HashSet<DiffSignature>();
	}

	@Override
	public void addDiffSignature(DiffSignature diffSignature) {
		this.diffSignatures.add(diffSignature);
	}

	@Override
	public DiffSignature getDiffSignature(DiffSignature diffSignature) {
		for(DiffSignature ds : this.diffSignatures) {
			if(ds.equals(diffSignature))
				return ds;
		}
		return null;
	}

	@Override
	public DiffSignature getDiffSignature(String name) {
		for(DiffSignature ds : this.diffSignatures) {
			if(name.equals(ds.getName()))
				return ds;
		}
		return null;
	}

	@Override
	public Set<DiffSignature> getDiffSignatures() {
		return this.diffSignatures;
	}

	@Override
	public Boolean containsDiffSignature(DiffSignature diffSignature) {
		return (this.getDiffSignature(diffSignature) != null)?Boolean.TRUE:Boolean.FALSE;
	}

	@Override
	public Boolean containsDiffSignature(String name) {
		return (this.getDiffSignature(name) != null)?Boolean.TRUE:Boolean.FALSE;
	}
	
	@Override
	public String getName() {
		return (this.getDiffObject() instanceof InterfaceType)?((InterfaceType)this.getDiffObject()).getName():null;
	}

}
