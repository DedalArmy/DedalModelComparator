package fr.imt.mines.ales.structure.impl.diffspecification;

import org.eclipse.emf.compare.Diff;

import dedal.CompRole;
import fr.imt.mines.ales.structure.AbstractDiffComponent;

public class DiffComponentRole extends AbstractDiffComponent{

	public DiffComponentRole(Diff diff) {
		super(diff);
	}
	
	@Override
	public String getName() {
		return (this.getDiffObject() instanceof CompRole)?((CompRole)this.getDiffObject()).getName():null;
	}

}
