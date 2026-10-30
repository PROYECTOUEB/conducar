-- ConduLog: usuarios y permisos
-- admin        -> todos los permisos sobre condulog
-- gestionador  -> consultar, insertar, modificar y borrar datos
-- consultor    -> solo consultar (lo usa la aplicación web)

DROP USER IF EXISTS 'admin', 'gestionador', 'consultor';

CREATE USER 'admin' IDENTIFIED BY 'Admin_2026';
GRANT ALL PRIVILEGES ON condulog.* TO 'admin';

CREATE USER 'gestionador' IDENTIFIED BY 'Gestion_2026';
GRANT SELECT, INSERT, UPDATE, DELETE ON condulog.* TO 'gestionador';

CREATE USER 'consultor' IDENTIFIED BY 'Consultor_2026';
GRANT SELECT ON condulog.* TO 'consultor';

SHOW GRANTS FOR 'consultor';
