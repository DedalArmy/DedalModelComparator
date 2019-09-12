package fr.imt.mines.ales.structure.impl.diffrepo;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.compare.Diff;

import dedal.Signature;
import fr.imt.mines.ales.structure.AbstractDiffDedal;
import fr.imt.mines.ales.structure.DiffParameter;
import fr.imt.mines.ales.structure.DiffSignature;

public class DiffSignatureImpl extends AbstractDiffDedal implements DiffSignature {

	private Set<DiffParameter> diffParameters;
	

	public DiffSignatureImpl(Diff diff) {
		super(diff);
		this.diffParameters = new HashSet<DiffParameter>();
	}
	
	@Override
	public void addDiffParameter(DiffParameter diffParameter) {
		this.diffParameters.add(diffParameter);
	}

	@Override
	public DiffParameter getDiffParameter(DiffParameter diffParameter) {
		for(DiffParameter dp : this.diffParameters) {
			if (dp.equals(diffParameter))
				return dp;
		}
		return null;
	}

	@Override
	public DiffParameter getDiffParameter(String name) {
		for(DiffParameter dp : this.diffParameters) {
			if(name.equals(dp.getName())) {
				return dp;
			}
		}
		return null;
	}

	@Override
	public Set<DiffParameter> getDiffParameters() {
		return this.diffParameters;
	}

	@Override
	public Boolean containsDiffParameter(DiffParameter diffParameter) {
		return this.getDiffParameter(diffParameter)!=null?Boolean.TRUE:Boolean.FALSE;
	}

	@Override
	public Boolean containsDiffParameter(String name) {
		return this.getDiffParameter(name)!=null?Boolean.TRUE:Boolean.FALSE;
	}
	

	@Override
	public String getName() {
		return (this.getDiffObject() instanceof Signature)?((Signature)this.getDiffObject()).getName():null;
	}

}
