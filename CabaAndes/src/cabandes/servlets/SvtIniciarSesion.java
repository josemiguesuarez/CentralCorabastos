package cabandes.servlets;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;

public class SvtIniciarSesion extends ServletTemplate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	public String darImagenTitulo(HttpServletRequest request) {
		return null;
	}
	@Override
	public String darTituloPagina() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void escribirContenido() throws IOException {
		String nick =request.getParameter( "nick" );
    	String pass =request.getParameter( "pass" );
    	String tipo = mundo.loginYDarTipo(nick, pass, request.getRemoteAddr());
		if (tipo == null)
		{
			imprimirAlert("No valid information. Please try again");
			redireccionar("index.htm");
		}
		else if ( tipo.equals(TU_ADMIN))
		{
			redireccionar("PrincipalCMNUser.htm");
			//TODO redireccionar("PrincipalAdmin.htm");
		}
		else if (tipo.equals(TU_ADMIN_CENTRAL))	
		{
			redireccionar("PrincipalCMNUser.htm");
			//TODO redireccionar("PrincipalAdmin.htm");
		}
		else if (tipo.equals(TU_MAYORISTA))
		{
			redireccionar("PrincipalCMNUser.htm");
		}
		else if (tipo.equals(TU_PROVEEDOR))
		{
			redireccionar("PrincipalCMNUser.htm");
		}
		
		
	}
	@Override
	public boolean requiereMenu() {
		return false;
	}

}
