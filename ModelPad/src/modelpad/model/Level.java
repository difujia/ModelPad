package modelpad.model;

class Level {

	private String[] allClassNames = new String[] { "Apple", "Orange", "Banana", "Lemon", "Melon", "Strawberry" };
	private String[] allAttrNames = new String[] { "myApple", "myOrange", "myBanana", "myLemon", "myMelon",
			"myStrawberry" };

	String[] getAllClassNames() {
		return allClassNames;
	}

	String[] getAllAttrNames() {
		return allAttrNames;
	}
}
