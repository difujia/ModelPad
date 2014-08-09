package modelpad.metamodel;

public class ReferenceViewModel extends ViewModelBase {

	private EReference mModel;

	public ReferenceViewModel(EReference model) {
		super(model);
		mModel = model;
	}

	@Override
	public String getStringDisplay() {
		if (mModel.getName().length() > 0) {
			String mult;
			if (mModel.getLowerBound().equals(mModel.getUpperBound())) {
				mult = String.format("[%s]", mModel.getLowerBound());
			} else {
				mult = String.format("[%s..%s]", mModel.getLowerBound(), mModel.getUpperBound());
			}
			return String.format("%s %s", mModel.getName(), mult);
		} else {
			return "";
		}
	}

}
