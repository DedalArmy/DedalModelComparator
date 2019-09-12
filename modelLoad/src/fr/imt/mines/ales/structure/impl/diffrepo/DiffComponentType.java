package fr.imt.mines.ales.structure.impl.diffrepo;

import org.eclipse.emf.compare.Diff;

import dedal.CompType;
import fr.imt.mines.ales.structure.AbstractDiffComponent;

public class DiffComponentType extends AbstractDiffComponent{
	
	public DiffComponentType(Diff diff) {
		super(diff);
	}

	@Override
	public String getName() {
		return (this.getDiffObject() instanceof CompType)?((CompType)this.getDiffObject()).getName():null;
	}

}
