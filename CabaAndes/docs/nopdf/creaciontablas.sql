CREATE TABLE Productos ( 
  nombreProducto VARCHAR (30) PRIMARY KEY NOT NULL,
  VALOR_ACTUAL NUMBER NOT NULL,
  tipo CHAR(1) NOT NULL CHECK (tipo IN ( 'P','N','R')));
 
  
CREATE TABLE Presentaciones (
  nombreProducto VARCHAR2(30) NOT NULL, 
  presentacion NUMBER NOT NULL,  
  promocion NUMBER (1,4) NOT NULL,
  FOREIGN KEY(nombreProducto) REFERENCES Productos(nombreProducto),
  primary Key (nombreProducto, presentacion)
  );
CREATE TABLE Usuarios (
dirElectronica VARCHAR(60) PRIMARY KEY NOT NULL CHECK (dirElectronica LIKE '%@%'),
login VARCHAR(30) UNIQUE NOT NULL check (login not like '%@%'),
clave VARCHAR (30) NOT NULL,
docIdentidad VARCHAR (20) NOT NULL,
nombreUsuario VARCHAR (40) NOT NULL,
esEmpresa CHAR (1) NOT NULL CHECK (esEmpresa IN ('T','F')),
rol CHAR (1) NOT NULL CHECK (rol IN ( 'P', 'A', 'M', 'U')),
telefono NUMBER (20,0) NOT NULL,
nacionalidad VARCHAR(30) NOT NULL,
departamento VARCHAR(30) NOT NULL,
ciudad VARCHAR(30) NOT NULL,
codPostal VARCHAR(30) NOT NULL,
ip VarChar (32) unique
);
CREATE TABLE almacenajes ( 
idAlmacenaje NUMBER PRIMARY KEY NOT NULL, 
nombreAlmacenaje VARCHAR(30) NOT NULL UNIQUE,
tipoAlmacenaje VARCHAR2(6) NOT NULL CHECK (tipoAlmacenaje IN ('Local', 'Bodega')),
capacidad NUMBER NOT NULL CHECK (capacidad > 0),
capacidadUsada number NOT NULL ,
tipo CHAR(1) NOT NULL CHECK (tipo IN ( 'P','N','R')),
dirElectronica VARCHAR(60),
foreign key (dirElectronica) references Usuarios(dirElectronica),
CHECK (capacidadUsada >= 0 and capacidadUsada <= capacidad)
);

CREATE TABLE items_Almacenajes (
  
  cantidad number NOT NULL CHECK (cantidad > 0),
  nombreProducto VARCHAR (30) NOT NULL,
  presentacion number NOT NULL,
  idAlmacenaje NUMBER NOT NULL,
  Fecha_expiracion DATE NOT NULL,
  FOREIGN KEY (idAlmacenaje) REFERENCES almacenajes(idAlmacenaje),
  FOREIGN KEY (nombreProducto, presentacion) REFERENCES Presentaciones(nombreProducto, presentacion),
  primary Key (idAlmacenaje, nombreProducto, presentacion, Fecha_expiracion)
  );
--ALTER TABLE items_Almacenajes ADD (Fecha_expiracion DATE);
--UPDATE items_Almacenajes SET Fecha_expiracion = TO_DATE('2014/05/10', 'yyyy/mm/dd');
--ALTER TABLE items_Almacenajes add (Fecha_expiracion NOT NULL);


CREATE TABLE  Pedidos_de_locales
(
idLocal Number NOT NULL,
idPedido NUMBER NOT NULL,
recibido CHAR NOT NULL,
fecha_pedido DATE NOT NULL,
CONSTRAINT pk_pedidosLocales PRIMARY KEY (idPedido),
CONSTRAINT fk_pedidosLocales FOREIGN KEY (idLocal) REFERENCES almacenajes(idAlmacenaje),
CONSTRAINT CK_pedidosLocales CHECK (recibido = 'T' OR recibido = 'F' OR recibido = 'N')
);

CREATE TABLE  Items_Pedidos_por_locales
(
cantidad NUMBER NOT NULL CHECK (cantidad>= 0),
nombreProducto VARCHAR(30) NOT NULL,
presentacion NUMBER NOT NULL, /*hacer el check de presentacion queda pendiente ("tiene que ser una presentacion valida")*/
idPedido NUMBER NOT NULL,
costo_unitario NUMBER NOT NULL,
fecha_esperada DATE,
FOREIGN KEY (nombreProducto, presentacion) REFERENCES Presentaciones(nombreProducto, presentacion),
CONSTRAINT fk_pedidosLocalesPedLoc FOREIGN KEY (idPedido) REFERENCES pedidos_de_locales(idPedido),
primary Key (idPedido, nombreProducto, presentacion)
);


CREATE TABLE  Facturas_locales
(
idFacturaLocal Number PRIMARY KEY,
fecha DATE NOT NULL,
idLocal NUMBER NOT NULL,
CONSTRAINT fk_facturasLocales FOREIGN KEY (idLocal) 
REFERENCES almacenajes(idAlmacenaje) 
);

CREATE TABLE  Items_comprados_en_locales
(
cantidad NUMBER NOT NULL CHECK (cantidad >=0),
nombreProducto VARCHAR(30) NOT NULL,
presentacion NUMBER NOT NULL,
idFacturaLocal NUMBER NOT NULL,
fechaDeExpiracion DATE NOT NULL,
FOREIGN KEY (nombreProducto, presentacion) REFERENCES Presentaciones(nombreProducto, presentacion),
CONSTRAINT fk_ItemsCompradosEnLocalesIDs FOREIGN KEY (idFacturaLocal) REFERENCES Facturas_locales(idFacturaLocal),
primary Key (idFacturaLocal, nombreProducto, presentacion)
);

CREATE TABLE Facturas_Usuarios
(
idFacturaUsuarios NUMBER PRIMARY KEY,
fecha DATE NOT NULL,
dirUsuario VARCHAR(60) NOT NULL, /*No puede ser admin ni proveedor*/
validez CHAR(1) NOT NULL CHECK (validez ='T' OR validez = 'F'),
CONSTRAINT fk_facturaUsuarios FOREIGN KEY (dirUsuario) 
REFERENCES usuarios(dirElectronica)
);

CREATE TABLE  Items_comprados_por_usuarios
(
cantidad NUMBER NOT NULL CHECK (cantidad>= 0),
nombreProducto VARCHAR(30) NOT NULL,
presentacion NUMBER NOT NULL, /*hacer el check de presentacion queda pendiente ("tiene que ser una presentacion valida")*/
idFacturaUsuarios NUMBER NOT NULL,
costoUnitario NUMBER NOT NULL,
FOREIGN KEY (nombreProducto, presentacion) REFERENCES Presentaciones(nombreProducto, presentacion),
CONSTRAINT fk_ItemsCompUsr FOREIGN KEY (idFacturaUsuarios) REFERENCES Facturas_usuarios(idFacturaUsuarios),
primary Key (idFacturaUsuarios, nombreProducto, presentacion)
);

CREATE TABLE Proveedores
(
dirElectronica VARCHAR(60) PRIMARY KEY, /*S?lo puede ser proveedor*/
tipoProductos CHAR(1) NOT NULL CHECK (tipoProductos='P' OR tipoProductos = 'N' OR tipoProductos = 'R'),
CONSTRAINT FK_provConIdUsr FOREIGN KEY (dirElectronica)
REFERENCES usuarios(dirElectronica)
);

CREATE TABLE Pedido_a_Proveedores
(
idLicitacion NUMBER PRIMARY KEY,
cantidad NUMBER NOT NULL CHECK (cantidad >=0),
nombreProducto VARCHAR(30) NOT NULL,
presentacion NUMBER NOT NULL, /*Debe ser una presentacin posible*/
proveedorEscogido VARCHAR(60),
fechaEsperada DATE NOT NULL,
fechaPublicacion DATE NOT NULL,
fechaDeCierre DATE NOT NULL,
FOREIGN KEY (nombreProducto, presentacion) REFERENCES Presentaciones(nombreProducto, presentacion),
CONSTRAINT fk_dirYIdLicitacionProv FOREIGN KEY (proveedorEscogido)REFERENCES Proveedores(dirElectronica)
);

CREATE TABLE Ofertas
(
idLicitacion NUMBER NOT NULL,
cantidad NUMBER NOT NULL CHECK (cantidad >= 0),
nombreProducto VARCHAR(30) NOT NULL, 
presentacion NUMBER NOT NULL, 
dirProveedor VARCHAR(60) NOT NULL, /*Debe ser un proveedor*/
fechaEntrega DATE NOT NULL,
efectiva CHAR NOT NULL CHECK (efectiva='T' OR efectiva='F'),
costoUnitario NUMBER NOT NULL CHECK (costoUnitario >= 0),
costoTotal number not null check (costoTotal >= 0),
CONSTRAINT pk_ofertas PRIMARY KEY (dirProveedor, idLicitacion),
CONSTRAINT fk_dirYIdLicitacion FOREIGN KEY (idLicitacion)REFERENCES Pedido_a_proveedores(idLicitacion),
FOREIGN KEY (nombreProducto, presentacion) REFERENCES Presentaciones(nombreProducto, presentacion),
CONSTRAINT fk_ofertaProv FOREIGN KEY (dirProveedor)REFERENCES Proveedores(dirElectronica)
);

CREATE TABLE movimientos
(
idMovimiento NUMBER PRIMARY KEY,
nombreProducto VARCHAR(30) NOT NULL,
presentacion NUMBER NOT NULL,
fecha DATE NOT NULL,
peso NUMBER NOT NULL,
costoTotal NUMBER NOT NULL,
TipoDeTransaccion CHAR(6) NOT NULL CHECK (TipoDeTransaccion ='venta' OR TipoDeTransaccion= 'pedido'),
FOREIGN KEY (nombreProducto, presentacion) REFERENCES Presentaciones(nombreProducto, presentacion)
);
--- Drop en orden para ser eliminadas
DROP TABLE Pedidos_de_locales;
DROP TABLE Items_Pedidos_por_locales;
DROP TABLE Facturas_locales;
DROP TABLE Items_comprados_en_locales;
DROP TABLE Facturas_Usuarios;
DROP TABLE Items_comprados_por_usuarios;
DROP TABLE Proveedores;
DROP TABLE Ofertas;
DROP TABLE Pedido_a_Proveedores;
DROP TABLE movimientos;
DROP TABLE items_almacenajes;
DROP TABLE almacenajes;
drop table usuarios;
DROP TABLE presentaciones;
DROP TABLE productos;

---Consultas b?sicas tablas
SELECT * FROM items_almacenajes;
SELECT * FROM almacenajes;
SELECT * FROM  usuarios;
select * from proveedores;
SELECT * FROM  presentaciones;
SELECT * FROM  productos;
SELECT * FROM movimientos;
SELECT * FROM PEDIDOS_DE_LOCALES;
select * from ITEMS_PEDIDOS_POR_LOCALES;

-- Consultas elaboradas
-----Usuarios
  SELECT * FROM usuarios WHERE ip = '1001';
  SELECT * FROM usuarios WHERE dirElectronica = 'jose' OR login = 'jose';  
  UPDATE usuarios SET ip = NULL WHERE ip= '1001';
----Items
  SELECT *
  FROM (ITEMS_ALMACENAJES natural join ALMACENAJES) natural left outer join PRODUCTOS;
  
  SELECT *
  FROM (ITEMS_ALMACENAJES NATURAL JOIN ALMACENAJES) natural left outer join PRODUCTOS
  WHERE nombreproducto = 'manzana' AND presentacion = 5 AND ALMACENAJES.TIPOALMACENAJE = 'Bodega';
  
  UPDATE ITEMS_ALMACENAJES
  set CANTIDAD = CANTIDAD-1
  WHERE IDALMACENAJE = 3 AND nombreproducto = 'pera' AND PRESENTACION = 5;
  SELECT * FROM (ITEMS_ALMACENAJES NATURAL JOIN ALMACENAJES natural left outer join PRESENTACIONES) natural left outer join PRODUCTOS where nombreproducto = 'pera' and presentacion = 5 and ALMACENAJES.TIPOALMACENAJE = 'Bodega';
  
----Productos
  SELECT NOMBREPRODUCTO FROM productos WHERE TIPO = 'N';
  SELECT Presentacion FROM PRESENTACIONES WHERE NOMBREPRODUCTO = 'manzana';
  select * from PRODUCTOS where NOMBREPRODUCTO = 'pera';
---Almacenaje
  SELECT * FROM almacenajes WHERE dirElectronica = 'jose_su@hotmail.com';
  DELETE FROM ALMACENAJES WHERE IDALMACENAJE = 9;
  SELECT * FROM items_almacenajes WHERE IDALMACENAJE = 3;
  delete from ITEMS_ALMACENAJES where NOMBREPRODUCTO = '' and PRESENTACION = '' and FECHA_EXPIRACION = '';
  INSERT INTO ALMACENAJES (IDALMACENAJE, TIPOALMACENAJE, CAPACIDAD, CAPACIDADUSADA, TIPO, DIRELECTRONICA, NOMBREALMACENAJE) VALUES (inc_id_Almacenajes.nextVal, 'Bodega', '1200', '1200', 'P',  null, 'Nueva');
--- pedidos
SELECT * FROM pedidos_de_locales;-- WHERE idLocal = 5 AND recibido = 'N';
select * from ITEMS_PEDIDOS_POR_LOCALES where idPedido = 7;
UPDATE pedidos_de_locales SET recibido = 'N' WHERE idpedido = 7;


UPDATE ITEMS_PEDIDOS_POR_LOCALES SET FECHA_ESPERADA = TO_DATE('2014-03-21 16:16:48', 'YYYY-MM-DD HH24:MI:SS') 
WHERE IDPEDIDO = 41 AND NOMBREPRODUCTO = 'manzana' and PRESENTACION = 5;

UPDATE ITEMS_PEDIDOS_POR_LOCALES SET FECHA_ESPERADA = TO_DATE('2014/03/26', 'yyyy/mm/dd'))  
WHERE IDPEDIDO = 101 AND NOMBREPRODUCTO = 'arroz' and PRESENTACION = 1;
  ---Apart
  UPDATE ITEMS_ALMACENAJES SET CANTIDAD = CANTIDAD+2 WHERE IDALMACENAJE = 1 AND nombreproducto = 'pera' AND PRESENTACION = 5;
  SELECT * FROM (ITEMS_ALMACENAJES NATURAL JOIN ALMACENAJES natural left outer join PRESENTACIONES) natural left outer join PRODUCTOS;



Select * from items_almacenajes where IDALMACENAJE = 3;  

UPDATE ITEMS_ALMACENAJES SET CANTIDAD = CANTIDAD+-79 WHERE IDALMACENAJE = 3 AND nombreproducto = 'manzana' AND PRESENTACION = 3 AND FECHA_EXPIRACION = TO_DATE('2015/05/25', 'yyyy/mm/dd');
Select * from  ITEMS_ALMACENAJES  where IDALMACENAJE = 3 and nombreproducto = 'manzana' and PRESENTACION = 3 and FECHA_EXPIRACION = TO_DATE('2015/05/25', 'yyyy/mm/dd');

SELECT 
CAPACIDAD,
DIRELECTRONICA,
IDALMACENAJE,
NOMBREALMACENAJE,
TIPO,
TIPOALMACENAJE,
NVL(CAPUSADA,0) AS CAPACIDADREALUSADA
from ( ALMACENAJES natural left outer join (SELECT IDALMACENAJE, SUM(CANTIDAD*PRESENTACION) as CAPUSADA from ITEMS_ALMACENAJES GROUP BY IDALMACENAJE));

INSERT INTO usuarios (dirElectronica, login, clave, docIdentidad, nombreUsuario, esEmpresa, rol, telefono, nacionalidad, departamento, ciudad, codPostal, ip )
VALUES ('esteUserNoDEBERIAEXISTIR@hotm.com', 'joseisjms', '1129921', '10159029250507', 'Jose Miguel Suarez Lopera', 'T', 'U', 102002828211, 'colombia', 'bogota', 'bogota', 'siosososis', '009393990'); 
INSERT INTO usuarios (dirElectronica, login, clave, docIdentidad, nombreUsuario, esEmpresa, rol, telefono, nacionalidad, departamento, ciudad, codPostal, ip )
VALUES ('esNoDEBERIAEXISTIR@hotm.com', 'joisjms', '1339921', '2209250507', 'Jose Miguel Suarez Lopera', 'T', 'U', 120828211, 'colombia', 'bogota', 'bogota', 'sio4osis', '02893990'); 
ROLLBACK;


SELECT * FROM ITEMS_ALMACENAJES WHERE FECHA_EXPIRACION = TO_DATE('24/04/2014', 'dd/mm/yyyy');
delete from ITEMS_ALMACENAJES where IDALMACENAJE = '45' and NOMBREPRODUCTO like '%pera%' and PRESENTACION = '5' and FECHA_EXPIRACION = TO_DATE('2015/04/02', 'yyyy/mm/dd');

DELETE FROM ITEMS_ALMACENAJES WHERE IDALMACENAJE = '45' AND NOMBREPRODUCTO = 'pera' AND PRESENTACION = '5';
SELECT *  FROM ITEMS_ALMACENAJES WHERE IDALMACENAJE = 45 AND NOMBREPRODUCTO = 'pera' AND PRESENTACION = 5 AND FECHA_EXPIRACION = TO_DATE('2014/04/14', 'yyyy/mm/dd');
SELECT *  FROM ITEMS_ALMACENAJES WHERE IDALMACENAJE = 45 AND NOMBREPRODUCTO = 'pera' AND PRESENTACION = 5 AND FECHA_EXPIRACION = TO_DATE('24/04/14', 'dd/mm/yy');
SELECT *  FROM ITEMS_ALMACENAJES WHERE NOMBREPRODUCTO = 'pera' AND PRESENTACION = 5 AND FECHA_EXPIRACION = TO_DATE('24/04/14', 'dd/mm/yy');
SELECT *  FROM ITEMS_ALMACENAJES WHERE IDALMACENAJE = 45 AND PRESENTACION = 5 AND FECHA_EXPIRACION = TO_DATE('24/04/14', 'dd/mm/yy');
SELECT *  FROM ITEMS_ALMACENAJES WHERE IDALMACENAJE = 5 AND FECHA_EXPIRACION = TO_DATE('24/04/14', 'dd/mm/yy');
SELECT *  FROM ITEMS_ALMACENAJES WHERE FECHA_EXPIRACION = TO_DATE('24/04/14', 'dd/mm/yy');




DELETE FROM movimientos WHERE IDMOVIMIENTO= 1;
DELETE FROM movimientos WHERE IDMOVIMIENTO= 2;
ROLLBACK;
commit;

SELECT * FROM pedidos_de_locales WHERE idLocal = 261 AND 
((recibido >= TO_DATE('2014/04/28', 'yyyy/mm/dd') AND recibido <= TO_DATE('2014/04/28', 'yyyy/mm/dd') )
or (FECHA_PEDIDO >= TO_DATE('2014/04/28', 'yyyy/mm/dd') AND FECHA_PEDIDO <= TO_DATE('2014/04/28', 'yyyy/mm/dd') ));

SELECT  nombreProducto, tipo, presentacion, fecha_expiracion, idAlmacenaje, cantidad, tipoalmacenaje, promocion, VALOR_ACTUAL  
FROM (ITEMS_ALMACENAJES NATURAL JOIN PRODUCTOS NATURAL JOIN PRESENTACIONES NATURAL LEFT OUTER JOIN almacenajes) 
where rownum < 100
ORDER BY nombreProducto ;




