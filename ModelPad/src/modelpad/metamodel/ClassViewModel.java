package modelpad.metamodel;

public class ClassViewModel extends ViewModelBase {

	private EClass mModel;

	public ClassViewModel(EClass model) {
		super(model);
		mModel = model;
	}

	@Override
	public String getStringDisplay() {
		return mModel.getName();
	}

}
