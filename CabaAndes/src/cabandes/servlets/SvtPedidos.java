package cabandes.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import cabandes.vos.ItemPedidoDeLocales;
import cabandes.vos.PedidoDeLocalesValue;

public class SvtPedidos extends ServletTemplate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String darTituloPagina() {
		// TODO Auto-generated method stub
		return "Pedidos";
	}

	@Override
	public String darImagenTitulo(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void escribirContenido() throws IOException 
	{
		String idPedidoHacer = request.getParameter("hacerPedido");
		if (idPedidoHacer != null)
		{
			String mensajeMostrar= mundo.satisfacerPedidoHastaDondeSePueda( idPedidoHacer, usuario.dirElectronica);
			respuesta.println("            <div style=\"border-radius: 10px; box-shadow: 0px 0px 10px 2px #999; width: 850px; background-color: #CCC;\">");
			respuesta.println( "  <strong> "+mensajeMostrar+"</strong>");
			respuesta.println( "  </div>");
		}	
		String llego = request.getParameter("llego");
		
		if (llego != null)
		{
			String mensajeMostrar= mundo.registrarLlegadaDePedidoALocal( llego, usuario.dirElectronica);
			respuesta.println("            <div style=\"border-radius: 10px; box-shadow: 0px 0px 10px 2px #999; width: 850px; background-color: #CCC;\">");
			respuesta.println( "  <strong> "+mensajeMostrar+"</strong>");
			respuesta.println( "  </div>");
		}
		
		
		
		escribirFiltros();
		escribirContenidoContinuarRegistro();
		String filtrarSatisfechos = request.getParameter("fSatisfecho");
		
		String filtrarPorFechasInicio = request.getParameter("fInicio");
		String filtrarPorFechasFin = request.getParameter("fFin");
		
		String filtrarPorCostoDesde = request.getParameter("fCostoDesde");
		String filtrarPorCostoHasta = request.getParameter("fCostoHasta");
		
		String filtarPorProductoNombre = request.getParameter("fNombreProducto");
		String filtarPorProductoPresentacion = request.getParameter("fResentacion");
		if (filtrarSatisfechos != null)
		{
			if ( filtrarSatisfechos.equals("Satisfied"))
			{
				escribirPedido("T");
			}
			else if (filtrarSatisfechos.equals("Not Satisfied"))
			{
				escribirPedido("F");
			}
			else
			{
				escribirPedido ("N");
			}
		}
		else if (filtrarPorFechasInicio != null && filtrarPorFechasFin != null)
		{
			String condicion = " FECHA_PEDIDO BETWEEN TO_DATE('"+ filtrarPorFechasInicio+ "', 'yyyy/mm/dd') AND TO_DATE('"+ filtrarPorFechasFin+ "', 'yyyy/mm/dd')";
			escribirPedido(condicion);
		}
		else if (filtrarPorCostoDesde != null && filtrarPorCostoHasta != null)
		{
			try {
				escribirPedido(Integer.parseInt(filtrarPorCostoDesde), Integer.parseInt(filtrarPorCostoHasta));
				
			}
			catch (Exception e)
			{
				escribirPedido ("N");
				escribirPedido("F");
				escribirPedido("T");
			}
		}
		else if (filtarPorProductoNombre != null && filtarPorProductoPresentacion != null)
		{
			try {
				escribirPedido(filtarPorProductoNombre, Integer.parseInt(filtarPorProductoPresentacion));
				
			}
			catch (Exception e)
			{
				escribirPedido ("N");
				escribirPedido("F");
				escribirPedido("T");
			}
			
		}
		
		else
		{
			escribirPedido ("N");
			escribirPedido("F");
			escribirPedido("T");
		}
	}






	

	private void escribirPedido(String filtarPorProductoNombre,int filtarPorProductoPresentacion) {
		ArrayList<PedidoDeLocalesValue>pedidos = mundo.darPedidosDeLocales("IDPEDIDO > -1", usuario.dirElectronica, true);
		respuesta.println("            <div style=\"border-radius: 10px; box-shadow: 0px 0px 10px 2px #999; width: 850px; background-color: #CCC;\">");

		for (int i = 0; i < pedidos.size(); i++) 
		{
			PedidoDeLocalesValue pedido = pedidos.get(i);
			if (pedido.tieneProducto (filtarPorProductoNombre, filtarPorProductoPresentacion))
			{
				respuesta.println("										<table class=\"table table-striped\">");
				respuesta.println("											<caption>");
				String fechaEsperada= (pedido.recibido.equals("T"))?"satisfied":(pedido.recibido.equals("N"))?"new":(pedido.fechaEsperada != null)? "  will be sent on "+pedido.fechaEsperada:"" ;
				respuesta.println("												N. Order: "+ pedido.idPedido+" on "+ pedido.fechaPedido + fechaEsperada+" whit total cost = "+ pedido.costoTotal+"</caption>");

				respuesta.println("											<tbody>");
				ArrayList<ItemPedidoDeLocales> items = pedido.items;
				for (int i1 = 0; i1 < items.size(); i1++) {
					
					ItemPedidoDeLocales item = items.get(i1);
					if ( item.getNombre().equals(filtarPorProductoNombre) && item.getPresentacion() == filtarPorProductoPresentacion)
					{
					respuesta.println("												<tr>");
					respuesta.println("													<td>" + item.cantidad+ "</td>");
					respuesta.println("													<td>" + item.nombreProducto+ "</td>");
					respuesta.println("													<td>" + item.presentacion+ " kg"+"</td>");
					respuesta.println("													<td> $" + item.costoUnit+ "</td>");
					respuesta.println("													<td> Total: $" + item.costoUnit*item.cantidad+ "</td>");
					respuesta.println("												</tr>");
					}
				}
				respuesta.println("											</tbody>");
				respuesta.println("										</table>");
			}
			
		}

		respuesta.println("            </div>");
		
	}

	private void escribirPedido(int costoInicio, int costoFin) {
		ArrayList<PedidoDeLocalesValue>pedidos = mundo.darPedidosDeLocales("IDPEDIDO > -1", usuario.dirElectronica, true);
		respuesta.println("            <div style=\"border-radius: 10px; box-shadow: 0px 0px 10px 2px #999; width: 850px; background-color: #CCC;\">");

		for (int i = 0; i < pedidos.size(); i++) 
		{
			PedidoDeLocalesValue pedido = pedidos.get(i);
			if (pedido.costoTotal < costoFin && pedido.costoTotal > costoInicio)
			{
				respuesta.println("										<table class=\"table table-striped\">");
				respuesta.println("											<caption>");
				String fechaEsperada= (pedido.recibido.equals("T"))?"satisfied":(pedido.recibido.equals("N"))?"new":(pedido.fechaEsperada != null)? "  will be sent on "+pedido.fechaEsperada:"" ;
				respuesta.println("												N. Order: "+ pedido.idPedido+" on "+ pedido.fechaPedido + fechaEsperada+" whit total cost = "+ pedido.costoTotal+"</caption>");

				respuesta.println("											<tbody>");
				ArrayList<ItemPedidoDeLocales> items = pedido.items;
				for (int i1 = 0; i1 < items.size(); i1++) {
					ItemPedidoDeLocales item = items.get(i1);
					respuesta.println("												<tr>");
					respuesta.println("													<td>" + item.cantidad+ "</td>");
					respuesta.println("													<td>" + item.nombreProducto+ "</td>");
					respuesta.println("													<td>" + item.presentacion+ " kg"+"</td>");
					respuesta.println("													<td> $" + item.costoUnit+ "</td>");
					respuesta.println("													<td> Total: $" + item.costoUnit*item.cantidad+ "</td>");
					respuesta.println("												</tr>");
				}
				respuesta.println("											</tbody>");
				respuesta.println("										</table>");
			}
			
		}

		respuesta.println("            </div>");
		
	}

	private void escribirFiltros()
	{
		respuesta.println("            <div style=\"border-radius: 10px; box-shadow: 0px 0px 10px 2px #999; width: 850px; background-color: #CCC;\">");
		respuesta.println("										<table class=\"table table-striped\">");
		respuesta.println("											<caption>");
		respuesta.println("												<strong>Filtros<strong></caption>");
		respuesta.println("											<tbody>");
		
		respuesta.println("												<tr>");
		respuesta.println("<FORM method=post action=\"pedidos.htm\">");
		respuesta.println("													<td>Mostrar: </td>");
		respuesta.println("													<td> ");
		respuesta.println( "     	<select style=\"width:120px\" size=\"1\" name=\"fSatisfecho\" class=\"normal\">");
		respuesta.println( "			<option value=\"Satisfied\">Satisfied</option>");
		respuesta.println( "			<option value=\"Not Satisfied\">Not Satisfied</option>");
		respuesta.println( "			<option value=\"New\">New</option>");
		respuesta.println( "		</select>");
		respuesta.println("													</td> ");
		respuesta.println("													<td><INPUT type=\"submit\" style=\"width:120px\" value=\"Filter\"></td>");
		respuesta.println("</FORM>");
		respuesta.println("												</tr>");
		
		
		respuesta.println("												<tr>");
		respuesta.println("<FORM method=post action=\"pedidos.htm\">");
		respuesta.println("													<td>Rango Fechas (YYYY/MM/DD): </td>");
		respuesta.println("													<td>From= </td>");
		respuesta.println("													<td> ");
		respuesta.println( "<input type=\"text\" style=\"width:120px\" name=\"fInicio\" id=\"fInicio\"   />");
		respuesta.println("													</td> ");
		respuesta.println("													<td>To= </td>");
		respuesta.println("													<td> ");
		respuesta.println( "<input type=\"text\" style=\"width:120px\" name=\"fFin\" id=\"fFin\"  />");
		respuesta.println("													</td> ");
		respuesta.println("													<td><INPUT type=\"submit\" style=\"width:120px\" value=\"Filter\"></td>");
		respuesta.println("</FORM>");
		respuesta.println("												</tr>");
		
		respuesta.println("												<tr>");
		respuesta.println("<FORM method=post action=\"pedidos.htm\">");
		respuesta.println("													<td>Cost: </td>");
		respuesta.println("													<td>From= </td>");
		respuesta.println("													<td> ");
		respuesta.println( "<input type=\"text\" style=\"width:120px\" name=\"fCostoDesde\" id=\"fCostoDesde\"   />");
		respuesta.println("													</td> ");
		respuesta.println("													<td>To= </td>");
		respuesta.println("													<td> ");
		respuesta.println( "<input type=\"text\" style=\"width:120px\" name=\"fCostoHasta\" id=\"fCostoHasta\"  />");
		respuesta.println("													</td> ");
		respuesta.println("													<td><INPUT type=\"submit\" style=\"width:120px\" value=\"Filter\"></td>");
		respuesta.println("</FORM>");
		respuesta.println("												</tr>");
		
		respuesta.println("												<tr>");
		respuesta.println("<FORM method=post action=\"pedidos.htm\">");
		respuesta.println("													<td>Product: </td>");
		respuesta.println("													<td>nombre: </td>");
		respuesta.println("													<td> ");
		respuesta.println( "<input type=\"text\" style=\"width:120px\" name=\"fNombreProducto\" id=\"fNombreProducto\"   />");
		respuesta.println("													</td> ");
		respuesta.println("													<td>Presentation: </td>");
		respuesta.println("													<td> ");
		respuesta.println( "<input type=\"text\" style=\"width:120px\" name=\"fResentacion\" id=\"fResentacion\"  />");
		respuesta.println("													</td> ");
		respuesta.println("													<td><INPUT type=\"submit\" style=\"width:120px\" value=\"Filter\"></td>");
		respuesta.println("</FORM>");
		respuesta.println("												</tr>");
		
		
		respuesta.println("											</tbody>");
		respuesta.println("										</table>");
		respuesta.println("            </div>");
		
		
	}
	private void escribirPedido(String tipo) {

		ArrayList<PedidoDeLocalesValue>pedidos = mundo.darPedidosDeLocales(tipo, usuario.dirElectronica, true);
		respuesta.println("            <div style=\"border-radius: 10px; box-shadow: 0px 0px 10px 2px #999; width: 850px; background-color: #CCC;\">");

		for (int i = 0; i < pedidos.size(); i++) 
		{
			PedidoDeLocalesValue pedido = pedidos.get(i);
			respuesta.println("										<table class=\"table table-striped\">");
			respuesta.println("											<caption>");
			//String fechaEsperada= (pedido.recibido.equals("T"))?"satisfied":(pedido.recibido.equals("N"))?"new":(pedido.fechaEsperada != null)? "  will be sent on "+pedido.fechaEsperada : "" ;
			String fechaEsperada= 
					(pedido.recibido== null && pedido.fechaEsperada != null)? "  will be sent on "+pedido.fechaEsperada:
						(pedido.recibido== null && pedido.fechaEsperada == null)? " ":
							(pedido.recibido.compareTo(new Date())>0)?"  new":"  Satisfied On " + pedido.recibido ;
			
			respuesta.println("												N. Order: "+ pedido.idPedido+" on "+ pedido.fechaPedido + fechaEsperada+" whit total cost = "+ pedido.costoTotal+"</caption>");

			respuesta.println("											<tbody>");
			ArrayList<ItemPedidoDeLocales> items = pedido.items;
			for (int i1 = 0; i1 < items.size(); i1++) {
				ItemPedidoDeLocales item = items.get(i1);
				respuesta.println("												<tr>");
				respuesta.println("													<td>" + item.cantidad+ "</td>");
				respuesta.println("													<td>" + item.nombreProducto+ "</td>");
				respuesta.println("													<td>" + item.presentacion+ " kg"+"</td>");
				respuesta.println("													<td> $" + item.costoUnit+ "</td>");
				respuesta.println("													<td> Total: $" + item.costoUnit*item.cantidad+ "</td>");
				respuesta.println("												</tr>");
			}

			respuesta.println("												<tr>");
			respuesta.println("<FORM method=post action=\"pedidos.htm\">");
			if (tipo.equals("N"))
			{
				respuesta.println("													<td><input type=\"hidden\" name=\"hacerPedido\" id=\"hacerPedido\" value = \""+ pedido.idPedido+"\" readonly=\"readonly\" /></td>");
			}
			else if (tipo.equals("F"))
			{
				respuesta.println("													<td><input type=\"hidden\" name=\"llego\" id=\"llego\" value = \""+ pedido.idPedido+"\" readonly=\"readonly\" /></td>");
			}
			respuesta.println("													<td></td>");
			respuesta.println("													<td></td>");
			respuesta.println("													<td></td>");
			if (tipo.equals("N"))
			{
				respuesta.println("													<td><INPUT type=\"submit\" style=\"width:120px\" value=\"Order\"></td>");
			}
			else if (tipo.equals("F"))
			{
				respuesta.println("													<td><INPUT type=\"submit\" style=\"width:120px\" value=\"Oreder has arrived\"></td>");
			}
			
			respuesta.println("</FORM>");
			respuesta.println("												</tr>");


			respuesta.println("											</tbody>");
			respuesta.println("										</table>");
		}

		respuesta.println("            </div>");

	}

	private void escribirContenidoContinuarRegistro() {
		String estado = request.getParameter("load");
		respuesta.println("            <div style=\"border-radius: 10px; box-shadow: 0px 0px 10px 2px #999; width: 850px; background-color: #CCC;\">");

		respuesta.println("										<table class=\"table table-striped\">");
		respuesta.println( "<thead>");
		respuesta.println( "                <tr>");
		respuesta.println( "                  <th>Type</th>");
		respuesta.println( "                  <th>Product</th>");
		respuesta.println( "                  <th>Presentation</th>");
		respuesta.println( "                  <th>Amount</th>");
		respuesta.println( "                  <th>Confirm</th>");
		respuesta.println( "                </tr>");
		respuesta.println( " </thead>");
		respuesta.println("											<tbody>");
		respuesta.println("												<tr>");
		if (estado==null)
		{
			mostrarSeleccionPrdocucto();

		}
		else if (estado.equals("productos"))
		{
			String tipoSeleccionado = request.getParameter("type");

			respuesta.println("<FORM method=post action=\"pedidos.htm\">");
			respuesta.println( "<input type=\"hidden\" name=\"load\" id=\"load\" value = \"presentaciones\" readonly=\"readonly\" />");
			respuesta.println("	<td>");
			respuesta.println( "<input type=\"text\" style=\"width:120px\" name=\"type\" id=\"type\" value = \"" + tipoSeleccionado + "\" readonly=\"readonly\" />");
			respuesta.println("	</td>");

			respuesta.println("	<td>");
			respuesta.println( "  	<div class=\"controls\">");
			respuesta.println( "     	<select style=\"width:120px\" size=\"1\" name=\"producto\" class=\"normal\">");
			ArrayList<String> lista = mundo.darProductosDeTipo(tipoSeleccionado);
			for (int i = 0; i < lista.size(); i++) {
				respuesta.println( "			<option value=\""+ lista.get(i)+ "\">"+ lista.get(i)+ "</option>");
			}

			respuesta.println( "		</select>");
			respuesta.println( "  </div>");
			respuesta.println("	</td>");
			respuesta.println("	<td>");
			respuesta.println("<INPUT type=\"submit\" style=\"width:120px\" value=\"Load\">");
			respuesta.println("	</td>");
			respuesta.println("</FORM>");
		}
		else if (estado.equals("presentaciones"))
		{
			String tipoSeleccionado = request.getParameter("type");
			String productoSeleccionado = request.getParameter("producto");

			respuesta.println("<FORM method=post action=\"pedidos.htm\">");
			respuesta.println( "<input type=\"hidden\" name=\"load\" id=\"load\" value = \"buy\" readonly=\"readonly\" />");

			respuesta.println("	<td>");
			respuesta.println( "<input type=\"text\" style=\"width:120px\"  name=\"type\" id=\"type\" value = \"" + tipoSeleccionado + "\" readonly=\"readonly\" />");
			respuesta.println("	</td>");
			respuesta.println("	<td>");
			respuesta.println( "<input type=\"text\" style=\"width:120px\" name=\"producto\" id=\"producto\" value = \"" + productoSeleccionado + "\" readonly=\"readonly\" />");
			respuesta.println("	</td>");

			respuesta.println("	<td>");
			respuesta.println( "  	<div class=\"controls\">");
			respuesta.println( "     	<select style=\"width:120px\" size=\"1\" name=\"presentaciones\" class=\"normal\">");
			ArrayList<String> lista = mundo.darPresentProducto(productoSeleccionado);
			for (int i = 0; i < lista.size(); i++) {
				respuesta.println( "			<option value=\""+ lista.get(i)+ "\">"+ lista.get(i)+ "</option>");
			}

			respuesta.println( "		</select>");

			respuesta.println("	<td>");
			respuesta.println( "<input type=\"text\" size=\"4\" name=\"cantidad\" id=\"cantidad\" />");
			respuesta.println("	</td>");

			respuesta.println( "  </div>");
			respuesta.println("	</td>");
			respuesta.println("	<td>");
			respuesta.println("<INPUT type=\"submit\" value=\"Add\">");
			respuesta.println("	</td>");
			respuesta.println("</FORM>");
		}
		else if (estado.equals("buy") )
		{
			String type = request.getParameter("type");
			String product = request.getParameter("producto");
			String presentation = request.getParameter("presentaciones");
			String amount = request.getParameter("cantidad");
			mundo.registrarPedidio(type, product, presentation, amount, usuario.dirElectronica);
			mostrarSeleccionPrdocucto();
			//			
		}


		respuesta.println("												</tr>");

		respuesta.println("											</tbody>");
		respuesta.println("										</table>");



		respuesta.println("            </div>");
	}

	private void mostrarSeleccionPrdocucto() {
		respuesta.println("<FORM method=post action=\"pedidos.htm\">");
		respuesta.println( "<input type=\"hidden\" name=\"load\" id=\"load\" value = \"productos\" readonly=\"readonly\" />");
		respuesta.println("	<td>");
		respuesta.println( "  	<div class=\"controls\">");
		respuesta.println( "     	<select style=\"width:120px\" size=\"1\" name=\"type\" class=\"normal\">");
		respuesta.println( "			<option value=\"Perishable\">Perishable</option>");
		respuesta.println( "			<option value=\"Not Perishable\">Not Perishable</option>");
		respuesta.println( "			<option value=\"Refrigerated\">Refrigerated</option>");
		respuesta.println( "		</select>");
		respuesta.println( "  </div>");
		respuesta.println("	</td>");

		respuesta.println("	<td>");
		respuesta.println("<INPUT type=\"submit\" value=\"Load\">");
		respuesta.println("	</td>");
		respuesta.println("</FORM>");
	}

	@Override
	public boolean requiereMenu() {
		// TODO Auto-generated method stub
		return true;
	}

}
