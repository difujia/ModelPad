package modelpad.metamodel;

public class AttributeViewModel extends ViewModelBase {

	private EAttribute mModel;

	public AttributeViewModel(EAttribute model) {
		super(model);
		mModel = model;
	}

	@Override
	public String getStringDisplay() {
		return String.format("%s : %s", mModel.getName(), mModel.getType());
	}

}
