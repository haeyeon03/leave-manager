package service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import domain.User;
import domain.entity.RemainingLeave;
import helper.InputHelper;
import util.FileUtil;
import util.PageUtil;

public class UserService {
	Scanner scan = new Scanner(System.in);

	// 생성자 호출 시 파일 읽기
	public UserService() {
	}

	/**
	 * 사원 인증
	 * 
	 * @return 인증된 사원 객체를 반환
	 */
	public User signIn(boolean check) {
		boolean found = false;
		System.out.println("사원번호를 입력해주세요:");
		int empNo = Integer.parseInt(scan.nextLine());
		System.out.println("비밀번호를 입력해주세요.");
		String password = scan.nextLine();

		for (User user : loadUserInfo()) {
			if (user.getEmpNo() == empNo && password.equals(user.getPassword()) && user.isAdmin() == check) {
				found = true;
				return user;
			}
		}

		if (!found) {
			System.out.println("존재하지 않습니다. 다시 입력해주세요.");
		}
	
		return null;
	}

	/**
	 * 개인정보
	 * 
	 */
	public static void userInfo(LeaveService leaveService, User generalUser) {
		List<RemainingLeave> remainingLeaveList = leaveService.loadRemainingLeaveList();
		System.out.println("=======================================");
		System.out.printf("%s님 로그인 되었습니다. \n", generalUser.getEmpName());
		System.out.printf("사번 : %d \n", generalUser.getEmpNo());
		System.out.printf("직급 : %s \n", generalUser.getPosition());
		System.out.printf("전화번호 : %s \n", generalUser.getPhoneNumber());
		System.out.printf("생년월일 : %s \n", generalUser.getBirthDate());
		System.out.printf("입사일자 : %s \n", generalUser.getHiredDate());

		for (RemainingLeave remainingLeave : remainingLeaveList) {
			if (remainingLeave.getEmpNo() == generalUser.getEmpNo()) {
				System.out.printf("잔여연차 : %d \n", remainingLeave.getRemainingDays());
				break;
			}
		}
		System.out.println("=======================================");
	}

	/**
	 * 1. 사원 목록 조회
	 */
	public void findUserList() {
		boolean quits = false;
		// 전체 페이지 수
		List<User> userList = loadUserInfo();
		int totalPage = PageUtil.calculateTotalPage(userList.size());
		System.out.printf("전체 페이지수: p.%d\n", totalPage);

		while (!quits) {
			System.out.println("원하는 페이지를 입력해주세요.");
			int currentPage = Integer.parseInt(scan.nextLine());

			List<User> paginated = PageUtil.pagenate(userList, currentPage);

			// 해당 페이지 데이터 출력
			if (paginated.size() > 0) {
				printUserList(paginated);
			} else {
				System.out.println("존재하지 않는 페이지 입니다.");
			}
			// 진행 여부 확인
			if (!InputHelper.shouldContinue()) {
				quits = true;
			}
		}
	}

	/**
	 * 2. 새 사원 등록
	 */
	public void addUser() {
		List<User> userList = loadUserInfo();
		System.out.println("==== 사원 등록을 시작합니다 ====");

		int empNo = InputHelper.inputEmpNo(userList);
		String password = InputHelper.inputPassword();
		String empName = InputHelper.inputEmpName();
		String position = InputHelper.inputPosition();
		LocalDate birthDate = InputHelper.inputBirthDate();
		LocalDate hireDate = InputHelper.inputJoinDate();
		String phoneNumber = InputHelper.inputPhoneNumber();
		boolean isAdmin = InputHelper.inputIsAdmin(empNo);
		createUser(new User(empNo, password, empName, position, birthDate, hireDate, phoneNumber, isAdmin));
		System.out.println("등록이 완료되었습니다!");
	}

	/**
	 * 3,4. 사원 목록 정렬(입사 순)
	 * 
	 * @param order 정렬방식(ASC/DESC)
	 * @throws IOException
	 */
	public void sortListBy(String order){
		List<User> userList = loadUserInfo();
		if ("ASC".equalsIgnoreCase(order)) {
			Collections.sort(userList);
			System.out.println("목록이 오름차순으로 정렬되었습니다.");
		} else if ("DESC".equalsIgnoreCase(order)) {
			Collections.sort(userList, Collections.reverseOrder());
			System.out.println("목록이 내림차순으로 정렬되었습니다.");
		}
		FileUtil.saveFile("user_file.txt", userList);
	}

	/**
	 * 5. 사원 정보 수정
	 * 
	 * @throws IOException
	 */
	public void modifyUser(){
		System.out.println("정보를 수정할 사원의 사번을 입력해주세요.");
		int empNo = Integer.parseInt(scan.nextLine());
		List<User> userList = loadUserInfo();
		User selectedUser = null;
		for (User user : userList) {
			if (user.getEmpNo() == empNo) {
				selectedUser = user;
			}
		}
		if (selectedUser == null) {
			System.out.println("존재하지 않는 사번입니다.");
			return;
		}

		boolean modified = false;
		while (!modified) {
			showEditMenu();
			System.out.println("수정할 정보의 번호를 입력해주세요.");
			int infoNum = Integer.parseInt(scan.nextLine());
			switch (infoNum) {
			case 1:
				System.out.printf("현재 이름: %s \n", selectedUser.getEmpName());
				System.out.println("새 이름을 입력해주세요.");
				selectedUser.setEmpName(scan.nextLine());
				break;
			case 2:
				System.out.printf("현재 직급: %s \n", selectedUser.getPosition());
				System.out.println("새 직급을 입력해주세요.");
				selectedUser.setPosition(scan.nextLine());
				break;
			case 3:
				System.out.printf("현재 전화번호: %s \n", selectedUser.getPhoneNumber());
				System.out.println("새 전화번호를 입력해주세요.");
				selectedUser.setPhoneNumber(scan.nextLine());
				break;
			case 4:
				System.out.println("수정을 종료합니다.");
				modified = true;
				break;
			default:
				System.out.println("올바른 숫자를 입력해주세요.(1~4)");
				break;
			}
		}
		FileUtil.saveFile("user_file.txt", userList);
		System.out.println("수정이 완료되었습니다.");
	}

	/**
	 * 6. 사원 정보 삭제
	 * 
	 * @throws IOException
	 */
	public void removeUser(){
		System.out.println("삭제할 정보의 사번을 입력해주세요.");
		int empNo = Integer.parseInt(scan.nextLine());
		List<User> userList = loadUserInfo();
		boolean removed = false;
		for (User user : userList) {
			if (user.getEmpNo() == empNo) {
				userList.remove(user);
				removed = true;
				System.out.println("삭제가 완료되었습니다.");
				break;
			}
		}

		if (!removed) {
			System.out.println("존재하지 않는 사번입니다.");
		} else {
			// 수정된 리스트를 파일에 다시 저장
			FileUtil.saveFile("user_file.txt", userList);

		}

	}

	// user 파일 로드
	private List<User> loadUserInfo() {
		List<String> dataList = FileUtil.readFile("user_file.txt");
		return convertFileDataToUserList(dataList);
	};

	// 로드 한 파일을 유저 리스트로 변환
	private List<User> convertFileDataToUserList(List<String> dataList) {
		List<User> userList = new ArrayList<>();
		// 두 번째 요소부터 리스트의 마지막 요소까지 자르기
		for (String data : dataList.subList(1, dataList.size())) {
			String[] tokens = data.split(",");
			int empNo = Integer.parseInt(tokens[0]);
			String password = tokens[1];
			String empName = tokens[2];
			String position = tokens[3];
			LocalDate birthDate = LocalDate.parse(tokens[4]);
			LocalDate joinDate = LocalDate.parse(tokens[5]);
			String phoneNumber = tokens[6];
			boolean isAdmin = Boolean.parseBoolean(tokens[7]);
			User user = new User(empNo, password, empName, position, birthDate, joinDate, phoneNumber, isAdmin);
			userList.add(user);
		}
		return userList;
	}

	private void createUser(User user) {
		try {
			FileWriter fw = new FileWriter("src/files/user_file.txt", true);
			// 여러 값을 하나의 문자열로 합침
			String output = String.format("\n%s,%s,%s,%s,%s,%s,%s,%b", user.getEmpNo(), user.getPassword(),
					user.getEmpName(), user.getPosition(), user.getBirthDate(), user.getHiredDate(),
					user.getPhoneNumber(), user.isAdmin());
			// 파일에 출력
			fw.write(output);
			// 파일 닫기
			fw.close();
		} catch (IOException e) {
			// 예외 발생 시 스택 트레이스 출력
			e.printStackTrace();
		}
	}

	// 사용자 정보 저장

	private void printUserList(List<User> list) {
		for (User user : list) {
			System.out.printf(
					"| 사번:%-6s | 비밀번호:%-10s | 이름:%-5s | 직급:%-4s | 생년월일:%-10s | 입사일자:%-10s | 전화번호:%-13s | 관리자 여부:%-3s |\n",
					user.getEmpNo(), user.getPassword(), user.getEmpName(), user.getPosition(), user.getBirthDate(),
					user.getHiredDate(), user.getPhoneNumber(), user.isAdmin());
		}
	}

	private void showEditMenu() {
		System.out.println("+---------------------------------------+");
		System.out.println("             수정목록      	       	");
		System.out.println("+---------------------------------------+");
		System.out.println(" 1. 이름                          		");
		System.out.println(" 2. 직급                         			");
		System.out.println(" 3. 전화번호                 				");
		System.out.println(" 4. 수정 종료               				");
		System.out.println("+---------------------------------------+");

	}

}
