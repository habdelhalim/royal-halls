#/bin/bash
pg_restore --verbose --clean --no-acl --no-owner -h localhost -U postgres $1
