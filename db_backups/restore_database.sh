#/bin/bash
psql -Upostgres <<EOF
create database royalhalls;
EOF
pg_restore --verbose --clean --no-acl --no-owner -h localhost -U postgres -d royalhalls $1
