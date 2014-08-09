package cabandes.complementos;

import java.util.Comparator;

import cabandes.vos.AlmacenajeValue;

public class CompBodegaConMasEspacioPriemero implements Comparator<AlmacenajeValue>{

	public CompBodegaConMasEspacioPriemero()
	{
		
	}
	@Override
	public int compare(AlmacenajeValue o1, AlmacenajeValue o2) 
	{
		return (int) ((o2.capacidad -o2.capacidadUsada) - (o1.capacidad - o1.capacidadUsada));
	}
	

}
