package modelpad.model;

import java.util.Locale;

public class ReferenceInfoViewModel extends ElementViewModel {

	private EReferenceInfo mModel;

	public ReferenceInfoViewModel(EReferenceInfo model) {
		super(model);
		mModel = model;
	}

	@Override
	public String getStringDisplay() {
		if (mModel.getName().length() > 0) {
			return String.format(Locale.getDefault(), "%s [%d..%d]", mModel.getName(), mModel.getLowerBound(),
					mModel.getUpperBound());
		} else {
			return "";
		}
	}

}
