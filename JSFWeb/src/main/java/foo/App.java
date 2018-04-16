package foo;

import org.flywaydb.core.Flyway;

public class App {

	public static void main(String[] args) {
		Flyway flyway = new Flyway();

		flyway.setDataSource("jdbc:postgresql://localhost:5432/vscale_db", "qwerty", "123");
		
		//flyway.setInitOnMigrate(true);
		flyway.migrate();
	}
}
