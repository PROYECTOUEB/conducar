

USE conducar;


SELECT codigo_categoria, descripcion, tipo_servicio FROM categoria_licencia;


SELECT nombres, apellidos, correo FROM instructor;


SELECT DISTINCT tipo_vehiculo FROM vehiculo;


SELECT codigo_categoria, valor_curso, valor_curso * 1.10 AS valor_con_incremento FROM categoria_licencia WHERE valor_curso IS NOT NULL;


SELECT placa, codigo_categoria, marca, linea FROM vehiculo WHERE estado = 'DISPONIBLE' ORDER BY codigo_categoria, placa;


SELECT id_matricula, id_alumno, codigo_categoria, fecha_matricula FROM matricula WHERE fecha_matricula BETWEEN '2026-03-01' AND '2026-06-30' ORDER BY fecha_matricula;


SELECT nombres, apellidos FROM alumno WHERE apellidos LIKE 'Ro%' ORDER BY apellidos;


SELECT id_pago, id_matricula, valor, medio_pago FROM pago WHERE medio_pago IN ('EFECTIVO', 'TARJETA') AND valor > 500000 ORDER BY valor DESC;


SELECT id_alumno, nombres, apellidos FROM alumno WHERE telefono IS NULL;


SELECT id_examen, id_matricula, tipo, puntaje FROM examen WHERE puntaje < 80 ORDER BY puntaje;


SELECT id_alumno, UPPER(CONCAT(nombres, ' ', apellidos)) AS nombre_completo FROM alumno ORDER BY id_alumno;


SELECT placa, SUBSTR(marca, 1, 3) AS inicial_marca, linea, LENGTH(linea) AS largo_linea FROM vehiculo;


SELECT id_pago, valor, ROUND(valor, -3) AS valor_redondeado FROM pago ORDER BY id_pago;


SELECT id_matricula, fecha_matricula, EXTRACT(YEAR FROM fecha_matricula) AS anio, EXTRACT(MONTH FROM fecha_matricula) AS mes FROM matricula ORDER BY fecha_matricula;


SELECT placa, marca, linea, fecha_venc_soat FROM vehiculo WHERE fecha_venc_soat <= CURRENT_DATE + INTERVAL '30' DAY ORDER BY fecha_venc_soat;


SELECT id_examen, tipo, puntaje, CASE WHEN puntaje >= 80 THEN 'APROBADO' ELSE 'REPROBADO' END AS resultado FROM examen ORDER BY id_examen;


SELECT codigo_categoria, COALESCE(CAST(valor_curso AS CHAR(12)), 'No ofrecido') AS valor FROM categoria_licencia;


SELECT placa, tipo_vehiculo, CASE WHEN LENGTH(placa) = 6 AND tipo_vehiculo = 'MOTOCICLETA' THEN 'Placa de moto (AAA00A)' ELSE 'Placa de carro (AAA000)' END AS formato FROM vehiculo;


SELECT codigo_categoria, COUNT(*) AS matriculas FROM matricula GROUP BY codigo_categoria ORDER BY matriculas DESC;


SELECT codigo_categoria, SUM(horas) AS total_horas FROM plan_curso GROUP BY codigo_categoria;


SELECT tipo, ROUND(AVG(puntaje), 2) AS promedio, MIN(puntaje) AS minimo, MAX(puntaje) AS maximo FROM examen GROUP BY tipo;


SELECT id_instructor, SUM(horas) AS horas_dictadas FROM clase WHERE estado = 'DICTADA' GROUP BY id_instructor ORDER BY horas_dictadas DESC;


SELECT m.id_matricula, m.valor_pactado, SUM(p.valor) AS pagado FROM matricula m JOIN pago p ON m.id_matricula = p.id_matricula GROUP BY m.id_matricula, m.valor_pactado HAVING SUM(p.valor) < m.valor_pactado;


SELECT placa, SUM(horas) AS horas FROM clase WHERE placa IS NOT NULL AND estado = 'DICTADA' GROUP BY placa HAVING SUM(horas) > 100 ORDER BY horas DESC;


SELECT m.id_matricula, a.nombres, a.apellidos, c.codigo_categoria, c.descripcion FROM matricula m JOIN alumno a ON m.id_alumno = a.id_alumno JOIN categoria_licencia c ON m.codigo_categoria = c.codigo_categoria ORDER BY m.id_matricula;


SELECT md.tipo, md.nombre, p.horas FROM plan_curso p JOIN modulo md ON p.id_modulo = md.id_modulo WHERE p.codigo_categoria = 'B1' ORDER BY md.tipo, md.nombre;


SELECT ic.codigo_categoria, i.nombres, i.apellidos, ic.fecha_habilitacion FROM instructor_categoria ic JOIN instructor i ON ic.id_instructor = i.id_instructor ORDER BY ic.codigo_categoria, i.apellidos;


SELECT cl.fecha, cl.hora_inicio, md.nombre AS modulo, i.apellidos AS instructor, v.placa, v.marca FROM clase cl JOIN modulo md ON cl.id_modulo = md.id_modulo JOIN instructor i ON cl.id_instructor = i.id_instructor JOIN vehiculo v ON cl.placa = v.placa WHERE cl.estado = 'PROGRAMADA' AND md.tipo = 'PRACTICA' ORDER BY cl.fecha, cl.hora_inicio;


SELECT m.id_matricula, m.estado, ca.numero_certificado, ca.fecha_expedicion FROM matricula m LEFT JOIN certificado_aptitud ca ON m.id_matricula = ca.id_matricula ORDER BY m.id_matricula;


SELECT a.id_alumno, a.nombres, a.apellidos FROM alumno a LEFT JOIN certificado_crc c ON a.id_alumno = c.id_alumno WHERE c.id_certificado IS NULL;


SELECT v1.placa, v2.placa, v1.marca FROM vehiculo v1 JOIN vehiculo v2 ON v1.marca = v2.marca AND v1.placa < v2.placa;


SELECT codigo_categoria, valor_curso FROM categoria_licencia WHERE valor_curso > (SELECT valor_curso FROM categoria_licencia WHERE codigo_categoria = 'B1');


SELECT id_alumno, nombres, apellidos FROM alumno WHERE id_alumno IN (SELECT id_alumno FROM matricula WHERE estado = 'ACTIVA') ORDER BY apellidos;


SELECT id_examen, tipo, puntaje FROM examen WHERE puntaje > (SELECT AVG(puntaje) FROM examen) ORDER BY puntaje DESC;


SELECT id_instructor, SUM(horas) AS horas FROM clase WHERE estado = 'DICTADA' GROUP BY id_instructor HAVING SUM(horas) >= ALL (SELECT SUM(horas) FROM clase WHERE estado = 'DICTADA' GROUP BY id_instructor);


SELECT m.id_matricula, m.id_alumno, m.codigo_categoria FROM matricula m WHERE (SELECT SUM(cl.horas) FROM clase cl WHERE cl.id_matricula = m.id_matricula AND cl.estado = 'DICTADA') >= (SELECT SUM(p.horas) FROM plan_curso p WHERE p.codigo_categoria = m.codigo_categoria) ORDER BY m.id_matricula;


SELECT i.nombres, i.apellidos FROM instructor i JOIN instructor_categoria ic ON i.id_instructor = ic.id_instructor GROUP BY i.id_instructor, i.nombres, i.apellidos HAVING COUNT(*) = (SELECT COUNT(*) FROM categoria_licencia WHERE valor_curso IS NOT NULL);


SELECT codigo_categoria FROM matricula UNION SELECT codigo_categoria FROM vehiculo;


SELECT id_alumno FROM matricula WHERE estado = 'FINALIZADA' INTERSECT SELECT id_alumno FROM matricula WHERE estado = 'ACTIVA';


SELECT placa FROM vehiculo EXCEPT SELECT placa FROM clase WHERE estado = 'PROGRAMADA' AND placa IS NOT NULL;


SELECT codigo_categoria FROM categoria_licencia EXCEPT SELECT codigo_categoria FROM plan_curso;


SELECT fecha FROM examen WHERE tipo = 'TEORICO' INTERSECT SELECT fecha FROM examen WHERE tipo = 'PRACTICO' ORDER BY fecha;


