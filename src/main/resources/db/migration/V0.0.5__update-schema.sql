CREATE TABLE inspection (
                            id SERIAL  PRIMARY KEY,
                            car_matricule VARCHAR(20),
                            date_inspection DATE,
                            state_inspection VARCHAR(20),
                            CONSTRAINT fk_car FOREIGN KEY (car_matricule) REFERENCES car(matricule)
);
