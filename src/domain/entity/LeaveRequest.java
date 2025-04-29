package domain.entity;

import java.time.LocalDate;

import domain.CustomFileWritable;

public class LeaveRequest implements CustomFileWritable {
	private int reqNo;
	private String leaveType;
	private LocalDate startDate;
	private LocalDate endDate;
	private String reason;
	private String status;
	private int empNo;

	public LeaveRequest(int reqNo, String leaveType, LocalDate startDate, LocalDate endDate, String reason,
			String status, int empNo) {
		this.reqNo = reqNo;
		this.leaveType = leaveType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.reason = reason;
		this.status = status;
		this.empNo = empNo;
	}

	public int getReqNo() {
		return reqNo;
	}

	public void setReqNo(int reqNo) {
		this.reqNo = reqNo;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getEmpNo() {
		return empNo;
	}

	public void setEmpNo(int empNo) {
		this.empNo = empNo;
	}

	@Override
	public String toString() {
		return "LeaveRequest [reqNo=" + reqNo + ", leaveType=" + leaveType + ", startDate=" + startDate + ", endDate="
				+ endDate + ", reason=" + reason + ", status=" + status + ", empNo=" + empNo + "]";
	}

	@Override
	public String joinWithComma() {
		return "\n" + String.join(",", Integer.toString(reqNo), leaveType, startDate.toString(), endDate.toString(),
				reason, status, Integer.toString(empNo));
	}

}
