package cabandes.fachada;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cabandes.dao.ConsultaDAO;
import cabandes.vos.*;


public class CabaAndes 
{
	public static CabaAndes instancia;
	public String path;

	public ConsultaDAO consultasDAO;
	/**
	 * Es un identificador para proveer la interfaz que el usuario requiere. Solo puede tomar los valores TU_ de la clase ServletTemplate y se vuelve null cuando se envia la pagina web
	 */
	public String tipoUsuarioTransActual;

	public CabaAndes (String pathP)
	{
		path = pathP.replaceAll("TwittSpy", "");
		consultasDAO = new ConsultaDAO();
		consultasDAO.inicializar(path);
		
	}
	/**
	 * retorna la instancia de la Caba andes y si no hay una entonces la crea
	 * @return
	 */
	public static CabaAndes getInstance(String pathPar) {
		if(instancia==null)
			instancia= new CabaAndes( pathPar);
		return instancia;
	}


	//Reemplazarlo por el de la base de datos
	public boolean agregarUsuario(String login, String pasword, String tipo, String ip) {
		
		//TODO
		return true;
	

	}
	public String loginYDarTipo(String nick, String pass, String ipNueva) {
		try {
			UsuarioValue user = consultasDAO.setIpUsuario(nick, pass, ipNueva);
			return (user==null)? null:user.rol;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	public UsuarioValue darUsuario(String remoteAddr) 
	{
		try {
			return consultasDAO.getUsuarioPor(ConsultaDAO.ATRIBUTO_IP, remoteAddr);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public ArrayList<ItemValue> darItemsExistentes(String atributos) {
		try {
			return consultasDAO.getItemsExistentesOrdenadosPor(atributos);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<ItemValue>();
		
	}
	
	public ArrayList<PedidoAProveedorValue> darPedidosAProveedoresCerradosSinAsignar()
	{
		try{
			return consultasDAO.getPedidosAProveedoresCerradosSinAsignar();
		}
		catch (Exception e){
			e.printStackTrace();
			return new ArrayList<PedidoAProveedorValue>();
		}
		
	}
	
	public ArrayList<OfertaDeProveedorValue> darOfertasDeProveedores(String idLicitacion)
	{
		try{
			return consultasDAO.getOfertasDeProveedores(idLicitacion);
		}
		catch (Exception e){
			e.printStackTrace();
			return new ArrayList<OfertaDeProveedorValue>();
		}
	}
	public void cambiarEstadoLicitacion(String proveedor, String licitacion) {
		try{
			consultasDAO.cambiarEstadoLicitacion(proveedor, licitacion);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	public ArrayList<String> darProductosDeTipo(String tipoEnPalabra)
	{
		try {
			return consultasDAO.getProductosDeTipo(tipoEnPalabra);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public ArrayList<String> darPresentProducto(String producto)
	{
		try {
			return consultasDAO.getPresentProducto(producto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public ArrayList<MovimientoNumVentasSolicitudesValue> darProductosConMayorMovimientos(Date fecha1, Date fecha2)
	{
		try {
			return consultasDAO.getProductosConMayorMovimientos(fecha1, fecha2);
		} catch (Exception e){
			e.printStackTrace();
		}
		return new ArrayList<MovimientoNumVentasSolicitudesValue>();
	}
	
	
	public void  registrarPedidio(String type, String product,
			String presentation, String amount, String dirElectronica) {
		String idLocal;
		try {
			idLocal = consultasDAO.getIdLocalConDirElectronica(dirElectronica);
			ArrayList<PedidoDeLocalesValue> pedidos = consultasDAO.getPedididosDeLocales("N", idLocal, false);
			if ( pedidos.size() == 0)
			{
				consultasDAO.addPedidoDeLocal(idLocal);
				pedidos = consultasDAO.getPedididosDeLocales("N", idLocal, false);
			}
			
			int costo = consultasDAO.getCostoDeProducto(product);
			consultasDAO.addItemDePedidoDeLocal(amount, product, presentation, pedidos.get(0).idPedido, costo*Integer.parseInt(presentation));
			//TODO 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public ArrayList<PedidoDeLocalesValue> darPedidosDeLocales(String tipo, String dirElectronica, boolean agregarItems) {

		String idLocal;
		try {
			idLocal = consultasDAO.getIdLocalConDirElectronica(dirElectronica);
			return consultasDAO.getPedididosDeLocales(tipo, idLocal, agregarItems);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public ArrayList<PedidoDeLocalesValue> darPedidosDeLocal(String idLocal, String fInic, String fFin) {

		try {
			return consultasDAO.getPedididosDeLocal( idLocal, fInic,  fFin);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	/**
	 * Trata de satisfacer el pedido, en caso de que no lo pueda hacer por falta de existencias revisa los proximos pedidos que estan por llegar y si tampoco se han pedido entonces crea una nueva licitacion
	 * @param idPedidoHacer
	 */
	public String satisfacerPedidoHastaDondeSePueda(String idPedidoHacer, String dirElectronicaAdmin) {
		try {
			String idLocal = consultasDAO.getIdLocalConDirElectronica(dirElectronicaAdmin);
			PedidoDeLocalesValue pedidoActual = consultasDAO.getPedididosDeLocales("N", idLocal, true).get(0);
			ArrayList<ItemPedidoDeLocales> itemsPedido = pedidoActual.items;
			Calendar fechaPedido = Calendar.getInstance();
			for (int i = 0; i < itemsPedido.size(); i++) 
			{
				ItemPedidoDeLocales itemPedido = itemsPedido.get(i);
				Calendar fecha = tratarDeSatisfacerPedidoDeUnItem(idLocal, itemPedido);
				if (fecha.compareTo(fechaPedido) > 0)
				{
					fechaPedido = fecha;
				}
			}
			
			consultasDAO.cambiarEstadoPedidoDeLocal(idPedidoHacer, "F");
			return "Your order has been processed and probably it will arrive on " + fechaPedido.getTime();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "There was an error while we were porcessed your order";
		} 
	}
	
	public Calendar  tratarDeSatisfacerPedidoDeUnItem( String idLocal, ItemPedidoDeLocales item) {
		try {
			System.out.println("Nuevo item pedido por local: " + idLocal);
			Calendar fechaEsperada = consultasDAO.doPedidoDeItemDeLocal(idLocal,item);
			consultasDAO.cambiarFechaEsperadaAItemPedido(idLocal, item, fechaEsperada);
			return fechaEsperada;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * 
	 */
	public ArrayList<LocalValue> darLocalConMayorVentas(String tipo,
			String consulta) {
		try{
			if(consulta.equals("tipo"))
			{
				return consultasDAO.getLocalConMayorVentas(tipo, 'T');
			}
			else
				return consultasDAO.getLocalConMayorVentas(tipo, 'P');
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new ArrayList<LocalValue>();
		}
		
	}
	public String registrarLlegadaDePedidoALocal(String idPedidoHacer,
			String dirElectronica) {
		try {
			String idLocal = consultasDAO.getIdLocalConDirElectronica(dirElectronica);
			ArrayList<PedidoDeLocalesValue> pedidosNoSatisfechos = consultasDAO.getPedididosDeLocales("F", idLocal, true);
			for (int i = 0; i < pedidosNoSatisfechos.size(); i++) {
				PedidoDeLocalesValue pedido= pedidosNoSatisfechos.get(i);
				if (pedido.idPedido == Integer.parseInt(idPedidoHacer))
				{
					for (int j = 0; j < pedido.items.size(); j++) {
						ItemPedidoDeLocales itemQueLlego = pedido.items.get(j);
						consultasDAO.addItemALocal(itemQueLlego, idLocal);
					}
				}
			}
			consultasDAO.cambiarEstadoPedidoDeLocal(idPedidoHacer, "T");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<AlmacenajeValue> darBodegas()
	{
		ArrayList<AlmacenajeValue> resp = null;
		try{
			resp = consultasDAO.getBodegas(true, null);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		return resp;
	}

	public ArrayList<ProveedorValue> darTodosLosProveedores()
	{
		ArrayList<ProveedorValue> resp = null;
		try{
			resp = consultasDAO.getTodosLosProveedores();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		return resp;
	}
	public String removeBodega(String idBodegaAEliminar) {
		try {
			return consultasDAO.removeBodega(idBodegaAEliminar);
		} catch (Exception e) {
			
			e.printStackTrace();
			return e.getMessage();
		}
	}
	public String addBodega(String idBodegaNueva, String capacidadBodegaNueva,
			String typeBodegaNueva) {
		// TODO Auto-generated method stub
		try {
			return consultasDAO.addBodega(idBodegaNueva, capacidadBodegaNueva, typeBodegaNueva);
		} catch (Exception e) {
			e.printStackTrace();
			return "Fatal Error Has Occured";
		}
	}
	public ArrayList<AlmacenajeValue> darBodegasPorTipoYPorcentaje( String tipoBodega, Double porcentajeDouble) {
		ArrayList<AlmacenajeValue> bodegas = darBodegas();
		ArrayList<AlmacenajeValue> resp = new ArrayList<AlmacenajeValue>();
		for (int i = 0; i <bodegas.size(); i++) {
			AlmacenajeValue bodegaL = bodegas.get(i);
			boolean uu  = ((bodegaL.capacidadUsada/bodegaL.capacidad) > porcentajeDouble);
			boolean u1 = tipoBodega.equals(bodegaL.tipo);
			System.out.println( "_________" + (bodegaL.capacidadUsada/bodegaL.capacidad) +">"+ porcentajeDouble +"   ("+ bodegaL.tipo +"==" +tipoBodega + ")    " + uu + u1);
			if ( ((bodegaL.capacidadUsada/bodegaL.capacidad) > porcentajeDouble) && tipoBodega.charAt(0) == bodegaL.tipo)
			{
				System.out.println("____________SI");
				resp.add(bodegaL);
			}
		}
		return resp;
	}
	public ArrayList<LocalValue> darLocales() throws Exception {
		// TODO Auto-generated method stub
		return consultasDAO.getLocales();
	}
	
	public ArrayList<MovimientoValue> buscarMovimientos(String fecha1, String fecha2, String condicion) {
		
		try{
			return consultasDAO.buscarMovimientos(fecha1, fecha2, condicion);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
	}

}
