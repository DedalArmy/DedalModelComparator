package fr.imt.mines.ales.structure.impl.diffspecification;

import org.eclipse.emf.compare.Diff;

import dedal.RoleConnection;
import fr.imt.mines.ales.structure.AbstractDiffConnection;

public class DiffRoleConnection extends AbstractDiffConnection {

	public DiffRoleConnection(Diff diff) {
		super(diff);
	}

	@Override
	public String getName() {
		return (this.getDiffObject() instanceof RoleConnection)?((RoleConnection) this.getDiffObject()).getRefID():null;
	}

}
