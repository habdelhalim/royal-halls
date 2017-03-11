FROM postgres
ADD bd511264-d491-47b2-8db0-5b2f0b33d8cc /royal/
ADD restore_database.sh /docker-entrypoint-initdb.d
ENV POSTGRES_DB postgres
EXPOSE 5432
