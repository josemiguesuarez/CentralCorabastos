package cabandes.vos;

import java.util.Date;

public class OfertaDeProveedorValue {
	
	public String dirElectronica;
	public int idLicitacion;
	public String nombreProducto;
	public int cantidad;
	public int presentacion;
	public Date fechaEntrega;
	public boolean efectiva;
	public int costo;
	
	public OfertaDeProveedorValue(String ndirElectronica,
	int nidLicitacion, String nnombreProducto, 
	int ncantidad, int npresentacion, Date nfechaEntrega,
	boolean nefectiva, int ncosto) {
		dirElectronica = ndirElectronica;
		idLicitacion = nidLicitacion;
		nombreProducto = nnombreProducto;
		cantidad = ncantidad;
		presentacion = npresentacion;
		fechaEntrega = nfechaEntrega;
		efectiva = nefectiva;
		costo = ncosto;
	}

}
