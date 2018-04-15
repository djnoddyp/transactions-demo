DROP TABLE IF EXISTS Bikes;

CREATE TABLE Bikes
(
  id      INT NOT NULL AUTO_INCREMENT,
  make    VARCHAR(20),
  model   VARCHAR(20),
  colour  VARCHAR(20),
  style   ENUM('MOUNTAIN', 'ROAD', 'HYBRID'),
  PRIMARY KEY (id)
);