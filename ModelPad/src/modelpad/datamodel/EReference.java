package modelpad.datamodel;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.Objects.ToStringHelper;

public class EReference extends AbstractElement {

	private static final long serialVersionUID = 5707299262724241156L;

	private EClass source;
	private EClass target;
	private EReference opposite;
	private EReferenceInfo info;

	/**
	 * DO NOT USE. This is for xml serialization
	 */
	protected EReference() {}

	protected EReference(EClass source, EClass target) {
		this.source = checkNotNull(source);
		this.target = checkNotNull(target);
		this.info = ModelFactory.createInfoPlaceHolder();
		this.info.setOwner(this);
	}

	@Override
	public String getName() {
		return info.getName();
	}

	@Override
	public void dispose() {
		checkState(source != null && target != null, "This reference has already been disposed!");
		super.dispose();
		info.dispose();
		info = ModelFactory.createInfoPlaceHolder();
		source.removeRef(this);
		source = null;
		target = null;
		if (opposite != null) {
			opposite.removeOpposite();
			opposite = null;
		}
	}

	@Override
	protected boolean lookslike(AbstractElement other) {
		if (getClass() != other.getClass()) {
			return false;
		}
		EReference that = (EReference) other;
		return info.lookslike(that.info);
	}

	@Override
	protected boolean match(AbstractElement other) {
		if (lookslike(other)) {
			EReference that = (EReference) other;
			return source.match(that.source) && target.match(that.target);
		} else {
			return false;
		}
	}

	protected void setInfo(EReferenceInfo info) {
		EReferenceInfo oldInfo = this.info;
		oldInfo.dispose();
		info.removeFromOwner();
		info.setOwner(this);
		this.info = info;
		notifyDataChanged();
	}

	protected EReferenceInfo getInfo() {
		return info;
	}

	protected void clearInfo(EReferenceInfo info) {
		checkArgument(this.info == info, "Info not the same! %s != %s", this.info, info);
		info.setOwner(null);
		this.info = ModelFactory.createInfoPlaceHolder();
		this.info.setOwner(this);
		notifyDataChanged();
	}

	protected boolean isContainment() {
		return info.isContainment();
	}

	protected String getLowerBound() {
		return info.getLowerbound();
	}

	protected String getUpperBound() {
		return info.getUpperbound();
	}

	protected EClass getTarget() {
		return target;
	}

	protected EReference getOpposite() {
		return opposite;
	}

	protected void setOpposite(EReference opposite) {
		removeOpposite();
		this.opposite = opposite;
	}

	private void removeOpposite() {
		opposite = null;
	}

	@Override
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper()					//
				.add("source", source != null ? source.getName() : "none")		//
				.add("target", target != null ? target.getName() : "none")		//
				.add("opposite", opposite != null ? opposite.getName() : "none")	//
				.add("containment", isContainment())	//
				.add("lowerbound", getLowerBound())			//
				.add("upperbound", getUpperBound());
	}

}
