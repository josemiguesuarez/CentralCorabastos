package cabandes.dao;

import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import TablaHashing.pruebasTablaHashing;
import listaPrioridad.ListaPrioridad;
import cabandes.complementos.CompBodegaConMasEspacioPriemero;
import cabandes.complementos.PersonalConverter;
import cabandes.servlets.ServletTemplate;
import cabandes.vos.*;

public class ConsultaDAO {

	/**
	 * ruta donde se encuentra el archivo de conexion.
	 */
	private static final String ARCHIVO_CONEXION = "conection.properties";

	/**
	 * nombre de la tabla videos
	 */
	private static final String tablaUsuarios = "Usuarios";

	public static final String ATRIBUTO_IP= "ip";
	public static final String ATRIBUTO_LOGIN_OR_EMAIL="Buscar login o email";

	public static final String ITEMS_JOIN_nombreProducto = "nombreProducto"; 
	public static final String ITEMS_JOIN_tipo="tipo";
	public static final String ITEMS_JOIN_presentacion="presentacion";
	public static final String ITEMS_JOIN_fecha_expiracion="fecha_expiracion"; 
	public static final String ITEMS_JOIN_idAlmacenaje="idAlmacenaje"; 
	public static final String ITEMS_JOIN_cantidad= "cantidad";
	public static final String ITEMS_JOIN_tipoalmacenaje="tipoalmacenaje";
	public static final String ITEMS_JOIN_promocion="promocion";

	public static final String MOV_PROVEEDOR_A_BODEGAS="proveedor a bodegas";
	public static final String MOV_BODEGAS_A_LOCALES="BODEGAS A LOCALES";
	public static final String MOV_LOCALES_A_CLIENTE="LOCALES A CLIENTE";
	public static final String MOV_BODEGAS_A_MAYORISTAS="BODEGAS A MAYORISTA";
	public static final String MOV_ELIMINACION_DE_P_VENCIDOS="ELIMINACION";

	public static final String ETAPA_SALIDA="salida";
	public static final String ETAPA_ENTRADA="entrada";

	public static final String TIPO_TRANSCACION_VENTA="venta";
	public static final String TIPO_TRANSCACION_PEDIDO="pedido";

	private static final int MAX_DATOS_PER = 100;
	public int  inicio = 0;




	//----------------------------------------------------
	//Consultas
	//----------------------------------------------------

	/**
	 * Consulta que 
	 */


	//----------------------------------------------------
	//Atributos
	//----------------------------------------------------
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

	/**
	 * constructor de la clase. No inicializa ningun atributo.
	 */
	public ConsultaDAO() 
	{		

	}

	// -------------------------------------------------
	// Metodos
	// -------------------------------------------------

	/**
	 * obtiene ls datos necesarios para establecer una conexion
	 * Los datos se obtienen a partir de un archivo properties.
	 * @param path ruta donde se encuentra el archivo properties.
	 */
	public void inicializar(String path)
	{
		inicio = 0;
		try
		{
			File arch= new File(path+ARCHIVO_CONEXION);
			Properties prop = new Properties();
			FileInputStream in = new FileInputStream( arch );

			prop.load( in );
			in.close( );

			cadenaConexion = prop.getProperty("url");	// El url, el usuario y passwd deben estar en un archivo de propiedades.
			// url: "jdbc:oracle:thin:@chie.uniandes.edu.co:1521:chie10";
			usuario = prop.getProperty("usuario");	
			clave = prop.getProperty("clave");	
			final String driver = prop.getProperty("driver");
			Class.forName(driver);

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}	
	}

	/**
	 * Metodo que se encarga de crear la conexion con el Driver Manager
	 * a partir de los parametros recibidos.
	 * @param url direccion url de la base de datos a la cual se desea conectar
	 * @param usuario nombre del usuario que se va a conectar a la base de datos
	 * @param clave clave de acceso a la base de datos
	 * @throws SQLException si ocurre un error generando la conexion con la base de datos.
	 */
	private void establecerConexion() throws SQLException
	{
		try
		{
			conexion = DriverManager.getConnection(cadenaConexion,usuario,clave);
			conexion.setAutoCommit(false);
		}
		catch( SQLException e)
		{
			e.printStackTrace();
			throw new SQLException( "ERROR: ConsultaDAO obteniendo una conexion." );
			
		}
	}

	/**
	 *Cierra la conexion activa a la base de datos. Ademas, con=null.
	 * @param con objeto de conexion a la base de datos
	 * @throws SistemaCinesException Si se presentan errores de conexion
	 */
	public void closeConnection() throws Exception {        
		try {
			conexion.commit();
			conexion.close();
			conexion = null;
		} catch (SQLException exception) {
			throw new Exception("ERROR: ConsultaDAO: closeConnection() = cerrando una conexion.");
		}
	} 


	// ---------------------------------------------------
	// Metodos asociados a los casos de uso: Consulta
	// ---------------------------------------------------

	public UsuarioValue getUsuarioPor(String atributo, String valor) throws Exception
	{
		PreparedStatement prepStmt = null;
		UsuarioValue userResp = null;

		try {
			establecerConexion();
			prepStmt = conexion.prepareStatement(sentenciaDarUsuarioPorIp(atributo, valor));

			ResultSet rs = prepStmt.executeQuery();
			if ( rs.next()) 
			{

				boolean esEmpresa = rs.getString ("esEmpresa").equals("T") ;
				String rol = rs.getString("rol");
				if (rol.equals("A"))
				{rol = ServletTemplate.TU_ADMIN;}
				else if (rol.equals("M"))
				{rol = ServletTemplate.TU_ADMIN_CENTRAL;}
				else if (rol.equals("U"))
				{rol = ServletTemplate.TU_MAYORISTA;}
				else if (rol.equals("A"))
				{rol = ServletTemplate.TU_PROVEEDOR;}

				userResp = new UsuarioValue(rs.getString("dirElectronica"), rs.getString("login"), 
						rs.getString("clave"), rs.getString("docIdentidad"), 
						rs.getString("nombreUsuario"), esEmpresa , rol, 
						rs.getInt("telefono"), rs.getString("nacionalidad"), 
						rs.getString("departamento"),rs.getString("ciudad"), 
						rs.getString("codPostal"),rs.getString("ip"));



			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(sentenciaDarUsuarioPorIp(atributo, valor));
			throw new Exception("ERROR = ConsultaDAO: darUsuarioPor Agregando parametros y executando el statement!!!");
		}finally 
		{
			if (prepStmt != null) 
			{
				try {
					prepStmt.close();
				} catch (SQLException exception) {

					throw new Exception("ERROR: ConsultaDAO: darUsuarioPor() =  cerrando una conexion.");
				}
			}
			closeConnection();
		}		
		return userResp;
	}

	/**
	 * Retorna el usuario y cambia su ip en caso de que las credenciales coincidan con la info en la base de datos
	 * @param loginOrDir
	 * @param clave
	 * @param ip
	 * @return usuario o null en caso de que las credenciales no coincidan 
	 * @throws Exception
	 */
	public UsuarioValue setIpUsuario(String loginOrDir, String clave, String ip) throws Exception
	{
		UsuarioValue userCambiarIp = getUsuarioPor(ATRIBUTO_LOGIN_OR_EMAIL, loginOrDir);
		PreparedStatement prepStmt = null;
		if (userCambiarIp != null)
		{
			try {
				establecerConexion();
				prepStmt = conexion.prepareStatement(sentenciaEliminarIp(ip));
				prepStmt.execute();


				prepStmt = conexion.prepareStatement(sentenciaCambiarIp(userCambiarIp,ip));
				prepStmt.execute();


			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println(sentenciaEliminarIp(ip) + " OR " + sentenciaCambiarIp(userCambiarIp,ip));
				throw new Exception("ERROR = ConsultaDAO: darUsuarioPor Agregando parametros y executando el statement!!!");
			}finally 
			{
				if (prepStmt != null) 
				{
					try {
						prepStmt.close();
					} catch (SQLException exception) {

						throw new Exception("ERROR: ConsultaDAO: darUsuarioPor() =  cerrando una conexion.");
					}
				}
				closeConnection();
			}
		}
		return userCambiarIp;
	}

	/**
	 * retorna todos los items ordenados por el parametro que recibe, el 
	 * @param atributos puede ser uno o mas de las constantes que empizan con ITEM_ separados por coma
	 * @return
	 * @throws Exception
	 */
	public ArrayList<ItemValue> getItemsExistentesOrdenadosPor(String atributos) throws Exception
	{
		PreparedStatement prepStmt = null;
		ArrayList <ItemValue> itemsResp = new ArrayList<ItemValue>();

		try {
			establecerConexion();
			prepStmt = conexion.prepareStatement(sentenciaDarExistenciasOrdenadasPor(atributos));
			System.out.println(":::::::::::::::::::::::::Se obtuvo satisfactoriamente el resultado:" + sentenciaDarExistenciasOrdenadasPor(atributos));
			ResultSet rs = prepStmt.executeQuery();

			System.out.println(":::::::::::::::::::::::::Se obtuvo satisfactoriamente el resultado:" + sentenciaDarExistenciasOrdenadasPor(atributos));
			
			int i= inicio;
			while ( rs.next() && i<MAX_DATOS_PER+inicio) 
			{
				System.out.println(":::::::::::::::::::::::::Se obtuvo satisfactoriamente el resultado");
				String tipo = rs.getString("tipo");
				if (tipo.equals("P"))
				{tipo = "Perecedero";}
				else if (tipo.equals("N"))
				{tipo = "No Perecedero";}
				else if (tipo.equals("R"))
				{tipo = "Refrigerado";}

				itemsResp.add(new ItemValue(rs.getString("nombreProducto"), tipo,
						rs.getInt("presentacion"), rs.getDate("fecha_expiracion"), 
						rs.getInt("idAlmacenaje"), rs.getInt("cantidad"), 
						rs.getString("tipoalmacenaje"), rs.getDouble("promocion"), rs.getInt("VALOR_ACTUAL")));	
				i++;
			}


		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(sentenciaDarExistenciasOrdenadasPor(atributos));
			throw new Exception("ERROR = ConsultaDAO: getItemsExistentesOrdenadosPor() parametros y executando el statement!!!");
		}finally 
		{
			if (prepStmt != null) 
			{
				try {
					prepStmt.close();
				} catch (SQLException exception) {

					throw new Exception("ERROR: ConsultaDAO: getItemsExistentesOrdenadosPor() =  cerrando una conexion.");
				}
			}
			closeConnection();
		}		
		return itemsResp;
	}

	/**
	 * El m[etodo se encarga solamente de registrar el movimiento en la tabla de movimientos
	 * @param cantidadARetirar 
	 * @param loginOrDir
	 * @param clave
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	private void addMovimiento(IItemVal item, String tipoTransaccion, int cantidadARetirar) throws Exception
	{
		PreparedStatement prepStmt = null;
		String sentencia = "";
		try {
			//establecerConexion();
			sentencia =  sentenciaRegistrarMovimiento(item, tipoTransaccion, cantidadARetirar);
			prepStmt = conexion.prepareStatement(sentencia);
			prepStmt.execute();


		} catch (SQLException e) {

			System.out.println( sentencia);
			e.printStackTrace();
			throw new Exception("ERROR = ConsultaDAO: addMovimiento = Agregando parametros y executando el statement!!!");
		}finally 
		{
			if (prepStmt != null) 
			{
				try {
					prepStmt.close();
				} catch (SQLException exception) {

					throw new Exception("ERROR: ConsultaDAO: addMovimiento =  cerrando una conexion.");
				}
			}
			//closeConnection();
		}
	}

	/**
	 * pre: el movimiento debe ser legal. El metodo no confirma, solo hace el registro 
	 * pre: la conexion ya se debio haber establecido
	 * @param tipoMovimiento
	 * @param idOrigen
	 * @param idDestino
	 * @param item
	 * @param cantidadARetirar 
	 * @return
	 * @throws Exception
	 */
	public boolean addMovimento ( String tipoMovimiento,String etapa,String idOrigen, String idDestino, IItemVal item, int cantidadARetirar ) throws Exception
	{
		boolean registrarMovimiento = false;
		String tipoTransaccion = "";
		if ( tipoMovimiento.equals(MOV_PROVEEDOR_A_BODEGAS))
		{
			//TODO
			tipoTransaccion = "";
		}
		else if ( tipoMovimiento.equals(MOV_BODEGAS_A_MAYORISTAS))
		{
			//TODO
			tipoTransaccion = "";
		}
		else if ( tipoMovimiento.equals(MOV_BODEGAS_A_LOCALES))
		{
			registrarMovimiento = doMovimientoBodegaALocal(etapa, idOrigen, idDestino, item , cantidadARetirar);
			tipoTransaccion = TIPO_TRANSCACION_PEDIDO;
		}
		else if ( tipoMovimiento.equals(MOV_LOCALES_A_CLIENTE))
		{
			//TODO
			tipoTransaccion = "";
		}
		else if ( tipoMovimiento.equals(MOV_ELIMINACION_DE_P_VENCIDOS))
		{
			//TODO
			tipoTransaccion = "";
		}


		if ( registrarMovimiento)
		{
			addMovimiento(item, tipoTransaccion, cantidadARetirar);
		}
		return registrarMovimiento;
	}

	private boolean doMovimientoBodegaALocal(String etapa, String idOrigen, String idDestino, IItemVal item, int cantidadARetirar) throws Exception 
	{
		PreparedStatement prepStmt = null;
		String sentencia = "";
		try {
			//establecerConexion();
			if(etapa.equals(ETAPA_SALIDA))
			{


				if ( item.getcantidad()-cantidadARetirar == 0)
				{
					sentencia = sentenciaEliminarItemDeBodega (idOrigen, item);
					prepStmt = conexion.prepareStatement(sentencia);
					prepStmt.execute();
				}
				else if ( item.getcantidad()-cantidadARetirar < 0)
				{
					System.out.println("Esto no debi[o haber pasado. Por favor arreglelo:  se pretnde que el el item" +item.getNombre()+ "quede con cantidad = " + (item.getcantidad()-cantidadARetirar));
				}	
				else
				{
					sentencia= sentencaiCambiarCantidadEnAlmacenaje(idOrigen, item, -cantidadARetirar);
					prepStmt = conexion.prepareStatement(sentencia);
					prepStmt.executeUpdate();
				}



				try {
					sentencia = sentenciaAnadirProductoAAlmacenaje(item, idDestino, cantidadARetirar);
					prepStmt = conexion.prepareStatement(sentencia);
					prepStmt.execute();
				}
				catch (Exception e)
				{
					System.out.println("Error al ejecutar la sentencia: " +sentencia);
					System.out.println("Error: (controlado si es de restriccion de unicidad) "+ e.getMessage());;
					sentencia = sentencaiCambiarCantidadEnAlmacenaje(idDestino, item, cantidadARetirar);
					prepStmt = conexion.prepareStatement(sentencia);
					prepStmt.executeUpdate();
				}




				return true;
			}

			else if (etapa.equals(ETAPA_ENTRADA))
			{

				return false;
			}
			else
			{
				throw new Exception("SE debe seleccionar una etapa valida: ETAPA_SALIDA or ETAPA_ENTRADA lo que ud selecciono fue :" + etapa);
			}


		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("ERROR = ConsultaDAO: "+sentencia+" Cambiando el estado de la liciacion");
		}finally 
		{
			if (prepStmt != null) 
			{
				try {
					prepStmt.close();
				} catch (SQLException exception) {

					throw new Exception("ERROR: ConsultaDAO: "+sentencia+" =  cerrando una conexion.");
				}
			}
			//closeConnection();
		}


	}

	private String sentenciaEliminarItemDeBodega(String idOrigen, IItemVal item) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		return "delete from ITEMS_ALMACENAJES where IDALMACENAJE = '"+ idOrigen +"' and NOMBREPRODUCTO = '"+item.getNombre()+"' and PRESENTACION = '"+item.getPresentacion()+"' and FECHA_EXPIRACION = TO_DATE('"+dateFormat.format(item.getFechaExpiracion())+"', 'yyyy/mm/dd')";
	}

	private String sentenciaAnadirProductoAAlmacenaje(IItemVal item, String idDestino, int cantidadARetirar) {

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		return "INSERT INTO items_Almacenajes (cantidad , nombreProducto , presentacion , idAlmacenaje, FECHA_EXPIRACION) VALUES ("+cantidadARetirar+",'"+item.getNombre()+"',"+item.getPresentacion()+","+idDestino+",TO_DATE('"+dateFormat.format(((ItemValue)item).fecha_expiracion)+"', 'yyyy/mm/dd'))";
	}

	public  Calendar doPedidoDeItemDeLocal(String idLocal, ItemPedidoDeLocales item) throws Exception 
	{
		PreparedStatement prepStmt = null;
		ArrayList <ItemValue> itemsResp = new ArrayList<ItemValue>();
		String sentencia = "No se ha hecho nignuna sentencia";
		try {
			establecerConexion();
			sentencia = sentenciaDarExistenciasEnBodegaDeIem(item);
			prepStmt = conexion.prepareStatement(sentencia);

			ResultSet rs = prepStmt.executeQuery();

			int cantidadProducto = 0;
			while ( rs.next()) 
			{
				String tipo = rs.getString("tipo");
				if (tipo.equals("P"))
				{tipo = "Perecedero";}
				else if (tipo.equals("N"))
				{tipo = "No Perecedero";}
				else if (tipo.equals("R"))
				{tipo = "Refrigerado";}
				int canitdadEnEstaBodega = rs.getInt("cantidad");
				String nombreProductoBodega=rs.getString("nombreproducto");
				int presentacionProductoBodega = rs.getInt("presentacion");
				int idAlmacenajeBodega = rs.getInt("idAlmacenaje");
				String tipoAlmacenajeBodega = rs.getString("tipoalmacenaje");
				int valorActual = rs.getInt("valor_actual");
				Double promocion = rs.getDouble("promocion");
				cantidadProducto += canitdadEnEstaBodega;
				itemsResp.add(new ItemValue(
						nombreProductoBodega, 
						tipo,
						presentacionProductoBodega, rs.getDate("fecha_expiracion"), 
						idAlmacenajeBodega, canitdadEnEstaBodega, 
						tipoAlmacenajeBodega, promocion, valorActual));	
			}
			System.out.println("Hay disponible " + cantidadProducto + " "+ item.nombreProducto + " en las bodegas y se necesitan " + item.cantidad);
			if ( cantidadProducto > item.cantidad)
			{
				int cantidadPorStidfacer = item.cantidad;
				for (int i = 0; i < itemsResp.size() && cantidadPorStidfacer != 0; i++) 
				{
					ItemValue itemBodegaActual = itemsResp.get(i);
					int cantidadARetirar= 0;
					if ( itemBodegaActual.cantidad > cantidadPorStidfacer)
					{cantidadARetirar = cantidadPorStidfacer;}
					else 
					{cantidadARetirar = itemBodegaActual.cantidad;}

					itemBodegaActual.cantidad = cantidadARetirar;

					addMovimento(MOV_BODEGAS_A_LOCALES,ETAPA_SALIDA, "" + itemBodegaActual.idAlmacenaje, idLocal, itemBodegaActual, cantidadARetirar);
					cantidadPorStidfacer -= cantidadARetirar;
				}
				Calendar hoy = Calendar.getInstance();
				return hoy;

			}
			else 
			{
				//TODO mirar los proximos pedidos o pedir a proveedores
				Calendar hoy = Calendar.getInstance();
				hoy.add(Calendar.DATE, 10);
				sentencia = sentenciaAgregarLicitacion(item, hoy);
				prepStmt = conexion.prepareStatement(sentencia);
				prepStmt.execute();
				//TODO cambiar el retorno+

				return hoy;
			}


		} catch (SQLException e) {e.printStackTrace();throw new Exception("ERROR: ConsultaDAO: getPedidosAProveedores= Hubo un error al buscar los pedidos a proveedores");}
		finally 
		{if (prepStmt != null) 
		{try {prepStmt.close();} catch (SQLException exception) {throw new Exception("ERROR: ConsultaDAO: getPedidosAProveedores=  cerrando una conexion.");}}
		closeConnection();
		}		
	}

	private String sentenciaAgregarLicitacion(ItemPedidoDeLocales item, Calendar Esperado) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		Calendar hoy = Calendar.getInstance();
		hoy.add(Calendar.DATE, -1);
		return "INSERT INTO PEDIDO_A_PROVEEDORES (IDLICITACION, CANTIDAD, NOMBREPRODUCTO, PRESENTACION, FECHAESPERADA, FECHAPUBLICACION, FECHADECIERRE) "
		+ " VALUES (inc_idPedidoProveedores.nextval, '"+item.cantidad+"', '"+item.nombreProducto+"', '"+item.presentacion+"', TO_DATE('"+dateFormat.format(Esperado.getTime())+"', 'yyyy/mm/dd'), TO_DATE('"+dateFormat.format(date)+"', 'yyyy/mm/dd'), TO_DATE('"+dateFormat.format(hoy.getTime())+"', 'yyyy/mm/dd'))";
	}

	private String sentencaiCambiarCantidadEnAlmacenaje(String idAlmacenaje, IItemVal item, int cantidad) {
		Date fechaExp = ((ItemValue)item).fecha_expiracion;
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		return "UPDATE ITEMS_ALMACENAJES set CANTIDAD = CANTIDAD+"+ cantidad+" where IDALMACENAJE = "+idAlmacenaje+" and nombreproducto = '"+item.getNombre()+"' and PRESENTACION = " + item.getPresentacion()+" and FECHA_EXPIRACION = TO_DATE('"+dateFormat.format(fechaExp)+"', 'yyyy/mm/dd')" ;
	}

	private String sentenciaDarExistenciasEnBodegaDeIem(ItemPedidoDeLocales item) {
		return "SELECT * FROM (ITEMS_ALMACENAJES NATURAL JOIN ALMACENAJES natural left outer join PRESENTACIONES) natural left outer join PRODUCTOS where nombreproducto = '"+item.nombreProducto+"' and presentacion = " + item.presentacion + " and ALMACENAJES.TIPOALMACENAJE = 'Bodega'";
	}

	/**
	 * El metodo devuelve una lista con la informacion de los pedidos a proveedores que aun no estan resultos y ya se cerraron
	 * @return un Arraylist con la informacion de los pedidos que ya se cerraron y aun no estan resueltos
	 * @throws Exception si hay error en la conexion con la base de datos
	 */
	public ArrayList<PedidoAProveedorValue> getPedidosAProveedoresCerradosSinAsignar() throws Exception
	{
		PreparedStatement prepStmt = null;
		ArrayList <PedidoAProveedorValue> resp = new ArrayList<PedidoAProveedorValue>();

		try {
			establecerConexion();
			prepStmt = conexion.prepareStatement(sentenciaDarPedidosCerradosSinAsignar());

			ResultSet rs = prepStmt.executeQuery();

			while ( rs.next()) 
			{
				resp.add(new PedidoAProveedorValue(rs.getString("IDLICITACION"),rs.getInt("CANTIDAD"), 
						rs.getString("NOMBREPRODUCTO"),rs.getInt("PRESENTACION"), 
						rs.getString("PROVEEDORESCOGIDO"),rs.getDate("FECHAESPERADA"), 
						rs.getDate("FECHAPUBLICACION"), rs.getDate("FECHADECIERRE")));
			}

		} catch (SQLException e) {e.printStackTrace();throw new Exception("ERROR: ConsultaDAO: getPedidosAProveedores= Hubo un error al buscar los pedidos a proveedores");}
		finally 
		{if (prepStmt != null) 
		{try {prepStmt.close();} catch (SQLException exception) {throw new Exception("ERROR: ConsultaDAO: getPedidosAProveedores=  cerrando una conexion.");}}
		closeConnection();}		
		return resp;
	}

	public ArrayList<OfertaDeProveedorValue> getOfertasDeProveedores(String idLicitacion) throws Exception
	{
		PreparedStatement prepStmt = null;
		ArrayList <OfertaDeProveedorValue> resp = new ArrayList<OfertaDeProveedorValue>();

		try {
			establecerConexion();
			prepStmt = conexion.prepareStatement(sentenciaDarOfertasDeLicitacion(idLicitacion));
			ResultSet rs = prepStmt.executeQuery();

			while ( rs.next()) 
			{
				String ef= rs.getString("EFECTIVA");
				boolean efectivo = ef.equals("T")?true:false; 

				resp.add(new OfertaDeProveedorValue(rs.getString("DIRPROVEEDOR"), 
						rs.getInt("IDLICITACION"), rs.getString("NOMBREPRODUCTO"), 
						rs.getInt("CANTIDAD"), rs.getInt("PRESENTACION"), 
						rs.getDate("FECHAENTREGA"), efectivo, rs.getInt("COSTOTOTAL")));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("ERROR: ConsultaDAO: getOfertasDeProveedores= Hubo un error al buscar los pedidos a proveedores");
		}finally 
		{
			if (prepStmt != null) 
			{
				try {
					prepStmt.close();
				} catch (SQLException exception) {

					throw new Exception("ERROR: ConsultaDAO: getOfertasDeProveedores=  cerrando una conexion.");
				}
			}
			closeConnection();
		}		
		return resp;
	}

	public void cambiarEstadoLicitacion(String proveedor, String licitacion) throws Exception {

		PreparedStatement prepStmt = null;

		try {
			establecerConexion();
			String sentencia = sentenciaCambiarProveedorDeLicitacion(proveedor, licitacion);
			prepStmt = conexion.prepareStatement(sentencia);
			System.out.println("Llego hasta la sentencia de ejecucion");
			prepStmt.executeUpdate();
			System.out.println("SUCCES!!!");


		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("ERROR = ConsultaDAO: cambiarEstadoLicitacion Cambiando el estado de la liciacion");
		}finally 
		{
			if (prepStmt != null) 
			{
				try {
					prepStmt.close();
				} catch (SQLException exception) {

					throw new Exception("ERROR: ConsultaDAO: cambiarEstadoLicitacion =  cerrando una conexion.");
				}
			}
			closeConnection();
		}
	}

	public ArrayList<MovimientoNumVentasSolicitudesValue> getProductosConMayorMovimientos(Date fecha1 , Date fecha2) throws Exception
	{
		PreparedStatement prepStmt = null;
		ArrayList <MovimientoNumVentasSolicitudesValue> resp = new ArrayList<MovimientoNumVentasSolicitudesValue>();

		try {
			establecerConexion();
			prepStmt = conexion.prepareStatement(sentenciaDarProductosConMayorMovimientos(fecha1, fecha2));
			ResultSet rs = prepStmt.executeQuery();

			while ( rs.next()) 
			{	
				String tipo = rs.getString("TIPO");
				if(tipo.equals("P"))
				{
					tipo = "perishable";
				}
				else if (tipo.equals("N"))
				{
					tipo = "non-perishable";
				}
				else
					tipo = "refrigerated";

				resp.add(new MovimientoNumVentasSolicitudesValue(rs.getString("NOMBREPRODUCTO"), 
						tipo, rs.getInt("PRESENTACION"), rs.getDouble("PESOPROMEDIO"),
						rs.getDouble("COSTOPROMEDIO"), rs.getInt("VECESSOLICITADO"), 
						rs.getInt("VECESVENDIDO")));
			}	
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("ERROR: ConsultaDAO: getOfertasDeProveedores= Hubo un error al buscar los pedidos a proveedores");
		}finally 
		{
			if (prepStmt != null) 
			{
				try {
					prepStmt.close();
				} catch (SQLException exception) {

					throw new Exception("ERROR: ConsultaDAO: getOfertasDeProveedores=  cerrando una conexion.");
				}
			}
			closeConnection();
		}		
		return resp;
	}

	public ArrayList<String> getProductosDeTipo(String tipoEnPalabra) throws Exception
	{
		PreparedStatement prepStmt = null;
		ArrayList <String> resp = new ArrayList<String>();

		try {
			establecerConexion();
			prepStmt = conexion.prepareStatement(sentenciaDarProductosDeTipo(tipoEnPalabra));
			ResultSet rs = prepStmt.executeQuery();

			while ( rs.next()) 
			{ 
				String a = rs.getString("NOMBREPRODUCTO");
				resp.add(a);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("ERROR: ConsultaDAO: getProductosDeTipo= Hubo un error al buscar los pedidos a proveedores");
		}finally 
		{
			if (prepStmt != null) 
			{
				try {
					prepStmt.close();
				} catch (SQLException exception) {

					throw new Exception("ERROR: ConsultaDAO: ggetProductosDeTipo=  cerrando una conexion.");
				}
			}
			closeConnection();
		}		
		return resp;
	}

	public ArrayList<String> getPresentProducto(String producto) throws Exception
	{
		PreparedStatement prepStmt = null;
		ArrayList <String> resp = new ArrayList<String>();

		try {
			establecerConexion();
			prepStmt = conexion.prepareStatement(sentenciaDarPresentacionesProducto(producto));
			ResultSet rs = prepStmt.executeQuery();

			while ( rs.next()) 
			{ 
				String a = rs.getString("Presentacion");
				resp.add(a);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("ERROR: ConsultaDAO: getPresentProducto= Hubo un error al buscar los pedidos a proveedores");
		}finally 
		{
			if (prepStmt != null) 
			{
				try {
					prepStmt.close();
				} catch (SQLException exception) {

					throw new Exception("ERROR: ConsultaDAO: getPresentProducto=  cerrando una conexion.");
				}
			}
			closeConnection();
		}		
		return resp;
	}


	//-------------------------------------------------------------------------------------------
	//Sentencias en metodos
	//-------------------------------------------------------------------------------------------

	private String sentenciaCambiarIp(UsuarioValue userCambiarIp, String ip) {
		return "UPDATE " + tablaUsuarios + " SET ip = '" + ip + "' WHERE dirElectronica = '" + userCambiarIp.dirElectronica + "'";
	}
	private String sentenciaEliminarIp(String ip) {
		return "UPDATE " + tablaUsuarios + " SET ip = NULL WHERE ip = '" + ip + "'";
	}
	private String sentenciaDarUsuarioPorIp(String Atributo, String valor) throws Exception
	{
		if (Atributo.equals(ATRIBUTO_IP))
			return "SELECT * FROM " + tablaUsuarios + " WHERE ip = '"+ valor+"'";
		else if (Atributo.equals(ATRIBUTO_LOGIN_OR_EMAIL))
			return "SELECT * FROM " + tablaUsuarios + " WHERE dirElectronica = '"+ valor + "' or login = '"+ valor +"'"; 
		else
			throw new Exception("Debe seleccionar atributoIP o atributoLoginOREmail como atributos de consulta");
	}
	private String sentenciaDarExistenciasOrdenadasPor(String atributosSeparadosPorComa)
	{
		return "SELECT nombreProducto, tipo, presentacion, fecha_expiracion, idAlmacenaje, cantidad, tipoalmacenaje, promocion, VALOR_ACTUAL "
				+ " FROM (ITEMS_ALMACENAJES NATURAL JOIN PRODUCTOS NATURAL JOIN PRESENTACIONES NATURAL left outer JOIN almacenajes)"
				+ " where rownum < 100 "
				+ " ORDER BY "+ atributosSeparadosPorComa+ " "; 

	}
	private String sentenciaRegistrarMovimiento(IItemVal item, String tipoTransaccion, int cantidadARetirar)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		return "INSERT INTO movimientos (idMovimiento,nombreProducto,presentacion,fecha,peso,costoTotal,TipoDeTransaccion)"
		+ " values(inc_id_Movimientos.nextval,'"+ item.getNombre()+"', "+item.getPresentacion()+",TO_DATE('"+ dateFormat.format(date)+"', 'yyyy/mm/dd')," + item.getcantidad()*item.getPresentacion() +","+item.getcantidad()*item.getCosto()+",'"+ tipoTransaccion+"')";
	}

	private String sentenciaDarPedidosCerradosSinAsignar()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();

		return "select IDLICITACION, CANTIDAD, NOMBREPRODUCTO, PRESENTACION, PROVEEDORESCOGIDO, FECHAESPERADA,"
		+ "FECHAPUBLICACION,FECHADECIERRE FROM PEDIDO_A_PROVEEDORES "
		+ "where PROVEEDORESCOGIDO is null and FECHADECIERRE < TO_DATE('"+dateFormat.format(date)+"', 'yyyy/mm/dd')";
	}

	private String sentenciaDarOfertasDeLicitacion(String idLicitacion)
	{
		return "select * from OFERTAS "
				+ "where IDLICITACION = "+ Integer.parseInt(idLicitacion);
	}

	private String sentenciaCambiarProveedorDeLicitacion(String proveedor, String licitacion)
	{
		return "update PEDIDO_A_PROVEEDORES set PROVEEDORESCOGIDO = '"+proveedor+"' "
				+ "WHERE IDLICITACION="+Integer.parseInt(licitacion);
	}

	private String sentenciaDarProductosDeTipo( String tipoEnPalabra )
	{
		String tipoEnChar = (tipoEnPalabra.equals("Perishable"))?"P":(tipoEnPalabra.equals("Not Perishable"))?"N":"R";
		return "select NOMBREPRODUCTO from productos where TIPO = '" + tipoEnChar+"'";

	}
	private String sentenciaDarPresentacionesProducto( String producto )
	{
		return "Select Presentacion from PRESENTACIONES where NOMBREPRODUCTO = '" + producto+"'";

	}
	private String sentenciaDarProductosConMayorMovimientos(Date fecha1, Date fecha2)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

		return "select * from( " +
		"		PRODUCTOS " +
		"	NATURAL JOIN " +
		"	(Select nombreProducto, presentacion from(  " +
		"		(select nombreProducto, presentacion, count(TipoDetransaccion) as numMovs " +
		"		FROM movimientos " +
		"		WHERE FECHA BETWEEN TO_DATE('"+dateFormat.format(fecha1)+"','YYYY/MM/DD' ) AND TO_DATE('"+dateFormat.format(fecha2)+"','YYYY/MM/DD') " +
		"		group by nombreProducto, presentacion) " +
		"		JOIN " +
		"		(Select max(numMovs) as maximo from " +
		"		(select nombreProducto, presentacion, count(TipoDetransaccion) as numMovs " +
		"		FROM movimientos " +
		"		WHERE FECHA BETWEEN TO_DATE('"+dateFormat.format(fecha1)+"','YYYY/MM/DD' ) AND TO_DATE('"+dateFormat.format(fecha2)+"','YYYY/MM/DD') " +
		"		group by nombreProducto, presentacion)) " +
		"		ON numMovs = maximo)) " +
		"natural join " +
		"		(SELECT nombreproducto, presentacion, avg(peso) as pesopromedio FROM MOVIMIENTOS " +
		"		WHERE FECHA BETWEEN TO_DATE('"+dateFormat.format(fecha1)+"','YYYY/MM/DD' ) AND TO_DATE('"+dateFormat.format(fecha2)+"','YYYY/MM/DD') " +
		" 		GROUP BY nombreProducto, presentacion) " +
		"natural join " +
		" 		(SELECT nombreproducto, presentacion, avg(costototal) as costopromedio FROM MOVIMIENTOS " +
		"		WHERE FECHA BETWEEN TO_DATE('"+dateFormat.format(fecha1)+"','YYYY/MM/DD' ) AND TO_DATE('"+dateFormat.format(fecha2)+"','YYYY/MM/DD') " +
		"		GROUP BY nombreProducto, presentacion) " +
		"natural join " +
		"		(select nombreproducto, presentacion, vecesSolicitado, vecesVendido from( " +
		"			(Select  nombreproducto, presentacion, NVL(vecesSolicitado,0) as vecesSolicitado from(     " +
		"				(SELECT nombreproducto, presentacion FROM MOVIMIENTOS " +
		"				 WHERE FECHA BETWEEN TO_DATE('"+dateFormat.format(fecha1)+"','YYYY/MM/DD' ) AND TO_DATE('"+dateFormat.format(fecha2)+"','YYYY/MM/DD') " +
		" 				 GROUP BY nombreProducto, presentacion) " +
		"			natural left join " +
		"			(SELECT nombreproducto, presentacion, COUNT(tipodetransaccion) as vecesSolicitado FROM MOVIMIENTOS " +
		" 			WHERE tipodetransaccion like '%pedido%' and  " +
		" 			FECHA BETWEEN TO_DATE('"+dateFormat.format(fecha1)+"','YYYY/MM/DD' ) AND TO_DATE('"+dateFormat.format(fecha2)+"','YYYY/MM/DD') " +
		"			GROUP BY nombreProducto, presentacion)))  " +
		" 			join  " +
		"		(Select  nombreproducto as nomP, presentacion as pres, NVL(vecesVendido,0) as vecesVendido from(  " +
		"			(SELECT nombreproducto, presentacion FROM MOVIMIENTOS " +
		"			WHERE FECHA BETWEEN TO_DATE('"+dateFormat.format(fecha1)+"','YYYY/MM/DD' ) AND TO_DATE('"+dateFormat.format(fecha2)+"','YYYY/MM/DD') " +
		" 			GROUP BY nombreProducto, presentacion) " +
		" 		natural left join " +
		"		(SELECT nombreproducto, presentacion, COUNT(tipodetransaccion) as vecesVendido FROM MOVIMIENTOS " +
		" 		WHERE tipodetransaccion like '%venta%' and " +
		"		FECHA BETWEEN TO_DATE('"+dateFormat.format(fecha1)+"','YYYY/MM/DD' ) AND TO_DATE('"+dateFormat.format(fecha2)+"','YYYY/MM/DD') " +
		"		GROUP BY nombreProducto, presentacion))) " +
		"		on nombreProducto=nomP and presentacion=pres)) " +
		"	) " +
		"Order by tipo, nombreProducto ";
	}

	//    public ArrayList<VideosValue> darVideosDefault() throws Exception
	//    {
	//    	PreparedStatement prepStmt = null;
	//    	
	//    	ArrayList<VideosValue> videos = new ArrayList<VideosValue>();
	//		VideosValue vidValue = new VideosValue();
	//    	
	//		try {
	//			establecerConexion(cadenaConexion, usuario, clave);
	//			prepStmt = conexion.prepareStatement(consultaVideosDefault);
	//			
	//			ResultSet rs = prepStmt.executeQuery();
	//			
	//			while(rs.next()){
	//				String titVid = rs.getString(tituloVideo);
	//				int anyoVid = rs.getInt(anyoVideo);
	//				
	//				vidValue.setTituloOriginal(titVid);
	//				vidValue.setAnyo(anyoVid);	
	//			
	//				videos.add(vidValue);
	//				vidValue = new VideosValue();
	//							
	//			}
	//		
	//		} catch (SQLException e) {
	//			e.printStackTrace();
	//			System.out.println(consultaVideosDefault);
	//			throw new Exception("ERROR = ConsultaDAO: loadRowsBy(..) Agregando parametros y executando el statement!!!");
	//		}finally 
	//		{
	//			if (prepStmt != null) 
	//			{
	//				try {
	//					prepStmt.close();
	//				} catch (SQLException exception) {
	//					
	//					throw new Exception("ERROR: ConsultaDAO: loadRow() =  cerrando una conexion.");
	//				}
	//			}
	//			closeConnection(conexion);
	//		}		
	//		return videos;
	//    }

	public String getIdLocalConDirElectronica(String dirElectronica) throws Exception {
		PreparedStatement prepStmt = null;
		String idAlmacenaje = null;

		try {
			establecerConexion();
			prepStmt = conexion.prepareStatement(sentenciaDarIdAlmacenajeConDirElec(dirElectronica));

			ResultSet rs = prepStmt.executeQuery();
			if ( rs.next()) 
			{
				idAlmacenaje = rs.getString("idAlmacenaje");
			}
			else 
			{
				throw new Exception("El usuario no tiene un Local asignado a-un");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(sentenciaDarIdAlmacenajeConDirElec(dirElectronica));
			throw new Exception("ERROR = ConsultaDAO: getIdLocalConDirElectronica = Agregando parametros y executando el statement!!!");
		}finally 
		{
			if (prepStmt != null) 
			{
				try {
					prepStmt.close();
				} catch (SQLException exception) {

					throw new Exception("ERROR: ConsultaDAO: getIdLocalConDirElectronica =  cerrando una conexion.");
				}
			}
			closeConnection();
		}		
		return idAlmacenaje;
	}

	private String sentenciaDarIdAlmacenajeConDirElec(String dirElectronica) {

		return "select * from almacenajes where TIPOALMACENAJE = 'Local' and dirElectronica = '"+ dirElectronica+"'";
	}

	public ArrayList<PedidoDeLocalesValue> getPedididosDeLocales(String tipo, String idLocal, boolean agregarItems) throws Exception {
		String nombreMetodo = "darPedididosDeLocales()";
		String sentencia = "No se hizo ninguna sentencia";
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		ArrayList <PedidoDeLocalesValue> resp = new ArrayList<PedidoDeLocalesValue>();

		try {
			establecerConexion();
			sentencia = sentenciaDarPedidosDeLocalDeTipo (tipo, idLocal);
			System.out.println("______________________________ \n ______________________________\n" + sentencia);
			prepStmt = conexion.prepareStatement(sentencia);
			rs = prepStmt.executeQuery();

			while ( rs.next()) 
			{ 
				int idLocalP = rs.getInt("idLocal");
				int idPedido = rs.getInt("idPedido");
				Date recibido = rs.getDate("recibido");
				Date fechaPedido = rs.getDate("fecha_pedido");
				PedidoDeLocalesValue pedido = new PedidoDeLocalesValue(idLocalP, idPedido, recibido, fechaPedido);
				resp.add(pedido);
				if(agregarItems)
				{
					sentencia = sentenciaDarItemsPedidoDeLocal (idPedido);
					prepStmt = conexion.prepareStatement(sentencia);
					ResultSet rs2 = prepStmt.executeQuery();
					while (rs2.next())
					{
						int idPedidoI = rs2.getInt("idPedido");
						String nombreProducto = rs2.getString("NombreProducto");
						int presentacion = rs2.getInt("Presentacion");
						int cantidad = rs2.getInt("Cantidad");
						int costoUnit = rs2.getInt("costo_unitario");
						Date fechaEsperada = rs2.getDate ("fecha_esperada");
						ItemPedidoDeLocales item = new ItemPedidoDeLocales(idPedidoI, nombreProducto, presentacion, cantidad, costoUnit, fechaEsperada);
						pedido.agregarItem(item);
					}
				}
			}

		} catch (SQLException e) {e.printStackTrace();System.out.println("Sentencia En ejecucion: " + sentencia);throw new Exception("ERROR: ConsultaDAO: " + nombreMetodo+"= Hubo un error al buscar los pedidos a proveedores");
		}finally 
		{if (prepStmt != null){try {prepStmt.close();} catch (SQLException exception) {throw new Exception("ERROR: ConsultaDAO: " + nombreMetodo+"= cerrando una conexion.");}}
		closeConnection();}		
		return resp;
	}

	private String sentenciaDarItemsPedidoDeLocal(int idPedido) {
		return "select * from ITEMS_PEDIDOS_POR_LOCALES where idPedido = " + idPedido;
	}

	private String sentenciaDarPedidosDeLocalDeTipo(String tipo, String idLocal) {

		if ( tipo.equals("N") )
		{
			return "SELECT * FROM pedidos_de_locales where idLocal = "+idLocal+" and recibido > "+PersonalConverter.getDateFormatForDB(new Date())+ "";
		}
		else if (tipo.equals("F") )
		{
			return "SELECT * FROM pedidos_de_locales where idLocal = "+idLocal+" and recibido IS NULL";
		}
		else if (tipo.equals("T"))
		{
			return "SELECT * FROM pedidos_de_locales where idLocal = "+idLocal+" and recibido IS NOT NULL AND recibido <= "+PersonalConverter.getDateFormatForDB(new Date());
		}
		else 
		{
			return "SELECT * FROM pedidos_de_locales where idLocal = "+idLocal+" and " + tipo;
		}
	}

	public void addPedidoDeLocal(String idLocal) throws Exception {
		String nombreMetodo = "addPedidoDeLocal";
		PreparedStatement prepStmt = null;
		String sentencia = "No se ha hecho ninguna sentencia";
		try {
			establecerConexion();

			sentencia = sentenciaCrearNuevoPedido(idLocal);
			prepStmt = conexion.prepareStatement(sentencia);
			prepStmt.execute();


		} catch (SQLException e) {e.printStackTrace();System.out.println(sentencia);throw new Exception("ERROR = ConsultaDAO: "+nombreMetodo+" Cambiando el estado de la liciacion");
		}finally 
		{
			if (prepStmt != null) 
			{try {prepStmt.close();} catch (SQLException exception) {throw new Exception("ERROR: ConsultaDAO: "+nombreMetodo+" =  cerrando una conexion.");}}
			closeConnection();
		}

	}

	private String sentenciaCrearNuevoPedido(String idLocal) {
		Calendar hoy = Calendar.getInstance();
		hoy.add(Calendar.YEAR, 3);
		Date date = hoy.getTime();
		
		return "INSERT INTO pedidos_de_locales (idLocal,idPedido, recibido, fecha_pedido) VALUES ("+idLocal+", inc_id_Pedidos_Locales.nextVal,"+ PersonalConverter.getDateFormatForDB(date)+","+ PersonalConverter.getDateFormatForDB(new Date())+")";
	}

	public int getCostoDeProducto(String product) throws Exception 
	{
		String nombreMetodo = "getCostoDeProducto";
		PreparedStatement prepStmt = null;
		String sentencia = "No se ha hecho ninguna sentencia";
		int a= -1;
		try {
			establecerConexion();
			prepStmt = conexion.prepareStatement(sentenciaDarCostoProducto (product));
			ResultSet rs = prepStmt.executeQuery();

			while ( rs.next()) 
			{ 
				a = rs.getInt("valor_actual");
			}

		} catch (SQLException e) {e.printStackTrace();System.out.println("Sentencia:    " + sentencia);throw new Exception("ERROR: ConsultaDAO: "+nombreMetodo +"= Hubo un error al buscar los pedidos a proveedores");
		}finally 
		{
			if (prepStmt != null) 
			{try {prepStmt.close();} catch (SQLException exception) {throw new Exception("ERROR: ConsultaDAO: "+nombreMetodo +"=  cerrando una conexion.");}}
			closeConnection();
		}		
		return a;
	}

	private String sentenciaDarCostoProducto(String product) {
		return "select * from PRODUCTOS where NOMBREPRODUCTO = '"+product+"'";
	}

	public void addItemDePedidoDeLocal(String amount, String product,
			String presentation, int idPedido, int CostoUnitario) throws Exception 
			{
		String nombreMetodo = "addItemDePedidoDeLocal";
		PreparedStatement prepStmt = null;
		String sentencia = "No se ha hecho ninguna sentencia";
		try {
			establecerConexion();

			sentencia = sentenciaCrearNuevoItemEnPedido (amount,product, presentation,idPedido, CostoUnitario);
			prepStmt = conexion.prepareStatement(sentencia);
			prepStmt.execute();


		} catch (SQLException e) {e.printStackTrace();System.out.println(sentencia);throw new Exception("ERROR = ConsultaDAO: "+nombreMetodo+" Cambiando el estado de la liciacion");
		}finally 
		{
			if (prepStmt != null) 
			{try {prepStmt.close();} catch (SQLException exception) {throw new Exception("ERROR: ConsultaDAO: "+nombreMetodo+" =  cerrando una conexion.");}}
			closeConnection();
		}


			}

	private String sentenciaCrearNuevoItemEnPedido(String amount,
			String product, String presentation, int idPedido, int costoUnitario) {
		return "INSERT INTO ITEMS_PEDIDOS_POR_LOCALES (IDPEDIDO,CANTIDAD, Costo_unitario, nombreproducto,presentacion, fecha_esperada) VALUES ("+idPedido+", "+amount+", "+costoUnitario+", '"+ product+"', "+presentation+", null)";
	}

	public void cambiarEstadoPedidoDeLocal(String idPedidoHacer, String nuevoEstado) throws Exception {
		String nombreMetodo = "cambiarEstadoPedidoDeLocal";
		PreparedStatement prepStmt = null;
		String sentencia = "No se ha hecho la sentencia";
		try {
			establecerConexion();
			sentencia =  sentenciaCambiarestadoPedidoLocal (idPedidoHacer, nuevoEstado);
			prepStmt = conexion.prepareStatement(sentencia);
			prepStmt.executeUpdate();


		} catch (SQLException e) {e.printStackTrace();throw new Exception("ERROR = ConsultaDAO: "+nombreMetodo+" Cambiando el estado de la liciacion");
		}finally 
		{if (prepStmt != null) {try {prepStmt.close();} catch (SQLException exception) {throw new Exception("ERROR: ConsultaDAO: "+nombreMetodo+" =  cerrando una conexion.");}}
		closeConnection();
		}

	}

	private String sentenciaCambiarestadoPedidoLocal(String idPedidoHacer,
			String nuevoEstado) throws Exception {
		if ( nuevoEstado.equals("T"))
		{
			return "Update pedidos_de_locales set recibido = "+ PersonalConverter.getDateFormatForDB(new Date())+ " where idpedido = "+ idPedidoHacer;
		}
		else if ( nuevoEstado.equals("F"))
		{
			return "Update pedidos_de_locales set recibido = null where idpedido = "+ idPedidoHacer;
		}
		else if ( nuevoEstado.equals("N"))
		{
			throw new Exception ("No es normal camiar el estado a nuevo");
		}
		else
		{
			throw new Exception ("Los posibles estados son T, N, F");
		}
		
	}

	/*
	 * Devuelve un arraylist con el o los locale que tienen mayor cantidad de ventas del producto o tipo de producto especificado
	 * tipoConsulta es P si se busca producto, o T si se busca por tipo de producto
	 * tipo: es el nombre del producto o tipo de producto que se busca
	 */
	public ArrayList<LocalValue> getLocalConMayorVentas(String nombre, char tipoConsulta) throws Exception {

		PreparedStatement prepStmt = null;
		ArrayList <LocalValue> resp = new ArrayList<LocalValue>();
		String sentencia = null;
		try {
			establecerConexion();
			sentencia = sentenciaDarLocalesConMayorVentas(nombre, tipoConsulta);
			prepStmt = conexion.prepareStatement(sentencia);
			ResultSet rs = prepStmt.executeQuery();

			while ( rs.next()) 
			{	
				resp.add(new LocalValue(rs.getInt("IDALMACENAJE"), 
						rs.getInt("CAPACIDAD"), rs.getString("TIPOALMACENAJE"), 
						rs.getString("DIRELECTRONICA")));
			}	
		} catch (SQLException e) {
			System.out.println(sentencia);
			e.printStackTrace();
			throw new Exception("ERROR: ConsultaDAO: getOfertasDeProveedores= Hubo un error al buscar los pedidos a proveedores");
		}finally 
		{
			if (prepStmt != null) 
			{
				try {
					prepStmt.close();
				} catch (SQLException exception) {

					throw new Exception("ERROR: ConsultaDAO: getOfertasDeProveedores=  cerrando una conexion.");
				}
			}
			closeConnection();
		}		
		return resp;
	}

	private String sentenciaDarLocalesConMayorVentas(String nombre, char tipoConsulta)
	{
		if(tipoConsulta == 'T')
		{
			return  "select IDALMACENAJE, TIPOALMACENAJE, CAPACIDAD, CAPACIDADUSADA, TIPO, DIRELECTRONICA from ( "+
					"ALMACENAJES JOIN  ((select idlocal,numVentas from( select idlocal, count(*) as numVentas FROM( "+
					"                    items_comprados_en_locales   natural join facturas_locales  NATURAL JOIN productos) WHERE tipo LIKE '"+nombre+"' group by IdLocal))   "+
					"                    JOIN    "+
					"                    (select max(numVentas) as maximo from(    (select count(*) as numVentas    FROM( "+
					"                        items_comprados_en_locales         natural join          facturas_locales        NATURAL JOIN PRODUCTOS)    "+
					"                        WHERE tipo LIKE '"+nombre+"'    group by IdLocal)))    "+
					"                    on numVentas = maximo) 	 "+
					"on IDALMACENAJE = IDLOCAL) ";
		}
		else
			return "select IDALMACENAJE, TIPOALMACENAJE, CAPACIDAD, CAPACIDADUSADA, TIPO, DIRELECTRONICA from (  "+	  
			"ALMACENAJES 	  JOIN 	    "+
			"((select idlocal, numVentas from( 	  select idlocal, count(*) as numVentas  FROM(  "+
			"items_comprados_en_locales natural join facturas_locales)  	     "+
			"WHERE NOMBREPRODUCTO LIKE '"+nombre+"' 	      "+
			"group by IdLocal)) 	   "+
			"JOIN 	     "+
			"(select max(numVentas) as maximo from( 	      "+
			"    select count(*) as numVentas    FROM(  "+
			"          items_comprados_en_locales               natural join               facturas_locales) 	   "+   
			"          WHERE NOMBREPRODUCTO LIKE '"+nombre+"' 	    group by IdLocal))  "+
			" on numVentas = maximo) 	  "+
			"	on IDALMACENAJE = IDLOCAL)  ";
	}

	public void addItemALocal(ItemPedidoDeLocales itemQueLlego, String idLocal) throws Exception 
	{
		String nombreMetodo = "addItemALocal";
		PreparedStatement prepStmt = null;
		String sentencia = "No se ha hecho la sentencia";
		try {
			establecerConexion();
			addMovimento(MOV_BODEGAS_A_LOCALES, ETAPA_ENTRADA, null, idLocal, itemQueLlego, itemQueLlego.getcantidad());


		} catch (SQLException e) {System.out.println("Hubo un error en la sentencia: " + sentencia);e.printStackTrace();throw new Exception("ERROR = ConsultaDAO: "+nombreMetodo+" Cambiando el estado de la liciacion");
		}finally 
		{if (prepStmt != null) {try {prepStmt.close();} catch (SQLException exception) {throw new Exception("ERROR: ConsultaDAO: "+nombreMetodo+" =  cerrando una conexion.");}}
		closeConnection();
		}

	}

	public void cambiarFechaEsperadaAItemPedido(String idLocal, ItemPedidoDeLocales item, Calendar fechaEsperada) throws Exception {
		String nombreMetodo = "cambiarFechaEsperadaAItemPedido";
		PreparedStatement prepStmt = null;
		String sentencia = "No se ha hecho la sentencia";
		try {
			establecerConexion();
			sentencia =  sentenciaCambiarFechaEsperadaAItemPedido(item, fechaEsperada);
			prepStmt = conexion.prepareStatement(sentencia);
			prepStmt.executeUpdate();


		} catch (SQLException e) {e.printStackTrace();throw new Exception("ERROR = ConsultaDAO: "+nombreMetodo+" Cambiando el estado de la liciacion");
		}finally 
		{if (prepStmt != null) {try {prepStmt.close();} catch (SQLException exception) {throw new Exception("ERROR: ConsultaDAO: "+nombreMetodo+" =  cerrando una conexion.");}}
		closeConnection();
		}

	}

	private String sentenciaCambiarFechaEsperadaAItemPedido( ItemPedidoDeLocales item, Calendar fechaEsperada) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

		return "UPDATE ITEMS_PEDIDOS_POR_LOCALES SET FECHA_ESPERADA = TO_DATE('"+dateFormat.format(fechaEsperada.getTime())+"', 'yyyy/mm/dd') "+
		" WHERE IDPEDIDO = "+item.idPedidoI+" AND NOMBREPRODUCTO = '"+item.nombreProducto+"' and PRESENTACION = "+item.presentacion;
	}

	public ArrayList<AlmacenajeValue> getBodegas(boolean establecerYCerrarConexxion, String condiciones) throws Exception
	{
		PreparedStatement prepStmt = null;
		ArrayList <AlmacenajeValue> resp = new ArrayList<AlmacenajeValue>();
		String sentencia = null;
		try {
			if ( establecerYCerrarConexxion)
				establecerConexion();
			sentencia = sentenciaDarBodegas(condiciones);
			prepStmt = conexion.prepareStatement(sentencia);
			ResultSet rs = prepStmt.executeQuery();

			while ( rs.next()) 
			{	
				resp.add(new AlmacenajeValue(rs.getInt("IDALMACENAJE"),  rs.getString("TIPOALMACENAJE"),
						rs.getInt("CAPACIDAD"),rs.getInt("CAPACIDADREALUSADA"), rs.getString("TIPO").charAt(0),
						rs.getString("DIRELECTRONICA"), rs.getString("NOMBREALMACENAJE")));
			}	
		} catch (SQLException e) {
			System.out.println(sentencia);
			e.printStackTrace();
			throw new Exception("ERROR: ConsultaDAO: getBodegas = Hubo un error al obtener las bodegas");
		}finally 
		{
			if (prepStmt != null) 
			{
				try {
					prepStmt.close();
				} catch (SQLException exception) {

					throw new Exception("ERROR: ConsultaDAO: getBodegas = cerrando una conexion.");
				}
			}
			if ( establecerYCerrarConexxion)
				closeConnection();
		}		
		return resp;
	}

	private String sentenciaDarBodegas(String condiciones)
	{
		String condicionesTotales = "";
		if ( condiciones != null && !condiciones.equals(""))
		{
			condicionesTotales = "  WHERE TIPOALMACENAJE LIKE 'Bodega' AND " + condiciones;
		}
		else 
		{
			condicionesTotales = "  WHERE TIPOALMACENAJE LIKE 'Bodega'";
		}
		

		return "SELECT "
				+" CAPACIDAD,"
				+" DIRELECTRONICA,"
				+" IDALMACENAJE,"
				+" NOMBREALMACENAJE,"
				+" TIPO,"
				+" TIPOALMACENAJE,"
				+" NVL(CAPUSADA,0) as CAPACIDADREALUSADA"
				+" from ( ALMACENAJES natural left outer join (SELECT IDALMACENAJE, SUM(CANTIDAD*PRESENTACION) as CAPUSADA from ITEMS_ALMACENAJES GROUP BY IDALMACENAJE))"
				+ condicionesTotales
				+ "ORDER BY TIPO, IDALMACENAJE";
			}

	public ArrayList<ProveedorValue> getTodosLosProveedores() throws Exception
	{
		PreparedStatement prepStmt = null;
		ArrayList <ProveedorValue> resp = new ArrayList<ProveedorValue>();
		String sentencia = null;
		try {
			establecerConexion();
			sentencia = sentenciaDarTodosLosProveedores();
			prepStmt = conexion.prepareStatement(sentencia);
			ResultSet rs = prepStmt.executeQuery();

			while ( rs.next()) 
			{	
				resp.add(new ProveedorValue(rs.getString("DIRELECTRONICA"),
						rs.getString("TIPOPRODUCTOS").charAt(0),rs.getString("LOGIN"),
						rs.getString("DOCIDENTIDAD"),rs.getString("NOMBREUSUARIO"),
						rs.getString("ESEMPRESA").charAt(0),rs.getInt("TELEFONO"),
						rs.getString("NACIONALIDAD"),rs.getString("DEPARTAMENTO"),
						rs.getString("CIUDAD"),rs.getString("CODPOSTAL")));
			}	
		} catch (SQLException e) {
			System.out.println(sentencia);
			e.printStackTrace();
			throw new Exception("ERROR: ConsultaDAO: getTodosLosProveedores = Hubo un error al obtener los proveedores");
		}finally 
		{
			if (prepStmt != null) 
			{
				try {
					prepStmt.close();
				} catch (SQLException exception) {

					throw new Exception("ERROR: ConsultaDAO: getTodosLosProveedores = cerrando una conexion.");
				}
			}
			closeConnection();
		}		
		return resp;
	}

	private String sentenciaDarTodosLosProveedores()
	{
		return "SELECT * FROM(USUARIOS "
				+ " NATURAL JOIN "
				+ " (SELECT * FROM proveedores)) ";
	}

	public String removeBodega(String idBodegaAEliminar) throws Exception {
		String nombreMetodo = "removeBodega";
		PreparedStatement prepStmt = null;
		String sentencia = "No se ha hecho ninguna sentencia";
		try {
			establecerConexion();
			//TODO
			String ensaje = equlibrarParaEliminar(Integer.valueOf(idBodegaAEliminar));
			if ( ensaje.equals(""))
			{
				sentencia = sentenciaEliminarBodega(idBodegaAEliminar);
				prepStmt = conexion.prepareStatement(sentencia);
				prepStmt.execute();
			}
			else
			{
				sentencia = "ROLLBACK";
				prepStmt = conexion.prepareStatement(sentencia);
				prepStmt.execute();
			}
			

			return "Storage" + idBodegaAEliminar + "has been removed successfully";
		} catch (SQLException e) {e.printStackTrace();System.out.println(sentencia);return "An error has occurred, please try again";
		}finally 
		{
			if (prepStmt != null) 
			{try {prepStmt.close();} catch (SQLException exception) {throw new Exception("ERROR: ConsultaDAO: "+nombreMetodo+" =  cerrando una conexion.");}}
			closeConnection();
		}
	}

	private String equlibrarParaEliminar(int idBodegaAEliminar) throws Exception 
	{
		ArrayList<AlmacenajeValue> bodegasArray = getBodegas(false, "IDALMACENAJE = " + idBodegaAEliminar);
		//Se Cargan los datos indispenzables para ejecutar el metodo
		bodegasArray = getBodegas(false, "TIPO = '" + bodegasArray.get(0).tipo+"'");
		
		ListaPrioridad<AlmacenajeValue> bodegasPrioridad = new ListaPrioridad<AlmacenajeValue>(new CompBodegaConMasEspacioPriemero());	
		for (int i = 0; i < bodegasArray.size() && i<4; i++) {
			bodegasPrioridad.agregarElemento(bodegasArray.get(i));
		}
		AlmacenajeValue bodegaAEliminar = bodegasPrioridad.eliminarElemento(new AlmacenajeValue(idBodegaAEliminar, null, 0, 0, 'o', null, ""));
		if ( bodegaAEliminar == null)
		{
			for (int i = 0; i < bodegasArray.size(); i++) {
				if ( bodegasArray.get(i).idAlmacenaje == idBodegaAEliminar )
					bodegaAEliminar = bodegasArray.get(i);
			}
		}
		//Se carga la lista de itemes en el AlmacenjaeValue
		getItemsAlmacenaje(bodegaAEliminar, false);

		
		// Se ejecuta el metodo
		while (bodegaAEliminar.itemsAlmacenaje != null && bodegaAEliminar.itemsAlmacenaje.darPrimero() != null && bodegasPrioridad.darPrimero() != null)
		{
			IItemVal itemCambiarAOtraBodega = bodegaAEliminar.itemsAlmacenaje.darPrimero();
			AlmacenajeValue bodegaMasVacia = bodegasPrioridad.darPrimero();
			int capacidadDisponibleEnBodega = (int) (bodegaMasVacia.getCapaDisponi());
			int pesoTotalItemCambiarDeBodega = (itemCambiarAOtraBodega.getcantidad()*itemCambiarAOtraBodega.getPresentacion());
			
			if ((capacidadDisponibleEnBodega ) >= pesoTotalItemCambiarDeBodega )
			{
				addMovimento(MOV_BODEGAS_A_LOCALES, ETAPA_SALIDA, "" +idBodegaAEliminar,""+bodegasPrioridad.darPrimero().idAlmacenaje , itemCambiarAOtraBodega, itemCambiarAOtraBodega.getcantidad());

				AlmacenajeValue bodegaADondeFueEnviadoElItem = bodegasPrioridad.eliminarPrimero();
				bodegaADondeFueEnviadoElItem.capacidadUsada += itemCambiarAOtraBodega.getPesoTotal();
				if (bodegaADondeFueEnviadoElItem.getCapaDisponi() != 0)
				{
					bodegasPrioridad.agregarElemento(bodegaADondeFueEnviadoElItem);
				}
				bodegaAEliminar.itemsAlmacenaje.eliminarPrimero();
			}
			else
			{
				if (capacidadDisponibleEnBodega >= itemCambiarAOtraBodega.getPresentacion())
					addMovimento(MOV_BODEGAS_A_LOCALES, ETAPA_SALIDA, "" +idBodegaAEliminar,""+bodegasPrioridad.darPrimero().idAlmacenaje , itemCambiarAOtraBodega, (capacidadDisponibleEnBodega/itemCambiarAOtraBodega.getPresentacion()));

				bodegasPrioridad.eliminarPrimero();
				itemCambiarAOtraBodega.setCantidad ( itemCambiarAOtraBodega.getcantidad()-(capacidadDisponibleEnBodega/itemCambiarAOtraBodega.getPresentacion()));
				System.out.println();
			}
		}
		if (bodegaAEliminar.itemsAlmacenaje != null && bodegaAEliminar.itemsAlmacenaje.darPrimero() != null)
		{
			
			return "ERROR: It was imposible to delete the Storage " + bodegaAEliminar.nombreAlmacenaje + ", because there are no Storages to store the Items";
			
		}
		return "";



	}

	private String sentenciaEliminarBodega(String idBodegaAEliminar) 
	{
		return "delete from ALMACENAJES where IDALMACENAJE = " + idBodegaAEliminar;
	}


	public void getItemsAlmacenaje(AlmacenajeValue almacenjae, boolean establecerYCerrarConexion) throws Exception
	{
		String nombreMetodo = "getItemsAlmacenaje"; 
		PreparedStatement prepStmt = null;
		String sentencia = null;
		try {
			if(establecerYCerrarConexion)
				establecerConexion();
			sentencia = sentenciaDarItemsAlmacenaje(almacenjae);
			prepStmt = conexion.prepareStatement(sentencia);
			ResultSet rs = prepStmt.executeQuery();

			while ( rs.next()) 
			{	
				almacenjae.addItem(new ItemValue(rs.getString("NOMBREPRODUCTO"), null, rs.getInt("PRESENTACION"), rs.getDate("FECHA_EXPIRACION"), rs.getInt("IDALMACENAJE"),
						rs.getInt("CANTIDAD"), null, 0, 0));
			}	
		} catch (SQLException e) {
			System.out.println(sentencia);
			e.printStackTrace();
			throw new Exception("ERROR: ConsultaDAO: "+ nombreMetodo+" = Hubo un error al obtener los itmes");
		}finally 
		{
			if (prepStmt != null) 
			{
				try {
					prepStmt.close();
				} catch (SQLException exception) {

					throw new Exception("ERROR: ConsultaDAO: "+ nombreMetodo+" = cerrando una conexion.");
				}
			}
			if(establecerYCerrarConexion)
				closeConnection();
		}
	}

	private String sentenciaDarItemsAlmacenaje(AlmacenajeValue almacenjae) {

		return "Select * from items_almacenajes where IDALMACENAJE="+ almacenjae.idAlmacenaje;
	}

	public String addBodega(String nombreBodegaNueva, String capacidadBodegaNueva, String typeBodegaNueva) throws Exception {
		String nombreMetodo = "addBodega";
		PreparedStatement prepStmt = null;
		String sentencia = "No se ha hecho ninguna sentencia";
		try {
			establecerConexion();

			try {
				sentencia = sentenciaCrearNuevaBodega(nombreBodegaNueva, capacidadBodegaNueva, typeBodegaNueva);
				prepStmt = conexion.prepareStatement(sentencia);
				prepStmt.execute();
			}catch(SQLException e)
			{
				return "It was Imposible to insert the new Storage called" + nombreBodegaNueva + ", because the name already exist";
			}


			ArrayList<AlmacenajeValue> bodegas = getBodegas(false, "TIPO = '" + typeBodegaNueva +"'");
			ListaPrioridad<AlmacenajeValue> bodegasOrdenadasPorDisp= new ListaPrioridad<AlmacenajeValue>(new CompBodegaConMasEspacioPriemero());
			for (int i = 0; i < bodegas.size(); i++) {bodegasOrdenadasPorDisp.agregarElemento(bodegas.get(i));}
			AlmacenajeValue bodegaNueva = bodegasOrdenadasPorDisp.eliminarElemento(new AlmacenajeValue(0, "", 0, 0, ' ', "", nombreBodegaNueva));
			AlmacenajeValue bodMenorDisp = bodegasOrdenadasPorDisp.darUltimo();
			bodegasOrdenadasPorDisp.eliminarElemento(bodMenorDisp);
			AlmacenajeValue bodMayorDisp = bodegasOrdenadasPorDisp.darPrimero();
			while (bodMayorDisp != null 
					&& ( bodegaNueva.getCapaDisponi())
					> (bodMayorDisp.getCapaDisponi()))
			{
				if ( bodMenorDisp.itemsAlmacenaje == null)
				{
					getItemsAlmacenaje(bodMenorDisp, false);
				}
				IItemVal itemAPasar = null;
				if ( bodMenorDisp.itemsAlmacenaje != null)
				{
					itemAPasar = bodMenorDisp.itemsAlmacenaje.darPrimero();
				}
				
				
				if ( itemAPasar != null)
				{
					int cantidadAPasar = 0;
					if ( itemAPasar.getPesoTotal() > bodegaNueva.getCapaDisponi())
					{
						cantidadAPasar =(int)bodMenorDisp.getCapaDisponi()/itemAPasar.getPresentacion();
						addMovimento(MOV_BODEGAS_A_LOCALES, ETAPA_SALIDA, ""+bodMenorDisp.idAlmacenaje, ""+bodegaNueva.idAlmacenaje, itemAPasar, cantidadAPasar);
						bodMenorDisp.capacidadUsada -= cantidadAPasar*itemAPasar.getPresentacion();
						bodegaNueva.capacidadUsada += cantidadAPasar*itemAPasar.getPresentacion();
					}
					else
					{
						cantidadAPasar = itemAPasar.getcantidad();
						addMovimento(MOV_BODEGAS_A_LOCALES, ETAPA_SALIDA, ""+bodMenorDisp.idAlmacenaje, ""+bodegaNueva.idAlmacenaje, itemAPasar, cantidadAPasar);
						bodMenorDisp.capacidadUsada -= cantidadAPasar*itemAPasar.getPresentacion();
						bodegasOrdenadasPorDisp.agregarElemento(bodMenorDisp);
						bodMenorDisp.itemsAlmacenaje.eliminarPrimero();
						bodegaNueva.capacidadUsada += cantidadAPasar*itemAPasar.getPresentacion();
					}
					
				}
					

				bodMenorDisp = bodegasOrdenadasPorDisp.darUltimo();
				bodMayorDisp = bodegasOrdenadasPorDisp.darPrimero();
				bodegasOrdenadasPorDisp.eliminarElemento(bodMenorDisp);
				
			}




			return "Storage" + nombreBodegaNueva + "has been registered successfully";
		} catch (SQLException e) {e.printStackTrace();System.out.println(sentencia);return "An error has occurred, please try again";
		}finally 
		{
			if (prepStmt != null) 
			{try {prepStmt.close();} catch (SQLException exception) {throw new Exception("ERROR: ConsultaDAO: "+nombreMetodo+" =  cerrando una conexion.");}}
			closeConnection();
		}
	}

	private String sentenciaCrearNuevaBodega(String idBodegaNueva, String capacidadBodegaNueva, String typeBodegaNueva) {
		// TODO Auto-generated method stub
		return "INSERT INTO ALMACENAJES (IDALMACENAJE, TIPOALMACENAJE, CAPACIDAD, CAPACIDADUSADA, TIPO, DIRELECTRONICA, NOMBREALMACENAJE) VALUES (inc_id_Almacenajes.nextVal, 'Bodega', '"+capacidadBodegaNueva+"', '0', '"+typeBodegaNueva+"',  null, '"+idBodegaNueva+"')";
	}

	public ArrayList<MovimientoValue> buscarMovimientos(String fecha1,
			String fecha2, String condicion) {
		
		ArrayList<MovimientoValue> movimientos = new ArrayList<MovimientoValue>();
		String sentencia = sentenciaBuscarMovimientos(fecha1, fecha2, condicion);
		

		PreparedStatement prepStmt = null;
		try {
			establecerConexion();
			//TODO
			prepStmt = conexion.prepareStatement(sentencia);
			
			long tiempo = System.currentTimeMillis();
			
			ResultSet rs = prepStmt.executeQuery();
			
			System.out.println( sentencia);
			long tiempof = System.currentTimeMillis() - tiempo;
			System.out.println("tiempo de consulta de movimientos en milisegundos: "+ (System.currentTimeMillis() - tiempo));
			
			while(rs.next())
			{
				movimientos.add(new MovimientoValue(rs.getString("idmovimiento"), rs.getString("nombreproducto"), rs.getString("presentacion"), 
						rs.getString("fecha"), rs.getString("peso"), rs.getString("costoTotal"), rs.getString("tipodeTransaccion"), 
						rs.getString("IDALMACENAJEENTRADA"), rs.getString("IDALMACENAJESALIDA")));
			}
			pruebasTablaHashing.mostrarTiempo(tiempof);
			

		}	
		catch (SQLException e) 
		{
			e.printStackTrace();

		}
		finally 
		{
			try {
				closeConnection();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return movimientos;

	}
	
	private String sentenciaBuscarMovimientos(String fecha1,
			String fecha2, String condicion)
	{
		return "SELECT * FROM MOVIMIENTOS WHERE fecha BETWEEN TO_DATE('"+fecha1+"','YYYY/MM/DD') "
				+ " AND TO_DATE('"+fecha2+"','YYYY/MM/DD') "+ condicion;
	}

	public ArrayList<LocalValue> getLocales() throws Exception {
		PreparedStatement prepStmt = null;
		ArrayList <LocalValue> resp = new ArrayList<LocalValue>();
		String sentencia = null;
		try {
			establecerConexion();
			sentencia = sentenciaDarLocales();
			prepStmt = conexion.prepareStatement(sentencia);
			ResultSet rs = prepStmt.executeQuery();

			while ( rs.next()) 
			{	
				resp.add(new LocalValue(rs.getInt("IDALMACENAJE"), 
						rs.getInt("CAPACIDAD"), rs.getString("TIPOALMACENAJE"), 
						rs.getString("DIRELECTRONICA")));
			}	
		} catch (SQLException e) {
			System.out.println(sentencia);
			e.printStackTrace();
			throw new Exception("ERROR: ConsultaDAO: getOfertasDeProveedores= Hubo un error al buscar los pedidos a proveedores");
		}finally 
		{
			if (prepStmt != null) 
			{
				try {
					prepStmt.close();
				} catch (SQLException exception) {

					throw new Exception("ERROR: ConsultaDAO: getOfertasDeProveedores=  cerrando una conexion.");
				}
			}
			closeConnection();
		}		
		return resp;
	}

	private String sentenciaDarLocales() {
		return "SELECT * FROM Almacenajes WHERE tipoAlmacenaje = 'Local'";
	}

	public ArrayList<PedidoDeLocalesValue> getPedididosDeLocal(String idLocal, String fInic, String fFin) throws Exception {
		String nombreMetodo = "getPedididosDeLocal()";
		String sentencia = "No se hizo ninguna sentencia";
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		ArrayList <PedidoDeLocalesValue> resp = new ArrayList<PedidoDeLocalesValue>();

		try {
			establecerConexion();
			sentencia = sentenciaDarPedidosDeLocalDeTipo ( idLocal, fInic, fFin);
			System.out.println("------------------" + sentencia );
			prepStmt = conexion.prepareStatement(sentencia);
			rs = prepStmt.executeQuery();

			while ( rs.next()) 
			{ 
				int idLocalP = rs.getInt("idLocal");
				int idPedido = rs.getInt("idPedido");
				Date recibido = rs.getDate("recibido");
				Date fechaPedido = rs.getDate("fecha_pedido");
				PedidoDeLocalesValue pedido = new PedidoDeLocalesValue(idLocalP, idPedido, recibido, fechaPedido);
				resp.add(pedido);
				
			}

		} catch (SQLException e) {e.printStackTrace();System.out.println("Sentencia En ejecucion: " + sentencia);throw new Exception("ERROR: ConsultaDAO: " + nombreMetodo+"= Hubo un error al buscar los pedidos a proveedores");
		}finally 
		{if (prepStmt != null){try {prepStmt.close();} catch (SQLException exception) {throw new Exception("ERROR: ConsultaDAO: " + nombreMetodo+"= cerrando una conexion.");}}
		closeConnection();}		
		return resp;
	}

	private String sentenciaDarPedidosDeLocalDeTipo(String idLocal,
			String fInic, String fFin) throws Exception {
		if ( fFin == null || fInic == null)
		{
			throw new Exception (" Please select dates");		}
		return "SELECT * FROM pedidos_de_locales WHERE idLocal = "+ idLocal+" AND " 
				+ "((recibido >= "+PersonalConverter.getDateFormatForDB(fInic)+" AND recibido <= "+PersonalConverter.getDateFormatForDB(fFin)+" )"
					+	" or (FECHA_PEDIDO >= "+PersonalConverter.getDateFormatForDB(fInic)+" AND FECHA_PEDIDO <= "+PersonalConverter.getDateFormatForDB(fFin)+" ))"
						+ " ORDER BY FECHA_PEDIDO";
	}
}
