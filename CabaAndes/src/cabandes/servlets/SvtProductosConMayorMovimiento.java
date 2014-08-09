package cabandes.servlets;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import cabandes.vos.MovimientoNumVentasSolicitudesValue;

public class SvtProductosConMayorMovimiento extends ServletTemplate {

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
		
		if(!(request.getParameter("fecha1") != null))
		{
			respuesta.println("<div style=\"border-radius: 10px; box-shadow: 0px 0px 10px 2px #999; width: 700px; background-color: #CCC;\">");
			respuesta.println("		<caption>");
			respuesta.println("				<strong>Ingreso de fechas</strong>");
			respuesta.println("		</caption>");
			respuesta.println("			<table class=\"table table-striped\">");
			respuesta.println("<thead>");
			respuesta.println("		<tr>");
			respuesta.println("				<FORM method=post action=\"ProductoMayorMovimiento.htm\">");
			respuesta.println("				<th> Primera fecha:</th>");
			//respuesta.println("				<th> <INPUT type=\"text\" name=\"fecha1\" /><FONT SIZE=1>(YYYY/MM/DD)</FONT SIZE=1></th>");
			respuesta.println( "<th><input id=\"fecha1\" type=\"text\" name=\"fecha1\" size=\"25\"><a href=\"javascript:NewCal('fecha1','yyyymmdd')\">");
			respuesta.println( "<img src=\"img/cal.gif\" width=\"16\" height=\"16\" border=\"0\" alt=\"Pick a date\"></a><FONT SIZE=1>(YYYY/MM/DD)</FONT SIZE=1></th>");
			respuesta.println("		</tr>");
			respuesta.println("		<tr>");
			respuesta.println("				<th>Segunda fecha:</th>");
			respuesta.println("				<th><INPUT type=\"text\" name=\"fecha2\" /><FONT SIZE=1>(YYYY/MM/DD)</FONT SIZE=1></th>");
			respuesta.println("		</tr>");
			respuesta.println("		<tr>");
			respuesta.println("				<th><INPUT type=\"submit\" value=\"enviar\" /></th>");
			respuesta.println("		</tr>");
			respuesta.println("</thead>");
			respuesta.println("			</table>");
			respuesta.println("</div>");
		}
		else
		{	
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			Date fecha1 = new Date();
			Date fecha2 = new Date();
			try
			{
				fecha1 = dateFormat.parse(request.getParameter("fecha1"));
				fecha2 = dateFormat.parse(request.getParameter("fecha2"));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			ArrayList<MovimientoNumVentasSolicitudesValue> pedidos = mundo.darProductosConMayorMovimientos(fecha1, fecha2);

			respuesta.println("            <div style=\"border-radius: 10px; box-shadow: 0px 0px 10px 2px #999; width: 700px; background-color: #CCC;\">");
			respuesta.println("											<caption>");
			respuesta.println("												<strong>Opciones</strong></caption>");
			respuesta.println("										<table class=\"table table-striped\">");
			respuesta.println( "<thead>");
			respuesta.println( "                <tr>");
			respuesta.println( "                  <th>Nombre</th>");
			respuesta.println( "                  <th>Tipo</th>");
			respuesta.println( "                  <th>Presentation</th>");
			respuesta.println( "                  <th>Peso Promedio</th>");
			respuesta.println( "                  <th>Costo Promedio</th>");
			respuesta.println( "                  <th>Veces Solicitado</th>");
			respuesta.println( "                  <th>Veces Vendido</th>");
			respuesta.println( "                </tr>");
			respuesta.println( " </thead>");
			respuesta.println("											<tbody>");	

			for (int i=0; i<pedidos.size(); i++){
				MovimientoNumVentasSolicitudesValue pedido = pedidos.get(i);

				respuesta.println("												<tr>");
				respuesta.println("													<td>" + pedido.nombre +"</a></td>");
				respuesta.println("													<td>" + pedido.tipo + "</td>");
				respuesta.println("													<td>" + pedido.presentacion+ "</td>");
				respuesta.println("													<td>" + pedido.pesoPromedio+ "</td>");
				respuesta.println("													<td>" + pedido.costoPromedio+ "</td>");
				respuesta.println("													<td>" + pedido.vecesSolicitado+ "</td>");
				respuesta.println("													<td>" + pedido.vecesVendido+ "</td>");
				respuesta.println("												</tr>");
			}

			respuesta.println("											</tbody>");
			respuesta.println("										</table>");
			respuesta.println("            </div>");
		}
	}

	@Override
	public boolean requiereMenu() {
		// TODO Auto-generated method stub
		return true;
	}

}
