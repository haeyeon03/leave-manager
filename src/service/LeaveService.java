package service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import domain.User;
import domain.entity.LeaveRequest;
import domain.entity.RemainingLeave;
import helper.InputHelper;
import util.DateUtil;
import util.FileUtil;
import util.PageUtil;
import util.TerminalUtil;

public class LeaveService {
	Scanner scan = new Scanner(System.in);

	public LeaveService() {
		// 생성자 호출 시 파일 일기
	}

	/**
	 * GeneralMode 1. 휴가신청
	 */
	public void requsetLeave(User generalUser) {
		requestLeave(generalUser);
	}

	/**
	 * AdminMode 7. 휴가 신청 내역 확인
	 */
	public void findLeaveRequestList() {
		boolean quits = false;
		while (!quits) {
			TerminalUtil.clear();
			showLeaveRequestListMenu();
			System.out.println("번호를 입력해주세요.");
			int no = Integer.parseInt(scan.nextLine());
			switch (no) {
			// 1. 휴가 리스트 보기
			case 1:
				inquirySickLeaveRequestList();
				break;
			// 2. 휴가 대기 중 목록 조회 및 승인/반려 결정
			case 2:
				List<LeaveRequest> leaveList = loadLeaveRequestList();
				List<LeaveRequest> waitings = new ArrayList<>();
				// 대기 상태인 리스트가 없을 때
				for (LeaveRequest lr : leaveList) {
					if ("대기".equals(lr.getStatus())) {
						waitings.add(lr);
					}
				}

				if (waitings.isEmpty()) {
					System.out.println("대기 중인 요청이 없습니다.");
					break;
				}
				printLeaveRequestList(waitings);
				System.out.println("승인/반려 처리하고 싶은 번호를 입력해주세요.");
				int reqNo = Integer.parseInt(scan.nextLine());

				boolean found = false;
				// 1. 대기 상태 파일 중 입력 값 여부 확인
				for (LeaveRequest leave : leaveList) {
					if (reqNo == leave.getReqNo()) {
						found = true;

						// 2. 승인/반려 중 선택
						System.out.println("승인(1), 반려(2)를 선택해주세요.");
						int statusNumber = Integer.parseInt(scan.nextLine());
						long updated = 0;
						if (statusNumber == 1 && "연차".equals(leave.getLeaveType())) {
							updated = updateRemainingDays(leave);
						}
						// 3. 숭인일 때 대기상태 파일 "승인"으로 수정
						if (updated == 1 && statusNumber == 1) {
							leave.setStatus("승인");
						} else if (updated == 0 || statusNumber == 2) {
							leave.setStatus("반려");
						} else {
							// TODO. while 으로 처리 필요.
							System.out.println("유효한 번호가 아닙니다.");
						}
					}
				}

				if (found) {
					FileUtil.saveFile("leave_request_file.txt", leaveList);
				} else {
					System.out.println("입력한 번호에 해당하는 요청이 없습니다.");
				}
				break;
			// 3. 종료
			case 3:
				System.out.println("신청 내역 확인을 종료합니다.");
				quits = true;
				break;
			default:
				System.out.println("올바른 숫자를 입력해주세요.(1~4)");
				break;
			}
		}

	}

	public void getMyLeaveRequests(User generalUser) {
		List<LeaveRequest> leaveList = loadLeaveRequestList();
		List<LeaveRequest> myRequestList = new ArrayList<>();
		for (LeaveRequest lr : leaveList) {
			if (lr.getEmpNo() == generalUser.getEmpNo()) {
				myRequestList.add(lr);
			}
		}

		if (myRequestList.isEmpty()) {
			System.out.println("대기 중인 요청이 없습니다.");
		} else {
			printLeaveRequestList(myRequestList);
		}

	}

	private long updateRemainingDays(LeaveRequest selected) {
		LeaveService leaveService = new LeaveService();
		int empNo = selected.getEmpNo();
		List<RemainingLeave> remainingLeaves = leaveService.loadRemainingLeaveList();

		Map<Integer, Integer> map = DateUtil.calculateWorkingDays(String.valueOf(selected.getStartDate()),
				String.valueOf(selected.getEndDate()));

		boolean[] resultArray = new boolean[map.size()];
		int i = 0;

		for (RemainingLeave leave : remainingLeaves) {
			for (Integer year : map.keySet()) {
				if (leave.getEmpNo() == empNo && leave.getYear() == year) {
					System.out.println("사번: " + empNo + " 연도" + year + " 남은휴가일" + leave.getRemainingDays());
					// RemainingDays 가 0 이상이여야 함.
					if (leave.getRemainingDays() > 0) {
						// RemainingDays 에서 휴가일을 뺐을때 0 이상이여야 함.
						int result = leave.getRemainingDays() - map.get(year);
						if (result >= 0) {
							// 남은 휴가를 RemainingDays 로 저장
							leave.setRemainingDays(result);
							// map.size() 만한 배열에 순차적으로 true 를 넣음.
							resultArray[i] = true;
							i++;
						} else {
							System.out.println("남은 휴가일이 부족합니다.");
						}
					} else {
						System.out.println("남은 휴가일이 부족합니다.");
					}
				}
			}
		}

		if (isAllTrue(resultArray)) {
			FileUtil.saveFile("remaining_leave_file.txt", remainingLeaves);
			return 1;
		} else {
			System.out.println("저장오류 입니다.");
		}
		return 0;
	}

	private boolean isAllTrue(boolean[] array) {
		for (boolean a : array) {
			if (!a) {
				return false;
			}
		}
		return true;
	}

	// 휴가 리스트 조회
	private void inquirySickLeaveRequestList() {
		boolean quits = false;
		// 전체 페이지 수
		int totalPage = PageUtil.calculateTotalPage(loadLeaveRequestList().size());
		System.out.printf("전체 페이지수: p.%d\n", totalPage);
		while (!quits) {
			// 원하는 페이지 수
			System.out.println("원하는 페이지를 입력해주세요.");
			int currentPage = Integer.parseInt(scan.nextLine());
			List<LeaveRequest> paginated = PageUtil.pagenate(loadLeaveRequestList(), currentPage);
			// 해당 페이지 데이터 출력
			if (paginated.size() > 0) {
				printLeaveRequestList(paginated);
			} else {
				System.out.println("존재하지 않는 페이지 입니다.");
			}
			// 진행 여부 확인
			if (!InputHelper.shouldContinue()) {
				quits = true;
			}
		}
	}

	private void printLeaveRequestList(List<LeaveRequest> list) {
		for (LeaveRequest leave : list) {
			System.out.printf("| %-4d | 사번:%-6s | 휴가유형:%-6s | 시작일:%-10s | 종료일:%-10s | 사유:%-8s | 상태:%-6s |\n",
					leave.getReqNo(), leave.getEmpNo(), leave.getLeaveType(), leave.getStartDate(), leave.getEndDate(),
					leave.getReason(), leave.getStatus());
		}
	}

	// 휴가 신청 파일 로드
	private List<LeaveRequest> loadLeaveRequestList() {
		List<String> dataList = FileUtil.readFile("leave_request_file.txt");
		return convertFileDataToleaveReauestList(dataList);
	};

	// 로드 한 파일을 휴가 사유 리스트로 변환
	private List<LeaveRequest> convertFileDataToleaveReauestList(List<String> dataList) {
		List<LeaveRequest> leaveReauestList = new ArrayList<>();
		// 두 번째 요소부터 리스트의 마지막 요소까지 자르기
		for (String data : dataList.subList(1, dataList.size())) {
			String[] tokens = data.split(",");
			int reqNo = Integer.parseInt(tokens[0]);
			String leaveType = tokens[1];
			LocalDate startDate = LocalDate.parse(tokens[2]);
			LocalDate endDate = LocalDate.parse(tokens[3]);
			String reason = tokens[4];
			String status = tokens[5];
			int empNo = Integer.parseInt(tokens[6]);
			LeaveRequest leave = new LeaveRequest(reqNo, leaveType, startDate, endDate, reason, status, empNo);
			leaveReauestList.add(leave);
		}
		return leaveReauestList;
	}

	// 마지막 reqNo를 얻는 메서드
	public int getLastReqNo() {
		int lastReqNo = 0;
		if (loadLeaveRequestList().isEmpty()) {
			return lastReqNo; // 빈 목록이면 0부터 시작
		}

		// 마지막 데이터 가져오기
		LeaveRequest lastLeave = loadLeaveRequestList().get(loadLeaveRequestList().size() - 1);
		lastReqNo = lastLeave.getReqNo(); // Leave 객체의 getReqNo()로 reqNo 가져오기
		return ++lastReqNo;
	}

	private void requestLeave(User generalUser) {
		// 마지막 reqNo 가져오기
		int reqNo = getLastReqNo();
		int empNo = generalUser.getEmpNo();
		String leaveType = InputHelper.inputLeaveType();
		LocalDate startDate = InputHelper.inputStartDate();
		LocalDate endDate = InputHelper.inputEndDate();
		String reason = InputHelper.inputLeaveReason();
		String status = "대기";
		createLeave(new LeaveRequest(reqNo, leaveType, startDate, endDate, reason, status, empNo));
		System.out.println("휴가 신청 등록이 완료되었습니다!");

	}

	private void createLeave(LeaveRequest leave) {
		try {
			FileWriter fw = new FileWriter("src/files/leave_request_file.txt", true);
			// 여러 값을 하나의 문자열로 합침
			String output = String.format("\n%d,%s,%s,%s,%s,%s,%d", leave.getReqNo(), leave.getLeaveType(),
					leave.getStartDate(), leave.getEndDate(), leave.getReason(), leave.getStatus(), leave.getEmpNo());
			// 파일에 출력
			fw.write(output);
			// 파일 닫기
			fw.close();
		} catch (IOException e) {
			e.printStackTrace(); // 예외 발생 시 스택 트레이스 출력
		}
	}

	// 남은 휴가 파일 로드
	public List<RemainingLeave> loadRemainingLeaveList() {
		List<String> dataList = FileUtil.readFile("remaining_leave_file.txt");
		return convertFileDataToRemainingLeaveList(dataList);
	};

	// 로드 한 파일을 휴가 사유 리스트로 변환
	private List<RemainingLeave> convertFileDataToRemainingLeaveList(List<String> dataList) {
		List<RemainingLeave> remainingLeaveList = new ArrayList<>();
		// 두 번째 요소부터 리스트의 마지막 요소까지 자르기
		for (String data : dataList.subList(1, dataList.size())) {
			String[] tokens = data.split(",");
			int year = Integer.parseInt(tokens[0]);
			int remainingDays = Integer.parseInt(tokens[1]);
			int empNo = Integer.parseInt(tokens[2]);
			RemainingLeave leave = new RemainingLeave(empNo, year, remainingDays);
			remainingLeaveList.add(leave);
		}
		return remainingLeaveList;
	}

	private void showLeaveRequestListMenu() {
		System.out.println("+---------------------------------------+");
		System.out.println("           휴가 신청 확인 목록            	");
		System.out.println("+---------------------------------------+");
		System.out.println(" 1. 휴가 신청 리스트                         ");
		System.out.println(" 2. 휴가 승인/반려                           ");
		System.out.println(" 3. 종료               					 ");
		System.out.println("+---------------------------------------+");

	}
}
