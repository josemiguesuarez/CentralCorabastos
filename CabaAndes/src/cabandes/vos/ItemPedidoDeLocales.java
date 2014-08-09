package cabandes.vos;

import java.util.Date;

public class ItemPedidoDeLocales implements IItemVal
{

	public int idPedidoI;
	public String nombreProducto;
	public int presentacion;
	public int cantidad;
	public int costoUnit;
	public Date fechaEsperada;
	
	public ItemPedidoDeLocales(int idPedidoIP, String nombreProductoP,
			int presentacionP, int cantidadP, int costoUnitP, Date fechaEsperadaP) {

		idPedidoI = idPedidoIP;
		nombreProducto = nombreProductoP;
		presentacion = presentacionP;
		cantidad = cantidadP;
		costoUnit = costoUnitP;
		fechaEsperada = fechaEsperadaP;
	}

	@Override
	public String getNombre() {
		return nombreProducto;
	}

	@Override
	public int getPresentacion() {
		return presentacion;
	}

	@Override
	public int getcantidad() {
		return cantidad;
	}

	@Override
	public int getCosto() {
		return costoUnit;
	}

	@Override
	public int compareTo(IItemVal o) {
		return (nombreProducto + presentacion + cantidad).compareTo(o.getNombre() + o.getPresentacion() + o.getcantidad());
	}

	@Override
	public Date getFechaExpiracion() {
		return null;
	}

	@Override
	public void setCantidad(int i) {
		cantidad = i ;
		
	}

	@Override
	public int getPesoTotal() {
		return presentacion*cantidad;
		
	}

}
