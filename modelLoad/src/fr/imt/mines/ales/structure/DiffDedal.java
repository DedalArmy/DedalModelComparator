package fr.imt.mines.ales.structure;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.ecore.EObject;

import fr.imt.mines.ales.comparators.ProjectComparator;

public interface DiffDedal {
	public void setDiff(Diff diff);
	public Diff getDiff();
	public DifferenceKind getDiffKind();
	public EObject getDiffObject();
	String getName();
	void setDiffObject(EObject diffObject);
	Boolean isSubstitutable(ProjectComparator pc);
	Boolean checkGlobalSubstitutability(ProjectComparator pc);
}
