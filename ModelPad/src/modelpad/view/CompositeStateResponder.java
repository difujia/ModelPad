package modelpad.view;

public class CompositeStateResponder implements StateResponder {

	private StateResponder[] mResponders;

	public CompositeStateResponder(StateResponder... responders) {
		mResponders = responders;
	}

	@Override
	public void beActive() {
		for (StateResponder responder : mResponders) {
			responder.beActive();
		}
	}

	@Override
	public void beTarget() {
		for (StateResponder responder : mResponders) {
			responder.beTarget();
		}
	}

	@Override
	public void beNormal() {
		for (StateResponder responder : mResponders) {
			responder.beNormal();
		}
	}

}
