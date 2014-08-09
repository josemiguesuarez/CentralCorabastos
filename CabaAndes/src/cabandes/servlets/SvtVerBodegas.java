package cabandes.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import cabandes.complementos.PersonalConverter;
import cabandes.vos.AlmacenajeValue;

public class SvtVerBodegas extends ServletTemplate{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<AlmacenajeValue> bodegas;

	@Override
	public String darTituloPagina() {
		return "Bodegas";
	}

	@Override
	public String darImagenTitulo(HttpServletRequest request) {
		return null;
	}

	@Override
	public void escribirContenido() throws IOException {
		bodegas = mundo.darBodegas();
		
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
		respuesta.println( "                  <th>name</th>");
		respuesta.println( "                  <th>Capacity</th>");
		respuesta.println( "                  <th>Used Capacity</th>");
		respuesta.println( "                  <th>Type of product</th>");
		respuesta.println( "                  <th>Email Address</th>");
		respuesta.println( "                  <th>Actions</th>");
		respuesta.println( "                </tr>");
		respuesta.println( " </thead>");
		respuesta.println("											<tbody>");
		
		for (int i=0; i<bodegas.size(); i++){
			AlmacenajeValue bodega = bodegas.get(i);			
			respuesta.println("												<tr>");
			respuesta.println("													<td>" + bodega.idAlmacenaje +"</td>");
			respuesta.println("													<td>" + bodega.nombreAlmacenaje +"</td>");
			respuesta.println("													<td>" + bodega.capacidad +"</td>");
			respuesta.println("													<td>" + bodega.capacidadUsada +"</td>");
			respuesta.println("													<td>" + PersonalConverter.TypetoString(bodega.tipo) +"</td>");
			respuesta.println("													<td>" + bodega.dirElectronica +"</td>");
			respuesta.println("													<td><a href=\"bodegas.htm?idBodegaE="+bodega.idAlmacenaje+"\"> delete</a></td>");
			respuesta.println("												</tr>");
		}
		
		respuesta.println("											</tbody>");
		respuesta.println("										</table>");
		respuesta.println("            </div>");
	}
	private String  ejecutarAcciones() {
		
		String idBodegaAEliminar = request.getParameter("idBodegaE");
		if (idBodegaAEliminar != null)
		{
			return mundo.removeBodega(idBodegaAEliminar);
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
		
		String porcentaje = request.getParameter("porcentajeDeBusqueda");
		String tipoBodega = request.getParameter("typeBodegaBusqueda");
		if ( porcentaje!= null || tipoBodega != null)
		{	
			Double porcentajeDouble;
			if ( porcentaje == null )
			{
				return "Fill the text field for percentage";
			}
			if ( tipoBodega == null)
			{
				return "Pleas select one type of storage";
			}
			try { porcentajeDouble = PersonalConverter.limpiarParaUtilizarComoDouble(porcentaje, PersonalConverter.CLASE_DATO_PORCENTAJE_0_1);
			} catch (Exception e1) {
				return e1.getMessage();
			}
			bodegas = mundo.darBodegasPorTipoYPorcentaje( tipoBodega, porcentajeDouble);
					
			
		}
		
		
		
		return "";
		
		
	}

	private void escribirAcciones()
	{
		respuesta.println("            <div style=\"border-radius: 10px; box-shadow: 0px 0px 10px 2px #999; width: 850px; background-color: #CCC;\">");
		respuesta.println("										<table class=\"table table-striped\">");
		respuesta.println("											<caption>");
		respuesta.println("												<strong>Show storages by capacity<strong></caption>");
		respuesta.println("											<tbody>");
		
		respuesta.println("												<tr>");
		respuesta.println("<FORM method=post action=\"bodegas.htm\">");
		
		respuesta.println("													<td>Capacity (Percentage): </td>");
		respuesta.println("													<td> ");
		respuesta.println( "<input type=\"text\" style=\"width:120px\" name=\"porcentajeDeBusqueda\" id=\"porcentajeDeBusqueda\"   />");
		respuesta.println("													</td> ");
		
		respuesta.println("													<td>Type: </td>");
		respuesta.println("													<td> ");
		respuesta.println( "     	<select style=\"width:120px\" size=\"1\" name=\"typeBodegaBusqueda\" class=\"normal\">");
		respuesta.println( "			<option value=\"P\">Perishable</option>");
		respuesta.println( "			<option value=\"N\">Not Perishable</option>");
		respuesta.println( "			<option value=\"R\">Refrigerated</option>");
		respuesta.println( "		</select>");
		respuesta.println("													</td> ");
		
		
		respuesta.println("													<td><INPUT type=\"submit\" style=\"width:120px\" value=\"Show\"></td>");
		respuesta.println("</FORM>");
		respuesta.println("												</tr>");
		respuesta.println("											</tbody>");
		respuesta.println("										</table>");
		respuesta.println("            </div>");
		
		
		respuesta.println("            <div style=\"border-radius: 10px; box-shadow: 0px 0px 10px 2px #999; width: 850px; background-color: #CCC;\">");
		respuesta.println("										<table class=\"table table-striped\">");
		respuesta.println("											<caption>");
		respuesta.println("												<strong>Add a new storage<strong></caption>");
		respuesta.println("											<tbody>");
		
		respuesta.println("												<tr>");
		respuesta.println("<FORM method=post action=\"bodegas.htm\">");
		respuesta.println("													<td>Name Storage: </td>");
		respuesta.println("													<td> ");
		respuesta.println( "<input type=\"text\" style=\"width:120px\" name=\"idBodegaNueva\" id=\"idBodegaNueva\"   />");
		respuesta.println("													</td> ");
		
		respuesta.println("													<td>Capacity: </td>");
		respuesta.println("													<td> ");
		respuesta.println( "<input type=\"text\" style=\"width:120px\" name=\"capacidadBodegaNueva\" id=\"capacidadBodegaNueva\"   />");
		respuesta.println("													</td> ");
		
		respuesta.println("													<td>Type: </td>");
		respuesta.println("													<td> ");
		respuesta.println( "     	<select style=\"width:120px\" size=\"1\" name=\"typeBodegaNueva\" class=\"normal\">");
		respuesta.println( "			<option value=\"P\">Perishable</option>");
		respuesta.println( "			<option value=\"N\">Not Perishable</option>");
		respuesta.println( "			<option value=\"R\">Refrigerated</option>");
		respuesta.println( "		</select>");
		respuesta.println("													</td> ");
		
		
		respuesta.println("													<td><INPUT type=\"submit\" style=\"width:120px\" value=\"Add\"></td>");
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
