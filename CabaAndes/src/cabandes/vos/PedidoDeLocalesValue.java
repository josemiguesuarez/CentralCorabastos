package cabandes.vos;

import java.util.ArrayList;
import java.util.Date;

public class PedidoDeLocalesValue 
{
	public int idLocal;
	public int idPedido;
	public Date recibido;
	public Date fechaPedido;
	public ArrayList<ItemPedidoDeLocales> items;
	public Date fechaEsperada;
	public int costoTotal;

	public PedidoDeLocalesValue(int idLocalP, int idPedidoP, Date recibidoP, Date fechaPedidoP) {
		idLocal = idLocalP;
		idPedido = idPedidoP;
		recibido = recibidoP;
		fechaPedido = fechaPedidoP;
		costoTotal = 0;
		fechaEsperada= null;
		items = new ArrayList<ItemPedidoDeLocales>();
	}

	public void agregarItem(ItemPedidoDeLocales item) {
		if (fechaEsperada == null)
		{
			fechaEsperada = item.fechaEsperada;
		}
		else if ( fechaEsperada.compareTo(item.fechaEsperada) < 0)
		{
			fechaEsperada = item.fechaEsperada;
		}
		costoTotal += item.costoUnit*item.cantidad;
		
		items.add(item);
		
	}

	public boolean tieneProducto(String filtarPorProductoNombre,
			int filtarPorProductoPresentacion) {
	
		for (int i = 0; i < items.size(); i++) {
			ItemPedidoDeLocales item = items.get(i);
			if ( item.getNombre().equals(filtarPorProductoNombre) && item.getPresentacion() == filtarPorProductoPresentacion)
			{
				return true;
			}
		}
		return false;
	}

}
