package cabandes.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import cabandes.dao.ConsultaDAO;
import cabandes.vos.OfertaDeProveedorValue;

public class SvtOfertasDeProveedores extends ServletTemplate
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String darTituloPagina() {
		return null;
	}

	@Override
	public String darImagenTitulo(HttpServletRequest request) {
		return null;
	}

	@Override
	public void escribirContenido() throws IOException {
		ArrayList<OfertaDeProveedorValue> pedidos = mundo.darOfertasDeProveedores(request.getParameter("id"));
		
		respuesta.println("<FORM method=post action=\"PrincipalCMNUser.htm\">");
		respuesta.println("            <div style=\"border-radius: 10px; box-shadow: 0px 0px 10px 2px #999; width: 850px; background-color: #CCC;\">");
		respuesta.println("											<caption>");
		respuesta.println("												<strong>Opciones</strong></caption>");
		respuesta.println("										<table class=\"table table-striped\">");
		respuesta.println( "<thead>");
		respuesta.println( "                <tr>");
		respuesta.println( "                  <th><a href=\"existencias.htm?ord=" + ConsultaDAO.ITEMS_JOIN_nombreProducto + "\">Proveedor</a></th>");
		respuesta.println( "                  <th><a href=\"existencias.htm?ord=" + ConsultaDAO.ITEMS_JOIN_tipo + "\">Name</a></th>");
		respuesta.println( "                  <th><a href=\"existencias.htm?ord=" + ConsultaDAO.ITEMS_JOIN_presentacion + "\">Weight (Kg)</a></th>");
		respuesta.println( "                  <th><a href=\"existencias.htm?ord=" + ConsultaDAO.ITEMS_JOIN_fecha_expiracion + "\">Presentation</a></th>");
		respuesta.println( "                  <th><a href=\"existencias.htm?ord=" + ConsultaDAO.ITEMS_JOIN_idAlmacenaje + "\">Delivery Date</a></th>");
		respuesta.println( "                  <th><a href=\"existencias.htm?ord=" + ConsultaDAO.ITEMS_JOIN_idAlmacenaje + "\">Total Cost</a></th>");
		respuesta.println( "                </tr>");
		respuesta.println( " </thead>");
		respuesta.println("											<tbody>");

		for (int i=0; i<pedidos.size(); i++){
			OfertaDeProveedorValue pedido = pedidos.get(i);

			respuesta.println("												<tr>");
			respuesta.println("													<td>" + pedido.dirElectronica +"</td>");
			respuesta.println("													<td>" + pedido.nombreProducto + "</td>");
			respuesta.println("													<td>" + Integer.toString(pedido.cantidad)+ "</td>");
			respuesta.println("													<td>" + Integer.toString(pedido.presentacion)+ "</td>");
			respuesta.println("													<td>" + pedido.fechaEntrega+ "</td>");
			respuesta.println("													<td>" + pedido.costo+ "</td>");
			respuesta.println("													<td> <INPUT type=radio value=\""+pedido.dirElectronica+"\"name=\"OfertaDeProveedor\" /></td>");
			respuesta.println("												</tr>");
		}

		respuesta.println("											</tbody>");
		respuesta.println("										</table>");
		respuesta.println("            </div>");
		respuesta.println("<INPUT type=hidden value=\""+request.getParameter("id")+"\"name=\"id\" />");
		respuesta.println("<INPUT type=\"submit\" value=\"Enviar\">");
		respuesta.println("</FORM>");
	}

	@Override
	public boolean requiereMenu() {
		return true;
	}

}
