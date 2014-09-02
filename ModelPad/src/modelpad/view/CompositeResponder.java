package modelpad.view;

public class CompositeResponder implements VisualResponder {

	private VisualResponder[] mResponders;

	public CompositeResponder(VisualResponder... responders) {
		mResponders = responders;
	}

	@Override
	public void beActive() {
		for (VisualResponder responder : mResponders) {
			responder.beActive();
		}
	}

	@Override
	public void beTarget() {
		for (VisualResponder responder : mResponders) {
			responder.beTarget();
		}
	}

	@Override
	public void beNormal() {
		for (VisualResponder responder : mResponders) {
			responder.beNormal();
		}
	}

}
