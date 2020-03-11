package bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import entidades.Autorizacion;
import entidades.ClaveAPI;

public class AutorizacionDAO {
	public static Autorizacion carga(String codigo) {
		try {
			Connection c = BD.abrirBD();
			PreparedStatement st = c.prepareStatement("select * from autorizaciones where codigo=?");
			st.setString(1, codigo);
			ResultSet rs = st.executeQuery();
			Autorizacion a = null;
			if (rs.next()) {
				a = new Autorizacion();
				a.setIdUsuario(rs.getInt("idUsuario"));
				a.setIdClave(rs.getInt("idClave"));
				a.setCodigo(rs.getString("codigo"));
			}
			rs.close();
			st.close();
			c.close();
			return a;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
