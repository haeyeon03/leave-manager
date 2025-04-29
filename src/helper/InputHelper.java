package helper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import domain.User;

public class InputHelper {

	private static final Scanner scan = new Scanner(System.in);

	public static int inputEmpNo(List<User> userList) {
		while (true) {
			System.out.println("사번을 입력해주세요. (4자리, 1XXX: 사용자 / 9XXX: 관리자)");
			String input = scan.nextLine();

			// 정규식으로 입력값이 유효한지 확인
			if (input.matches("^[1|9]\\d{3}$")) {
				boolean isDuplicate = false;

				// 중복된 사번 체크
				for (User user : userList) {
					if (input.equals(user.getEmpNo())) { // 같은 사번이 있으면
						isDuplicate = true;
						break;
					}
				}

				if (isDuplicate) {
					System.out.println("이미 있는 사번입니다.");
				} else {
					return Integer.parseInt(input); // 유효한 사번이면서 중복되지 않으면 반환
				}
			} else {
				System.out.println("잘못된 사번입니다.");
			}
		}

	}

	public static String inputPassword() {
		while (true) {
			System.out.println("비밀번호를 입력해주세요. (영어 + 특수문자 !@# 만 사용 가능)");
			String input = scan.nextLine();
			if (input.matches("^[a-zA-Z!@#]+$")) {
				return input;
			} else {
				System.out.println("잘못된 비밀번호입니다.");
			}
		}
	}

	public static String inputEmpName() {
		while (true) {
			System.out.println("이름을 입력해주세요. (한글 5글자 이내)");
			String input = scan.nextLine();
			if (input.matches("^[가-힣]{1,5}$")) {
				return input;
			} else {
				System.out.println("잘못된 이름입니다.");
			}
		}
	}

	public static String inputPosition() {
		while (true) {
			System.out.println("직급을 입력해주세요. (한글 3글자 이내)");
			String input = scan.nextLine();
			if (input.matches("^[가-힣]{1,3}$")) {
				return input;
			} else {
				System.out.println("잘못된 직급입니다.");
			}
		}
	}

	public static LocalDate inputBirthDate() {
		return inputDate("생년월일을 입력해주세요. (YYYY-MM-DD)");
	}

	public static LocalDate inputJoinDate() {
		return inputDate("입사일자를 입력해주세요. (YYYY-MM-DD)");
	}

	private static LocalDate inputDate(String message) {
		while (true) {
			System.out.println(message);
			String input = scan.nextLine();
			try {
				return LocalDate.parse(input, DateTimeFormatter.ISO_LOCAL_DATE);
			} catch (DateTimeParseException e) {
				System.out.println("잘못된 날짜 형식입니다. (예: 1990-01-01)");
			}
		}
	}

	public static String inputPhoneNumber() {
		while (true) {
			System.out.println("전화번호를 입력해주세요. (예: 010-1234-5678 또는 010-123-4567)");
			String input = scan.nextLine();
			if (input.matches("^(\\d{3}-\\d{3,4}-\\d{4})$") && input.length() <= 13) {
				return input;
			} else {
				System.out.println("잘못된 전화번호입니다.");
			}
		}
	}

	public static boolean inputIsAdmin(int empNo) {
		// empNo를 String으로 변환
		String empNoStr = String.valueOf(empNo);

		// 정규식을 사용하여 첫 번째 자리가 1 또는 9인지 확인
		if (empNoStr.matches("^[19]\\d{3}$")) {
			// 첫 번째 자리가 9이면 관리자(true), 1이면 사용자(false)
			return empNoStr.charAt(0) == '9';
		} else {
			System.out.println("잘못된 사번입니다.");
			return false; // 잘못된 사번이므로 false 반환
		}
	}

	public static LocalDate inputStartDate() {
		return inputDate("휴가 시작일을 입력하세요. (YYYY-MM-DD)");
	}

	public static LocalDate inputEndDate() {
		return inputDate("휴가 종료일을 입력하세요. (YYYY-MM-DD)");
	}

	public static String inputLeaveType() {
		while (true) {
			System.out.print("휴가 유형을 선택하세요. (1: 병가, 2: 휴가): ");
			String leaveType = scan.nextLine();

			if ("1".equals(leaveType)) {
				return "병가";
			} else if ("2".equals(leaveType)) {
				return "휴가";
			} else {
				System.out.println("잘못 입력하셨습니다. 1 또는 2를 입력해주세요.");
			}
		}
	}

	public static String inputLeaveReason() {
		System.out.print("사유를 입력해주세요 (한글 20자 내외): ");
		String input = scan.nextLine();
		while (true) {
			if (input.matches("^[가-힣]{1,20}$")) {
				return input;
			} else {
				System.out.print("잘못된 입력입니다. (한글만 입력 가능)");
			}
		}
	}

	/**
	 * 진행 여부 확인
	 * 
	 * @return 'Y' 계속 진행; 'N' 중지
	 */
	public static boolean shouldContinue() {
		while (true) {
			System.out.println("계속 하시겠습니까?(Y/N)");
			char continues = scan.nextLine().charAt(0);
			if (continues == 'Y' || continues == 'y') {
				return true;
			} else if (continues == 'N' || continues == 'n') {
				return false;
			} else {
				System.out.println("Y 또는 N을 입력해주세요.");
			}
		}
	}
}
