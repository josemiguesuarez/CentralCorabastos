package cabandes.vos;

public class LocalValue 
{
	public int idLocal;
	public int capacidad;
	public String tipoProducto;
	public String dirElectronica;
	
	public LocalValue(int nidLocal,
						int ncapacidad,
						String ntipoProducto,
						String ndirElectronica)
	{
		idLocal = nidLocal;
		capacidad = ncapacidad;
		tipoProducto = ntipoProducto;
		dirElectronica = ndirElectronica;
	}
}
