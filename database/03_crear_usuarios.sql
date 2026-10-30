-- ConduCar: usuarios y permisos
-- admin        -> todos los permisos sobre conducar
-- gestionador  -> consultar, insertar, modificar y borrar datos
-- consultor    -> solo consultar (lo usa la aplicación web)

DROP USER IF EXISTS 'admin', 'gestionador', 'consultor';

CREATE USER 'admin' IDENTIFIED BY 'Admin_2026';
GRANT ALL PRIVILEGES ON conducar.* TO 'admin';

CREATE USER 'gestionador' IDENTIFIED BY 'Gestion_2026';
GRANT SELECT, INSERT, UPDATE, DELETE ON conducar.* TO 'gestionador';

CREATE USER 'consultor' IDENTIFIED BY 'Consultor_2026';
GRANT SELECT ON conducar.* TO 'consultor';

SHOW GRANTS FOR 'consultor';
