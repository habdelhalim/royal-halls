#!/bin/bash
set -e 
POSTGRES_USER=postgres

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<EOF
create database royalhalls;
EOF

pg_restore --verbose --clean --no-acl --no-owner -h localhost -U "$POSTGRES_USER" -d royalhalls /royal/bd511264-d491-47b2-8db0-5b2f0b33d8cc
