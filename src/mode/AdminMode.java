package mode;
import java.util.Scanner;

import service.LeaveService;
import service.UserService;
import util.TerminalUtil;

public class AdminMode {
	public static UserService userService;
	public static LeaveService leaveService;

	public AdminMode(UserService userService, LeaveService leaveService) {
		this.userService = userService;
		this.leaveService = leaveService;
	}

	public void run(){

		Scanner scan = new Scanner(System.in);
		boolean quits = false;
		while (!quits) {
			TerminalUtil.clear();
			showMenu();
			System.out.println("번호를 입력해주세요.");
			int no = Integer.parseInt(scan.nextLine());
			switch (no) {
			// 사원조회
			case 1:
				userService.findUserList();
				scan.nextLine();
				break;
			// 사원등록
			case 2:
				userService.addUser();
				scan.nextLine();
				break;
			// 사원정보 오름차순(입사순)
			case 3:
				userService.sortListBy("ASC");
				scan.nextLine();
				break;
			// 사원정보 내림차순(입사순)
			case 4:
				userService.sortListBy("DESC");
				scan.nextLine();
				break;
			// 사원정보 수정
			case 5:
				userService.modifyUser();
				scan.nextLine();
				break;
			// 사원정보 삭제
			case 6:
				userService.removeUser();
				scan.nextLine();
				break;
			// 휴가 신청 내역 확인
			case 7:
				leaveService.findLeaveRequestList();
				scan.nextLine();
				break;
			// 로그아웃
			case 8:
				// 자동저장
				System.out.println("로그아웃이 완료되었습니다.");
				quits = true;
				scan.nextLine();
				break;
			// 종료
			case 9:
				System.out.println("프로그램을 종료합니다.");
				scan.nextLine();
				quits = true;
				System.exit(0);
				break;
			default:
				System.out.println("올바른 번호를 입력해주세요.(1번~6번)");
				scan.nextLine();
				break;

			}
		}
	}

	public static void showMenu() {
		System.out.println("+---------------------------------------+");
		System.out.println("              관리자 메뉴      				");
		System.out.println("+---------------------------------------+");
		System.out.println(" 1. 사원조회                          		");
		System.out.println(" 2. 사원등록                          		");
		System.out.println(" 3. 사원정보 오름차순(입사순)                 	");
		System.out.println(" 4. 사원정보 내림차순(입사순)                 	");
		System.out.println(" 5. 사원정보 수정                       	");
		System.out.println(" 6. 사원정보 삭제                       	");
		System.out.println(" 7. 휴가신청 리스트 확인                 		");
		System.out.println(" 8. 로그아웃      	               	 		");
		System.out.println(" 9. 종료                         			");
		System.out.println("+---------------------------------------+");
	}

}
