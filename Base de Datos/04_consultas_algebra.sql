-- ConduCar: SQL de los ejercicios de álgebra relacional

USE conducar;

-- 1. ¿Qué vehículos están disponibles para las clases, con su marca, línea y modelo?
SELECT placa, marca, linea, modelo_anio FROM vehiculo WHERE estado = 'DISPONIBLE';

-- 2. ¿Qué vehículos tienen transmisión automática?
SELECT placa, codigo_categoria, marca, linea FROM vehiculo WHERE transmision = 'AUTOMATICA';

-- 3. ¿Qué categorías de licencia son de servicio público?
SELECT codigo_categoria, descripcion, edad_minima FROM categoria_licencia WHERE tipo_servicio = 'PUBLICO';

-- 4. ¿Qué categorías ofrece la escuela y cuál es el valor de cada curso?
SELECT codigo_categoria, valor_curso FROM categoria_licencia WHERE valor_curso IS NOT NULL;

-- 5. ¿Cuáles son los módulos de práctica en vehículo?
SELECT id_modulo, nombre FROM modulo WHERE tipo = 'PRACTICA';

-- 6. ¿Qué clases están programadas, con su fecha, hora e instructor?
SELECT id_clase, fecha, hora_inicio, id_instructor FROM clase WHERE estado = 'PROGRAMADA';

-- 7. ¿Qué pagos mayores a $500 000 se hicieron por transferencia?
SELECT id_pago, id_matricula, fecha_pago, valor FROM pago WHERE valor > 500000 AND medio_pago = 'TRANSFERENCIA';

-- 8. ¿Qué exámenes fueron reprobados (puntaje menor a 80)?
SELECT id_examen, id_matricula, tipo, puntaje FROM examen WHERE puntaje < 80;

-- 9. ¿Qué instructores tienen la licencia de instructor vencida o por vencer antes de 2027?
SELECT id_instructor, nombres, apellidos, fecha_venc_licencia FROM instructor WHERE fecha_venc_licencia < '2027-01-01';

-- 10. ¿Qué alumnos se registraron con tarjeta de identidad o cédula de extranjería?
SELECT id_alumno, nombres, apellidos, tipo_documento FROM alumno WHERE tipo_documento = 'TI' OR tipo_documento = 'CE';

-- 11. ¿Qué tipos de vehículo tiene la flota?
SELECT DISTINCT tipo_vehiculo FROM vehiculo;

-- 12. ¿A qué categoría y descripción pertenece cada vehículo? (resuelta con producto cartesiano)
SELECT V.placa, V.marca, V.codigo_categoria, C.descripcion FROM vehiculo V, categoria_licencia C
WHERE V.codigo_categoria = C.codigo_categoria;

-- 13. ¿A qué categoría y descripción pertenece cada vehículo? (resuelta con reunión theta)
SELECT V.placa, V.marca, V.codigo_categoria, C.descripcion FROM vehiculo V
JOIN categoria_licencia C ON V.codigo_categoria = C.codigo_categoria;

-- 14. ¿Qué alumno realizó cada matrícula y en qué categoría? (reunión natural)
SELECT id_matricula, nombres, apellidos, codigo_categoria FROM matricula NATURAL JOIN alumno;

-- 15. ¿Qué módulos componen el curso de la categoría B1 y cuántas horas tiene cada uno?
SELECT M.nombre, M.tipo, P.horas FROM plan_curso P JOIN modulo M ON P.id_modulo = M.id_modulo
WHERE P.codigo_categoria = 'B1';

-- 16. ¿Qué instructores están habilitados para enseñar la categoría C1 y desde cuándo?
SELECT I.nombres, I.apellidos, H.fecha_habilitacion FROM instructor_categoria H
JOIN instructor I ON H.id_instructor = I.id_instructor WHERE H.codigo_categoria = 'C1';

-- 17. ¿Qué clases están programadas, con el nombre del módulo y del instructor?
SELECT C.id_clase, C.fecha, C.hora_inicio, M.nombre AS modulo, I.nombres, I.apellidos FROM clase C
JOIN modulo M ON C.id_modulo = M.id_modulo
JOIN instructor I ON C.id_instructor = I.id_instructor
WHERE C.estado = 'PROGRAMADA';

-- 18. ¿Qué pagos han hecho los alumnos con matrícula activa?
SELECT A.nombres, A.apellidos, P.fecha_pago, P.valor, P.medio_pago FROM pago P
JOIN matricula M ON P.id_matricula = M.id_matricula
JOIN alumno A ON M.id_alumno = A.id_alumno
WHERE M.estado = 'ACTIVA';

-- 19. ¿Qué instructor calificó cada examen práctico y qué puntaje dio?
SELECT E.id_examen, E.fecha, E.puntaje, I.nombres, I.apellidos FROM examen E
JOIN instructor I ON E.id_instructor = I.id_instructor WHERE E.tipo = 'PRACTICO';

-- 20. ¿Qué vehículo se usó en cada clase práctica dictada en octubre de 2026?
SELECT C.id_clase, C.fecha, V.placa, V.marca, V.linea FROM clase C
JOIN vehiculo V ON C.placa = V.placa
WHERE C.estado = 'DICTADA' AND C.fecha BETWEEN '2026-10-01' AND '2026-10-31';

-- 21. ¿Qué certificados de aptitud se han expedido, a qué alumno y para qué categoría?
SELECT CA.numero_certificado, CA.fecha_expedicion, A.nombres, A.apellidos, M.codigo_categoria
FROM certificado_aptitud CA
JOIN matricula M ON CA.id_matricula = M.id_matricula
JOIN alumno A ON M.id_alumno = A.id_alumno;

-- 22. ¿Qué parejas de instructores están habilitadas en la misma categoría?
SELECT H1.codigo_categoria, H1.id_instructor AS instructor_1, H2.id_instructor AS instructor_2
FROM instructor_categoria H1 JOIN instructor_categoria H2
ON H1.codigo_categoria = H2.codigo_categoria AND H1.id_instructor < H2.id_instructor;

-- 23. ¿Qué parejas de vehículos son de la misma marca?
SELECT V1.marca, V1.placa AS placa_1, V2.placa AS placa_2 FROM vehiculo V1
JOIN vehiculo V2 ON V1.marca = V2.marca AND V1.placa < V2.placa;

-- 24. ¿Qué alumnos presentaron más de un examen en un CRC (repitieron el certificado médico)?
SELECT C1.id_alumno, C1.fecha_examen AS primer_examen, C1.resultado AS primer_resultado,
C2.fecha_examen AS segundo_examen, C2.resultado AS segundo_resultado
FROM certificado_crc C1 JOIN certificado_crc C2
ON C1.id_alumno = C2.id_alumno AND C1.fecha_examen < C2.fecha_examen;

-- 25. ¿En qué matrículas el examen práctico tuvo mejor puntaje que el teórico?
SELECT T.id_matricula, T.puntaje AS teorico, P.puntaje AS practico FROM examen T
JOIN examen P ON T.id_matricula = P.id_matricula
WHERE T.tipo = 'TEORICO' AND P.tipo = 'PRACTICO' AND P.puntaje > T.puntaje;

-- 26. ¿En qué matrículas el examen teórico y el práctico los calificaron instructores distintos?
SELECT T.id_matricula, T.id_instructor AS instructor_teorico, P.id_instructor AS instructor_practico
FROM examen T JOIN examen P ON T.id_matricula = P.id_matricula
WHERE T.tipo = 'TEORICO' AND P.tipo = 'PRACTICO' AND T.id_instructor <> P.id_instructor;

-- 27. ¿Cuál es el directorio de todas las personas de la escuela (alumnos e instructores)?
SELECT numero_documento, nombres, apellidos, telefono FROM alumno
UNION
SELECT numero_documento, nombres, apellidos, telefono FROM instructor;

-- 28. ¿Qué fechas tienen actividad en la escuela, sea una clase o un examen?
SELECT fecha FROM clase
UNION
SELECT fecha FROM examen;

-- 29. ¿Qué instructores están habilitados tanto en A2 (moto) como en B1 (carro)?
SELECT id_instructor FROM instructor_categoria WHERE codigo_categoria = 'A2'
INTERSECT
SELECT id_instructor FROM instructor_categoria WHERE codigo_categoria = 'B1';

-- 30. ¿Qué alumnos han pagado tanto en efectivo como con tarjeta?
SELECT M.id_alumno FROM pago P JOIN matricula M ON P.id_matricula = M.id_matricula WHERE P.medio_pago = 'EFECTIVO'
INTERSECT
SELECT M.id_alumno FROM pago P JOIN matricula M ON P.id_matricula = M.id_matricula WHERE P.medio_pago = 'TARJETA';

-- 31. ¿Qué instructores están habilitados en B1 pero no en C1?
SELECT id_instructor FROM instructor_categoria WHERE codigo_categoria = 'B1'
EXCEPT
SELECT id_instructor FROM instructor_categoria WHERE codigo_categoria = 'C1';

-- 32. ¿Qué categorías de licencia no tienen ninguna matrícula?
SELECT codigo_categoria FROM categoria_licencia
EXCEPT
SELECT codigo_categoria FROM matricula;

-- 33. ¿Qué matrículas finalizadas no tienen examen práctico registrado?
SELECT id_matricula FROM matricula WHERE estado = 'FINALIZADA'
EXCEPT
SELECT id_matricula FROM examen WHERE tipo = 'PRACTICO';

-- 34. ¿Qué instructores no han calificado ningún examen?
SELECT id_instructor FROM instructor
EXCEPT
SELECT id_instructor FROM examen;

-- 35. ¿Qué instructores están habilitados en todas las categorías que ofrece la escuela?
SELECT id_instructor FROM instructor_categoria
WHERE codigo_categoria IN (SELECT codigo_categoria FROM categoria_licencia WHERE valor_curso IS NOT NULL)
GROUP BY id_instructor
HAVING COUNT(*) = (SELECT COUNT(*) FROM categoria_licencia WHERE valor_curso IS NOT NULL);

-- 36. ¿Qué módulos hacen parte del curso de todas las categorías que ofrece la escuela?
SELECT id_modulo FROM plan_curso
WHERE codigo_categoria IN (SELECT codigo_categoria FROM categoria_licencia WHERE valor_curso IS NOT NULL)
GROUP BY id_modulo
HAVING COUNT(*) = (SELECT COUNT(*) FROM categoria_licencia WHERE valor_curso IS NOT NULL);

-- 37. ¿Qué matrículas de la categoría B1 ya recibieron clases dictadas de todos los módulos de su plan?
SELECT C.id_matricula FROM clase C JOIN matricula M ON C.id_matricula = M.id_matricula
WHERE C.estado = 'DICTADA' AND M.codigo_categoria = 'B1'
GROUP BY C.id_matricula
HAVING COUNT(DISTINCT C.id_modulo) = (SELECT COUNT(*) FROM plan_curso WHERE codigo_categoria = 'B1');

-- 38. ¿Qué instructores han dictado clases de todos los módulos de práctica?
SELECT id_instructor FROM clase
WHERE estado = 'DICTADA' AND id_modulo IN (SELECT id_modulo FROM modulo WHERE tipo = 'PRACTICA')
GROUP BY id_instructor
HAVING COUNT(DISTINCT id_modulo) = (SELECT COUNT(*) FROM modulo WHERE tipo = 'PRACTICA');

-- 39. ¿Qué instructores han usado en sus clases todas las motocicletas de la escuela?
SELECT id_instructor FROM clase
WHERE placa IN (SELECT placa FROM vehiculo WHERE tipo_vehiculo = 'MOTOCICLETA')
GROUP BY id_instructor
HAVING COUNT(DISTINCT placa) = (SELECT COUNT(*) FROM vehiculo WHERE tipo_vehiculo = 'MOTOCICLETA');

-- 40. ¿Qué alumnos han pagado con todos los medios de pago registrados?
SELECT M.id_alumno FROM pago P JOIN matricula M ON P.id_matricula = M.id_matricula
GROUP BY M.id_alumno
HAVING COUNT(DISTINCT P.medio_pago) = (SELECT COUNT(DISTINCT medio_pago) FROM pago);

-- 41. ¿Cuántas matrículas tiene cada categoría?
SELECT codigo_categoria, COUNT(id_matricula) AS matriculas FROM matricula GROUP BY codigo_categoria;

-- 42. ¿Cuántas horas tiene en total el curso de cada categoría?
SELECT codigo_categoria, SUM(horas) AS total_horas FROM plan_curso GROUP BY codigo_categoria;

-- 43. ¿Cuántas horas de teoría, taller y práctica tiene el curso B1?
SELECT M.tipo, SUM(P.horas) AS horas FROM plan_curso P JOIN modulo M ON P.id_modulo = M.id_modulo
WHERE P.codigo_categoria = 'B1' GROUP BY M.tipo;

-- 44. ¿Cuál es el puntaje promedio, máximo y mínimo de cada tipo de examen?
SELECT tipo, ROUND(AVG(puntaje), 2) AS promedio, MAX(puntaje) AS maximo, MIN(puntaje) AS minimo
FROM examen GROUP BY tipo;

-- 45. ¿Cuánto dinero se ha recaudado por cada medio de pago?
SELECT medio_pago, SUM(valor) AS total, COUNT(id_pago) AS pagos FROM pago GROUP BY medio_pago;

-- 46. ¿Cuántas horas de clase ha dictado cada instructor?
SELECT I.nombres, I.apellidos, SUM(C.horas) AS horas_dictadas FROM clase C
JOIN instructor I ON C.id_instructor = I.id_instructor
WHERE C.estado = 'DICTADA'
GROUP BY I.id_instructor, I.nombres, I.apellidos;

-- 47. ¿Qué instructores han dictado más de 200 horas de clase?
SELECT id_instructor, SUM(horas) AS horas_dictadas FROM clase WHERE estado = 'DICTADA'
GROUP BY id_instructor HAVING SUM(horas) > 200;

-- 48. ¿Cuál es el saldo pendiente de cada matrícula activa?
SELECT M.id_matricula, M.valor_pactado, SUM(P.valor) AS pagado, M.valor_pactado - SUM(P.valor) AS saldo
FROM matricula M JOIN pago P ON M.id_matricula = P.id_matricula
WHERE M.estado = 'ACTIVA'
GROUP BY M.id_matricula, M.valor_pactado
HAVING M.valor_pactado - SUM(P.valor) > 0;

-- 49. ¿Cuántas clases se cancelaron en cada mes?
SELECT EXTRACT(MONTH FROM fecha) AS mes, COUNT(id_clase) AS canceladas FROM clase
WHERE estado = 'CANCELADA' GROUP BY EXTRACT(MONTH FROM fecha) ORDER BY mes;

-- 50. ¿Qué edad aproximada tenía cada alumno al matricularse?
SELECT M.id_matricula, A.nombres, EXTRACT(YEAR FROM M.fecha_matricula) - EXTRACT(YEAR FROM A.fecha_nacimiento) AS edad
FROM matricula M JOIN alumno A ON M.id_alumno = A.id_alumno;

-- 51. ¿Cuántas matrículas tiene cada categoría, incluyendo las que no tienen ninguna?
SELECT C.codigo_categoria, COUNT(M.id_matricula) AS matriculas FROM categoria_licencia C
LEFT JOIN matricula M ON C.codigo_categoria = M.codigo_categoria
GROUP BY C.codigo_categoria ORDER BY C.codigo_categoria;

-- 52. ¿Cuántas clases se han dictado en cada vehículo, incluyendo los que no tienen clases?
SELECT V.placa, COUNT(C.id_clase) AS clases FROM vehiculo V
LEFT JOIN clase C ON V.placa = C.placa AND C.estado = 'DICTADA'
GROUP BY V.placa ORDER BY V.placa;

-- 53. ¿Qué matrículas tienen certificado de aptitud y cuáles no?
SELECT M.id_matricula, M.estado, CA.numero_certificado FROM matricula M
LEFT JOIN certificado_aptitud CA ON M.id_matricula = CA.id_matricula ORDER BY M.id_matricula;

-- 54. ¿Cuántos exámenes ha calificado cada instructor, incluyendo los que no han calificado ninguno?
SELECT I.id_instructor, I.nombres, COUNT(E.id_examen) AS examenes FROM instructor I
LEFT JOIN examen E ON I.id_instructor = E.id_instructor
GROUP BY I.id_instructor, I.nombres ORDER BY I.id_instructor;

-- 55. ¿Qué vehículos tienen el SOAT o la revisión técnico-mecánica vencidos o por vencer antes de 2027?
SELECT placa, marca, fecha_venc_soat, fecha_venc_rtm FROM vehiculo
WHERE fecha_venc_soat < '2027-01-01' OR fecha_venc_rtm < '2027-01-01';

-- 56. ¿Qué matrículas aprobaron el examen teórico y también el práctico?
SELECT id_matricula FROM examen WHERE tipo = 'TEORICO' AND puntaje >= 80
AND id_matricula IN (SELECT id_matricula FROM examen WHERE tipo = 'PRACTICO' AND puntaje >= 80);

-- 57. ¿Cuál es el instructor que más horas de clase ha dictado?
SELECT id_instructor, SUM(horas) AS horas FROM clase WHERE estado = 'DICTADA'
GROUP BY id_instructor
HAVING SUM(horas) >= ALL (SELECT SUM(horas) FROM clase WHERE estado = 'DICTADA' GROUP BY id_instructor);

-- 58. ¿Qué exámenes teóricos tienen un puntaje mayor al promedio de todos los exámenes teóricos?
SELECT id_examen, id_matricula, puntaje FROM examen
WHERE tipo = 'TEORICO' AND puntaje > (SELECT AVG(puntaje) FROM examen WHERE tipo = 'TEORICO');

-- 59. ¿Cuántas horas ha recibido cada matrícula activa frente a las horas de su plan?
SELECT M.id_matricula, M.codigo_categoria, HD.horas_dictadas, HP.horas_plan FROM matricula M
JOIN (SELECT id_matricula, SUM(horas) AS horas_dictadas FROM clase WHERE estado = 'DICTADA' GROUP BY id_matricula) HD
  ON M.id_matricula = HD.id_matricula
JOIN (SELECT codigo_categoria, SUM(horas) AS horas_plan FROM plan_curso GROUP BY codigo_categoria) HP
  ON M.codigo_categoria = HP.codigo_categoria
WHERE M.estado = 'ACTIVA';

-- 60. ¿Qué alumnos con matrícula activa tienen un certificado CRC con resultado APTO?
SELECT A.nombres, A.apellidos, C.nombre_crc, C.fecha_examen FROM matricula M
JOIN alumno A ON M.id_alumno = A.id_alumno
JOIN certificado_crc C ON A.id_alumno = C.id_alumno
WHERE M.estado = 'ACTIVA' AND C.resultado = 'APTO';

-- 61. ¿Qué clases programadas tienen un instructor que no está habilitado en la categoría de la matrícula?
SELECT C.id_clase, C.id_instructor, M.codigo_categoria FROM clase C
JOIN matricula M ON C.id_matricula = M.id_matricula WHERE C.estado = 'PROGRAMADA'
EXCEPT
SELECT C.id_clase, C.id_instructor, M.codigo_categoria FROM clase C
JOIN matricula M ON C.id_matricula = M.id_matricula
JOIN instructor_categoria H ON C.id_instructor = H.id_instructor AND M.codigo_categoria = H.codigo_categoria
WHERE C.estado = 'PROGRAMADA';

-- 62. ¿Qué vehículos se usaron en clases de una categoría distinta a la del vehículo?
SELECT C.id_clase, V.placa, V.codigo_categoria AS categoria_vehiculo, M.codigo_categoria AS categoria_matricula FROM clase C
JOIN vehiculo V ON C.placa = V.placa
JOIN matricula M ON C.id_matricula = M.id_matricula
WHERE V.codigo_categoria <> M.codigo_categoria;

