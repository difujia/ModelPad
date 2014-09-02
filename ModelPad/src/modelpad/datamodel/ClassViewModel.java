package modelpad.datamodel;

public class ClassViewModel extends AbstractViewModel {

	private EClass model;

	public ClassViewModel(EClass model) {
		super(model);
		this.model = model;
	}

	@Override
	public String getStringDisplay() {
		return model.getName();
	}

}
