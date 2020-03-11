package bd;

import java.sql.Connection;
import java.sql.DriverManager;

import com.mysql.cj.jdbc.Driver;

public class BD {

	static {
		try {
			DriverManager.registerDriver(new Driver());
		}
		catch (Exception e) {}
	}
	
	public static Connection abrirBD() {
		try {
			Connection c = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/proyectorest"
					+ "?useUnicode=true"
					+ "&useJDBCCompliantTimezoneShift=true"
					+ "&useLegacyDatetimeCode=false"
					+ "&serverTimezone=UTC",
					"izquierdo",
					"izquierdo");
			return c;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
