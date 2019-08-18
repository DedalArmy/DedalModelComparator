package fr.imt.mines.ales.component;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;

import dedal.DIRECTION;
import fr.imt.mines.ales.utils.DiffObjectJson;

public interface IChecker4DedalInterface {
	public DiffObjectJson check(Diff diffObject, DIRECTION direction, DifferenceKind differenceKind) throws ClassNotFoundException;
}
