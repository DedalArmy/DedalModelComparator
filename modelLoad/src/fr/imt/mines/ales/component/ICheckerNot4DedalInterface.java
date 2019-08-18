package fr.imt.mines.ales.component;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;

import fr.imt.mines.ales.utils.DiffObjectJson;

public interface ICheckerNot4DedalInterface {
	public DiffObjectJson check(Diff diffObject, DifferenceKind differenceKind) throws ClassNotFoundException;
}
