package domain.entity;

import domain.CustomFileWritable;

public class RemainingLeave implements CustomFileWritable {
	private int empNo;
	private int year;
	private int remainingDays;
	
	public RemainingLeave(int empNo, int year, int remainingDays) {
		this.empNo = empNo;
		this.year = year;
		this.remainingDays = remainingDays;
	}

	public int getEmpNo() {
		return empNo;
	}


	public void setEmpNo(int empNo) {
		this.empNo = empNo;
	}


	public int getYear() {
		return year;
	}


	public void setYear(int year) {
		this.year = year;
	}


	public int getRemainingDays() {
		return remainingDays;
	}


	public void setRemainingDays(int remainingDays) {
		this.remainingDays = remainingDays;
	}


	@Override
	public String toString() {
		return "RemainingLeave [empNo=" + empNo + ", year=" + year + ", remainingDays=" + remainingDays + "]";
	}
	
	@Override
	public String joinWithComma() {
		return "\n"
				+ String.join(",", Integer.toString(year), Integer.toString(remainingDays), Integer.toString(empNo));
	}
	
	
}
