package authorization;

import javax.faces.bean.ManagedBean;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

@ManagedBean
public class getInfo implements Serializable {
	private boolean success = false;

	private String login;
	private String password;

	private String durl;
	private String dlogin;
	private String dpassword;

	private static final long serialVersionUID = 1L;

	public void GetProperty() {
		try {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
			Properties property = new Properties();
			property.load(inputStream);

			durl = property.getProperty("db.url");
			dlogin = property.getProperty("db.login");
			dpassword = property.getProperty("db.password");
			inputStream.close();

			System.out.println("Get Property ... ok");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void checkUser(String name, String password) {
		try {
			Connection con = DriverManager.getConnection(durl, dlogin, dpassword);
			System.out.println("Connect ... ok");
			try {
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT name, password FROM users");

				System.out.println("Select table ... ok");
				System.out.println("login " + name + " ... ok");
				while (rs.next()) {
					String names = rs.getString("name");
					
					String passwords = rs.getString("password");
					
					System.out.println(names + "	" + passwords + " ... ok");
					if (names.equals(name) && passwords.equals(password)) {
						success = true;
						System.out.println("Validate ... ok");
					}
				}

				rs.close();
				stmt.close();
			} finally {
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String checkState() 
	{
		if(!success) return "validation.xhtml";
		
		return null;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getValidate() {
		GetProperty();
		checkUser(login, password);
		if (success)
			return "success";

		return "";
	}
}
