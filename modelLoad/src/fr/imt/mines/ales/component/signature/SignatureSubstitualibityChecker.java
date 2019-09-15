package fr.imt.mines.ales.component.signature;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;

import dedal.Signature;
import fr.imt.ales.redoc.type.hierarchy.build.HierarchyBuilder;
import fr.imt.ales.redoc.type.hierarchy.structure.JavaType;
import fr.imt.mines.ales.component.CheckerNot4DedalInterface;

public class SignatureSubstitualibityChecker extends CheckerNot4DedalInterface{
	public SignatureSubstitualibityChecker(HierarchyBuilder hierarchyBuilderOld, HierarchyBuilder hierarchyBuilderNew) {
		super(hierarchyBuilderOld, hierarchyBuilderNew);
	}
	
	public Boolean check(Diff diffObject ,DifferenceKind differenceKind) throws ClassNotFoundException {
				
		if(differenceKind == DifferenceKind.DELETE) {
			return Boolean.FALSE;
		}
		
		if(differenceKind == DifferenceKind.ADD) {
			return Boolean.TRUE;
		}
		
		if (differenceKind == DifferenceKind.CHANGE && 
				diffObject.getMatch().getRight() instanceof Signature &&
				diffObject.getMatch().getLeft() instanceof Signature) {
			
			JavaType jTypeParamNewClass = getHierarchyBuilderNew().findJavaType(
					((Signature)diffObject.getMatch().getRight()).getType());
			JavaType jTypeParamOldClass = getHierarchyBuilderOld().findJavaType(
					((Signature)diffObject.getMatch().getLeft()).getType());

			if(jTypeParamNewClass.isSubtypeOf(jTypeParamOldClass)) {
				return Boolean.TRUE;
			}else if(jTypeParamOldClass.isSubtypeOf(jTypeParamNewClass)){
				return Boolean.FALSE;
			}else {
				return Boolean.FALSE;
			}
		}
		return null;
	}

}
