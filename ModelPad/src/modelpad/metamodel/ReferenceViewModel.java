package modelpad.metamodel;

public class ReferenceViewModel extends ViewModelBase {

	private EReference model;

	public ReferenceViewModel(EReference model) {
		super(model);
		this.model = model;
	}

	@Override
	public String getStringDisplay() {
		if (model.getName().length() > 0) {
			String mult;
			if (model.getLowerBound().equals(model.getUpperBound())) {
				mult = String.format("[%s]", model.getLowerBound());
			} else {
				mult = String.format("[%s..%s]", model.getLowerBound(), model.getUpperBound());
			}
			return String.format("%s %s", model.getName(), mult);
		} else {
			return "";
		}
	}

}
