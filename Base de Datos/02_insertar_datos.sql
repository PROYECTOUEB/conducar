-- ConduCar: datos del equipo (incluye los cambios aplicados después: examen 2 = 62.50,
-- plan de los módulos 4 y 5, y certificado CRC del alumno 2 = NO APTO)

USE conducar;

INSERT INTO categoria_licencia (codigo_categoria, descripcion, tipo_servicio, edad_minima, valor_curso) VALUES 
('A1', 'Motocicletas con cilindrada hasta 125 c.c.', 'PARTICULAR', 16, NULL), 
('A2', 'Motocicletas, motociclos y mototriciclos con cilindrada mayor a 125 c.c.', 'PARTICULAR', 16, 950000), 
('B1', 'Automóviles, motocarros, cuatrimotos, camperos, camionetas y microbuses de servicio particular', 'PARTICULAR', 16, 1250000), 
('B2', 'Camiones rígidos, busetas y buses de servicio particular', 'PARTICULAR', 18, NULL), 
('B3', 'Vehículos articulados de servicio particular', 'PARTICULAR', 18, NULL), 
('C1', 'Automóviles, camperos, camionetas y microbuses de servicio público', 'PUBLICO', 18, 1550000), 
('C2', 'Camiones rígidos, busetas y buses de servicio público', 'PUBLICO', 18, NULL), 
('C3', 'Vehículos articulados de servicio público', 'PUBLICO', 18, NULL);

INSERT INTO alumno (id_alumno, tipo_documento, numero_documento, nombres, apellidos, fecha_nacimiento, telefono, correo, fecha_registro) VALUES 
(1, 'CC', '1089106819', 'Juan Diego', 'Contreras Dager', '2005-03-22', '3158678186', 'JuanDiContreras@gmail.com', '2026-01-08'), 
(2, 'TI', '1016953204', 'Laura', 'Morales Camargo', '2009-02-19', '3004434632', 'lauraMorales@gmail.com', '2026-01-10'), -- Corregido: fecha_registro antes de su certificado CRC
(3, 'CC', '1046321078', 'Nicolas Santiago', 'Rincon Colorado', '2007-03-11', '3019301581', 'NicolasRincon@gmail.com', '2026-01-17');

INSERT INTO certificado_crc (id_certificado, id_alumno, nombre_crc, fecha_examen, resultado) VALUES 
(1, 1, 'CRC Medicar Suba', '2026-01-08', 'APTO'), 
(2, 2, 'CRC Salud Vial Norte', '2026-01-12', 'NO APTO'), 
(3, 3, 'CRC Medicar Suba', '2026-01-19', 'APTO');

INSERT INTO instructor (id_instructor, numero_documento, nombres, apellidos, telefono, correo, numero_licencia_instructor, fecha_venc_licencia, estado) VALUES 
(1, '79334130', 'Luis', 'Morales Adame', '3198056747', 'luis.Morales@conducar.edu.co', 'INS-2037', '2027-08-31', 'ACTIVO'), 
(2, '79887507', 'Ana María', 'Rodriguez Jimenez', '3198400346', 'ana.Rodriguez@conducar.edu.co', 'INS-2074', '2028-10-01', 'ACTIVO'), 
(3, '79956436', 'Samuel', 'González Martínez', '3118543956', 'samuel.gonzalez@conducar.edu.co', 'INS-2111', '2026-11-23', 'ACTIVO');

INSERT INTO instructor_categoria (id_instructor, codigo_categoria, fecha_habilitacion) VALUES 
(1, 'A2', '2023-04-07'), 
(1, 'B1', '2022-12-21'), 
(1, 'C1', '2020-11-01'), 
(2, 'A2', '2021-04-18'), 
(2, 'B1', '2022-12-21'), 
(3, 'B1', '2023-10-19'), 
(3, 'C1', '2020-11-01');

INSERT INTO vehiculo (placa, codigo_categoria, tipo_vehiculo, marca, linea, modelo_anio, transmision, estado, fecha_venc_soat, fecha_venc_rtm) VALUES 
('ABC12F', 'A2', 'MOTOCICLETA', 'Yamaha', 'XTZ 150', 2023, 'MECANICA', 'DISPONIBLE', '2027-08-19', '2027-08-26'), 
('LMN789', 'B1', 'AUTOMOVIL', 'Kia', 'Picanto', 2024, 'AUTOMATICA', 'DISPONIBLE', '2027-05-28', '2027-05-15'), 
('OPQ567', 'C1', 'CAMIONETA', 'Chevrolet', 'N400 Max', 2023, 'MECANICA', 'DISPONIBLE', '2027-07-01', '2027-04-10');

INSERT INTO modulo (id_modulo, nombre, tipo, descripcion) VALUES 
(1, 'Movilidad segura y sostenible', 'TEORIA', 'Factores de riesgo, actores viales vulnerables, alcohol, fatiga y distracciones.'), 
(2, 'Normas de tránsito', 'TEORIA', 'Código Nacional de Tránsito, infracciones, sanciones, prelación y documentos del conductor.'), 
(3, 'Señalización e infraestructura vial', 'TEORIA', 'Señales verticales, horizontales, semáforos y dispositivos de control.'),
(4, 'Revisión preoperacional', 'TALLER', 'Inspección de llantas, luces, frenos, niveles y documentos antes de conducir.'), 
(5, 'Conducción en carretera', 'PRACTICA', 'Adelantamiento, conducción en pendiente y frenado de emergencia.');

INSERT INTO plan_curso (codigo_categoria, id_modulo, horas) VALUES 
('A2', 1, 8), ('B1', 1, 8), ('C1', 1, 8), 
('A2', 2, 1), ('B1', 2, 2), ('C1', 2, 2), 
('A2', 3, 3), ('B1', 3, 5), ('C1', 3, 10),
('A2', 4, 3), ('A2', 5, 15), ('B1', 4, 5), ('B1', 5, 20), ('C1', 4, 5), ('C1', 5, 30);

INSERT INTO matricula (id_matricula, id_alumno, codigo_categoria, fecha_matricula, valor_pactado, estado) VALUES 
(1, 1, 'A2', '2026-01-10', 950000, 'FINALIZADA'), 
(2, 2, 'B1', '2026-01-25', 1250000, 'CANCELADA'), 
(3, 3, 'C1', '2026-01-13', 1550000, 'ACTIVA');

INSERT INTO pago (id_pago, id_matricula, fecha_pago, valor, medio_pago) VALUES 
(1, 1, '2026-01-11', 775000.0, 'EFECTIVO'), 
(2, 2, '2026-02-03', 1350000.0, 'TRANSFERENCIA'), 
(3, 3, '2026-01-14', 1700000.0, 'TARJETA');

INSERT INTO clase (id_clase, id_matricula, id_modulo, id_instructor, placa, fecha, hora_inicio, horas, estado) VALUES 
(1, 1, 1, 1, NULL, '2026-03-12', '09:00:00', 4, 'DICTADA'), 
(2, 2, 2, 2, 'LMN789', '2026-02-28', '16:00:00', 2, 'CANCELADA'), 
(3, 3, 3, 3, 'OPQ567', '2026-02-28', '07:00:00', 4, 'DICTADA'); 

INSERT INTO examen (id_examen, id_matricula, tipo, fecha, puntaje, id_instructor) VALUES 
(1, 1, 'TEORICO', '2026-03-20', 88.73, 1), 
(2, 2, 'TEORICO', '2026-03-21', 62.50, 2), 
(3, 3, 'PRACTICO', '2026-03-06', 89.69, 3);

INSERT INTO certificado_aptitud (id_certificado, id_matricula, numero_certificado, fecha_expedicion) VALUES 
(1, 1, 'CAC-2026-00001', '2026-03-22'), 
(2, 2, 'CAC-2026-00002', '2026-03-22'), 
(3, 3, 'CAC-2026-00003', '2026-04-07');

	