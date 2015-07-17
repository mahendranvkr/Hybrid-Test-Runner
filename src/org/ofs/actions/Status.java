package org.ofs.actions;

public class Status {
	static String Message = "";
	static boolean Result = true;

	void setMessage(String message) {
		Message = message;
	}

	Status(boolean result, String message) {
		Result = result;
		Message = message;
	}

	public Status() {
		// TODO Auto-generated constructor stub
	}

	public void setResult(boolean result) {
		Result = result;

	}

	public boolean getResult() {

		return Result;
	}

	public String getMessage() {

		return Message;
	}
}
