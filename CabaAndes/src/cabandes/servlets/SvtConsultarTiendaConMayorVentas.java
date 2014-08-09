package cabandes.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import cabandes.complementos.PersonalConverter;
import cabandes.vos.AlmacenajeValue;
import cabandes.vos.LocalValue;

public class SvtConsultarTiendaConMayorVentas extends ServletTemplate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<LocalValue> locales;

	@Override
	public String darTituloPagina() {
		// TODO Auto-generated method stub
		return "Locales";
	}

	@Override
	public String darImagenTitulo(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void escribirContenido() throws IOException {
		try {
			locales = mundo.darLocales();
			respuesta.println("            <div style=\"border-radius: 10px; box-shadow: 0px 0px 10px 2px #999; width: 850px; background-color: #CCC;\">");
			respuesta.println("												<strong>"
					
			+ ejecutarAcciones () +"</strong>");
			
			respuesta.println("            </div>");


			escribirAcciones();

			escribirDatos();
		} catch (Exception e) {
			respuesta.println("            <div style=\"border-radius: 10px; box-shadow: 0px 0px 10px 2px #999; width: 850px; background-color: #CCC;\">");
			respuesta.println("												<strong>"
					
			+ "There was an error while we were trying to show the Locals: " + e.getMessage() +"</strong>");
			
			respuesta.println("            </div>");
		}

			
	}


	private void escribirDatos() {

		respuesta.println("            <div style=\"border-radius: 10px; box-shadow: 0px 0px 10px 2px #999; width: 700px; background-color: #CCC;\">");
		respuesta.println("											<caption>");
		respuesta.println("												<strong>Opciones</strong></caption>");
		respuesta.println("										<table class=\"table table-striped\">");
		respuesta.println( "<thead>");
		respuesta.println( "                <tr>");
		respuesta.println( "                  <th>idStore</th>");
		respuesta.println( "                  <th>Capacity</th>");
		respuesta.println( "                  <th>type</th>");
		respuesta.println( "                  <th>Email</th>");
		respuesta.println( "                  <th>Orders</th>");
		respuesta.println( "                </tr>");
		respuesta.println( " </thead>");
		respuesta.println("											<tbody>");	

		for (int i=0; i<locales.size(); i++){
			LocalValue pedido = locales.get(i);

			respuesta.println("												<tr>");
			respuesta.println("													<td>" + pedido.idLocal +"</a></td>");
			respuesta.println("													<td>" + pedido.capacidad + "</td>");
			respuesta.println("													<td>" + pedido.tipoProducto+ "</td>");
			respuesta.println("													<td>" + pedido.dirElectronica+ "</td>");
			respuesta.println("		<td>");
			respuesta.println("				<FORM method=post action=\"PedidosDeLocalBusqueda.htm\">");
			respuesta.println("				 	<INPUT type=\"hidden\" name=\"idLocal\" value=\""+pedido.idLocal+"\" />");
			respuesta.println("					<INPUT type=\"submit\" value=\"Show Oreders\" />");
			
			
			
			
			
			respuesta.println("				</FORM>");
			respuesta.println("		</td>");
			respuesta.println("												</tr>");
		}

		respuesta.println("											</tbody>");
		respuesta.println("										</table>");
		respuesta.println("            </div>");
	}
	private String  ejecutarAcciones() {

		String producto = request.getParameter("producto");
		String tipo = request.getParameter("tipo");
		if (tipo != null)
		{	

			if(!producto.equals("")){
				locales = mundo.darLocalConMayorVentas(producto, "producto");	
			}
			else
			{
				locales = mundo.darLocalConMayorVentas(tipo, "tipo");	
			}




		}

		String idBodegaNueva = request.getParameter("idBodegaNueva");
		if ( idBodegaNueva!= null)
		{
			String capacidadBodegaNueva = request.getParameter("capacidadBodegaNueva");
			String typeBodegaNueva = request.getParameter("typeBodegaNueva");
			if ( capacidadBodegaNueva != null && typeBodegaNueva!= null)
			{
				return mundo.addBodega(idBodegaNueva, capacidadBodegaNueva, typeBodegaNueva);
			}
			else
			{
				return "Por favor llene todos los datos";
			}	
		}




		return "";


	}

	private void escribirAcciones()
	{
		respuesta.println("<div style=\"border-radius: 10px; box-shadow: 0px 0px 10px 2px #999; width: 700px; background-color: #CCC;\">");
		respuesta.println("		<caption>");
		respuesta.println("				<strong>Search best store of</strong>");
		respuesta.println("		</caption>");
		respuesta.println("			<table class=\"table table-striped\">");
		respuesta.println("<thead>");
		respuesta.println("		<tr>");
		respuesta.println("				<FORM method=post action=\"LocalConMayorVentas.htm\">");
		respuesta.println("				<th> Buscar por producto: </th>");
		respuesta.println("				<th> <INPUT type=\"text\" name=\"producto\" /></th>");
		respuesta.println("				<th><INPUT type=\"submit\" value=\"enviar\" /></th>");
		respuesta.println("				</FORM>");
		respuesta.println("		</tr>");
		respuesta.println("		<tr>");
		respuesta.println("				<FORM method=post action=\"LocalConMayorVentas.htm\">");
		respuesta.println("				<th>Buscar por tipo de producto:</th>");
		respuesta.println("				<th> <select name=\"tipo\">");
		respuesta.println("					<option value=\"P\">Perecedero</option>");
		respuesta.println(" 				<option value=\"N\">No Perecedero</option>");
		respuesta.println("					<option value=\"R\">Refrigerado</option>");
		respuesta.println("					</select>	</th>");
		respuesta.println("				<th><INPUT type=\"submit\" value=\"enviar\" /></th>");
		respuesta.println("				</FORM>");
		respuesta.println("		</tr>");
		respuesta.println("</thead>");
		respuesta.println("			</table>");
		respuesta.println("</div>");


	}
	@Override
	public boolean requiereMenu() {
		// TODO Auto-generated method stub
		return true;
	}

}
