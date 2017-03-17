#!/bin/bash
set -e 
POSTGRES_USER=postgres

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<EOF
create database royalhalls;
EOF

pg_restore --verbose --clean --no-acl --no-owner -h localhost -U "$POSTGRES_USER" -d royalhalls /royal/9546b908-b979-4307-aedd-6d00219c5570
