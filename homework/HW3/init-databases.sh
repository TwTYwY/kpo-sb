#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE filestoredb;
    CREATE DATABASE analysisdb;
    GRANT ALL PRIVILEGES ON DATABASE filestoredb TO postgres;
    GRANT ALL PRIVILEGES ON DATABASE analysisdb TO postgres;
EOSQL