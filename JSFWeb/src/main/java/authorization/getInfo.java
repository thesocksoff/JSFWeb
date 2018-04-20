package authorization;

import javax.faces.application.Application;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.bean.*;
import javax.faces.event.*;

import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

@ManagedBean
@SessionScoped
public class getInfo implements SystemEventListener {
	private static boolean success = false;

	private String result;

	private String login;
	private String password;

	private String durl;
	private String dlogin;
	private String dpassword;

	@Override
	public void processEvent(SystemEvent event) throws AbortProcessingException {
		if (event instanceof PreDestroyApplicationEvent) {
			success = false;
			durl = null;
			dlogin = null;
			dpassword = null;
			login = null;
			password = null;
		}
	}

	@Override
	public boolean isListenerForSource(Object source) {
		return source instanceof Application;
	}

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

				while (rs.next()) {
					String names = rs.getString("name");
					String passwords = rs.getString("password");

					if (names.equals(name) && passwords.equals(password)) {
						success = true;
						System.out.println(names + "	" + passwords + " ... ok");

						System.out.println("Validate ... " + success);
						break;
					} else
						result = "Incorect data";
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

	public void checkState() throws IOException {
		if (!success) {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(ec.getRequestContextPath() + "/validation.xhtml");

			System.out.println("index ... " + success);
		}
	}

	public String getLogin() {
		return login;
	}

	public String getResult() {
		return result;
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
		password = new String(com.sun.org.apache.xml.internal.security.utils.Base64.encode(password.getBytes()));

		checkUser(login, password);

		if (success)
			return "success";
		else
			return "fail";
	}
}
