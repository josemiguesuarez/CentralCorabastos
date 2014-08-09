package cabandes.vos;

import java.util.Comparator;

import listaPrioridad.ListaPrioridad;

public class AlmacenajeValue implements Comparable<AlmacenajeValue> {

	public int idAlmacenaje;
	public String tipoAlmacenaje;
	public double capacidad;
	public double capacidadUsada;
	public char tipo;
	public String dirElectronica;
	public String nombreAlmacenaje;
	public ListaPrioridad<IItemVal> itemsAlmacenaje;

	public AlmacenajeValue(
			int nidAlmacenaje,
			String ntipoAlmacenaje,
			double ncapacidad,
			double ncapacidadUsada,
			char ntipo,
			String ndirElectronica, String nnombre)
	{
		idAlmacenaje = nidAlmacenaje;
		nombreAlmacenaje = nnombre;
		tipoAlmacenaje = ntipoAlmacenaje;
		capacidad = ncapacidad;
		capacidadUsada = ncapacidadUsada;
		tipo = ntipo;
		dirElectronica = ndirElectronica;

	}

	public void addItem ( IItemVal item)
	{
		if ( itemsAlmacenaje == null)
			itemsAlmacenaje = new ListaPrioridad<IItemVal>(new Comparator<IItemVal>() {
				public int compare(IItemVal o1, IItemVal o2) {
					return  o2.getPesoTotal() - o1.getPesoTotal();
				}
			} );
		itemsAlmacenaje.agregarElemento(item);
	}

	@Override
	public int compareTo(AlmacenajeValue o) {

		return (nombreAlmacenaje.equals(o.nombreAlmacenaje) )? 0: idAlmacenaje - o.idAlmacenaje;
	}
	
	public double getCapaDisponi()
	{
		return capacidad-capacidadUsada;
	}

}
