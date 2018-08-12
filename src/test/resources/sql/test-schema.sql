DROP TABLE IF EXISTS Bikes;
DROP TYPE IF EXISTS Style;

CREATE TYPE Style AS ENUM ('MOUNTAIN', 'ROAD', 'HYBRID');

CREATE TABLE Bikes (
  id      SERIAL,
  make    VARCHAR(20),
  model   VARCHAR(20),
  colour  VARCHAR(20),
  style   Style,
  PRIMARY KEY (id)
);