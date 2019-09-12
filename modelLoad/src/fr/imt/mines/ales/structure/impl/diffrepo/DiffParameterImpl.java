package fr.imt.mines.ales.structure.impl.diffrepo;

import org.eclipse.emf.compare.Diff;

import dedal.Parameter;
import fr.imt.mines.ales.structure.AbstractDiffDedal;
import fr.imt.mines.ales.structure.DiffParameter;

public class DiffParameterImpl extends AbstractDiffDedal implements DiffParameter {
	
	public DiffParameterImpl(Diff diff) {
		super(diff);
	}

	@Override
	public String getName() {
		return (this.getDiffObject() instanceof Parameter)?((Parameter)this.getDiffObject()).getName():null;
	}
	
}
