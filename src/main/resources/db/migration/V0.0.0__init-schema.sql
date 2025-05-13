CREATE TABLE client (
                        id INT PRIMARY KEY,
                        first_name VARCHAR(100),
                        last_name VARCHAR(100),
                        debts FLOAT,
                        state_client VARCHAR(50)
);

CREATE TABLE car (
                     matricule VARCHAR PRIMARY KEY,
                     model VARCHAR(100),
                     state VARCHAR(10)
);

CREATE TABLE location (
                          id INT,
                          car_matricule varchar,
                          date_begin DATE,
                          date_end DATE,
                          amount DECIMAL(10, 2),
                          location_state VARCHAR,
                          client_id INT,
                          PRIMARY KEY (id),
                          FOREIGN KEY (car_matricule ) REFERENCES car(matricule),
                          FOREIGN KEY (client_id) REFERENCES client(id)
);
