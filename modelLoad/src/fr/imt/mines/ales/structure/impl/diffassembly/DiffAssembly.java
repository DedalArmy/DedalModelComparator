package fr.imt.mines.ales.structure.impl.diffassembly;

import org.eclipse.emf.compare.Diff;

import dedal.CompInstance;
import fr.imt.mines.ales.structure.AbstractDiffArchitectureLevel;
import fr.imt.mines.ales.structure.DiffComponent;

public class DiffAssembly extends AbstractDiffArchitectureLevel {

	public DiffAssembly(Diff diff) {
		super(diff);
	}

	@Override
	protected Boolean hasGoodType(DiffComponent diffComponent) {
		return (diffComponent.getDiffObject() instanceof CompInstance)?Boolean.TRUE:Boolean.FALSE;
	}

}
