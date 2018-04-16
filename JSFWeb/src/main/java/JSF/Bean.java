package JSF;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.flywaydb.core.Flyway;

@ManagedBean
@SessionScoped
public class Bean {
	private String name;
	private String url = "jdbc:postgresql://localhost:5432/vscale_db";
	private String login = "qwerty";
	private String password = "123";

	public String getName() {
		return name;
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

            flyway.setDataSource("jdbc:postgresql://localhost:5432/vscale_db", "qwerty", "123");
            
            flyway.migrate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
