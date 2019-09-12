package fr.imt.mines.ales.structure;

import org.eclipse.emf.compare.Diff;

public abstract class AbstractDiffConnection extends AbstractDiffDedal implements DiffConnection {

	private DiffComponent serverDiffComponent;
	private DiffComponent clientDiffComponent;
	private DiffInterface serverDiffInterface;
	private DiffInterface clientDiffInterface;
	
	public AbstractDiffConnection(Diff diff) {
		super(diff);
	}
	
	@Override
	public void setServerDiffComponent(DiffComponent diffComponent) {
		this.serverDiffComponent = diffComponent;
	}

	@Override
	public void setClientDiffComponent(DiffComponent diffComponent) {
		this.clientDiffComponent = diffComponent;
	}

	@Override
	public DiffComponent getServerDiffComponent() {
		return this.serverDiffComponent;
	}

	@Override
	public DiffComponent getClientDiffComponent() {
		return this.clientDiffComponent;
	}

	@Override
	public void setServerDiffInterface(DiffInterface diffInterface) {
		this.serverDiffInterface = diffInterface;
	}

	@Override
	public void setClientDiffInterface(DiffInterface diffInterface) {
		this.clientDiffInterface = diffInterface;
	}
	
	@Override
	public DiffInterface getServerDiffInterface() {
		return this.serverDiffInterface;
	}
	
	@Override
	public DiffInterface getClientDiffInterface() {
		return this.clientDiffInterface;
	}

}
