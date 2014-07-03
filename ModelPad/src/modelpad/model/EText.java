package modelpad.model;

public class EText extends Element {

	protected EText(String name) {
		super(name);
	}

	@Override
	public void recycle() {
		// not really recycleable
	}
}
