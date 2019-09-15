package fr.imt.mines.ales.structure.impl.diffconfiguration;

import org.eclipse.emf.compare.Diff;

import dedal.ClassConnection;
import fr.imt.mines.ales.comparators.ProjectComparator;
import fr.imt.mines.ales.structure.AbstractDiffConnection;

public class DiffClassConnection extends AbstractDiffConnection {

	public DiffClassConnection(Diff diff) {
		super(diff);
	}

	@Override
	public String getName() {
		return (this.getDiffObject() instanceof ClassConnection)?((ClassConnection) this.getDiffObject()).getRefID():null;
	}

	@Override
	public Boolean checkGlobalSubstitutability(ProjectComparator pc) {
		// TODO Auto-generated method stub
		return null;
	}

}
