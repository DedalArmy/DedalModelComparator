package fr.imt.mines.ales.structure.impl.diffassembly;

import org.eclipse.emf.compare.Diff;

import dedal.InstConnection;
import fr.imt.mines.ales.structure.AbstractDiffConnection;

public class DiffInstanceConnection extends AbstractDiffConnection {

	public DiffInstanceConnection(Diff diff) {
		super(diff);
	}

	@Override
	public String getName() {
		return (this.getDiffObject() instanceof InstConnection)?((InstConnection) this.getDiffObject()).getRefID():null;
	}

}
