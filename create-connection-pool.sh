#!/usr/bin/env bash
./asadmin create-jdbc-connection-pool --datasourceclassname org.postgresql.ds.PGConnectionPoolDataSource \
        --restype javax.sql.ConnectionPoolDataSource \
        --property PortNumber=31601:User=testuser:ServerName="192.168.99.100":Password=123456789:ApplicationName=Lottoland:DatabaseName=lotto \
        CockroachDB

./asadmin create-jdbc-resource --connectionpoolid CockroachDB jdbc/cockroach
