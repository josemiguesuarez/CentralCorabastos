package cabandes.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import cabandes.dao.ConsultaDAO;
import cabandes.vos.PedidoAProveedorValue;

public class SvtPedidosAProvs extends ServletTemplate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String darTituloPagina() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String darImagenTitulo(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void escribirContenido() throws IOException {
		
		ArrayList<PedidoAProveedorValue> pedidos = mundo.darPedidosAProveedoresCerradosSinAsignar();
		
		respuesta.println("            <div style=\"border-radius: 10px; box-shadow: 0px 0px 10px 2px #999; width: 700px; background-color: #CCC;\">");
		respuesta.println("											<caption>");
		respuesta.println("												<strong>Opciones</strong></caption>");
		respuesta.println("										<table class=\"table table-striped\">");
		respuesta.println( "<thead>");
		respuesta.println( "                <tr>");
		respuesta.println( "                  <th><a href=\"existencias.htm?ord=" + ConsultaDAO.ITEMS_JOIN_nombreProducto + "\">Licitation No.</a></th>");
		respuesta.println( "                  <th><a href=\"existencias.htm?ord=" + ConsultaDAO.ITEMS_JOIN_tipo + "\">Name</a></th>");
		respuesta.println( "                  <th><a href=\"existencias.htm?ord=" + ConsultaDAO.ITEMS_JOIN_presentacion + "\">Amount</a></th>");
		respuesta.println( "                  <th><a href=\"existencias.htm?ord=" + ConsultaDAO.ITEMS_JOIN_fecha_expiracion + "\">Presentation</a></th>");
		respuesta.println( "                  <th><a href=\"existencias.htm?ord=" + ConsultaDAO.ITEMS_JOIN_idAlmacenaje + "\">Expected date</a></th>");
		respuesta.println( "                  <th><a href=\"existencias.htm?ord=" + ConsultaDAO.ITEMS_JOIN_tipoalmacenaje + "\">Publish Date</a></th>");
		respuesta.println( "                </tr>");
		respuesta.println( " </thead>");
		respuesta.println("											<tbody>");	
		
		for (int i=0; i<pedidos.size(); i++){
			PedidoAProveedorValue pedido = pedidos.get(i);
			
			respuesta.println("												<tr>");
			respuesta.println("													<td><a href=\"licitaciones.htm?id="+pedido.idLicitacion+"\">"+pedido.idLicitacion +"</a></td>");
			respuesta.println("													<td>" + pedido.nombreProducto + "</td>");
			respuesta.println("													<td>" + Integer.toString(pedido.cantidad)+ "</td>");
			respuesta.println("													<td>" + Integer.toString(pedido.presentacion)+ "</td>");
			respuesta.println("													<td>" + pedido.fechaEsperada.toString() + "</td>");
			respuesta.println("													<td>" + pedido.fechaPublicacion.toString()+ "</td>");
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
