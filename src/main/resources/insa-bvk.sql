INSERT INTO customers (device_id, reg, dev_eui, device_type, address, profile, note)
VALUES (60062440, '73482/0', '000000000F1D8693', 1, 'Crnotravska br. 11', '"Diehl" - DN 80', 'korisnik stambena zgrada');

INSERT INTO customers (device_id, reg, dev_eui, device_type, address, profile, note)
VALUES (60041362, '30456/0', '0018B210000062A5', 1, 'Ljubice Luković br. 1', '"Diehl" - DN 50', 'korisnik Ustanova studentski centar "Beograd"');

INSERT INTO customers (device_id, reg, dev_eui, device_type, address, profile, note)
VALUES (112241, '19131/0', '3', 2, 'Vojvode skopljanca BB', '"INSA" - DN 25', 'korisnik JKP Beogradske pijace');

INSERT INTO customers (device_id, reg, dev_eui, device_type, address, profile, note)
VALUES (2864600, '164941/0', '4', 2, 'Danijelova br. 33', '"INSA" - DN 80', 'korisnik JP "Gradsko stambeno"');

INSERT INTO customers (device_id, reg, dev_eui, device_type, address, profile, note)
VALUES (30215423, '47043/0', '5', 2, 'Mis Irbijeva br. 59', '"EWT" - DN 65', 'korisnik stambena zgrada');

INSERT INTO customers (device_id, reg, dev_eui, device_type, address, profile, note)
VALUES (30221062, '76810/0', '6', 2, 'Bulevar despota Stefana br. 68a', '"EWT" - DN 65', 'korisnik stambena zgrada');


CREATE TABLE customers (
  id BIGSERIAL NOT NULL,
  device_id bigint,
  reg varchar(255),
  dev_eui varchar(255),
  device_type int,
  address varchar(255),
  profile varchar(255),
  note varchar(455),
  CONSTRAINT customers_pkey PRIMARY KEY (id),
  CONSTRAINT customers_dev_eui_unique UNIQUE (dev_eui)
);


CREATE TABLE measurements (
  id BIGSERIAL NOT NULL,
  customer_id bigint,
  counter integer,
  read_at timestamp,
  CONSTRAINT measurements_pkey PRIMARY KEY (id),
  FOREIGN KEY (customer_id) REFERENCES customers (id)
);
