package modelpad.metamodel;

public class ModelFactory {

	private static final EClass headerClass = new EClass("Classes");
	private static final EAttribute headerAttr = new EAttribute("Attributes", null);
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

	public static ViewModelBase createViewModel(ElementBase model) {
		if (model == headerClass || model == headerAttr || model == headerRef) {
			return new HeaderViewModel(model);
		} else if (model instanceof EClass) {
			return new ClassViewModel((EClass) model);
		} else if (model instanceof EAttribute) {
			return new AttributeViewModel((EAttribute) model);
		} else if (model instanceof EReferenceInfo) {
			return new ReferenceInfoViewModel((EReferenceInfo) model);
		} else {
			return null;
		}
	}

	private static class HeaderViewModel extends ViewModelBase {

		private ElementBase mModel;

		public HeaderViewModel(ElementBase model) {
			super(model);
			mModel = model;
		}

		@Override
		public String getStringDisplay() {
			return mModel.getName();
		}
	}
}
