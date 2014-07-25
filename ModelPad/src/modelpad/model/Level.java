package modelpad.model;


public class Level {

	private String[] classNames = new String[] { "Apple", "Orange", "Banana", "Lemon", "Melon", "Strawberry" };
	private String[] attrNames = new String[] { "myApple", "myOrange", "myBanana", "myLemon", "myMelon", "myStrawberry" };
	private String[] refNames = new String[] { "sister", "brother" };
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
			allAttrs[i] = new EAttribute(attrNames[i], "int");
		}

		allRefInfos = new EReferenceInfo[refNames.length];
		for (int i = 0; i < allRefInfos.length; i++) {
			allRefInfos[i] = new EReferenceInfo(refNames[i]);
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
