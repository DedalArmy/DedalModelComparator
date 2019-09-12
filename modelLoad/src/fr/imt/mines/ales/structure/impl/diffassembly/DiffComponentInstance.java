package fr.imt.mines.ales.structure.impl.diffassembly;

import org.eclipse.emf.compare.Diff;

import dedal.CompInstance;
import fr.imt.mines.ales.structure.AbstractDiffComponent;

public class DiffComponentInstance extends AbstractDiffComponent{

	public DiffComponentInstance(Diff diff) {
		super(diff);
	}
	
	@Override
	public String getName() {
		return (this.getDiffObject() instanceof CompInstance)?((CompInstance)this.getDiffObject()).getName():null;
	}

}
