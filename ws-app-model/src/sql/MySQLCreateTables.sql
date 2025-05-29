-- ----------------------------------------------------------------------------
-- App Model: Definición de tablas Curso e Inscripcion
-------------------------------------------------------------------------------

-- Eliminación (para recrear)
DROP TABLE IF EXISTS Inscripcion;
DROP TABLE IF EXISTS Curso;

-- --------------------------------- Curso ------------------------------------
CREATE TABLE Curso (
                       cursoId        BIGINT NOT NULL AUTO_INCREMENT,
                       ciudad         VARCHAR(100) NOT NULL,
                       nombre         VARCHAR(255) NOT NULL,
                       fechaInicio    DATETIME NOT NULL,
                       fechaAlta      DATETIME NOT NULL,
                       precio         FLOAT NOT NULL,
                       plazasMaximas  INT NOT NULL,
                       CONSTRAINT CursoPK PRIMARY KEY(cursoId),
                       CONSTRAINT validPrecio CHECK(precio >= 0),
                       CONSTRAINT validPlazas CHECK(plazasMaximas >= 0)
) ENGINE = InnoDB;

-- ------------------------------ Inscripcion -------------------------------
CREATE TABLE Inscripcion (
                             inscripcionId    BIGINT NOT NULL AUTO_INCREMENT,
                             cursoId          BIGINT NOT NULL,
                             emailUsuario     VARCHAR(100) NOT NULL,
                             tarjetaPago      VARCHAR(16) NOT NULL,
                             fechaInscripcion DATETIME NOT NULL,
                             CONSTRAINT InscripcionPK PRIMARY KEY(inscripcionId),
                             CONSTRAINT FK_Curso FOREIGN KEY(cursoId)
                             REFERENCES Curso(cursoId) ON DELETE CASCADE
) ENGINE = InnoDB;
