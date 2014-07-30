package modelpad.model;

import java.util.Locale;

public class AttributeViewModel extends ElementViewModel {

	private EAttribute mModel;

	public AttributeViewModel(EAttribute model) {
		super(model);
		mModel = model;
	}

	@Override
	public String getStringDisplay() {
		return String.format(Locale.getDefault(), "%s : %s", mModel.getName(), mModel.getType());
	}

}
