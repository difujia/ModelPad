package modelpad.model;

public class ClassViewModel extends ElementViewModel {

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
