package modelpad.datamodel;

public class AttributeViewModel extends AbstractViewModel {

	private EAttribute model;

	public AttributeViewModel(EAttribute model) {
		super(model);
		this.model = model;
	}

	@Override
	public String getStringDisplay() {
		return String.format("%s : %s", model.getName(), model.getType());
	}

}
