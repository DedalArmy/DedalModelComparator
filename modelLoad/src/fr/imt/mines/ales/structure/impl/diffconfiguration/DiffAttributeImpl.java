package fr.imt.mines.ales.structure.impl.diffconfiguration;

import org.eclipse.emf.compare.Diff;

import dedal.Attribute;
import fr.imt.mines.ales.comparators.ProjectComparator;
import fr.imt.mines.ales.structure.AbstractDiffDedal;
import fr.imt.mines.ales.structure.DiffAttribute;

public class DiffAttributeImpl extends AbstractDiffDedal implements DiffAttribute {

	public DiffAttributeImpl(Diff diff) {
		super(diff);
	}

	@Override
	public String getName() {
		return (this.getDiffObject() instanceof Attribute)?((Attribute) this.getDiffObject()).getName():null;
	}
	
	@Override
	public Boolean checkGlobalSubstitutability(ProjectComparator pc) {
		return isSubstitutable(pc);
	}

}
