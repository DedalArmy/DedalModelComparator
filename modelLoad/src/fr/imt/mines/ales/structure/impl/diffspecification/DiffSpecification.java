package fr.imt.mines.ales.structure.impl.diffspecification;

import org.eclipse.emf.compare.Diff;

import dedal.CompRole;
import fr.imt.mines.ales.structure.AbstractDiffArchitectureLevel;
import fr.imt.mines.ales.structure.DiffComponent;

public class DiffSpecification extends AbstractDiffArchitectureLevel {

	public DiffSpecification(Diff diff) {
		super(diff);
	}

	@Override
	protected Boolean hasGoodType(DiffComponent diffComponent) {
		return (diffComponent.getDiffObject() instanceof CompRole)?Boolean.TRUE:Boolean.FALSE;
	}

}
