CREATE DATABASE bip;
CREATE USER develop WITH PASSWORD 'develop';
ALTER DATABASE bip OWNER TO develop;
GRANT ALL PRIVILEGES ON DATABASE bip TO develop;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO develop;