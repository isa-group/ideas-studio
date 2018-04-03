FROM mysql:latest

ENV MYSQL_USER ideas
ENV MYSQL_ROOT_PASSWORD r00t
ENV MYSQL_PASSWORD 1d34s
ENV MYSQL_DATABASE ideas-db

ADD dump.sql /docker-entrypoint-initdb.d/dump.sql

#RUN /bin/bash -c "/usr/bin/mysqld_safe --skip-grant-tables &" && \    
#  sleep 5 && \  
#  mysql -uroot ideas-db < /tmp/dump.sql 
# sleep 5 && \
# /bin/bash -c "/usr/bin/mysqld_safe stop"