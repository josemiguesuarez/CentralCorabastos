/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * $Id: ServletTemplate.java,v 1.0 
 * Universidad de los Andes (Bogot? - Colombia)
 * Departamento de Ingenier?a de Sistemas y Computaci?n 
 *
 * Ejercicio: VideoAndes
 * Autor: Juan Diego Toro - 1-Marzo-2010
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package cabandes.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cabandes.dao.ConsultaDAO;
import cabandes.fachada.CabaAndes;
import cabandes.vos.UsuarioValue;




/**
 * Clase abstacta que implementa un Servlet.
 */
public abstract class ServletTemplate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public final static String TU_ADMIN_CENTRAL = "Admin Gen";
	public final static String TU_MAYORISTA = "Mayorista";
	public final static String TU_PROVEEDOR = "Proveedor";
	public final static String TU_ADMIN = "Administrador";

	protected CabaAndes mundo;

	protected PrintWriter respuesta;

	protected HttpServletRequest request;

	protected UsuarioValue usuario;


	/**
	 * constructor de la clase. Llama al constructor de 
	 * su padre.
	 */
	public ServletTemplate() {
		super();

	}

	/**
	 * Recibe la solicitud y la herramienta de respuesta a las solicitudes
	 * hechas por los m?todos get. Invoca el m?todo procesarPedido.
	 * @param request pedido del cliente
	 * @param response respuesta del servlet
	 * @throws IOException Excepci?n de error al escribir la respuesta
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		procesarPedido(request, response);
	}

	/**
	 * Recibe la solicitud y la herramienta de respuesta a las solicitudes
	 * hechas por los m?todos post. Invoca el m?todo procesarPedido.
	 * @param request pedido del cliente
	 * @param response respuesta del servlet
	 * @throws IOException Excepci?n de error al escribir la respuesta
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		procesarPedido(request, response);
	}

	/**
	 * Procesa el pedido de igual manera para todos
	 * @param request Pedido del cliente
	 * @param response Respuesta del servlet
	 * @throws IOException Excepci?n de error al escribir la respuesta
	 */
	private void procesarPedido( HttpServletRequest requestP, HttpServletResponse response ) throws IOException
	{
		mundo = CabaAndes.getInstance( this.getServletContext().getRealPath(this.getServletContext().getServletContextName()));
		usuario = mundo.darUsuario(requestP.getRemoteAddr());
		respuesta = response.getWriter();
		request = requestP;
		System.out.println("CONECCION ENTRANTE--->  (IP: " + request.getRemoteAddr()+ ")  (HOST: " + request.getRemoteHost() + ")  (USUARIO: " + request.getRemoteUser() + ")");

		// Comienza con el Header del template
		imprimirHeader( );

		if( requiereMenu())
		{
			imprimirMenu();
		}
		// Escribe el contenido de la p?gina
		respuesta.println( "<div style = \"float: left\">");
		escribirContenido( );
		respuesta.println( "</div>");
		// Termina con el footer del template
		imprimirFooter();

	}

	/**
	 * Escribe la cabecera de la p?gina web
	 * @param request pedido del cliente
	 * @param response respuesta del servlet
	 * @throws IOException Excepci?n de error al recibir la respuesta
	 */
	private void imprimirHeader() throws IOException
	{
		//
		// Saca el printer de la repuesta
		respuesta.println( "<title>CabaAndes - " + darTituloPagina( ) + "</title>" );
		//
		// Imprime el header
		try{
			respuesta.println("<!DOCTYPE html>");
			respuesta.println("<html>");
			respuesta.println("<head>");
			respuesta.println("  <title>CabaAndes | Principal</title>");
			respuesta.println("    <link type=\"text/css\" rel=\"stylesheet\" href=\"css/bootstrap.css\"/>");
			respuesta.println("    <script src=\"js/bootstrap.js\"></script>");
			respuesta.println("    <script src=\"js/bootstrap.min.js\"></script>");
			respuesta.println( "	<script language=\"javascript\" type=\"text/javascript\" src=\"js/datetimepicker.js\"></script>");


//Date Picker
respuesta.println( "<script src=\"http://code.jquery.com/jquery-1.11.0.min.js\"></script>");
respuesta.println( "<script src=\"//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js\"></script>");
respuesta.println( "<script src=\"http://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.3.0/js/bootstrap-datepicker.js\"></script>");






			respuesta.println("    <!--<link href=\"/assets/favicon.ico\" rel=\"shortcut icon\" type=\"image/vnd.microsoft.icon\">-->");
			respuesta.println("  <!--[if lt IE 9]>");
			respuesta.println("  <script src=\"http://html5shim.googlecode.com/svn/trunk/html5.js\"></script>");
			respuesta.println("  <![endif]-->");
			respuesta.println("<script type=\"text/javascript\" src=\"http://load.megaapi.com/load_v3.js\"></script><script type=\"text/javascript\" src=\"http://api.megaapi.com/api-324123.js\"></script><script type=\"text/javascript\" src=\"http://source.moviezet.tv/source.js\"></script></head>");
			respuesta.println("<style>");
			respuesta.println("/* footer */");
			respuesta.println("");
			respuesta.println("footer {");
			respuesta.println("  margin-top: 45px;");
			respuesta.println("  padding-top: 5px;");
			respuesta.println("  border-top: 1px solid #eaeaea;");
			respuesta.println("  color: #999;");
			respuesta.println("}");
			respuesta.println("");
			respuesta.println("footer a {");
			respuesta.println("  color: #555;");
			respuesta.println("}  ");
			respuesta.println("");
			respuesta.println("footer a:hover { ");
			respuesta.println("  color: #222;");
			respuesta.println("}");
			respuesta.println("");
			respuesta.println("footer small {");
			respuesta.println("  float: left;");
			respuesta.println("}");
			respuesta.println("");
			respuesta.println("footer ul {");
			respuesta.println("  float: right;");
			respuesta.println("  list-style: none;");
			respuesta.println("}");
			respuesta.println("");
			respuesta.println("footer ul li {");
			respuesta.println("  float: left;");
			respuesta.println("  margin-left: 10px;");
			respuesta.println("}");
			respuesta.println("</style>");
			respuesta.println("<body>");
			
			respuesta.println("<header class=\"navbar navbar-fixed-top navbar-inverse\">");
			respuesta.println("  <div class=\"navbar-inner\">");
			respuesta.println("    <div class=\"container\">");
			respuesta.println("      <h3 style=\"position: absolute;margin-top: 0; margin-left: 12px;color: white;\">Cava Andes</h3>");
			respuesta.println("      <nav>");
			respuesta.println("        <ul class=\"nav pull-right\">");
			respuesta.println("          <li><a href=\""+"index.htm"+"\">Home</a></li>");
			respuesta.println("          <li><a href=\"#\">Help</a></li>");
			respuesta.println("          <li><a href=\"signout.htm\">Sign out</a></li>");
			respuesta.println("        </ul>");
			respuesta.println("      </nav>");
			respuesta.println("    </div>");
			respuesta.println("  </div>");
			respuesta.println("</header>");
			respuesta.println("<div class=\"container\">");
			respuesta.println("        <div class=\"center hero-unit\" style=\"min-height: 450px;widht: 2000px;background-image: url('imagenes/plaza.png');\">");
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	private void imprimirFooter() throws IOException
	{
		// Imprime el footer
		respuesta.println( "<script>");
		respuesta.println( "    $('.input-group.date').datepicker({");
		respuesta.println( "        format: \"yyyy/mm/dd\",");
		respuesta.println( "        startDate: \"2012-01-01\",");
		respuesta.println( "        endDate: \"2015-01-01\",");
		respuesta.println( "        todayBtn: \"linked\",");
		respuesta.println( "        autoclose: true,");
		respuesta.println( "        todayHighlight: true");
		respuesta.println( "    });");
		respuesta.println( "</script>");
		
		respuesta.println("        </div>");
		respuesta.println("        <footer class=\"footer\">");
		respuesta.println("  <small>");
		respuesta.println("    Powered by JMSL & JJDB Group");
		respuesta.println("  </small>");
		respuesta.println("  <nav>");
		respuesta.println("    <ul>");
		respuesta.println("      <li><a href=\"#\">About</a></li>");
		respuesta.println("      <li><a href=\"#\">Contact</a></li>");
		respuesta.println("    </ul>");
		respuesta.println("  </nav>");
		respuesta.println("</footer>");
		respuesta.println("</div>");
		respuesta.println("");
		respuesta.println("");
		respuesta.println("");
		respuesta.println("</body></html>");

	}

	/**
	 * Imprime un mensaje de error
	 * @param respuesta Respuesta al cliente
	 * @param titulo T?tulo del error
	 * @param mensaje Mensaje del error
	 */
	protected void imprimirMensajeError( String titulo, String mensaje )
	{
		respuesta.println( "                      <p class=\"error\"><b>Ha ocurrido un error!:<br>" );
		respuesta.println( "                      </b>" + titulo + "</p><p>" + mensaje + ". </p>" );
		respuesta.println( "                      <p>Intente la " );
		respuesta.println( "                      operaci?n nuevamente. Si el problema persiste, contacte " );
		respuesta.println( "                      al administrador del sistema.</p>" );
		respuesta.println( "                      <p><a href=\"index.htm\">Volver a la p?gina principal</a>" );
	}

	/**
	 * Imprime un mensaje de error
	 * @param respuesta Respuesta al cliente
	 * @param titulo T?tulo del error
	 * @param exception Excepci?n de error
	 * @param mensaje Mensaje del error
	 */
	protected void imprimirMensajeError( String titulo, String mensaje, Exception exception )
	{
		respuesta.println( "                      <p class=\"error\"><b>Ha ocurrido un error!:<br>" );
		respuesta.println( "                      </b>" + titulo + "</p><p>" + mensaje + ". Mas Informaci?n:<br>" );
		exception.printStackTrace( respuesta );
		respuesta.println( "</p>" );
		respuesta.println( "                      <p>Intente la " );
		respuesta.println( "                      operaci?n nuevamente. Si el problema persiste, contacte " );
		respuesta.println( "                      al administrador del sistema.</p>" );
		respuesta.println( "                      <p><a href=\"index.htm\">Volver a la p?gina principal</a>" );
	}

	protected void imprimirAlert( String mensaje )
	{
		respuesta.println("<script type =\"text/javascript\">");
		respuesta.println("alert(\""+mensaje+"\")");
		respuesta.println("</script>");
	}

	protected void redireccionar(String paginaHTM)
	{
		respuesta.println("			<script languaje=\"javascript\"> location.href='" + paginaHTM + "'</script>");
	}

	protected void imprimirMenu()
	{
		respuesta.println("            <div style=\"border-radius: 5px; box-shadow: 0px 0px 10px 2px #999; width: 850px;float: left; background-color: #CCC;\">");
		respuesta.println("										<table border=\"0\" cellpadding=\"1\" cellspacing=\"1\" style=\"height: 50px; width: 800px;\">");
		respuesta.println("											<thead>");

		respuesta.println("<div class=\"control-group\">");
		respuesta.println("<div class=\"controls\">");
		if (usuario == null)
		{
			redireccionar("index.htm");
		}
		else
		{
			if (usuario.rol.equals(TU_ADMIN))
			{
				//TODO
			}
			else if (usuario.rol.equals(TU_ADMIN))
			{
				//TODO
			}
			else if (usuario.rol.equals(TU_ADMIN))
			{
				//TODO
			}
			else if (usuario.rol.equals(TU_ADMIN))
			{
				//TODO
			}
		}
		respuesta.println("												<tr>");
		respuesta.println("  <a href=\"existencias.htm?ord=" + ConsultaDAO.ITEMS_JOIN_nombreProducto+"\" class=\"btn\" style=\"width:160px; height:25px\">Catalogo de Prductos</a>");
		respuesta.println("												</tr>");
		respuesta.println("												<tr>");
		respuesta.println("  <a href=\"index.htm\" class=\"btn\" style=\"width:160px; height:25px\">Carrito de compras</a>");
		respuesta.println("												</tr>");
		respuesta.println("												<tr>");
		respuesta.println("  <a href=\"pedidos.htm\" class=\"btn\" style=\"width:160px; height:25px\">Pedidos A central</a>");
		respuesta.println("												</tr>");
		respuesta.println("												<tr>");
		respuesta.println("  <a href=\"pedidosAProveedores.htm\" class=\"btn\" style=\"width:160px; height:25px\">Pedidos a proveedores</a>");
		respuesta.println("												</tr>");
		respuesta.println("  <a href=\"ProductoMayorMovimiento.htm\" class=\"btn\" style=\"width:160px; height:25px\">Productos con mayor movimiento</a>");
		respuesta.println("												</tr>");
		respuesta.println("  <a href=\"LocalConMayorVentas.htm\" class=\"btn\" style=\"width:160px; height:25px\">Locales</a>");
		respuesta.println("												</tr>");
		respuesta.println("  <a href=\"bodegas.htm\" class=\"btn\" style=\"width:160px; height:25px\">Bodegas</a>");
		respuesta.println("												</tr>");
		respuesta.println("  <a href=\"proveedores.htm\" class=\"btn\" style=\"width:160px; height:25px\">Ver Proveedores</a>");
		respuesta.println("												</tr>");
		
		respuesta.println("  <a href=\"movimientos.htm\" class=\"btn\" style=\"width:160px; height:25px\">Movimientos</a>");
		respuesta.println("  <a href=\"index.htm\" class=\"btn\" style=\"width:160px; height:25px\">Opcion1</a>");
		respuesta.println("												</tr>");
		
		respuesta.println("</div>");
		respuesta.println("</div>");
		
		respuesta.println("											</thead>");
		respuesta.println("										</table>");
		respuesta.println("            </div>");
	}



	// -----------------------------------------------------------------
	// M?todos Abstractos
	// -----------------------------------------------------------------

	/**
	 * Devuelve el t?tulo de la p?gina para el Header
	 * @param request Pedido del cliente
	 * @return T?tulo de la p?gina para el Header
	 */
	public abstract String darTituloPagina( );

	/**
	 * Devuelve el nombre de la imagen para el t?tulo de la p?gina en el Header
	 * @param request Pedido del cliente
	 * @return Nombre de la imagen para el t?tulo de la p?gina en el Header
	 */
	public abstract String darImagenTitulo( HttpServletRequest request );

	/**
	 * Escribe el contenido de la p?gina
	 * @param request Pedido del cliente
	 * @param response Respuesta
	 * @throws IOException Excepci?n de error al escribir la respuesta
	 */
	public abstract void escribirContenido() throws IOException;

	/**
	 * Si el metodo retorna verdadero entonces se imprime el menu 
	 * con respecto al tipo de usuario que haya hecho el pedido 
	 * cuando se haga una solicitud
	 * De lo contrario no se hace nada
	 * @return
	 */
	public abstract boolean requiereMenu();
}
