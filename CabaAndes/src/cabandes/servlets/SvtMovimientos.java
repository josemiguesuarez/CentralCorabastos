package cabandes.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import cabandes.complementos.PersonalConverter;
import cabandes.fachada.CabaAndes;
import cabandes.vos.AlmacenajeValue;
import cabandes.vos.MovimientoValue;

public class SvtMovimientos extends ServletTemplate{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<MovimientoValue> movimientos;

	@Override
	public String darTituloPagina() {
		return "Movimientos";
	}

	@Override
	public String darImagenTitulo(HttpServletRequest request) {
		return null;
	}

	@Override
	public void escribirContenido() throws IOException {
		
		movimientos = new ArrayList<MovimientoValue>();
		
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
		respuesta.println("												<strong>Storages</strong></caption>");
		respuesta.println("										<table class=\"table table-striped\">");
		respuesta.println( "<thead>");
		respuesta.println( "                <tr>");
		respuesta.println( "                  <th>Id.</th>");
		respuesta.println( "                  <th>Product</th>");
		respuesta.println( "                  <th>Date</th>");
		respuesta.println( "                  <th>Weight</th>");
		respuesta.println( "                  <th>Cost</th>");
		respuesta.println( "                  <th>Type of transaction</th>");
		respuesta.println( "                  <th>Storage of Entrance</th>");
		respuesta.println( "                  <th>Storage of Delivery</th>");
		respuesta.println( "                </tr>");
		respuesta.println( " </thead>");
		respuesta.println("											<tbody>");
		
		for (int i=0; i<movimientos.size(); i++){
			MovimientoValue movimiento = movimientos.get(i);			
			respuesta.println("												<tr>");
			respuesta.println("													<td>" + movimiento.idMovimiento+"</td>");
			respuesta.println("													<td>" + movimiento.producto +"</td>");
			respuesta.println("													<td>" + movimiento.fecha +"</td>");
			respuesta.println("													<td>" + movimiento.peso +"</td>");
			respuesta.println("													<td>" + movimiento.costo +"</td>");
			respuesta.println("													<td>" + movimiento.tipoDeTransaccion +"</td>");
			respuesta.println("													<td>" + movimiento.almacenajeDeEntrada +"</td>");
			respuesta.println("													<td>" + movimiento.almacenajeDeSalida +"</td>");
			respuesta.println("												</tr>");
		}
		
		respuesta.println("											</tbody>");
		respuesta.println("										</table>");
		respuesta.println("            </div>");
	
	
	}
	private String  ejecutarAcciones() {
		

		if(request.getParameter("busqueda")!=null && !request.getParameter("fecha1").equals("") && !request.getParameter("fecha2").equals(""))
		{
			String fecha1 = request.getParameter("fecha1");
			String fecha2 = request.getParameter("fecha2");
			
			String condicion = "";
			
			String producto = request.getParameter("producto");
			String productoDiferente = request.getParameter("productoDiferente");
			
			String origen = request.getParameter("origen");
			String origenDiferente = request.getParameter("origenDiferente");
			
			String destino =  request.getParameter("destino");
			String destinoDiferente =  request.getParameter("destinoDiferente");
			
			
			if(!producto.equals(""))
			{
				condicion = condicion + " AND nombreproducto LIKE '" + producto + "'";
			}
			if(!productoDiferente.equals(""))
			{
				condicion = condicion + " AND nombreproducto NOT LIKE '" + productoDiferente+"'";
			}
			
			if(!origen.equals(""))
			{
				condicion = condicion + " AND idalmacenajeEntrada =" + origen + " ";
			}
			if(!origenDiferente.equals(""))
			{
				condicion = condicion + " AND idalmacenajeEntrada != " + origenDiferente + " ";
			}
			
			if(!destino.equals(""))
			{
				condicion = condicion + " AND idalmacenajeSalida = " + destino + " ";
			}
			if(!destinoDiferente.equals(""))
			{
				condicion = condicion + " AND idalmacenajeSalida != " + destinoDiferente+" ";
			}
			
			movimientos = mundo.buscarMovimientos(fecha1,fecha2, condicion);
		}
		
		
		
		return "";
	}

	private void escribirAcciones()
	{
		

		respuesta.println("<div style=\"border-radius: 10px; box-shadow: 0px 0px 10px 2px #999; width: 850px; background-color: #CCC;\">");
		respuesta.println("<table class=\"table table-striped\">");
		respuesta.println("<caption>");
		respuesta.println("<strong>Select date interval<strong></caption>");
		respuesta.println("<tbody>");
		respuesta.println("<tr>");
		respuesta.println("<FORM method=get action=\"movimientos.htm\">");
		respuesta.println("<td><label for=\"f_rangeStart\">Initial date:</label> </td>");
		respuesta.println( "		<td><div class=\"input-group date\">");
		respuesta.println( "            <input name =\"fecha1\" type=\"text\" class=\"form-control\"><span class=\"input-group-addon\"><i class=\"glyphicon glyphicon-th\"></i></span>");
		respuesta.println( "        </div></td>");
		//respuesta.println("<td><input type=\"text\" name=\"fecha1\" id=\"fecha1\" /> (YYYY/MM/DD) </td>");
		
		respuesta.println("<td><label for=\"f_rangeStart\">Final date:</label> </td>");
		respuesta.println( "		<td><div class=\"input-group date\">");
		respuesta.println( "            <input name =\"fecha2\" type=\"text\" class=\"form-control\"><span class=\"input-group-addon\"><i class=\"glyphicon glyphicon-th\"></i></span>");
		respuesta.println( "        </div></td>");
		//respuesta.println("<td><input type=\"text\" name=\"fecha2\" id=\"fecha2\" /> (YYYY/MM/DD) ");
		
		respuesta.println("</td>");
	                         
	    respuesta.println("</tr>");
	    respuesta.println("		</tbody>");
	    respuesta.println("		</table>");
	    respuesta.println("		</div>");
		
		
		respuesta.println("            <div style=\"border-radius: 10px; box-shadow: 0px 0px 10px 2px #999; width: 850px; background-color: #CCC;\">");
		respuesta.println("										<table class=\"table table-striped\">");
		respuesta.println("											<caption>");
		respuesta.println("												<strong>Search movements with an \"equals\" condition<strong></caption>");
		respuesta.println("											<tbody>");
		
		respuesta.println("												<tr>");
		respuesta.println("													<td>Product = </td>");
		respuesta.println("													<td> ");
		respuesta.println(" <input type=\"text\" style=\"width:120px\" name=\"producto\" id=\"producto\" value=\"\"  />");
		respuesta.println("													</td> ");
		
		respuesta.println("												<tr>");
		respuesta.println("													<td>Storage of origin =  </td>");
		respuesta.println("													<td> ");
		respuesta.println( "<input type=\"text\" style=\"width:120px\" name=\"origen\" id=\"origen\" value=\"\"  />");
		respuesta.println("													</td> ");
		respuesta.println("												</tr>");
		
		respuesta.println("												<tr>");
		respuesta.println("													<td>Storage of destination = </td>");
		respuesta.println("													<td> ");
		respuesta.println( "<input type=\"text\" style=\"width:120px\" name=\"destino\" id=\"destino\" value=\"\"  />");
		respuesta.println("													</td> ");
		respuesta.println("												</tr>");
		
		respuesta.println("													<td><INPUT type=\"submit\" style=\"width:120px\" name=\"busqueda\" value=\"Search Equals\"></td>");

		respuesta.println("												</tr>");
		respuesta.println("											</tbody>");
		respuesta.println("										</table>");
		respuesta.println("            </div>");
		
		
		respuesta.println("            <div style=\"border-radius: 10px; box-shadow: 0px 0px 10px 2px #999; width: 850px; background-color: #CCC;\">");
		respuesta.println("										<table class=\"table table-striped\">");
		respuesta.println("											<caption>");
		respuesta.println("												<strong>Search movements with a \"different of\" condition<strong></caption>");
		respuesta.println("											<tbody>");
		
		respuesta.println("												<tr>");
		respuesta.println("													<td>Product != </td>");
		respuesta.println("													<td> ");
		respuesta.println(" <input type=\"text\" style=\"width:120px\" name=\"productoDiferente\" id=\"productoDiferente\" value=\"\"  />");
		respuesta.println("													</td> ");
		
		respuesta.println("												<tr>");
		respuesta.println("													<td>Storage of origin !=  </td>");
		respuesta.println("													<td> ");
		respuesta.println( "<input type=\"text\" style=\"width:120px\" name=\"origenDiferente\" id=\"origenDiferente\" value=\"\"  />");
		respuesta.println("													</td> ");
		respuesta.println("												</tr>");
		
		respuesta.println("												<tr>");
		respuesta.println("													<td>Storage of destination = </td>");
		respuesta.println("													<td> ");
		respuesta.println( "<input type=\"text\" style=\"width:120px\" name=\"destinoDiferente\" id=\"destinoDiferente\"  value=\"\"   />");
		respuesta.println("													</td> ");
		respuesta.println("												</tr>");
		
		respuesta.println("													<td><INPUT type=\"submit\" style=\"width:120px\" name=\"busqueda\" value=\"Search Diferent\"></td>");
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
