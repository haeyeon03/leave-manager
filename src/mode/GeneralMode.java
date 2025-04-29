package mode;

import java.util.Scanner;

import domain.User;
import service.LeaveService;
import service.UserService;
import util.TerminalUtil;

public class GeneralMode {
	public static UserService userService;
	public static LeaveService leaveService;

	public GeneralMode(UserService userService, LeaveService leaveService) {
		this.userService = userService;
		this.leaveService = leaveService;
	}

	public void run(User generalUser) {

		Scanner scan = new Scanner(System.in);
		boolean quits = false;
		while (!quits) {
			UserService.userInfo(leaveService, generalUser);
			TerminalUtil.clear();
			showMenu();
			System.out.println("번호를 입력해주세요.");
			int no = Integer.parseInt(scan.nextLine());
			switch (no) {
			// 휴가신청
			case 1:
				leaveService.requsetLeave(generalUser);
				scan.nextLine();
				break;
			// 휴가 신청 내역 확인
			case 2:
				leaveService.getMyLeaveRequests(generalUser);
				scan.nextLine();
				break;
			// 연차신청
			case 3:
				// 자동저장
				System.out.println("로그아웃이 완료되었습니다.");
				quits = true;
				scan.nextLine();
				break;
			// 종료
			case 4:
				System.out.println("프로그램을 종료합니다.");
				quits = true;
				scan.nextLine();
				System.exit(0);
				break;
			default:
				System.out.println("올바른 번호를 입력해주세요.(1번~6번)");
				break;
			}
		}
	}

	public void showMenu() {
		System.out.println("+---------------------------------------+");
		System.out.println("           	 메뉴      	       			");
		System.out.println("+---------------------------------------+");
		System.out.println(" 1. 휴가신청                          		");
		System.out.println(" 2. 신청내역 확인                          	");
		System.out.println(" 3. 로그아웃                        		");
		System.out.println(" 4. 종료                         			");
		System.out.println("+---------------------------------------+");
	}

}
