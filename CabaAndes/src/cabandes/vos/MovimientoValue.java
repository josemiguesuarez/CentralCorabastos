package cabandes.vos;

public class MovimientoValue {
	
	public String idMovimiento;
	public String producto;
	public String presentacion;
	public String fecha;
	public String peso;
	public String costo;
	public String tipoDeTransaccion;
	
	public String almacenajeDeEntrada;
	public String almacenajeDeSalida;
	
	public MovimientoValue(String nidMovimiento,
			String nproducto, String npresentacion, String nfecha,
			String npeso, String ncosto, String ntipo,
			String nalmacenajeEntrada, String nalmacenajeSalida) {
		
		idMovimiento = nidMovimiento;
		producto = nproducto;
		presentacion = npresentacion;
		fecha = nfecha;
		peso = npeso;
		costo = ncosto;
		tipoDeTransaccion = ntipo;
		almacenajeDeEntrada = nalmacenajeEntrada;
		almacenajeDeSalida = nalmacenajeSalida;
		
	}
}
