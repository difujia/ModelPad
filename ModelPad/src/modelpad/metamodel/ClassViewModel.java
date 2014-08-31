package modelpad.metamodel;

public class ClassViewModel extends ViewModelBase {

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
