package fr.imt.mines.ales.structure;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.ecore.EObject;

import fr.imt.mines.ales.comparators.ProjectComparator;

public abstract class AbstractDiffDedal implements DiffDedal {

	Diff diff;
	EObject diffObject;
	Boolean substitutability = null;
	
	public AbstractDiffDedal(Diff diff) {
		this.diff = diff;
		this.diffObject = (diff instanceof ReferenceChange)?((ReferenceChange)diff).getValue():null;
	}

	@Override
	public void setDiff(Diff diff) {
		this.diff = diff;
	}

	@Override
	public Diff getDiff() {
		return this.diff;
	}

	@Override
	public DifferenceKind getDiffKind() {
		return this.diff!=null?this.diff.getKind():null;
	}

	@Override
	public Boolean isSubstitutable(ProjectComparator pc) {
		if(this.diff!=null && this.substitutability == null)
			this.substitutability = pc.checkSubstitutability(this);
		return this.substitutability;
	}
	
	@Override
	public EObject getDiffObject() {
		return diffObject;
	}
	
	@Override
	public void setDiffObject(EObject diffObject) {
		this.diffObject = diffObject;
	}

}
