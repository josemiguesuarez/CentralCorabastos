package cabandes.vos;

import java.util.Date;


public interface IItemVal extends Comparable<IItemVal>
{
	public String getNombre();
	public int getPresentacion();
	public int getcantidad();
	public int getCosto();
	public Date getFechaExpiracion();
	public void setCantidad(int i);
	public int getPesoTotal();
}
