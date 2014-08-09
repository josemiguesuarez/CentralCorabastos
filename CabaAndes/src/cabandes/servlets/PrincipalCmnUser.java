package cabandes.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import cabandes.dao.ConsultaDAO;
import cabandes.vos.ItemValue;

public class PrincipalCmnUser extends ServletTemplate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String darTituloPagina() {
		// TODO Auto-generated method stub
		return "Principal | User";
	}

	@Override
	public String darImagenTitulo(HttpServletRequest request) {
		return null;
	}

	@Override
	public void escribirContenido() throws IOException {
		
		//en caso de que antes se estaba en la seleccion de un proveedor
		if(request.getParameter("OfertaDeProveedor")!=null)
		{
			mundo.cambiarEstadoLicitacion(request.getParameter("OfertaDeProveedor"), request.getParameter("id"));
		}
		
		ArrayList<ItemValue> items = mundo.darItemsExistentes(ConsultaDAO.ITEMS_JOIN_nombreProducto);
		respuesta.println("            <div style=\"border-radius: 10px; box-shadow: 0px 0px 10px 2px #999; width: 850px; background-color: #CCC;\">");
    	respuesta.println("											<caption>");
		respuesta.println("												<strong>Opciones</strong></caption>");
		respuesta.println("										<table class=\"table table-striped\">");
		respuesta.println( "<thead>");
		respuesta.println( "                <tr>");
		respuesta.println( "                  <th><a href=\"existencias.htm?ord=" + ConsultaDAO.ITEMS_JOIN_nombreProducto + "\">Name</a></th>");
		respuesta.println( "                  <th><a href=\"existencias.htm?ord=" + ConsultaDAO.ITEMS_JOIN_tipo + "\">Type</a></th>");
		respuesta.println( "                  <th><a href=\"existencias.htm?ord=" + ConsultaDAO.ITEMS_JOIN_presentacion + "\">Weight (kg)</a></th>");
		respuesta.println( "                  <th><a href=\"existencias.htm?ord=" + ConsultaDAO.ITEMS_JOIN_fecha_expiracion + "\">Exp. Date</a></th>");
		respuesta.println( "                  <th><a href=\"existencias.htm?ord=" + ConsultaDAO.ITEMS_JOIN_idAlmacenaje + "\">Storage</a></th>");
		respuesta.println( "                  <th><a href=\"existencias.htm?ord=" + ConsultaDAO.ITEMS_JOIN_tipoalmacenaje + "\">Storage type</a></th>");
		respuesta.println( "                  <th><a href=\"existencias.htm?ord=" + ConsultaDAO.ITEMS_JOIN_cantidad + "\">amount</a></th>");
		respuesta.println( "                </tr>");
		respuesta.println( " </thead>");
		respuesta.println("											<tbody>");
		for (int i = 0; i < items.size(); i++) {
			ItemValue item = items.get(i);
			respuesta.println("												<tr>");
			respuesta.println("													<td>" + item.nombreProducto+ "</td>");
			respuesta.println("													<td>" + item.tipo+ "</td>");
			respuesta.println("													<td>" + item.presentacion+ "</td>");
			respuesta.println("													<td>" + item.fecha_expiracion+ "</td>");
			respuesta.println("													<td>" + item.idAlmacenaje+ "</td>");
			respuesta.println("													<td>" + item.tipoalmacenaje+ "</td>");
			respuesta.println("													<td>" + item.cantidad+ "</td>");
			respuesta.println("												</tr>");
		}
		
		respuesta.println("											</tbody>");
		respuesta.println("										</table>");
		respuesta.println("            </div>");

	}

	@Override
	public boolean requiereMenu() {
		// TODO Auto-generated method stub
		return true;
	}

}
