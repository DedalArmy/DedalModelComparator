package fr.imt.mines.ales.structure;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.ecore.EObject;

public abstract class AbstractDiffDedal implements DiffDedal {

	Diff diff;
	EObject diffObject;
	
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
		return this.diff.getKind();
	}

	@Override
	public Boolean isSubstitutable() {
		// TODO Auto-generated method stub
		return null;
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
