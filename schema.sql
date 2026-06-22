-- =====================================================================
-- TP Programación Avanzada - Esquema base de datos
-- Motor: MySQL 8 (probado en MAMP)
-- Conexión esperada por el código: jdbc:mysql://localhost:3306/universidad
--
-- Uso:
--   mysql -u root -proot < schema.sql
-- =====================================================================

CREATE DATABASE IF NOT EXISTS universidad CHARACTER SET utf8mb4;
USE universidad;

CREATE TABLE IF NOT EXISTS alumnos (
    DNI          INT          PRIMARY KEY,
    NOMBRE       VARCHAR(100) NOT NULL,
    APELLIDO     VARCHAR(100) NOT NULL,
    FECNAC       DATE         NULL,
    FECING       DATE         NULL,
    PROMEDIO     DOUBLE       NULL,
    CANTMATAPROB INT          NULL,
    ESTADO       CHAR(1)      NOT NULL DEFAULT 'A'   -- 'A' = Alta, 'B' = Baja logica
);

-- Datos de ejemplo (2 activos, 1 dado de baja para probar "Ver eliminados")
INSERT IGNORE INTO alumnos
    (DNI, NOMBRE, APELLIDO, FECNAC, FECING, PROMEDIO, CANTMATAPROB, ESTADO)
VALUES
    (40111222, 'Juan',  'Perez', '2000-03-15', '2019-03-01', 8.5, 12, 'A'),
    (38999111, 'Maria', 'Gomez', '1998-07-22', '2017-03-01', 7.2, 20, 'A'),
    (41222333, 'Lucas', 'Diaz',  '2001-11-05', '2020-03-01', 6.0,  5, 'B');
