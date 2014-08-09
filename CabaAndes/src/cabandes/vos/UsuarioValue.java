package cabandes.vos;

public class UsuarioValue 
{


	public String dirElectronica;
	public String login; 
	public String clave;
	public String docIdentidad;
	public String nombreUsuario;
	public boolean esEmpresa;
	public String rol;
	public int telefono;
	public String nacionalidad;
	public String departamento;
	public String ciudad; 
	public String codPostal; 
	public String ip; 

	public UsuarioValue(String dirElectronicaP,
			String loginP,
			String claveP,
			String docIdentidadP,
			String nombreUsuarioP,
			boolean esEmpresaP,
			String rolP,
			int telefonoP,
			String nacionalidadP,
			String departamentoP,
			String ciudadP, 
			String codPostalP, 
			String ipP) 
	{
		dirElectronica= dirElectronicaP;
		login= loginP;
		clave=claveP;
		docIdentidad= docIdentidadP;
		nombreUsuario= nombreUsuarioP;
		esEmpresa= esEmpresaP;
		rol=rolP;
		telefono= telefonoP;
		nacionalidad= nacionalidadP;
		departamento= departamentoP;
		ciudad= ciudadP;
		codPostal= codPostalP;
		ip=  ipP;
	}

}
