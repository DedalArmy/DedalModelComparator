package fr.imt.mines.ales.structure.impl.diffconfiguration;

import org.eclipse.emf.compare.Diff;

import dedal.CompClass;
import fr.imt.mines.ales.structure.AbstractDiffArchitectureLevel;
import fr.imt.mines.ales.structure.DiffComponent;

public class DiffConfiguration extends AbstractDiffArchitectureLevel {

	public DiffConfiguration(Diff diff) {
		super(diff);
	}

	@Override
	protected Boolean hasGoodType(DiffComponent diffComponent) {
		return (diffComponent.getDiffObject() instanceof CompClass)?Boolean.TRUE:Boolean.FALSE;
	}

}
