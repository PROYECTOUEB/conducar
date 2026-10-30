-- ConduCar: consultas SQL de ejemplo (temas del curso)

USE conducar;

-- 1. [Recuperar datos con SELECT] ¿Qué categorías de licencia existen y para qué vehículos son?
SELECT codigo_categoria, descripcion, tipo_servicio FROM categoria_licencia;

-- 2. [Recuperar datos con SELECT] ¿Cuál es el nombre completo y el correo de cada instructor?
SELECT nombres, apellidos, correo FROM instructor;

-- 3. [Recuperar datos con SELECT] ¿Qué tipos de vehículo tiene la escuela (sin repetir)?
SELECT DISTINCT tipo_vehiculo FROM vehiculo;

-- 4. [Recuperar datos con SELECT] ¿Cuánto costaría cada curso ofrecido con un incremento del 10 %?
SELECT codigo_categoria, valor_curso, valor_curso * 1.10 AS valor_con_incremento FROM categoria_licencia WHERE valor_curso IS NOT NULL;

-- 5. [Restringir y ordenar datos] ¿Qué vehículos están disponibles para clases, ordenados por categoría?
SELECT placa, codigo_categoria, marca, linea FROM vehiculo WHERE estado = 'DISPONIBLE' ORDER BY codigo_categoria, placa;

-- 6. [Restringir y ordenar datos] ¿Qué matrículas se hicieron entre marzo y junio de 2026?
SELECT id_matricula, id_alumno, codigo_categoria, fecha_matricula FROM matricula WHERE fecha_matricula BETWEEN '2026-03-01' AND '2026-06-30' ORDER BY fecha_matricula;

-- 7. [Restringir y ordenar datos] ¿Qué alumnos tienen apellido que empieza por "Ro"?
SELECT nombres, apellidos FROM alumno WHERE apellidos LIKE 'Ro%' ORDER BY apellidos;

-- 8. [Restringir y ordenar datos] ¿Qué pagos se hicieron en efectivo o con tarjeta por más de 500.000 pesos?
SELECT id_pago, id_matricula, valor, medio_pago FROM pago WHERE medio_pago IN ('EFECTIVO', 'TARJETA') AND valor > 500000 ORDER BY valor DESC;

-- 9. [Restringir y ordenar datos] ¿Qué alumnos no tienen teléfono registrado?
SELECT id_alumno, nombres, apellidos FROM alumno WHERE telefono IS NULL;

-- 10. [Restringir y ordenar datos] ¿Qué exámenes obtuvieron un puntaje menor a 80 (no aprobados)?
SELECT id_examen, id_matricula, tipo, puntaje FROM examen WHERE puntaje < 80 ORDER BY puntaje;

-- 11. [Funciones de una sola fila] ¿Cuál es el nombre completo en mayúsculas de cada alumno?
SELECT id_alumno, UPPER(CONCAT(nombres, ' ', apellidos)) AS nombre_completo FROM alumno ORDER BY id_alumno;

-- 12. [Funciones de una sola fila] ¿Cuáles son las tres primeras letras de la marca de cada vehículo y la longitud de su línea?
SELECT placa, SUBSTR(marca, 1, 3) AS inicial_marca, linea, LENGTH(linea) AS largo_linea FROM vehiculo;

-- 13. [Funciones de una sola fila] ¿Cuál es el valor de cada pago redondeado a miles?
SELECT id_pago, valor, ROUND(valor, -3) AS valor_redondeado FROM pago ORDER BY id_pago;

-- 14. [Funciones de una sola fila] ¿En qué año y mes se matriculó cada alumno?
SELECT id_matricula, fecha_matricula, EXTRACT(YEAR FROM fecha_matricula) AS anio, EXTRACT(MONTH FROM fecha_matricula) AS mes FROM matricula ORDER BY fecha_matricula;

-- 15. [Funciones de una sola fila] ¿Qué vehículos tienen el SOAT vencido o por vencer en los próximos 30 días?
SELECT placa, marca, linea, fecha_venc_soat FROM vehiculo WHERE fecha_venc_soat <= CURRENT_DATE + INTERVAL '30' DAY ORDER BY fecha_venc_soat;

-- 16. [Conversión y expresiones condicionales] ¿Cuál es el resultado (aprobado o reprobado) de cada examen?
SELECT id_examen, tipo, puntaje, CASE WHEN puntaje >= 80 THEN 'APROBADO' ELSE 'REPROBADO' END AS resultado FROM examen ORDER BY id_examen;

-- 17. [Conversión y expresiones condicionales] ¿Qué valor tiene cada curso? (mostrar "No ofrecido" cuando no tiene valor)
SELECT codigo_categoria, COALESCE(CAST(valor_curso AS CHAR(12)), 'No ofrecido') AS valor FROM categoria_licencia;

-- 18. [Conversión y expresiones condicionales] ¿Qué vehículos usan placa de moto y cuáles de carro?
SELECT placa, tipo_vehiculo, CASE WHEN LENGTH(placa) = 6 AND tipo_vehiculo = 'MOTOCICLETA' THEN 'Placa de moto (AAA00A)' ELSE 'Placa de carro (AAA000)' END AS formato FROM vehiculo;

-- 19. [Funciones de grupo (GROUP BY y HAVING)] ¿Cuántos alumnos hay matriculados en cada categoría?
SELECT codigo_categoria, COUNT(*) AS matriculas FROM matricula GROUP BY codigo_categoria ORDER BY matriculas DESC;

-- 20. [Funciones de grupo (GROUP BY y HAVING)] ¿Cuántas horas tiene en total el curso de cada categoría?
SELECT codigo_categoria, SUM(horas) AS total_horas FROM plan_curso GROUP BY codigo_categoria;

-- 21. [Funciones de grupo (GROUP BY y HAVING)] ¿Cuál es el puntaje promedio, mínimo y máximo por tipo de examen?
SELECT tipo, ROUND(AVG(puntaje), 2) AS promedio, MIN(puntaje) AS minimo, MAX(puntaje) AS maximo FROM examen GROUP BY tipo;

-- 22. [Funciones de grupo (GROUP BY y HAVING)] ¿Cuántas horas de clase ha dictado cada instructor?
SELECT id_instructor, SUM(horas) AS horas_dictadas FROM clase WHERE estado = 'DICTADA' GROUP BY id_instructor ORDER BY horas_dictadas DESC;

-- 23. [Funciones de grupo (GROUP BY y HAVING)] ¿Qué matrículas han pagado menos del valor pactado?
SELECT m.id_matricula, m.valor_pactado, SUM(p.valor) AS pagado FROM matricula m JOIN pago p ON m.id_matricula = p.id_matricula GROUP BY m.id_matricula, m.valor_pactado HAVING SUM(p.valor) < m.valor_pactado;

-- 24. [Funciones de grupo (GROUP BY y HAVING)] ¿Qué vehículos se han usado en más de 100 horas de clase?
SELECT placa, SUM(horas) AS horas FROM clase WHERE placa IS NOT NULL AND estado = 'DICTADA' GROUP BY placa HAVING SUM(horas) > 100 ORDER BY horas DESC;

-- 25. [Mostrar datos de varias tablas (JOIN)] ¿Qué alumno está inscrito en cada matrícula y en qué categoría?
SELECT m.id_matricula, a.nombres, a.apellidos, c.codigo_categoria, c.descripcion FROM matricula m JOIN alumno a ON m.id_alumno = a.id_alumno JOIN categoria_licencia c ON m.codigo_categoria = c.codigo_categoria ORDER BY m.id_matricula;

-- 26. [Mostrar datos de varias tablas (JOIN)] ¿Qué módulos (materias) componen el curso B1 y cuántas horas tiene cada uno?
SELECT md.tipo, md.nombre, p.horas FROM plan_curso p JOIN modulo md ON p.id_modulo = md.id_modulo WHERE p.codigo_categoria = 'B1' ORDER BY md.tipo, md.nombre;

-- 27. [Mostrar datos de varias tablas (JOIN)] ¿Qué instructores están habilitados para enseñar cada categoría?
SELECT ic.codigo_categoria, i.nombres, i.apellidos, ic.fecha_habilitacion FROM instructor_categoria ic JOIN instructor i ON ic.id_instructor = i.id_instructor ORDER BY ic.codigo_categoria, i.apellidos;

-- 28. [Mostrar datos de varias tablas (JOIN)] ¿Qué clases prácticas están programadas, con instructor y vehículo?
SELECT cl.fecha, cl.hora_inicio, md.nombre AS modulo, i.apellidos AS instructor, v.placa, v.marca FROM clase cl JOIN modulo md ON cl.id_modulo = md.id_modulo JOIN instructor i ON cl.id_instructor = i.id_instructor JOIN vehiculo v ON cl.placa = v.placa WHERE cl.estado = 'PROGRAMADA' AND md.tipo = 'PRACTICA' ORDER BY cl.fecha, cl.hora_inicio;

-- 29. [Mostrar datos de varias tablas (JOIN)] ¿Cuál es el certificado de aptitud de cada matrícula, incluidas las que aún no lo tienen?
SELECT m.id_matricula, m.estado, ca.numero_certificado, ca.fecha_expedicion FROM matricula m LEFT JOIN certificado_aptitud ca ON m.id_matricula = ca.id_matricula ORDER BY m.id_matricula;

-- 30. [Mostrar datos de varias tablas (JOIN)] ¿Qué alumnos no tienen ningún certificado del CRC?
SELECT a.id_alumno, a.nombres, a.apellidos FROM alumno a LEFT JOIN certificado_crc c ON a.id_alumno = c.id_alumno WHERE c.id_certificado IS NULL;

-- 31. [Mostrar datos de varias tablas (JOIN)] ¿Qué pares de vehículos son de la misma marca?
SELECT v1.placa, v2.placa, v1.marca FROM vehiculo v1 JOIN vehiculo v2 ON v1.marca = v2.marca AND v1.placa < v2.placa;

-- 32. [Subconsultas] ¿Qué cursos cuestan más que el curso B1?
SELECT codigo_categoria, valor_curso FROM categoria_licencia WHERE valor_curso > (SELECT valor_curso FROM categoria_licencia WHERE codigo_categoria = 'B1');

-- 33. [Subconsultas] ¿Qué alumnos tienen una matrícula activa?
SELECT id_alumno, nombres, apellidos FROM alumno WHERE id_alumno IN (SELECT id_alumno FROM matricula WHERE estado = 'ACTIVA') ORDER BY apellidos;

-- 34. [Subconsultas] ¿Qué exámenes tienen un puntaje mayor que el promedio de todos los exámenes?
SELECT id_examen, tipo, puntaje FROM examen WHERE puntaje > (SELECT AVG(puntaje) FROM examen) ORDER BY puntaje DESC;

-- 35. [Subconsultas] ¿Qué instructor ha dictado más horas de clase?
SELECT id_instructor, SUM(horas) AS horas FROM clase WHERE estado = 'DICTADA' GROUP BY id_instructor HAVING SUM(horas) >= ALL (SELECT SUM(horas) FROM clase WHERE estado = 'DICTADA' GROUP BY id_instructor);

-- 36. [Subconsultas] ¿Qué alumnos completaron todas las horas de su curso?
SELECT m.id_matricula, m.id_alumno, m.codigo_categoria FROM matricula m WHERE (SELECT SUM(cl.horas) FROM clase cl WHERE cl.id_matricula = m.id_matricula AND cl.estado = 'DICTADA') >= (SELECT SUM(p.horas) FROM plan_curso p WHERE p.codigo_categoria = m.codigo_categoria) ORDER BY m.id_matricula;

-- 37. [Subconsultas] ¿Qué instructores están habilitados en todas las categorías que ofrece la escuela?
SELECT i.nombres, i.apellidos FROM instructor i JOIN instructor_categoria ic ON i.id_instructor = ic.id_instructor GROUP BY i.id_instructor, i.nombres, i.apellidos HAVING COUNT(*) = (SELECT COUNT(*) FROM categoria_licencia WHERE valor_curso IS NOT NULL);

-- 38. [Operadores de conjuntos] ¿Qué categorías se han usado en matrículas o en vehículos?
SELECT codigo_categoria FROM matricula UNION SELECT codigo_categoria FROM vehiculo;

-- 39. [Operadores de conjuntos] ¿Qué alumnos tienen una matrícula finalizada y también una activa?
SELECT id_alumno FROM matricula WHERE estado = 'FINALIZADA' INTERSECT SELECT id_alumno FROM matricula WHERE estado = 'ACTIVA';

-- 40. [Operadores de conjuntos] ¿Qué vehículos no tienen clases programadas?
SELECT placa FROM vehiculo EXCEPT SELECT placa FROM clase WHERE estado = 'PROGRAMADA' AND placa IS NOT NULL;

-- 41. [Operadores de conjuntos] ¿Qué categorías de licencia no ofrece la escuela (no tienen plan de estudios)?
SELECT codigo_categoria FROM categoria_licencia EXCEPT SELECT codigo_categoria FROM plan_curso;

-- 42. [Operadores de conjuntos] ¿Qué fechas tuvieron exámenes teóricos y también exámenes prácticos?
SELECT fecha FROM examen WHERE tipo = 'TEORICO' INTERSECT SELECT fecha FROM examen WHERE tipo = 'PRACTICO' ORDER BY fecha;

