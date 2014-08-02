package modelpad.model;

public class Level {

	private String[] classNames = new String[] { "Bank", "Client", "Account", "Balance" };
	private String[] attrNames = new String[] { "numOfClients", "numOfAccounts", "balance" };
	private String[] attrTypes = new String[] { "int", "int", "float" };
	private String[] refNames = new String[] { "clients", "owner", "accounts", "accounts" };
	private int[] lowerBounds = new int[] { 0, 1, 0, 0 };
	private int[] upperBounds = new int[] { 10, 1, 50, 50 };
	private EClass[] allClasses;
	private EAttribute[] allAttrs;
	private EReferenceInfo[] allRefInfos;

	public Level() {
		allClasses = new EClass[classNames.length];
		for (int i = 0; i < allClasses.length; i++) {
			allClasses[i] = new EClass(classNames[i]);
		}

		allAttrs = new EAttribute[attrNames.length];
		for (int i = 0; i < allAttrs.length; i++) {
			allAttrs[i] = new EAttribute(attrNames[i], attrTypes[i]);
		}

		allRefInfos = new EReferenceInfo[refNames.length];
		for (int i = 0; i < allRefInfos.length; i++) {
			allRefInfos[i] = new EReferenceInfo(refNames[i]);
			allRefInfos[i].setLowerBound(lowerBounds[i]);
			allRefInfos[i].setUpperBound(upperBounds[i]);
		}
	}

	public EClass[] getAllClasses() {
		return allClasses;
	}

	public EAttribute[] getAllAttrs() {
		return allAttrs;
	}

	public EReferenceInfo[] getAllRefs() {
		return allRefInfos;
	}
}
