package fr.imt.mines.ales.component;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;

public interface ICheckerNot4DedalInterface {
	public Boolean check(Diff diffObject, DifferenceKind differenceKind) throws ClassNotFoundException;
}
