package fr.imt.mines.ales.component;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;

import dedal.DIRECTION;

public interface IChecker4DedalInterface {
	public Boolean check(Diff diffObject, DIRECTION direction, DifferenceKind differenceKind) throws ClassNotFoundException;
}
