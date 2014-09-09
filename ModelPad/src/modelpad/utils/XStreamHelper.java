package modelpad.utils;

import modelpad.datamodel.AbstractElement;
import modelpad.datamodel.EAttribute;
import modelpad.datamodel.EClass;
import modelpad.datamodel.EReference;
import modelpad.datamodel.EReferenceInfo;
import modelpad.datamodel.Level;
import modelpad.datamodel.Solution;

import com.thoughtworks.xstream.XStream;

public class XStreamHelper {

	private static volatile XStream instance;

	private XStreamHelper() {}

	public static XStream getReusableConfiguredXStream() {
		if (instance == null) {
			synchronized (XStreamHelper.class) {
				if (instance == null) instance = getNewConfiguredXStream();
			}
		}
		return instance;
	}

	/**
	 * Get a new ready-to-use xstream instance.
	 * 
	 * @return
	 */
	public static XStream getNewConfiguredXStream() {
		// changes to this method result in recreate all xml level files!

		XStream xstream = new XStream();
		xstream.setMode(XStream.ID_REFERENCES);

		xstream.alias("class", EClass.class);
		xstream.alias("attribute", EAttribute.class);
		xstream.alias("reference", EReference.class);
		xstream.alias("info", EReferenceInfo.class);
		xstream.alias("solution", Solution.class);
		xstream.alias("level", Level.class);

		xstream.addImplicitCollection(Solution.class, "structure", EClass.class);
		xstream.addImplicitCollection(EClass.class, "references", EReference.class);
		xstream.addImplicitCollection(EClass.class, "attributes", EAttribute.class);

		xstream.useAttributeFor(String.class);
		xstream.useAttributeFor(boolean.class);

		xstream.omitField(AbstractElement.class, "observable");
		xstream.omitField(AbstractElement.class, "recycler");

		xstream.aliasSystemAttribute("rid", "reference");	// to avoid confusion
		return xstream;
	}
}
