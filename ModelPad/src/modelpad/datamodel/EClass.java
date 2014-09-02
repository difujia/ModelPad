package modelpad.datamodel;

import java.util.Collections;
import java.util.Set;

import com.google.common.base.Objects.ToStringHelper;
import com.google.common.collect.Sets;

public class EClass extends AbstractElement {

	private static final long serialVersionUID = 803979828454442810L;

	private Set<EAttribute> attributes;
	private Set<EReference> references;
	private String name;

	/**
	 * DO NOT USE. This is for xml serialization
	 */
	protected EClass() {}

	protected EClass(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		for (EAttribute attr : getAttrs().toArray(new EAttribute[getAttrs().size()])) {
			attr.dispose();
		}

		for (EReference ref : getRefs().toArray(new EReference[getRefs().size()])) {
			if (ref.getOpposite() != null) {
				ref.getOpposite().dispose();
			}
			ref.dispose();
		}
		attributes.clear();
		references.clear();
		super.dispose();
	}

	@Override
	protected boolean match(AbstractElement other) {
		return lookslike(other);
	}

	@Override
	protected boolean lookslike(AbstractElement other) {
		if (getClass() != other.getClass()) {
			return false;
		}
		EClass that = (EClass) other;
		return name.equals(that.name);
	}

	protected Set<EAttribute> getAttributes() {
		return Collections.unmodifiableSet(getAttrs());
	}

	protected Set<EReference> getReferences() {
		return Collections.unmodifiableSet(getRefs());
	}

	protected boolean addAttr(EAttribute attr) {
		if (hasAttr(attr)) return false;
		attr.removeFromOwner();
		attr.setOwner(this);
		return getAttrs().add(attr);
	}

	protected boolean removeAttr(EAttribute attr) {
		if (!hasAttr(attr)) return false;
		attr.setOwner(null);
		return getAttrs().remove(attr);
	}

	protected boolean hasAttr(EAttribute attr) {
		return getAttrs().contains(attr);
	}

	protected boolean addRef(EReference ref) {
		return getRefs().add(ref);
	}

	protected boolean removeRef(EReference ref) {
		return getRefs().remove(ref);
	}

	@Override
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper().add("attrs", getAttrs()).add("refs", getRefs());
	}

	/**
	 * Lazy instantiation
	 * 
	 * @return
	 */
	private Set<EAttribute> getAttrs() {
		if (attributes == null) {
			attributes = Sets.newHashSet();
		}
		return attributes;
	}

	/**
	 * Lazy instantiation
	 * 
	 * @return
	 */
	private Set<EReference> getRefs() {
		if (references == null) {
			references = Sets.newHashSet();
		}
		return references;
	}

}
