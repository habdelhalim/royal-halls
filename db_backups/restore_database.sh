#!/bin/bash
set -e 
POSTGRES_USER=postgres

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<EOF
create database royalhalls;
EOF

pg_restore --verbose --clean --no-acl --no-owner -h localhost -U "$POSTGRES_USER" -d royalhalls /royal/71ae95ad-3636-471c-9b22-8dcfa333a3d2
