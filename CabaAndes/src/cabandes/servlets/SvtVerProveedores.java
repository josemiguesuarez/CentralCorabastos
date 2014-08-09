package cabandes.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import cabandes.vos.ProveedorValue;

public class SvtVerProveedores extends ServletTemplate{
	
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
		ArrayList<ProveedorValue> pedidos = mundo.darTodosLosProveedores();
		
		respuesta.println("            <div style=\"border-radius: 10px; box-shadow: 0px 0px 10px 2px #999; width: 850px; background-color: #CCC;\">");
		respuesta.println("											<caption>");
		respuesta.println("												<strong>Opciones</strong></caption>");
		respuesta.println("										<table class=\"table table-striped\">");
		respuesta.println( "<thead>");
		respuesta.println( "                <tr>");
		respuesta.println( "                  <th>User</th>");
		respuesta.println( "                  <th>identification</th>");
		respuesta.println( "                  <th>Company</th>");
		respuesta.println( "                  <th>Phone</th>");
		respuesta.println( "                  <th>Nacionality</th>");
		respuesta.println( "                  <th>Department</th>");
		respuesta.println( "                  <th>ZIP</th>");
		respuesta.println( "                </tr>");
		respuesta.println( " </thead>");
		respuesta.println("											<tbody>");

		for (int i=0; i<pedidos.size(); i++){
			ProveedorValue proveedor = pedidos.get(i);
			String empresa = proveedor.esEmpresa=='T'?"Yes":"No";
			respuesta.println("												<tr>");
			respuesta.println("													<td>" + proveedor.nombreUsuario +" <br/> &nbsp; &nbsp; Email:" + proveedor.dirElectronica +"</td>");
			
			respuesta.println("													<td>" + proveedor.docIdentidad +" <br/>	Login:" + proveedor.login+"</td>");
			respuesta.println("													<td>" + empresa +"</td>");
			respuesta.println("													<td>" + proveedor.telefono +"</td>");
			respuesta.println("													<td>" + proveedor.departamento +"</td>");
			respuesta.println("													<td>" + proveedor.ciudad +", "  + proveedor.nacionalidad +"</td>");
			respuesta.println("													<td>" + proveedor.codPostal +"</td>");
			
			respuesta.println("												</tr>");
		}

		respuesta.println("											</tbody>");
		respuesta.println("										</table>");
		respuesta.println("            </div>");
	}

	@Override
	public boolean requiereMenu() {
		return true;
	}

}
