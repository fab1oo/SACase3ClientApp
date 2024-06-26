package ch.fhnw.digi.mockups.case3;

public class JobRequestMessage {

	private String jobnumber;
	private String requestingEmployee;

	public JobRequestMessage(String jobnumber, String requestingEmployee) {
		this.jobnumber = jobnumber;
		this.requestingEmployee = requestingEmployee;
	}

	public String getJobnumber() {
		return jobnumber;
	}

	public void setJobnumber(String jobnumber) {
		this.jobnumber = jobnumber;
	}

	public String getRequestingEmployee() {
		return requestingEmployee;
	}

	public void setRequestingEmployee(String requestingEmployee) {
		this.requestingEmployee = requestingEmployee;
	}

}
