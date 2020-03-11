package entidades;

public class LoginEntidad {
	private int idUsuario;
	private boolean login;
	
	
	public LoginEntidad() {
		this.idUsuario = 0;
		this.login = false;
	}
	public int getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}
	public boolean isLogin() {
		return login;
	}
	public void setLogin(boolean login) {
		this.login = login;
	}
	
	
}
