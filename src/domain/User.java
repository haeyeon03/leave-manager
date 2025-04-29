package domain;

import java.time.LocalDate;

public class User implements Comparable, CustomFileWritable {
	private int empNo;
	private String password;
	private String empName;
	private String position;
	private LocalDate birthDate;
	private LocalDate hiredDate;
	private String phoneNumber;
	private boolean isAdmin;

	public User() {
		this(0, null, null, null, null, null, null, false);
	}

	public User(int empNo, String password, String empName, String position, LocalDate birthDate, LocalDate hiredDate,
			String phoneNumber, boolean isAdmin) {
		super();
		this.empNo = empNo;
		this.password = password;
		this.empName = empName;
		this.position = position;
		this.birthDate = birthDate;
		this.hiredDate = hiredDate;
		this.phoneNumber = phoneNumber;
		this.isAdmin = isAdmin;
	}

	public int getEmpNo() {
		return empNo;
	}

	public void setEmpNo(int empNo) {
		this.empNo = empNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public LocalDate getHiredDate() {
		return hiredDate;
	}

	public void setHiredDate(LocalDate hiredDate) {
		this.hiredDate = hiredDate;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Override
	public String toString() {
		return "User [empNo=" + empNo + ", password=" + password + ", empName=" + empName + ", position=" + position
				+ ", birthDate=" + birthDate + ", hiredDate=" + hiredDate + ", phoneNumber=" + phoneNumber
				+ ", isAdmin=" + isAdmin + "]";
	}

	@Override
	public int compareTo(Object object) {
		User user = null;
		// Object User가 있는지 점검한다.
		if (object instanceof User) {
			user = (User) object;
		}
		// 비교
		if (this.getHiredDate().isAfter(user.getHiredDate())) {
			return 1;
		} else if (this.getHiredDate().isBefore(user.getHiredDate())) {
			return -1;
		} else {
			return 0;
		}
	}

	@Override
	public String joinWithComma() {
		return "\n" + String.join(",", Integer.toString(empNo), password, empName, position, birthDate.toString(),
				hiredDate.toString(), phoneNumber, Boolean.toString(isAdmin));
	}

}
