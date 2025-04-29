
public enum SelectOption {
	ADMIN(1, "관리자 로그인"), GENERAL(2, "사용자 로그인"), EXIT(3, "종료");

	SelectOption(int optionNumber, String optionName) {
		this.optionNumber = optionNumber;
		this.optionName = optionName;
	}

	private final int optionNumber;
	private final String optionName;

	public int getOptionNumber() {
		return optionNumber;
	}

	public String getOptionName() {
		return optionName;
	}

	public static SelectOption byOptionNumber(int number) {
		for (SelectOption o : values()) {
			if (o.getOptionNumber() == number) {
				return o;
			}
		}
		return null;
	}

	public static void showMenu() {
		System.out.println("+---------------------------------------+");
		System.out.println("           	  메뉴      	      		");
		System.out.println("+---------------------------------------+");
		System.out.println(" 1. 관리자 로그인                          	");
		System.out.println(" 2. 사용자 로그인                         	");
		System.out.println(" 3. 종료				        			");
		System.out.println("+---------------------------------------+");
	}
}
