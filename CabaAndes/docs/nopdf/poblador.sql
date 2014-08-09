----- Incrementos

CREATE SEQUENCE inc_idFacturaLocal
INCREMENT BY 1
START WITH 1;
CREATE SEQUENCE inc_idPedidoProveedores
INCREMENT BY 1
START WITH 1;
CREATE SEQUENCE inc_id_Almacenajes
INCREMENT BY 1
START WITH 1;
CREATE SEQUENCE inc_id_Pedidos_Locales
INCREMENT BY 1
START WITH 1;
CREATE SEQUENCE inc_id_Movimientos
INCREMENT BY 1
START WITH 1;

--- poblamiento de tablas
---- Usuarios
INSERT INTO usuarios (dirElectronica, login, clave, docIdentidad, nombreUsuario, esEmpresa, rol, telefono, nacionalidad, departamento, ciudad, codPostal, ip )
VALUES ('jose_su@hotmail.com', 'josems', '111', '101550507', 'Jose Miguel Suarez Lopera', 'T', 'U', 10200211, 'colombia', 'bogota', 'bogota', 'sisosis', '0090'); 
INSERT INTO usuarios (dirElectronica, login, clave, docIdentidad, nombreUsuario, esEmpresa, rol, telefono, nacionalidad, departamento, ciudad, codPostal, ip )
VALUES ('jose_su_lo@hotmail.com', 'jose', '111', '101550507', 'Jose Miguel Suarez Lopera', 'T', 'A', 10200211, 'colombia', 'bogota', 'bogota', 'sisosis', '00002'); 
INSERT INTO usuarios (dirElectronica, login, clave, docIdentidad, nombreUsuario, esEmpresa, rol, telefono, nacionalidad, departamento, ciudad, codPostal, ip )
VALUES ('jose_@hotmail.com', 'josem', '111', '101550507', 'Jose Miguel Suarez Lopera', 'T', 'M', 10200211, 'colombia', 'bogota', 'bogota', 'sisosis', '002002'); 
INSERT INTO usuarios (dirElectronica, login, clave, docIdentidad, nombreUsuario, esEmpresa, rol, telefono, nacionalidad, departamento, ciudad, codPostal, ip )
VALUES ('jose@hotmail.com', 'josep', '111', '101550507', 'Jose Miguel Suarez Lopera', 'T', 'P', 10200211, 'colombia', 'bogota', 'bogota', 'sisosis', '0020002'); 
INSERT INTO usuarios (dirElectronica, login, clave, docIdentidad, nombreUsuario, esEmpresa, rol, telefono, nacionalidad, departamento, ciudad, codPostal, ip )
VALUES ('jose_isos@hotmail.com', 'josepr', '111', '101550507', 'Jose Miguel Suarez Lopera', 'T', 'P', 10200211, 'colombia', 'bogota', 'bogota', 'sisosis', '002000582'); 
INSERT INTO usuarios (dirElectronica, login, clave, docIdentidad, nombreUsuario, esEmpresa, rol, telefono, nacionalidad, departamento, ciudad, codPostal, ip )
VALUES ('jose_miguel@hotmail.com', 'josepn', '111', '101550507', 'Jose Miguel Suarez Lopera', 'T', 'P', 10200211, 'colombia', 'bogota', 'bogota', 'sisosis', '0020008552'); 

Insert into proveedores (DIRELECTRONICA, TIPOPRODUCTOS)
values ('jose@hotmail.com', 'P');
Insert into proveedores (DIRELECTRONICA, TIPOPRODUCTOS)
values ('jose_isos@hotmail.com', 'R');
Insert into proveedores (DIRELECTRONICA, TIPOPRODUCTOS)
values ('jose_miguel@hotmail.com', 'N');

---- Productos
INSERT INTO productos (nombreProducto, tipo, VALOR_ACTUAL)
VALUES('pera','P', 500);
INSERT INTO productos (nombreProducto, tipo, VALOR_ACTUAL)
VALUES('manzana','P', 200);
INSERT INTO productos (nombreProducto, tipo, VALOR_ACTUAL)
VALUES('mandarina','P', 300);
INSERT INTO productos (nombreProducto, tipo, VALOR_ACTUAL)
VALUES('pescado','R', 8000);
INSERT INTO productos (nombreProducto, tipo, VALOR_ACTUAL)
VALUES('arroz','N', 2500);
INSERT INTO productos (nombreProducto, tipo, VALOR_ACTUAL)
VALUES('garbanzo','N', 1900);
INSERT INTO productos (nombreProducto, tipo, VALOR_ACTUAL)
VALUES('papa francesa','R', 2000);

---- Presentaciones
INSERT INTO Presentaciones (nombreProducto, presentacion, promocion)
VALUES ('pera',5,0);
INSERT INTO Presentaciones (nombreProducto, presentacion, promocion)
VALUES ('pera',3,0);
INSERT INTO Presentaciones (nombreProducto, presentacion, promocion)
VALUES ('pera',2,0);
INSERT INTO Presentaciones (nombreProducto, presentacion, promocion)
VALUES ('pera',1,0);
INSERT INTO Presentaciones (nombreProducto, presentacion, promocion)
VALUES ('manzana',5,0);
INSERT INTO Presentaciones (nombreProducto, presentacion, promocion)
VALUES ('manzana',3,0);
INSERT INTO Presentaciones (nombreProducto, presentacion, promocion)
VALUES ('manzana',2,0);
INSERT INTO Presentaciones (nombreProducto, presentacion, promocion)
VALUES ('manzana',1,0);
INSERT INTO Presentaciones (nombreProducto, presentacion, promocion)
VALUES ('mandarina',1,0);
INSERT INTO Presentaciones (nombreProducto, presentacion, promocion)
VALUES ('mandarina',2,0);
INSERT INTO Presentaciones (nombreProducto, presentacion, promocion)
VALUES ('mandarina',5,0);
INSERT INTO Presentaciones (nombreProducto, presentacion, promocion)
VALUES ('arroz',1,0);
INSERT INTO Presentaciones (nombreProducto, presentacion, promocion)
VALUES ('pescado',1,0);
INSERT INTO Presentaciones (nombreProducto, presentacion, promocion)
VALUES ('garbanzo',1,0);
INSERT INTO Presentaciones (nombreProducto, presentacion, promocion)
VALUES ('papa francesa',1,0);
INSERT INTO Presentaciones (nombreProducto, presentacion, promocion)
VALUES ('papa francesa',2,0);
---- Almacenajes 
INSERT INTO almacenajes( idAlmacenaje,tipoAlmacenaje, capacidad, capacidadUsada, tipo, dirElectronica)
VALUES (inc_id_Almacenajes.NextVal,'Bodega',1000,0,'R',NULL);
INSERT INTO almacenajes( idAlmacenaje,tipoAlmacenaje, capacidad, capacidadUsada, tipo, dirElectronica)
VALUES (inc_id_Almacenajes.NextVal,'Bodega',2000,0,'P',NULL);
INSERT INTO almacenajes( idAlmacenaje,tipoAlmacenaje, capacidad, capacidadUsada, tipo, dirElectronica)
VALUES (inc_id_Almacenajes.NextVal,'Bodega',2000,0,'N',NULL);
INSERT INTO almacenajes( idAlmacenaje,tipoAlmacenaje, capacidad, capacidadUsada, tipo, dirElectronica)
VALUES (inc_id_Almacenajes.NextVal,'Local',1400,0,'N','jose_su_lo@hotmail.com');
---- Items Almacenaje
INSERT INTO items_Almacenajes (cantidad , nombreProducto , presentacion , idAlmacenaje, FECHA_EXPIRACION)
VALUES (80,'manzana',3,3,TO_DATE('2015/05/25', 'yyyy/mm/dd'));
INSERT INTO items_Almacenajes (cantidad , nombreProducto , presentacion , idAlmacenaje, FECHA_EXPIRACION)
VALUES (50,'manzana',1,3,TO_DATE('2015/05/22', 'yyyy/mm/dd'));
INSERT INTO items_Almacenajes (cantidad , nombreProducto , presentacion , idAlmacenaje, FECHA_EXPIRACION)
VALUES (20,'manzana',5,3,TO_DATE('2015/05/19', 'yyyy/mm/dd'));
INSERT INTO items_Almacenajes (cantidad , nombreProducto , presentacion , idAlmacenaje, FECHA_EXPIRACION)
VALUES (30,'garbanzo',1,4,TO_DATE('2015/05/20', 'yyyy/mm/dd'));
INSERT INTO items_Almacenajes (cantidad , nombreProducto , presentacion , idAlmacenaje, FECHA_EXPIRACION)
VALUES (80,'garbanzo',1,4,TO_DATE('2015/05/30', 'yyyy/mm/dd'));
INSERT INTO items_Almacenajes (cantidad , nombreProducto , presentacion , idAlmacenaje, FECHA_EXPIRACION)
VALUES (40,'pescado',1,2,TO_DATE('2015/05/12', 'yyyy/mm/dd'));
INSERT INTO items_Almacenajes (cantidad , nombreProducto , presentacion , idAlmacenaje, FECHA_EXPIRACION)
VALUES (30,'papa francesa',1,2,TO_DATE('2015/05/20', 'yyyy/mm/dd'));
--- Pedidos
INSERT INTO pedidos_de_locales (idLocal,idPedido, recibido, fecha_pedido)
VALUES (5, inc_id_Pedidos_Locales.nextVal,'N',TO_DATE('2015/05/10', 'yyyy/mm/dd'));
  INSERT INTO ITEMS_PEDIDOS_POR_LOCALES (IDPEDIDO,CANTIDAD, Costo_unitario, nombreproducto,presentacion, fecha_esperada)
  VALUES (1, 5, 200, 'manzana', 5, NULL);
  INSERT INTO ITEMS_PEDIDOS_POR_LOCALES (IDPEDIDO,CANTIDAD, Costo_unitario, nombreproducto,presentacion, fecha_esperada)
  VALUES (1, 2, 500, 'pera', 5, NULL);
INSERT INTO pedidos_de_locales (idLocal,idPedido, recibido, fecha_pedido)
VALUES (5, inc_id_Pedidos_Locales.nextVal,'F',TO_DATE('2015/05/10', 'yyyy/mm/dd'));
  INSERT INTO ITEMS_PEDIDOS_POR_LOCALES (IDPEDIDO,CANTIDAD, Costo_unitario, nombreproducto,presentacion, fecha_esperada)
  VALUES (21, 2, 200, 'manzana', 5, NULL);
  INSERT INTO ITEMS_PEDIDOS_POR_LOCALES (IDPEDIDO,CANTIDAD, Costo_unitario, nombreproducto,presentacion, fecha_esperada)
  VALUES (21, 8, 500, 'pera', 5, NULL);
  
  
---Pedido  PROVEEDORES
INSERT INTO PEDIDO_A_PROVEEDORES (IDLICITACION, CANTIDAD, NOMBREPRODUCTO, PRESENTACION, FECHAESPERADA, FECHAPUBLICACION, FECHADECIERRE) 
VALUES (inc_idPedidoProveedores.nextval, '20', 'manzana', '1', TO_DATE('2014-03-20 15:40:32', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2014-03-16 15:40:57', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2014-03-18 15:41:09', 'YYYY-MM-DD HH24:MI:SS'));
---- Movimientos
INSERT INTO movimientos (idMovimiento,nombreProducto,presentacion,fecha,peso,costoTotal,TipoDeTransaccion)
values(1,'manzana', 5,TO_DATE('2015/05/10', 'yyyy/mm/dd'),30,2500,'venta');


---Facturas Locales

insert into facturas_locales (idFacturaLocal ,fecha,idLocal)
values (inc_idFacturaLocal.nextVal,TO_DATE('2013/05/10', 'yyyy/mm/dd'), 5);
  insert into items_comprados_en_locales (cantidad,nombreProducto ,presentacion,idFacturaLocal ,fechaDeExpiracion)
  values(4,'manzana',5,1,TO_DATE('2013/04/10', 'yyyy/mm/dd'));
  insert into items_comprados_en_locales (cantidad,nombreProducto ,presentacion,idFacturaLocal ,fechaDeExpiracion)
  values(9,'pera',1,1,TO_DATE('2013/05/10', 'yyyy/mm/dd'));
insert into facturas_locales (idFacturaLocal ,fecha,idLocal)
values (inc_idFacturaLocal.nextVal,TO_DATE('2013/04/10', 'yyyy/mm/dd'), 5);
  insert into items_comprados_en_locales (cantidad,nombreProducto ,presentacion,idFacturaLocal ,fechaDeExpiracion)
  values(8,'manzana',5,2,TO_DATE('2013/04/10', 'yyyy/mm/dd'));
  insert into items_comprados_en_locales (cantidad,nombreProducto ,presentacion,idFacturaLocal ,fechaDeExpiracion)
  values(10,'pera',1,2,TO_DATE('2013/05/10', 'yyyy/mm/dd'));
   insert into items_comprados_en_locales (cantidad,nombreProducto ,presentacion,idFacturaLocal ,fechaDeExpiracion)
  values(30,'mandarina',1,2,TO_DATE('2013/05/10', 'yyyy/mm/dd'));
