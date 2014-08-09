package cabandes.vos;

import java.util.Date;

public class PedidoAProveedorValue {
	
	public String idLicitacion; 
	public int  cantidad;
	public String nombreProducto;
	public int presentacion; 
	public String proveedorEscogido; 
	public Date fechaEsperada;
	public Date fechaPublicacion;
	public Date fechaDeCierre;
	
	public PedidoAProveedorValue(String licitacion, int nCantidad, 
			String nNombreProducto,int nPresentacion, 
			String nProveedorEscogido, Date nFechaEsperada, 
			Date nFechaPublicacion, Date nFechaDeCierre) 
	{
		idLicitacion = licitacion; 
		cantidad = nCantidad;
		nombreProducto = nNombreProducto;
		presentacion = nPresentacion;
		proveedorEscogido = nProveedorEscogido;
		fechaEsperada = nFechaEsperada;
		fechaPublicacion = nFechaPublicacion;
		fechaDeCierre = nFechaDeCierre;
	}
	
	

	
}
