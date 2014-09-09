package modelpad.datamodel;

public class ModelFactory {

	private static final EClass headerClass = new EClass("Classes");
	private static final EAttribute headerAttr = new EAttribute("Attributes", "");
	private static final EReferenceInfo headerRef = new EReferenceInfo("References", "", "");

	public static EClass getHeaderClass() {
		return headerClass;
	}

	public static EAttribute getHeaderAttr() {
		return headerAttr;
	}

	public static EReferenceInfo getHeaderRef() {
		return headerRef;
	}

	public static EReferenceInfo createInfoPlaceHolder() {
		return new EReferenceInfo("", "", "");
	}

	public static AbstractViewModel createViewModel(AbstractElement model) {
		if (model == headerClass || model == headerAttr || model == headerRef) {
			return new HeaderViewModel(model);
		} else if (model instanceof EClass) {
			return new ClassViewModel((EClass) model);
		} else if (model instanceof EAttribute) {
			return new AttributeViewModel((EAttribute) model);
		} else if (model instanceof EReferenceInfo) {
			return new ReferenceInfoViewModel((EReferenceInfo) model);
		} else if (model instanceof EReference) {
			return new ReferenceViewModel((EReference) model);
		} else {
			throw new IllegalArgumentException();
		}
	}

	private static class HeaderViewModel extends AbstractViewModel {

		private AbstractElement mModel;

		public HeaderViewModel(AbstractElement model) {
			super(model);
			mModel = model;
		}

		@Override
		public String getStringDisplay() {
			return mModel.getName();
		}
	}
}
