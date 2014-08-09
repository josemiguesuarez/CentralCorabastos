package cabandes.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;




import junit.framework.TestCase;


public class UnicidadTuplasTest extends TestCase{
	
	//-----------------------------------------------
	//Atributos
	//--------------------------------------------------
	/**
	 * conexion con la base de datos
	 */
	public Connection conexion;

	/**
	 * nombre del usuario para conectarse a la base de datos.
	 */
	private String usuario;

	/**
	 * clave de conexion a la base de datos.
	 */
	private String clave;

	/**
	 * URL al cual se debe conectar para acceder a la base de datos.
	 */
	private String cadenaConexion;
	
	
	//----------------------------------------
	//metodos de conexion con la base de datos
	//---------------------------------------
	/**
	 * obtiene ls datos necesarios para establecer una conexion
	 * Los datos se obtienen a partir de un archivo properties.
	 * @param path ruta donde se encuentra el archivo properties.
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
	
	public void testTablaAlmacenaje()
	{
		inicializar();
		establecerConexion();
		PreparedStatement prepStmt = null;
		String primeraTupla = "Insert into ALMACENAJES(idAlmacenaje, TipoAlmacenaje, capacidad, capacidadUsada, tipo) values(9999,'Bodega',1000,0,'R')";
		try {
			//establecerConexion();
			prepStmt = conexion.prepareStatement(primeraTupla);
			prepStmt.execute();
			try {
				String segundaTupla = "Insert into ALMACENAJES(idAlmacenaje, TipoAlmacenaje, capacidad, capacidadUsada, tipo) values(9999,'Bodega',1000,0,'R')";
				prepStmt = conexion.prepareStatement(segundaTupla);
				prepStmt.execute();

				fail("Debio lanzar exception");
			}
			catch (Exception e) {}


		} catch (Exception e) {

			e.printStackTrace();

		}finally 
		{
			try {
				String eliminarTupla = "Delete from ALMACENAJES where IDALMACENAJE = 9999";
				prepStmt = conexion.prepareStatement(eliminarTupla);
				prepStmt.execute();
				closeConnection();
			} catch (Exception e) {
				fail("No dejo eliminar la primera tupla insertada");
				e.printStackTrace();
			}
		}
	}
	
	public void testTablaFacturasLocales()
	{
		inicializar();
		establecerConexion();
		PreparedStatement prepStmt = null;
		String crearLocal= "Insert into ALMACENAJES(idAlmacenaje, TipoAlmacenaje, capacidad, capacidadUsada, tipo) values(9999,'Local',1000,0,'R')";
		try {
			//establecerConexion();
			prepStmt = conexion.prepareStatement(crearLocal);
			prepStmt.execute();

			String crearFactura= "Insert into FACTURAS_LOCALES(IDFACTURALOCAL,fecha, IDLOCAL) values(9999,TO_DATE('2014/03/10','YYYY/MM/DD'),9999)";
			prepStmt = conexion.prepareStatement(crearFactura);
			prepStmt.execute();
			try{
				String crearFactura2= "Insert into FACTURAS_LOCALES(IDFACTURALOCAL,fecha, IDLOCAL) values(9999,TO_DATE('2014/03/10','YYYY/MM/DD'),9999)";
				prepStmt = conexion.prepareStatement(crearFactura2);
				prepStmt.execute();

				fail("Debio lanzar exception");
			}
			catch (Exception e) {}


		} catch (Exception e) {

			e.printStackTrace();

		}finally 
		{
			try {
				
				String eliminarFactura = "Delete from FACTURAS_LOCALES where IDFACTURALOCAL = 9999";
				prepStmt = conexion.prepareStatement(eliminarFactura);
				prepStmt.execute();
				
				String eliminarLocal = "Delete from ALMACENAJES where IDALMACENAJE = 9999";
				prepStmt = conexion.prepareStatement(eliminarLocal);
				prepStmt.execute();
				
				
				
				closeConnection();
			} catch (Exception e) {
				fail("No dejo eliminar alguna de las tuplas insertadas");
				e.printStackTrace();
			}
		}
	}
	
	public void testTablaPedidosDeLocales()
	{
		inicializar();
		establecerConexion();
		PreparedStatement prepStmt = null;
		String crearLocal = "Insert into ALMACENAJES(idAlmacenaje, TipoAlmacenaje, capacidad, capacidadUsada, tipo) values(9999,'Local',1000,0,'R')";
		try {
			//establecerConexion();
			prepStmt = conexion.prepareStatement(crearLocal);
			prepStmt.execute();

			String crearPedido = "Insert into PEDIDOS_DE_LOCALES(IDLOCAL, IDPEDIDO, RECIBIDO, FECHA_PEDIDO) values(9999,9999,'F',TO_DATE('2014/03/10','YYYY/MM/DD'))";
			prepStmt = conexion.prepareStatement(crearPedido);
			prepStmt.execute();
			try{	
				String crearPedido2 = "Insert into PEDIDOS_DE_LOCALES(IDLOCAL, IDPEDIDO, RECIBIDO, FECHA_PEDIDO) values(9999,9999,'F',TO_DATE('2014/03/10','YYYY/MM/DD'))";
				prepStmt = conexion.prepareStatement(crearPedido2);
				prepStmt.execute();

				fail("Debio lanzar exception");

			}
			catch (Exception e) {}


		} catch (Exception e) {

			e.printStackTrace();

		}finally 
		{
			try {
				String eliminarPedido = "Delete from PEDIDOS_DE_LOCALES where IDPEDIDO = 9999";
				prepStmt = conexion.prepareStatement(eliminarPedido);
				prepStmt.execute();
				
				String eliminarLocal= "Delete from ALMACENAJES where IDALMACENAJE = 9999";
				prepStmt = conexion.prepareStatement(eliminarLocal);
				prepStmt.execute();
				closeConnection();
				
			} catch (Exception e) {
				
				fail("No dejo eliminar la primera tupla insertada");
				e.printStackTrace();
				
			}
		}
	}
	
	public void testTablaItemsPedidosPorLocales()
	{
		inicializar();
		establecerConexion();
		PreparedStatement prepStmt = null;
		String crearLocal = "Insert into ALMACENAJES(idAlmacenaje, TipoAlmacenaje, capacidad, capacidadUsada, tipo) values(9999,'Local',1000,0,'R')";
		try {
			//establecerConexion();
			prepStmt = conexion.prepareStatement(crearLocal);
			prepStmt.execute();
			
			String crearPedido = "Insert into PEDIDOS_DE_LOCALES(IDLOCAL, IDPEDIDO, RECIBIDO, FECHA_PEDIDO) values(9999,9999,'F',TO_DATE('2014/03/10','YYYY/MM/DD'))";
			prepStmt = conexion.prepareStatement(crearPedido);
			prepStmt.execute();
			
			String crearProducto= "Insert into PRODUCTOS(NOMBREPRODUCTO, VALOR_ACTUAL, tipo) values('prueba','99','R')";
			prepStmt = conexion.prepareStatement(crearProducto);
			prepStmt.execute();
			
			String crearPresentacion = "Insert into PRESENTACIONES(NOMBREPRODUCTO, PRESENTACION, PROMOCION) values('prueba',10,0)";
			prepStmt = conexion.prepareStatement(crearPresentacion);
			prepStmt.execute();

			String crearItem = "Insert into ITEMS_PEDIDOS_POR_LOCALES(CANTIDAD, NOMBREPRODUCTO, PRESENTACION, IDPEDIDO, COSTO_UNITARIO, FECHA_ESPERADA) values(10,'prueba',10,9999,10,TO_DATE('2014/03/10','YYYY/MM/DD'))";
			prepStmt = conexion.prepareStatement(crearItem);
			prepStmt.execute();
			try{
				String crearItem2 = "Insert into ITEMS_PEDIDOS_POR_LOCALES(CANTIDAD, NOMBREPRODUCTO, PRESENTACION, IDPEDIDO, COSTO_UNITARIO, FECHA_ESPERADA) values(10,'prueba',10,9999,10,TO_DATE('2014/03/10','YYYY/MM/DD'))";
				prepStmt = conexion.prepareStatement(crearItem2);
				prepStmt.execute();

				fail("Debio lanzar exception");

			}
			catch (Exception e) {}


		} catch (Exception e) {

			e.printStackTrace();

		}finally 
		{
			try {
				String eliminarItem = "Delete from ITEMS_PEDIDOS_POR_LOCALES where NOMBREPRODUCTO= 'prueba' AND PRESENTACION = 10 AND IDPEDIDO = 9999";
				prepStmt = conexion.prepareStatement(eliminarItem);
				prepStmt.execute();
				
				String eliminarPresentacion = "Delete from PRESENTACIONES where NOMBREPRODUCTO= 'prueba'";
				prepStmt = conexion.prepareStatement(eliminarPresentacion);
				prepStmt.execute();

				String eliminarProducto = "Delete from PRODUCTOS where NOMBREPRODUCTO= 'prueba'";
				prepStmt = conexion.prepareStatement(eliminarProducto);
				prepStmt.execute();
				
				String eliminarPedido = "Delete from PEDIDOS_DE_LOCALES where IDPEDIDO = 9999";
				prepStmt = conexion.prepareStatement(eliminarPedido);
				prepStmt.execute();
				
				String eliminarLocal= "Delete from ALMACENAJES where IDALMACENAJE = 9999";
				prepStmt = conexion.prepareStatement(eliminarLocal);
				prepStmt.execute();
				closeConnection();
				
			} catch (Exception e) {
				
				fail("No dejo eliminar la primera tupla insertada");
				e.printStackTrace();
				
			}
		}
	}
	
	
	public void testTablaItemsCompradosEnLocales()
	{
		inicializar();
		establecerConexion();
		PreparedStatement prepStmt = null;
		String crearLocal = "Insert into ALMACENAJES(idAlmacenaje, TipoAlmacenaje, capacidad, capacidadUsada, tipo) values(9999,'Local',1000,0,'R')";
		try {
			//establecerConexion();
			prepStmt = conexion.prepareStatement(crearLocal);
			prepStmt.execute();
			
			String crearPedido = "Insert into PEDIDOS_DE_LOCALES(IDLOCAL, IDPEDIDO, RECIBIDO, FECHA_PEDIDO) values(9999,9999,'F',TO_DATE('2014/03/10','YYYY/MM/DD'))";
			prepStmt = conexion.prepareStatement(crearPedido);
			prepStmt.execute();
			
			String crearFactura= "Insert into FACTURAS_LOCALES(IDFACTURALOCAL,fecha, IDLOCAL) values(9999,TO_DATE('2014/03/10','YYYY/MM/DD'),9999)";
			prepStmt = conexion.prepareStatement(crearFactura);
			prepStmt.execute();
			
			String crearProducto= "Insert into PRODUCTOS(NOMBREPRODUCTO, VALOR_ACTUAL, tipo) values('prueba','99','R')";
			prepStmt = conexion.prepareStatement(crearProducto);
			prepStmt.execute();
			
			String crearPresentacion = "Insert into PRESENTACIONES(NOMBREPRODUCTO, PRESENTACION, PROMOCION) values('prueba',10,0)";
			prepStmt = conexion.prepareStatement(crearPresentacion);
			prepStmt.execute();

			String crearItem = "Insert into ITEMS_COMPRADOS_EN_LOCALES(CANTIDAD, NOMBREPRODUCTO, PRESENTACION, IDFACTURALOCAL, FECHADEEXPIRACION) values(10,'prueba',10,9999,TO_DATE('2014/03/10','YYYY/MM/DD'))";
			prepStmt = conexion.prepareStatement(crearItem);
			prepStmt.execute();
			try{	
				String crearItem2 = "Insert into ITEMS_COMPRADOS_EN_LOCALES(CANTIDAD, NOMBREPRODUCTO, PRESENTACION, IDFACTURALOCAL, FECHADEEXPIRACION) values(10,'prueba',10,9999,TO_DATE('2014/03/10','YYYY/MM/DD'))";
				prepStmt = conexion.prepareStatement(crearItem2);
				prepStmt.execute();

				fail("Debio lanzar exception");

			}
			catch (Exception e) {}


		} catch (Exception e) {

			e.printStackTrace();

		}finally 
		{
			try {
				String eliminarItem = "Delete from ITEMS_COMPRADOS_EN_LOCALES where NOMBREPRODUCTO= 'prueba' AND PRESENTACION = 10 AND IDFACTURALOCAL = 9999";
				prepStmt = conexion.prepareStatement(eliminarItem);
				prepStmt.execute();
				
				String eliminarFactura = "Delete from FACTURAS_LOCALES where IDFACTURALOCAL = 9999";
				prepStmt = conexion.prepareStatement(eliminarFactura);
				prepStmt.execute();
				
				String eliminarPresentacion = "Delete from PRESENTACIONES where NOMBREPRODUCTO= 'prueba'";
				prepStmt = conexion.prepareStatement(eliminarPresentacion);
				prepStmt.execute();

				String eliminarProducto = "Delete from PRODUCTOS where NOMBREPRODUCTO= 'prueba'";
				prepStmt = conexion.prepareStatement(eliminarProducto);
				prepStmt.execute();
				
				String eliminarPedido = "Delete from PEDIDOS_DE_LOCALES where IDPEDIDO = 9999";
				prepStmt = conexion.prepareStatement(eliminarPedido);
				prepStmt.execute();
				
				String eliminarLocal= "Delete from ALMACENAJES where IDALMACENAJE = 9999";
				prepStmt = conexion.prepareStatement(eliminarLocal);
				prepStmt.execute();
				closeConnection();
				
			} catch (Exception e) {
				
				fail("No dejo eliminar la primera tupla insertada");
				e.printStackTrace();
				
			}
		}
	}
	
	public void testItemsAlmacenajes()
	{
		inicializar();
		establecerConexion();
		PreparedStatement prepStmt = null;
		String crearProducto= "Insert into PRODUCTOS(NOMBREPRODUCTO, VALOR_ACTUAL, tipo) values('prueba',10,'R')";
		try {
			//establecerConexion();
			prepStmt = conexion.prepareStatement(crearProducto);
			prepStmt.execute();

			String crearPresentacion = "Insert into PRESENTACIONES(NOMBREPRODUCTO, PRESENTACION, PROMOCION) values('prueba',10,0)";
			prepStmt = conexion.prepareStatement(crearPresentacion);
			prepStmt.execute();

			String crearLocal= "Insert into ALMACENAJES(idAlmacenaje, TipoAlmacenaje, capacidad, capacidadUsada, tipo) values(9999,'Local',1000,0,'R')";
			prepStmt = conexion.prepareStatement(crearLocal);
			prepStmt.execute();

			String crearItem = "Insert into ITEMS_ALMACENAJES(CANTIDAD, NOMBREPRODUCTO, PRESENTACION, IDALMACENAJE, FECHA_EXPIRACION) values(10,'prueba',10,9999,TO_DATE('2014/03/10','YYYY/MM/DD'))";
			prepStmt = conexion.prepareStatement(crearItem);
			prepStmt.execute();
			try{	
				String crearItem2 = "Insert into ITEMS_ALMACENAJES(CANTIDAD, NOMBREPRODUCTO, PRESENTACION, IDALMACENAJE, FECHA_EXPIRACION) values(10,'prueba',10,9999,TO_DATE('2014/03/10','YYYY/MM/DD'))";
				prepStmt = conexion.prepareStatement(crearItem2);
				prepStmt.execute();

				fail("Debio lanzar exception");

			}
			catch (Exception e) {}


		} catch (Exception e) {

			e.printStackTrace();

		}finally 
		{
			try {
				String eliminarItem= "Delete from ITEMS_ALMACENAJES where NOMBREPRODUCTO= 'prueba' AND PRESENTACION = 10 AND IDALMACENAJE= 9999";
				prepStmt = conexion.prepareStatement(eliminarItem);
				prepStmt.execute();
				
				String eliminarLocal = "Delete from ALMACENAJES where IDALMACENAJE = 9999";
				prepStmt = conexion.prepareStatement(eliminarLocal);
				prepStmt.execute();
				
				String eliminarPresentacion = "Delete from PRESENTACIONES where NOMBREPRODUCTO= 'prueba'";
				prepStmt = conexion.prepareStatement(eliminarPresentacion);
				prepStmt.execute();

				String eliminarProducto = "Delete from PRODUCTOS where NOMBREPRODUCTO= 'prueba'";
				prepStmt = conexion.prepareStatement(eliminarProducto);
				prepStmt.execute();

				closeConnection();
			} catch (Exception e) {
				fail("No dejo eliminar alguna de las tuplas insertadas");
				e.printStackTrace();
			}
		}
	}
	
	public void testTablaProductos()
	{
		inicializar();
		establecerConexion();
		PreparedStatement prepStmt = null;
		String crearProducto= "Insert into PRODUCTOS(NOMBREPRODUCTO, VALOR_ACTUAL, tipo) values('prueba','99','R')";
		try {
			//establecerConexion();
			prepStmt = conexion.prepareStatement(crearProducto);
			prepStmt.execute();
			try{	
				String crearProducto2= "Insert into PRODUCTOS(NOMBREPRODUCTO, VALOR_ACTUAL, tipo) values('prueba','99','R')";
				prepStmt = conexion.prepareStatement(crearProducto2);
				prepStmt.execute();			
				fail("Debio lanzar exception");

			}
			catch (Exception e) {}


		} catch (Exception e) {

			e.printStackTrace();

		}finally 
		{
			try {
				String eliminarProducto = "Delete from PRODUCTOS where NOMBREPRODUCTO= 'prueba'";
				prepStmt = conexion.prepareStatement(eliminarProducto);
				prepStmt.execute();
				
				closeConnection();
			} catch (Exception e) {
				fail("No dejo eliminar alguna de las tuplas insertadas");
				e.printStackTrace();
			}
		}
	}
	
	public void testTablaPresentaciones()
	{
		inicializar();
		establecerConexion();
		PreparedStatement prepStmt = null;
		String crearProducto= "Insert into PRODUCTOS(NOMBREPRODUCTO, VALOR_ACTUAL, tipo) values('prueba',10,'R')";
		try {
			//establecerConexion();
			prepStmt = conexion.prepareStatement(crearProducto);
			prepStmt.execute();

			String crearPresentacion = "Insert into PRESENTACIONES(NOMBREPRODUCTO, PRESENTACION, PROMOCION) values('prueba',10,0)";
			prepStmt = conexion.prepareStatement(crearPresentacion);
			prepStmt.execute();
			try{
				String crearPresentacion2 = "Insert into PRESENTACIONES(NOMBREPRODUCTO, PRESENTACION, PROMOCION) values('prueba',10,0)";
				prepStmt = conexion.prepareStatement(crearPresentacion2);
				prepStmt.execute();			
				fail("Debio lanzar exception");

			}
			catch (Exception e) {}


		} catch (Exception e) {

			e.printStackTrace();

		}finally 
		{
			try {
				String eliminarPresentacion = "Delete from PRESENTACIONES where NOMBREPRODUCTO= 'prueba'";
				prepStmt = conexion.prepareStatement(eliminarPresentacion);
				prepStmt.execute();

				String eliminarProducto = "Delete from PRODUCTOS where NOMBREPRODUCTO= 'prueba'";
				prepStmt = conexion.prepareStatement(eliminarProducto);
				prepStmt.execute();

				closeConnection();
			} catch (Exception e) {
				fail("No dejo eliminar alguna de las tuplas insertadas");
				e.printStackTrace();
			}
		}

	}
	
	public void testTablaMovimientos()
	{
		inicializar();
		establecerConexion();
		PreparedStatement prepStmt = null;
		String crearProducto= "Insert into PRODUCTOS(NOMBREPRODUCTO, VALOR_ACTUAL, tipo) values('prueba',10,'R')";
		try {
			//establecerConexion();
			prepStmt = conexion.prepareStatement(crearProducto);
			prepStmt.execute();

			String crearPresentacion = "Insert into PRESENTACIONES(NOMBREPRODUCTO, PRESENTACION, PROMOCION) values('prueba',10,0)";
			prepStmt = conexion.prepareStatement(crearPresentacion);
			prepStmt.execute();

			String crearMovimiento = "Insert into MOVIMIENTOS(IDMOVIMIENTO, NOMBREPRODUCTO, PRESENTACION, FECHA,PESO, COSTOTOTAL, TIPODETRANSACCION) values(9999,'prueba',10,TO_DATE('2014/03/10','YYYY/MM/DD'), 10,10,'venta')";
			prepStmt = conexion.prepareStatement(crearMovimiento);
			prepStmt.execute();
			try{	
				String crearMovimiento2 = "Insert into MOVIMIENTOS(IDMOVIMIENTO, NOMBREPRODUCTO, PRESENTACION, FECHA,PESO, COSTOTOTAL, TIPODETRANSACCION) values(9999,'prueba',10,TO_DATE('2014/03/10','YYYY/MM/DD'), 10,10,'venta')";
				prepStmt = conexion.prepareStatement(crearMovimiento2);
				prepStmt.execute();

				fail("Debio lanzar exception");

			}
			catch (Exception e) {}


		} catch (Exception e) {

			e.printStackTrace();

		}finally 
		{
			try {
				String eliminarMovimiento = "Delete from MOVIMIENTOS where IDMOVIMIENTO= 9999";
				prepStmt = conexion.prepareStatement(eliminarMovimiento);
				prepStmt.execute();
				
				String eliminarPresentacion = "Delete from PRESENTACIONES where NOMBREPRODUCTO= 'prueba'";
				prepStmt = conexion.prepareStatement(eliminarPresentacion);
				prepStmt.execute();

				String eliminarProducto = "Delete from PRODUCTOS where NOMBREPRODUCTO= 'prueba'";
				prepStmt = conexion.prepareStatement(eliminarProducto);
				prepStmt.execute();

				closeConnection();
			} catch (Exception e) {
				fail("No dejo eliminar alguna de las tuplas insertadas");
				e.printStackTrace();
			}
		}
	}
	
	public void testTablaUsuarios()
	{

		inicializar();
		establecerConexion();
		PreparedStatement prepStmt = null;
		String crearUsuario= "Insert into USUARIOS(DIRELECTRONICA, LOGIN, CLAVE,DOCIDENTIDAD,NOMBREUSUARIO,ESEMPRESA,ROL,TELEFONO,NACIONALIDAD,DEPARTAMENTO,CIUDAD,CODPOSTAL) values('prueba@prueba','prueba','prueba','prueba','prueba','T','P',0,'colombiano','bogota','bogota',0)";
		try {
			//establecerConexion();
			prepStmt = conexion.prepareStatement(crearUsuario);
			prepStmt.execute();
			try{	
				String crearUsuario2= "Insert into USUARIOS(DIRELECTRONICA, LOGIN, CLAVE,DOCIDENTIDAD,NOMBREUSUARIO,ESEMPRESA,ROL,TELEFONO,NACIONALIDAD,DEPARTAMENTO,CIUDAD,CODPOSTAL) values('prueba@prueba','prueba','prueba','prueba','prueba','T','P',0,'colombiano','bogota','bogota',0)";
				prepStmt = conexion.prepareStatement(crearUsuario2);
				prepStmt.execute();
				fail("Debio lanzar exception");

			}
			catch (Exception e) {}


		} catch (Exception e) {

			e.printStackTrace();

		}finally 
		{
			try {
				String eliminarUsuario = "Delete from USUARIOS where DIRELECTRONICA = 'prueba@prueba'";
				prepStmt = conexion.prepareStatement(eliminarUsuario);
				prepStmt.execute();

				closeConnection();
			} catch (Exception e) {
				fail("No dejo eliminar alguna de las tuplas insertadas");
				e.printStackTrace();
			}
		}
	}
	
	public void testTablaProveedores()
	{
		inicializar();
		establecerConexion();
		PreparedStatement prepStmt = null;
		String crearUsuario= "Insert into USUARIOS(DIRELECTRONICA, LOGIN, CLAVE,DOCIDENTIDAD,NOMBREUSUARIO,ESEMPRESA,ROL,TELEFONO,NACIONALIDAD,DEPARTAMENTO,CIUDAD,CODPOSTAL) values('prueba@prueba','prueba','prueba','prueba','prueba','T','P',0,'colombiano','bogota','bogota',0)";
		try {
			//establecerConexion();
			prepStmt = conexion.prepareStatement(crearUsuario);
			prepStmt.execute();

			String crearProveedor= "Insert into PROVEEDORES(DIRELECTRONICA, TIPOPRODUCTOS) values('prueba@prueba','P')";
			prepStmt = conexion.prepareStatement(crearProveedor);
			prepStmt.execute();
			try{	
				String crearProveedor2= "Insert into PROVEEDORES(DIRELECTRONICA, TIPOPRODUCTOS) values('prueba@prueba','P')";
				prepStmt = conexion.prepareStatement(crearProveedor2);
				prepStmt.execute();

				fail("Debio lanzar exception");

			}
			catch (Exception e) {}


		} catch (Exception e) {

			e.printStackTrace();

		}finally 
		{
			try {
				String eliminarProveedor= "Delete from PROVEEDORES where DIRELECTRONICA = 'prueba@prueba'";
				prepStmt = conexion.prepareStatement(eliminarProveedor);
				prepStmt.execute();

				String eliminarUsuario = "Delete from USUARIOS where DIRELECTRONICA = 'prueba@prueba'";
				prepStmt = conexion.prepareStatement(eliminarUsuario);
				prepStmt.execute();

				closeConnection();
			} catch (Exception e) {
				fail("No dejo eliminar alguna de las tuplas insertadas");
				e.printStackTrace();
			}
		}
	}
	
	public void testTablaPedidosAProveedores()
	{
		inicializar();
		establecerConexion();
		PreparedStatement prepStmt = null;
		String crearProducto= "Insert into PRODUCTOS(NOMBREPRODUCTO, VALOR_ACTUAL, tipo) values('prueba',10,'R')";
		try {
			//establecerConexion();
			prepStmt = conexion.prepareStatement(crearProducto);
			prepStmt.execute();

			String crearPresentacion = "Insert into PRESENTACIONES(NOMBREPRODUCTO, PRESENTACION, PROMOCION) values('prueba',10,0)";
			prepStmt = conexion.prepareStatement(crearPresentacion);
			prepStmt.execute();

			String crearLicitacion = "Insert into PEDIDO_A_PROVEEDORES(IDLICITACION,CANTIDAD,NOMBREPRODUCTO, PRESENTACION,FECHAESPERADA,FECHAPUBLICACION,FECHADECIERRE) values(9999,10,'prueba',10,TO_DATE('2014/03/10','YYYY/MM/DD'),TO_DATE('2014/03/10','YYYY/MM/DD'),TO_DATE('2014/03/10','YYYY/MM/DD'))";
			prepStmt = conexion.prepareStatement(crearLicitacion);
			prepStmt.execute();			
			try{	
				String crearLicitacion2 = "Insert into PEDIDOS_A_PROVEEDORES(IDLICITACION,CANTIDAD,NOMBREPRODUCTO, PRESENTACION,FECHAESPERADA,FECHAPUBLICACION,FECHADECIERRE) values(9999,10,'prueba',10,TO_DATE('2014/03/10','YYYY/MM/DD'),TO_DATE('2014/03/10','YYYY/MM/DD'),TO_DATE('2014/03/10','YYYY/MM/DD')";
				prepStmt = conexion.prepareStatement(crearLicitacion2);
				prepStmt.execute();

				fail("Debio lanzar exception");

			}
			catch (Exception e) {}


		} catch (Exception e) {

			e.printStackTrace();

		}finally 
		{
			try {
				String eliminarLicitacion= "Delete from PEDIDO_A_PROVEEDORES where idlicitacion = 9999";
				prepStmt = conexion.prepareStatement(eliminarLicitacion);
				prepStmt.execute();
				
				String eliminarPresentacion = "Delete from PRESENTACIONES where NOMBREPRODUCTO= 'prueba'";
				prepStmt = conexion.prepareStatement(eliminarPresentacion);
				prepStmt.execute();

				String eliminarProducto = "Delete from PRODUCTOS where NOMBREPRODUCTO= 'prueba'";
				prepStmt = conexion.prepareStatement(eliminarProducto);
				prepStmt.execute();

				closeConnection();
			} catch (Exception e) {
				fail("No dejo eliminar alguna de las tuplas insertadas");
				e.printStackTrace();
			}
		}
	}
	
	public void testTablaOfertas()
	{
		inicializar();
		establecerConexion();
		PreparedStatement prepStmt = null;
		String crearUsuario= "Insert into USUARIOS(DIRELECTRONICA, LOGIN, CLAVE,DOCIDENTIDAD,NOMBREUSUARIO,ESEMPRESA,ROL,TELEFONO,NACIONALIDAD,DEPARTAMENTO,CIUDAD,CODPOSTAL) values('prueba@prueba','prueba','prueba','prueba','prueba','T','P',0,'colombiano','bogota','bogota',0)";
		try {
			//establecerConexion();
			prepStmt = conexion.prepareStatement(crearUsuario);
			prepStmt.execute();
			
			String crearProveedor= "Insert into PROVEEDORES(DIRELECTRONICA, TIPOPRODUCTOS) values('prueba@prueba','P')";
			prepStmt = conexion.prepareStatement(crearProveedor);
			prepStmt.execute();
			
			String crearProducto= "Insert into PRODUCTOS(NOMBREPRODUCTO, VALOR_ACTUAL, tipo) values('prueba',10,'R')";
			prepStmt = conexion.prepareStatement(crearProducto);
			prepStmt.execute();
			
			String crearPresentacion = "Insert into PRESENTACIONES(NOMBREPRODUCTO, PRESENTACION, PROMOCION) values('prueba',10,0)";
			prepStmt = conexion.prepareStatement(crearPresentacion);
			prepStmt.execute();

			String crearLicitacion = "Insert into PEDIDO_A_PROVEEDORES(IDLICITACION,CANTIDAD,NOMBREPRODUCTO, PRESENTACION,FECHAESPERADA,FECHAPUBLICACION,FECHADECIERRE) values(9999,10,'prueba',10,TO_DATE('2014/03/10','YYYY/MM/DD'),TO_DATE('2014/03/10','YYYY/MM/DD'),TO_DATE('2014/03/10','YYYY/MM/DD'))";
			prepStmt = conexion.prepareStatement(crearLicitacion);
			prepStmt.execute();

			String crearOferta = "Insert into OFERTAS(IDLICITACION,CANTIDAD,NOMBREPRODUCTO,PRESENTACION,DIRPROVEEDOR,FECHAENTREGA,EFECTIVA,COSTOUNITARIO,COSTOTOTAL) values(9999,10,'prueba',10,'prueba@prueba',TO_DATE('2014/03/10','YYYY/MM/DD'),'F',10,10)";
			prepStmt = conexion.prepareStatement(crearOferta);
			prepStmt.execute();
			try{	
				String crearOferta2 = "Insert into OFERTAS(IDLICITACION,CANTIDAD,NOMBREPRODUCTO,PRESENTACION,DIRPROVEEDOR,FECHAENTREGA,EFECTIVA,COSTOUNITARIO,COSTOTOTAL) values(9999,10,'prueba',10,'prueba@prueba',TO_DATE('2014/03/10','YYYY/MM/DD'),'F',10,10)";
				prepStmt = conexion.prepareStatement(crearOferta2);
				prepStmt.execute();

				fail("Debio lanzar exception");

			}
			catch (Exception e) {}


		} catch (Exception e) {

			e.printStackTrace();

		}finally 
		{
			try {

				String eliminarOferta= "Delete from OFERTAS where idlicitacion = 9999 AND DIRPROVEEDOR='prueba@prueba'";
				prepStmt = conexion.prepareStatement(eliminarOferta);
				prepStmt.execute();
				
				String eliminarLicitacion= "Delete from PEDIDO_A_PROVEEDORES where idlicitacion = 9999";
				prepStmt = conexion.prepareStatement(eliminarLicitacion);
				prepStmt.execute();
				
				String eliminarPresentacion = "Delete from PRESENTACIONES where NOMBREPRODUCTO= 'prueba'";
				prepStmt = conexion.prepareStatement(eliminarPresentacion);
				prepStmt.execute();

				String eliminarProducto = "Delete from PRODUCTOS where NOMBREPRODUCTO= 'prueba'";
				prepStmt = conexion.prepareStatement(eliminarProducto);
				prepStmt.execute();
				
				String eliminarProveedor= "Delete from PROVEEDORES where DIRELECTRONICA = 'prueba@prueba'";
				prepStmt = conexion.prepareStatement(eliminarProveedor);
				prepStmt.execute();
				
				String eliminarUsuario = "Delete from USUARIOS where DIRELECTRONICA = 'prueba@prueba'";
				prepStmt = conexion.prepareStatement(eliminarUsuario);
				prepStmt.execute();

				closeConnection();
			} catch (Exception e) {
				fail("No dejo eliminar alguna de las tuplas insertadas");
				e.printStackTrace();
			}
		}
	}
	
	public void testTablaFacturasUsuarios()
	{
		inicializar();
		establecerConexion();
		PreparedStatement prepStmt = null;
		String crearUsuario= "Insert into USUARIOS(DIRELECTRONICA, LOGIN, CLAVE,DOCIDENTIDAD,NOMBREUSUARIO,ESEMPRESA,ROL,TELEFONO,NACIONALIDAD,DEPARTAMENTO,CIUDAD,CODPOSTAL) values('prueba@prueba','prueba','prueba','prueba','prueba','T','P',0,'colombiano','bogota','bogota',0)";
		try {
			//establecerConexion();
			prepStmt = conexion.prepareStatement(crearUsuario);
			prepStmt.execute();

			String crearFactura= "Insert into FACTURAS_USUARIOS(IDFACTURAUSUARIOS,FECHA,DIRUSUARIO,VALIDEZ) values(9999,TO_DATE('2014/03/10','YYYY/MM/DD'),'prueba@prueba','F')";
			prepStmt = conexion.prepareStatement(crearFactura);
			prepStmt.execute();
			try{	
				String crearFactura2= "Insert into FACTURAS_USUARIOS(IDFACTURAUSUARIOS,FECHA,DIRUSUARIO,VALIDEZ) values(9999,TO_DATE('2014/03/10','YYYY/MM/DD'),'prueba@prueba','F')";
				prepStmt = conexion.prepareStatement(crearFactura2);
				prepStmt.execute();

				fail("Debio lanzar exception");

			}
			catch (Exception e) {}


		} catch (Exception e) {

			e.printStackTrace();

		}finally 
		{
			try {
				String eliminarFactura= "Delete from FACTURAS_USUARIOS where IDFACTURAUSUARIOS=9999";
				prepStmt = conexion.prepareStatement(eliminarFactura);
				prepStmt.execute();
				
				String eliminarUsuario = "Delete from USUARIOS where DIRELECTRONICA = 'prueba@prueba'";
				prepStmt = conexion.prepareStatement(eliminarUsuario);
				prepStmt.execute();

				closeConnection();
			} catch (Exception e) {
				fail("No dejo eliminar alguna de las tuplas insertadas");
				e.printStackTrace();
			}
		}
	}
	
}
