package cabandes.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import cabandes.vos.PedidoDeLocalesValue;

public class SvtVerPedidosDeUnLocal extends ServletTemplate{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<PedidoDeLocalesValue> pedidosDeLocal;

	private String idLocal;

	@Override
	public String darTituloPagina() {
		return "Pedidos De Local";
	}

	@Override
	public String darImagenTitulo(HttpServletRequest request) {
		return null;
	}

	@Override
	public void escribirContenido() throws IOException {
		pedidosDeLocal = new ArrayList<PedidoDeLocalesValue>();
		
		respuesta.println("            <div style=\"border-radius: 10px; box-shadow: 0px 0px 10px 2px #999; width: 850px; background-color: #CCC;\">");
		respuesta.println("												<strong>"
		+ ejecutarAcciones () +"</strong>");
		respuesta.println("            </div>");
		
		
		escribirAcciones();
		
		escribirDatos();
		
	}

	private void escribirDatos() {
		
		respuesta.println("            <div style=\"border-radius: 10px; box-shadow: 0px 0px 10px 2px #999; width: 850px; background-color: #CCC;\">");
		respuesta.println("											<caption>");
		respuesta.println("												<strong>Orders</strong></caption>");
		respuesta.println("										<table class=\"table table-striped\">");
		respuesta.println( "<thead>");
		respuesta.println( "                <tr>");
		respuesta.println( "                  <th>Id.</th>");
		respuesta.println( "                  <th>Order Date</th>");
		respuesta.println( "                  <th>Hope Date</th>");
		respuesta.println( "                  <th>Received On</th>");
		respuesta.println( "                  <th>Total Cost</th>");
		respuesta.println( "                </tr>");
		respuesta.println( " </thead>");
		respuesta.println("											<tbody>");
		
		for (int i=0; i<pedidosDeLocal.size(); i++){
			PedidoDeLocalesValue pedido1 = pedidosDeLocal.get(i);			
			respuesta.println("												<tr>");
			respuesta.println("													<td>" + pedido1.idPedido +"</td>");
			respuesta.println("													<td>" + pedido1.fechaPedido +"</td>");
			respuesta.println("													<td>" + ((pedido1.fechaEsperada== null)? "soon": pedido1.fechaEsperada) +"</td>");
			respuesta.println("													<td>" + ((pedido1.recibido == null)? "Pending": pedido1.recibido)+"</td>");
			respuesta.println("													<td>" + pedido1.costoTotal +"</td>");
			respuesta.println("												</tr>");
		}
		
		respuesta.println("											</tbody>");
		respuesta.println("										</table>");
		respuesta.println("            </div>");
	}
	private String  ejecutarAcciones() {
		
		idLocal = request.getParameter("idLocal");
		String fInic = request.getParameter("fInic");
		String	fFin = request.getParameter("fFin");
		System.out.println(idLocal);
		if (idLocal != null && fInic != null && fFin != null)
		{
			pedidosDeLocal = mundo.darPedidosDeLocal(idLocal, fInic, fFin);
			
		}
		else
		{
			return "Please, select a start date and end date";
		}
		
		return "";
		
		
	}

	private void escribirAcciones()
	{
		respuesta.println("            <div style=\"border-radius: 10px; box-shadow: 0px 0px 10px 2px #999; width: 850px; background-color: #CCC;\">");
		respuesta.println("										<table class=\"table table-striped\">");
		respuesta.println("											<caption>");
		respuesta.println("												<strong>Show Active Orders In A Period Time <strong></caption>");
		respuesta.println("											<tbody>");
		
		respuesta.println("												<tr>");
		
		respuesta.println("<FORM method=post action=\"PedidosDeLocalBusqueda.htm\">");
		
		respuesta.println("													<td>FROM: </td>");
		respuesta.println("				 	<INPUT type=\"hidden\" name=\"idLocal\" value=\""+ idLocal +"\" />");
		respuesta.println("													<td> ");
		respuesta.println( "		<div class=\"input-group date\">");
		respuesta.println( "            <input name =\"fInic\" type=\"text\" class=\"form-control\"><span class=\"input-group-addon\"><i class=\"glyphicon glyphicon-th\"></i></span>");
		respuesta.println( "        </div>");
		respuesta.println("													</td> ");
		respuesta.println("													<td>TO: </td>");
		respuesta.println("													<td> ");
		respuesta.println( "		<div class=\"input-group date\">");
		respuesta.println( "            <input name =\"fFin\" type=\"text\" class=\"form-control\"><span class=\"input-group-addon\"><i class=\"glyphicon glyphicon-th\"></i></span>");
		respuesta.println( "        </div>");
		respuesta.println("													</td> ");
		
		respuesta.println("													<td><INPUT type=\"submit\" style=\"width:120px\" value=\"Show\"></td>");
		respuesta.println("</FORM>");
		respuesta.println("												</tr>");
		respuesta.println("											</tbody>");
		respuesta.println("										</table>");
		respuesta.println("            </div>");
		
		
		
		
		
	}

	@Override
	public boolean requiereMenu() {
		return true;
	}



}
