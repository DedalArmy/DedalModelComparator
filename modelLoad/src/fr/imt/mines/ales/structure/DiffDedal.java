package fr.imt.mines.ales.structure;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.ecore.EObject;

public interface DiffDedal {
	public void setDiff(Diff diff);
	public Diff getDiff();
	public DifferenceKind getDiffKind();
	public Boolean isSubstitutable();
	public EObject getDiffObject();
	String getName();
	void setDiffObject(EObject diffObject);
}
