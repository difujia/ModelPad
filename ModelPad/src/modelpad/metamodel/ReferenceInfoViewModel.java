package modelpad.metamodel;

public class ReferenceInfoViewModel extends ViewModelBase {

	private EReferenceInfo mModel;

	public ReferenceInfoViewModel(EReferenceInfo model) {
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
