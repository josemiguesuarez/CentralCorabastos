package cabandes.vos;

import java.util.Date;

public class ItemValue implements IItemVal
{
	public String nombreProducto; 
	public String tipo;
	public int presentacion;
	public Date fecha_expiracion; 
	public int idAlmacenaje; 
	public int cantidad;
	public String tipoalmacenaje;
	public double promocion;
	public int costo;
	
	public ItemValue(String nombreProductoP, String tipoP, int presentacionP, Date fecha_expiracionP, int idAlmacenajeP, int cantidadP, String tipoalmacenajeP, double promocionP, int costoP)
	{
		nombreProducto=nombreProductoP;
		tipo=tipoP;
		presentacion=presentacionP;
		fecha_expiracion= fecha_expiracionP;
		idAlmacenaje= idAlmacenajeP;
		cantidad=cantidadP;
		tipoalmacenaje=tipoalmacenajeP;
		promocion=promocionP;
		costo = costoP;
	}

	@Override
	public String getNombre() {
		// TODO Auto-generated method stub
		return nombreProducto;
	}

	@Override
	public int getPresentacion() {
		// TODO Auto-generated method stub
		return presentacion;
	}

	@Override
	public int getcantidad() {
		// TODO Auto-generated method stub
		return cantidad;
	}

	@Override
	public int getCosto() {
		// TODO Auto-generated method stub
		return costo;
	}

	@Override
	public int compareTo(IItemVal o) {
		// TODO Auto-generated method stub
		return (nombreProducto + presentacion + cantidad + fecha_expiracion).compareTo(o.getNombre() + o.getPresentacion() + o.getcantidad() + o.getFechaExpiracion());
	}

	@Override
	public Date getFechaExpiracion() {
		// TODO Auto-generated method stub
		return fecha_expiracion;
	}

	@Override
	public void setCantidad(int i) {
		cantidad = i;
		
	}

	@Override
	public int getPesoTotal() {
		// TODO Auto-generated method stub
		return presentacion*cantidad;
	}
	

	


}
