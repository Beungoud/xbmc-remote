/**
 * 
 */
package fr.beungoud.util;

/**
 * @author Benoit
 *
 */
public class Server {
	private int id;
	
	private String serverName;
	
	private int port;
	
	private String login;
	
	private String password;
	
	private boolean isCurrent = false;
	
	private String hostname;

	/**
	 * @param serverName
	 * @param hostname
	 * @param port
	 * @param login
	 * @param password
	 * @param isCurrent
	 */
	public Server(int id, String serverName, String hostname, int port, String login, String password, boolean isCurrent) {
		super();
		this.id = id;
		this.serverName = serverName;
		this.hostname = hostname;
		this.port = port;
		this.login = login;
		this.password = password;
		this.isCurrent = isCurrent;
	}
	
	public Server()
	{
		id = -1;
		serverName = "NewServer";
		hostname= "192.168.1.1";
		port = 8080;
		login = "xbmc";
		password = "xbmc";
		isCurrent = false;
	}

	/**
	 * @return the serverName
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * @param serverName the serverName to set
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the isCurrent
	 */
	public boolean isCurrent() {
		return isCurrent;
	}

	/**
	 * @param isCurrent the isCurrent to set
	 */
	public void setCurrent(boolean isCurrent) {
		this.isCurrent = isCurrent;
	}

	/**
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * @param hostname the hostname to set
	 */
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return serverName;
	}

}
