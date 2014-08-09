package cabandes.vos;

public class ProveedorValue {
	
	public String dirElectronica;
	public char tipoProducto;
	public String login;
	public String docIdentidad;
	public String nombreUsuario;
	public char esEmpresa;
	public int telefono;
	public String nacionalidad;
	public String departamento;
	public String ciudad;
	public String codPostal;

	
	public ProveedorValue(String ndirElectronica,
			char ntipoProducto,
			String nlogin,
			String ndocIdentidad,
			String nnombreUsuario,
			char nesEmpresa,
			int ntelefono,
			String nnacionalidad,
			String ndepartamento,
			String nciudad,
			String ncodPostal) {
		
		dirElectronica = ndirElectronica;
		tipoProducto = ntipoProducto;
		login = nlogin;
		docIdentidad = ndocIdentidad;
		nombreUsuario = nnombreUsuario;
		esEmpresa = nesEmpresa;
		telefono = ntelefono;
		nacionalidad = nnacionalidad;
		departamento = ndepartamento;
		ciudad = nciudad;
		codPostal = ncodPostal;		
	}
}
