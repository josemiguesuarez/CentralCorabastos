package cabandes.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

public class SvtRegistrar extends ServletTemplate
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String darTituloPagina() {
		return "registrar";
	}

	@Override
	public String darImagenTitulo(HttpServletRequest request) {
		return null;
	}

	@Override
	public void escribirContenido() throws IOException {
		String tipo = request.getParameter("TYPE");
		String pasword = request.getParameter("pass");
		String pasword2 = request.getParameter("repass");
		String login = request.getParameter("nick");
		
		if ( pasword.equals(pasword2))
		{
			if (mundo.agregarUsuario(login, pasword, tipo, request.getRemoteAddr()))
			{
				imprimirAlert("The user has been created");
				if ( tipo.equals(TU_ADMIN))
				{
					redireccionar("PrincipalAdmin.htm");
				}
				else if (tipo.equals(TU_ADMIN_CENTRAL))	
				{
					redireccionar("PrincipalAdmin.htm");
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
			else
			{
				imprimirAlert("This login has been used. Please change it or try to login");
				redireccionar("index.htm");
			}
			
			
			
			
		}
		else
		{
			imprimirMensajeError("Error de Registro", "Las contrasenas no coinciden");
			redireccionar("index.htm");
		}
		
	}

	@Override
	public boolean requiereMenu() {
		return false;
	}

}
