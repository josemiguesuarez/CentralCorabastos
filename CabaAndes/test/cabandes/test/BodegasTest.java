package cabandes.test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import cabandes.dao.ConsultaDAO;
import cabandes.fachada.CabaAndes;
import cabandes.vos.AlmacenajeValue;
import junit.framework.TestCase;

public class BodegasTest extends TestCase {

	//-----------------------------------------------
	//Atributos
	//--------------------------------------------------

	private String cadenaConexion;
	private String usuario;
	private String clave;
	
	/**
	 * conexion con la base de datos
	 */
	public Connection conexion;
	
	/**
	 * Conexion con la clase con los metodos del proyecto
	 */
	static ConsultaDAO instancia = null;

	//----------------------------------------
	//metodos de conexion con la base de datos
	//---------------------------------------
	
	public static ConsultaDAO getInstance() {
		if(instancia==null)
			instancia = new ConsultaDAO();
			instancia.inicializar("../CabaAndes/data/html/");
		return instancia;
	}

	
	/**
	 * obtiene los datos necesarios para establecer una conexion
	 */
	public void inicializar()
	{
		try
		{
			/*File arch= new File("./cabandes/data/"+ARCHIVO_CONEXION);
				Properties prop = new Properties();
				FileInputStream in = new FileInputStream( arch );

				prop.load( in );
				in.close( );*/

			/*cadenaConexion = prop.getProperty("url");	// El url, el usuario y passwd deben estar en un archivo de propiedades.
				// url: "jdbc:oracle:thin:@chie.uniandes.edu.co:1521:chie10";
				usuario = prop.getProperty("usuario");	
				clave = prop.getProperty("clave");	
				final String driver = prop.getProperty("driver");
				Class.forName(driver);
			 */
			cadenaConexion = "jdbc:oracle:thin:@157.253.238.224:1531:prod";
			usuario = "isis2304431410";
			clave = "v1Idesdevan";
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}	
	}

	/**
	 * Metodo que se encarga de crear la conexion con el Driver Manager
	 * a partir de los parametros recibidos.
	 * @throws SQLException si ocurre un error generando la conexion con la base de datos.
	 */
	private void establecerConexion()
	{
		inicializar();
		try
		{
			conexion = DriverManager.getConnection(cadenaConexion,usuario,clave);
		}
		catch( Exception exception )
		{
			exception.printStackTrace();
			System.out.println("ERROR: ConsultaDAO obteniendo una conexion." );
		}
	}

	/**
	 *Cierra la conexion activa a la base de datos. Ademas, con=null.
	 * @param con objeto de conexion a la base de datos
	 * @throws SistemaCinesException Si se presentan errores de conexion
	 */
	public void closeConnection() throws Exception {        
		try {
			conexion.close();
			conexion = null;
		} catch (SQLException exception) {
			throw new Exception("ERROR: ConsultaDAO: closeConnection() = cerrando una conexion.");
		}
	} 

	//-----------------------------------------
	//Metodos de pruebas
	//------------------------------------------
	/**
	public void testBorrar()
	{
		instancia = getInstance();

		try {
			instancia.removeBodega("128");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	*/
	public void testEliminarBodegas()
	{
		
		inicializar();
		establecerConexion();
		PreparedStatement prepStmt = null;
		String agregarPrueba = "insert into ALMACENAJES (idalmacenaje, tipoAlmacenaje, capacidad, capacidadUsada, Tipo, direlectronica, nombreAlmacenaje) values (9999, 'Bodega',1,0, 'N',null,'BodegaTest')";
		
		try {
			//establecerConexion();
			prepStmt = conexion.prepareStatement(agregarPrueba);
			prepStmt.execute();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
		
		
		instancia = getInstance();
		ArrayList<AlmacenajeValue> bodegas = null;
		try {

			instancia.removeBodega("9999");
			bodegas = instancia.getBodegas(true, null);

		} catch (Exception e) {}

		boolean encontro = false;

		for (int i = 0; i<bodegas.size() && encontro ;i++)
		{
			AlmacenajeValue actual = bodegas.get(i);

			if(actual.nombreAlmacenaje.equals("BodegaTest"))
			{
				encontro = true;
			}
		}
		
		assertFalse("La bodega debio eliminarse", encontro);
	}
	
	public void testComprobarReOrdenamiento()
	{
		//Crea una bodega con gran capacidad
		instancia = getInstance();
		ArrayList<AlmacenajeValue> bodegas = new ArrayList<AlmacenajeValue>();
		try {

			instancia.addBodega("BodegaTest", "40000", "P");
			bodegas = instancia.getBodegas(true, null);

		} catch (Exception e) {
			e.printStackTrace();
		}

		boolean encontro = false;

		for (int i = 0; i<bodegas.size() && !encontro ;i++)
		{
			AlmacenajeValue actual = bodegas.get(i);

			if(actual.nombreAlmacenaje.equals("BodegaTest"))
			{
				encontro = true;
			}
		}
		assertTrue("La bodega debio encontrarse", encontro);
				
		//trata de eliminar dicha bodega, no deberia poderse, ya que los objetos se ordenan
		inicializar();
		establecerConexion();
		PreparedStatement prepStmt = null;
		String eliminarPrueba = "delete from ALMACENAJES where nombreAlmacenaje like 'BodegaTest'";
		
		try {
			//establecerConexion();
			prepStmt = conexion.prepareStatement(eliminarPrueba);
			prepStmt.execute();
			fail("Deberia arrojar exception");
		}
		catch (Exception e)
		{}
		
		//debe primero buscar el codigo asignado a la bodega de prueba
		instancia = getInstance();
		ArrayList<AlmacenajeValue> bodegas3 = null;
		String idBodega = null;
		try {
			bodegas3 = instancia.getBodegas(true, null);

		} catch (Exception e) {}

		boolean encontro3 = false;

		for (int i = 0; i<bodegas3.size() && !encontro3 ;i++)
		{
			AlmacenajeValue actual = bodegas3.get(i);

			if(actual.nombreAlmacenaje.equals("BodegaTest"))
			{
				idBodega = Integer.toString(actual.idAlmacenaje);
				encontro3 = true;
			}
		}
		assertTrue("La bodega debe existir", encontro);
		
		
		//elimina la bodega usando el metodo
		try {
			
			instancia.removeBodega(idBodega);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
		
		
		instancia = getInstance();
		ArrayList<AlmacenajeValue> bodegas2 = null;
		try {
			bodegas2 = instancia.getBodegas(true, null);

		} catch (Exception e) {}

		boolean encontro2 = false;

		for (int i = 0; i<bodegas2.size() && !encontro ;i++)
		{
			AlmacenajeValue actual = bodegas2.get(i);

			if(actual.nombreAlmacenaje.equals("BodegaTest"))
			{
				encontro2 = true;
			}
		}
		
		assertFalse("La bodega debio eliminarse", encontro2);
		
	}
	
	
}
