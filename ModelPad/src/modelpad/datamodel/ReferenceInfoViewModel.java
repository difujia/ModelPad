package modelpad.datamodel;

public class ReferenceInfoViewModel extends AbstractViewModel {

	private EReferenceInfo model;

	public ReferenceInfoViewModel(EReferenceInfo model) {
		super(model);
		this.model = model;
	}

	@Override
	public String getStringDisplay() {
		if (model.getName().length() > 0) {
			String mult;
			if (model.getLowerbound().equals(model.getUpperbound())) {
				mult = String.format("[%s]", model.getLowerbound());
			} else {
				mult = String.format("[%s..%s]", model.getLowerbound(), model.getUpperbound());
			}
			return String.format("%s %s", model.getName(), mult);
		} else {
			return "";
		}
	}

}
