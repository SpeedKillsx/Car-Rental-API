CREATE TABLE client (
                        id INT PRIMARY KEY,
                        first_name VARCHAR(100),
                        last_name VARCHAR(100),
                        debts FLOAT,
                        statu_client VARCHAR(50)
);

CREATE TABLE car (
                     matricule VARCHAR PRIMARY KEY,
                     model VARCHAR(100)
);

CREATE TABLE location (
                          id INT,
                          matricule varchar,
                          date_begin DATE,
                          date_end DATE,
                          amount DECIMAL(10, 2),  -- Montant en euros ou autres, avec 2 décimales
                          clientId INT,
                          PRIMARY KEY (id, matricule),
                          FOREIGN KEY (matricule) REFERENCES car(matricule),
                          FOREIGN KEY (clientId) REFERENCES client(id)
);

CREATE TABLE inspection (
                            id INT PRIMARY KEY,
                            car_matricule VARCHAR,
                            date_inspection DATE,
                            state_inspection VARCHAR(100),
                            FOREIGN KEY (car_matricule) REFERENCES car(matricule)
);
