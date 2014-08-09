package cabandes.vos;

public class MovimientoNumVentasSolicitudesValue {
	
	public String nombre;
	public String tipo;
	public int presentacion;
	public double pesoPromedio;
	public double costoPromedio;
	public int vecesSolicitado;
	public int vecesVendido;

	public MovimientoNumVentasSolicitudesValue(String nnombre,
			String ntipo,
			int npresentacion,
			double npesoPromedio,
			double ncostoPromedio,
			int nvecesSolicitado,
			int nvecesVendido) {
		
		nombre = nnombre;
		tipo = ntipo;
		presentacion = npresentacion;
		pesoPromedio = npesoPromedio;
		costoPromedio = ncostoPromedio;
		vecesSolicitado = nvecesSolicitado;
		vecesVendido = nvecesVendido;
	}
}