package modelpad.model;

public class EText extends NamedElement {

	public EText(NameSet names) {
		super(names, "text");
	}
	
	@Override
	public boolean isDestroyed() {
		return false;
	}
	
	@Override
	public void destroy() {
		// not really destroyable
	}
}
