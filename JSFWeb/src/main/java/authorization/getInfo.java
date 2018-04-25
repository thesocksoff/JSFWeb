package authorization;

import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

@ManagedBean
@SessionScoped
public class getInfo {

	private String message;

	private String login;
	private String password;

	private String durl;
	private String dlogin;
	private String dpassword;

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

	public void tryConnect() throws IOException, ServletException {
		System.out.println("servlet ... ok");
		FacesContext context = FacesContext.getCurrentInstance();
		GetProperty();

		try {
			Connection con = DriverManager.getConnection(durl, dlogin, dpassword);

			System.out.println("Connect ... ok");
			try {
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT name, password FROM users");

				System.out.println("Select table ... ok");

				password = new String(
						com.sun.org.apache.xml.internal.security.utils.Base64.encode(password.getBytes()));

				while (rs.next()) {
					String logins = rs.getString("name");
					String passwords = rs.getString("password");

					if (logins.equals(login) && passwords.equals(password)) {
						System.out.println(logins + "	" + passwords + " ... ok");

						context.getExternalContext().getSessionMap().put("user", login);

						System.out.println("Validate ... ok");

						context.getExternalContext().redirect("index.xhtml");

					} else {
						// Send an error message on Login Failure
						message = "Authentication Failed. Check username or password.";

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

	public String getLogin() {
		return login;
	}

	public String getMessage() {
		return message;
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
}