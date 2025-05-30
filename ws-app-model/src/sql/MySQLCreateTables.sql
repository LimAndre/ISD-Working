-- Borra tablas si ya existen (útil en desarrollo)
DROP TABLE IF EXISTS Inscripcion;
DROP TABLE IF EXISTS Curso;

-- -----------------------------------------------------------------
-- Tabla Curso
-- -----------------------------------------------------------------
CREATE TABLE Curso (
                       cursoId BIGINT NOT NULL AUTO_INCREMENT,
                       ciudad VARCHAR(100) NOT NULL,
                       nombre VARCHAR(200) NOT NULL,
                       fechaInicio DATETIME NOT NULL,
                       fechaAlta DATETIME NOT NULL,
                       precio FLOAT NOT NULL,
                       plazasMaximas INT NOT NULL,
                       PRIMARY KEY (cursoId)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------------
-- Tabla Inscripcion
-- -----------------------------------------------------------------
CREATE TABLE Inscripcion (
                             inscripcionId BIGINT NOT NULL AUTO_INCREMENT,
                             cursoId       BIGINT NOT NULL,
                             emailUsuario  VARCHAR(100) NOT NULL,
                             tarjetaPago   CHAR(16)    NOT NULL,
                             fechaInscripcion DATETIME NOT NULL,
                             PRIMARY KEY (inscripcionId),
                             FOREIGN KEY (cursoId)
                                 REFERENCES Curso(cursoId)
                                 ON DELETE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4;
