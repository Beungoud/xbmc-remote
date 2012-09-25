package fr.beungoud.xbmcremote;

public class RequestException extends Exception {
	public RequestException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		// TODO Auto-generated constructor stub
	}

	public RequestException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

	public RequestException(String message) {
		super(message);
	}

	public RequestException() {
		super();
	}
}
