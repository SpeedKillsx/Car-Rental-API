CREATE SEQUENCE IF NOT EXISTS location_id_seq START WITH 1 INCREMENT BY 1;
ALTER TABLE location ALTER COLUMN id SET DEFAULT nextval('location_id_seq');
ALTER SEQUENCE location_id_seq OWNED BY location.id;