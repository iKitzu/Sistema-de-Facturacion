create database bofi9zvykt97phksfooe;

use bofi9zvykt97phksfooe;

CREATE TABLE clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100),
    estrato INT,
    total_gastado DOUBLE DEFAULT 0
);

CREATE TABLE productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio DOUBLE NOT NULL
);

CREATE TABLE facturas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT,
    fecha DATE,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id)
);

CREATE TABLE detalle_factura (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_factura INT,
    id_producto INT,
    cantidad INT,
    subtotal DOUBLE,
    FOREIGN KEY (id_factura) REFERENCES facturas(id),
    FOREIGN KEY (id_producto) REFERENCES productos(id)
);

-- Verificar datos insertados
SELECT * FROM clientes;

delete from clientes where id = 6;

SELECT * FROM productos;

SELECT * FROM facturas;

SELECT * FROM detalle_factura;

-- Insertar datos de ejemplo en la tabla clientes
INSERT INTO clientes (nombre, apellido, estrato, total_gastado) VALUES
('Juanito', 'Perez', 3, 0.00),
('Ana Banana', 'Gomez', 2, 0.00),
('Super Mario', 'Bros', 1, 0.00),
('Lola Bunny', 'Looney', 4, 0.00);

-- Insertar datos de ejemplo en la tabla productos
INSERT INTO productos (nombre, precio) VALUES
('Chocolates Chiflados', 5.99),
('Papas Super Saladas', 2.49),
('Refresco Espacial', 1.99),
('Galletas Misteriosas', 3.89),
('Sopa Sorpresa', 4.49),
('Cereal del Espacio', 6.29);

-- Insertar datos de ejemplo en la tabla facturas
INSERT INTO facturas (id_cliente, fecha) VALUES
(1, NOW()),
(2, NOW()),
(3, NOW()),
(4, NOW());

-- Insertar datos de ejemplo en la tabla detalle_factura
INSERT INTO detalle_factura (id_factura, id_producto, cantidad, subtotal) VALUES
(1, 1, 2, 11.98),  -- Juanito compra 2 Chocolates Chiflados
(1, 3, 1, 1.99),   -- Juanito compra 1 Refresco Espacial
(2, 2, 3, 7.47),   -- Ana Banana compra 3 Papas Super Saladas
(2, 5, 1, 4.49),   -- Ana Banana compra 1 Sopa Sorpresa
(3, 4, 5, 19.45),  -- Super Mario compra 5 Galletas Misteriosas
(3, 6, 1, 6.29),   -- Super Mario compra 1 Cereal del Espacio
(4, 1, 1, 5.99),   -- Lola Bunny compra 1 Chocolates Chiflados
(4, 4, 2, 7.78);   -- Lola Bunny compra 2 Galletas Misteriosas

