#!/usr/bin/env bash
./asadmin create-jdbc-connection-pool --datasourceclassname org.postgresql.ds.PGConnectionPoolDataSource \
        --restype javax.sql.ConnectionPoolDataSource \
        --property PortNumber=26257:User=testuser:ServerName="localhost":Password=123456789:ApplicationName=Lottoland:DatabaseName=lotto \
        CockroachDB

./asadmin create-jdbc-resource --connectionpoolid CockroachDB jdbc/cockroach
