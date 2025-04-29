
import java.util.Scanner;

import domain.User;
import mode.AdminMode;
import mode.GeneralMode;
import service.LeaveService;
import service.UserService;
import util.TerminalUtil;

public class LeaveManagerMain {

	public static void main(String[] args){
		Scanner scan = new Scanner(System.in);
		UserService userService = new UserService();
		LeaveService leaveService = new LeaveService();
		AdminMode adminMode = new AdminMode(userService, leaveService);
		GeneralMode generalMode = new GeneralMode(userService,leaveService);
		boolean quits = false;

		while (!quits) {
			TerminalUtil.clear();
			SelectOption.showMenu();
			System.out.println("번호를 입력해주세요.");
			// TODO. 입력값 타입 유효성 검사 필요(try/catch)
			int selectedNumber = Integer.parseInt(scan.nextLine());
			
			switch (SelectOption.byOptionNumber(selectedNumber)) {
			case ADMIN:
				User adminUser = userService.signIn(true);
				if (adminUser != null) {
					adminMode.run();
				}
				break;
			case GENERAL:
				User generalUser = userService.signIn(false);
				if (generalUser != null) {
					generalMode.run(generalUser);
				}
				break;
			case EXIT:
				System.out.println("프로그램을 종료합니다.");
				quits = true;
				scan.close();
				break;
			default:
				System.out.println("다시 입력해주세요.");
				break;
			}
		}
	}

}
