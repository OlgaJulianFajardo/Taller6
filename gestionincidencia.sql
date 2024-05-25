-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 25-05-2024 a las 14:04:34
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `gestionincidencia`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `configuracion`
--
CREATE DATABASE IF NOT EXISTS `gestionincidencia` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `gestionincidencia`;

CREATE TABLE `configuracion` (
  `fecha` date NOT NULL,
  `ultimo_numero` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `configuracion`
--

INSERT INTO `configuracion` (`fecha`, `ultimo_numero`) VALUES
('2024-05-24', 10),
('2024-05-25', 8);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `incidencias`
--

CREATE TABLE `incidencias` (
  `codigo` varchar(50) NOT NULL,
  `puesto` int(11) DEFAULT NULL,
  `problema` text DEFAULT NULL,
  `estado` varchar(20) DEFAULT NULL,
  `fecha_resolucion` date DEFAULT NULL,
  `resolucion` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `incidencias`
--

INSERT INTO `incidencias` (`codigo`, `puesto`, `problema`, `estado`, `fecha_resolucion`, `resolucion`) VALUES
('24/05/2024-13:27-1', 9, 'No enciende.', 'PENDIENTE', NULL, NULL),
('24/05/2024-13:28-2', 1, 'Pantallazo azul', 'RESUELTA', '2024-05-24', 'Funciona!!'),
('24/05/2024-13:28-3', 7, 'No arranca', 'RESUELTA', '2024-05-24', 'No estaba enchufado '),
('24/05/2024-14:17-4', 9, 'Probando que el número de incidencia se asigna correctamente después de abrir la aplicación el mismo día', 'RESUELTA', '2024-05-24', 'Se asigna estupendamente ;)'),
('24/05/2024-19:07-6', 7, 'Probando, probando', 'RESUELTA', '2024-05-24', 'Probado...'),
('24/05/2024-21:22-9', 31, 'Probando de nuevo', 'PENDIENTE', NULL, NULL),
('24/05/2024-21:39-10', 62, 'Error al conectar con base de datos', 'PENDIENTE', NULL, NULL),
('25/05/2024-11:09-1', 54, 'no arranca', 'RESUELTA', '2024-05-25', 'No estaba conectado a la red'),
('25/05/2024-11:53-2', 74, 'Volviendo a probar...', 'PENDIENTE', NULL, NULL),
('25/05/2024-13:13-3', 36, 'Probando el orden', 'RESUELTA', '2024-05-23', 'Probando'),
('25/05/2024-13:54-5', 7, 'Probando usuario de nuevo', 'PENDIENTE', NULL, NULL),
('25/05/2024-13:58-6', 52, 'dsss', 'PENDIENTE', NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `incidenciaseliminadas`
--

CREATE TABLE `incidenciaseliminadas` (
  `codigo` varchar(50) NOT NULL,
  `puesto` int(11) DEFAULT NULL,
  `problema` text DEFAULT NULL,
  `estado` varchar(20) DEFAULT NULL,
  `fecha_eliminacion` date DEFAULT NULL,
  `causa_eliminacion` text DEFAULT NULL,
  `nombreUsuario` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `incidenciaseliminadas`
--

INSERT INTO `incidenciaseliminadas` (`codigo`, `puesto`, `problema`, `estado`, `fecha_eliminacion`, `causa_eliminacion`, `nombreUsuario`) VALUES
('24/05/2024-14:21-5', 13, 'No carga el sistema', 'ELIMINADA', '2024-05-24', 'ya carga', NULL),
('24/05/2024-20:46-8', 1, 'no enciende la pantalla', 'ELIMINADA', '2024-05-25', 'ya ha encendido', NULL),
('25/05/2024-13:40-4', 2, 'Probando usuario', 'ELIMINADA', '2024-05-25', 'Probando', NULL),
('25/05/2024-14:02-8', 55, 'Probando usuario de nuevo', 'ELIMINADA', '2024-05-25', 'Probando...', 'Olga');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `configuracion`
--
ALTER TABLE `configuracion`
  ADD PRIMARY KEY (`fecha`);

--
-- Indices de la tabla `incidencias`
--
ALTER TABLE `incidencias`
  ADD PRIMARY KEY (`codigo`);

--
-- Indices de la tabla `incidenciaseliminadas`
--
ALTER TABLE `incidenciaseliminadas`
  ADD PRIMARY KEY (`codigo`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;


-- Asignamos permisos al usuario pepe con contraseña 12345.

GRANT ALL PRIVILEGES ON gestionincidencia.*
TO pepe@localhost IDENTIFIED BY '12345';


-- Asignamos permisos al usuario olga con contraseña 12345.

GRANT ALL PRIVILEGES ON gestionincidencia.*
TO olga@localhost IDENTIFIED BY '12345';