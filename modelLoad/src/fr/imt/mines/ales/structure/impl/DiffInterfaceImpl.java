package fr.imt.mines.ales.structure.impl;

import org.eclipse.emf.compare.Diff;

import dedal.Interface;
import fr.imt.mines.ales.structure.AbstractDiffDedal;
import fr.imt.mines.ales.structure.DiffInterface;
import fr.imt.mines.ales.structure.DiffInterfaceType;

public class DiffInterfaceImpl extends AbstractDiffDedal implements DiffInterface {

	private DiffInterfaceType diffInterfaceType;
	
	public DiffInterfaceImpl(Diff diff) {
		super(diff);
	}
	
	public DiffInterfaceImpl(Diff diff, DiffInterfaceType diffInterfaceType) {
		super(diff);
		this.diffInterfaceType = diffInterfaceType;
	}

	@Override
	public String getName() {
		return (this.getDiffObject() instanceof Interface)?((Interface)this.getDiffObject()).getName():null;
	}

	@Override
	public void setDiffInterfaceType(DiffInterfaceType diffInterfaceType) {
		this.diffInterfaceType = diffInterfaceType;
	}

	@Override
	public DiffInterfaceType getInterfaceType() {
		return this.diffInterfaceType;
	}

}
