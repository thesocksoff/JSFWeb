package JSF;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.flywaydb.core.Flyway;

import java.io.*;
import java.util.Properties;

@ManagedBean
@SessionScoped
public class Bean {
	private String name;

	private String url;
	private String login;
	private String password;

	public String getName() {
		return name;
	}

	public void GetProperty() {
		try {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
			Properties property = new Properties();
			property.load(inputStream);

			url = property.getProperty("db.url");
			login = property.getProperty("db.login");
			password = property.getProperty("db.password");
			
			migrate();
			//System.out.println("url: " + url + ", login: " + login + ", password: " + password);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void testDatabase() {
		try {
			Class.forName("org.postgresql.Driver");

			Connection con = DriverManager.getConnection(url, login, password);
			try {
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT message FROM hello");

				while (rs.next()) {
					name = rs.getString("message");
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

	public void migrate() {
		try {
			Flyway flyway = new Flyway();

			flyway.setDataSource(url, login, password);

			flyway.migrate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
