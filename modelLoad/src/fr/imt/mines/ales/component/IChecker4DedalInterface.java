package fr.imt.mines.ales.component;

import java.util.List;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;

import dedal.DIRECTION;
import fr.imt.mines.ales.utils.DiffObjectJson;

public interface IChecker4DedalInterface {
	public List<DiffObjectJson> check(Diff diffObject, DIRECTION direction, DifferenceKind differenceKind) throws ClassNotFoundException;
}
