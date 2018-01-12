FROM docker-repo.repo.lottoland.io/dreamit/payara-full:174.1

ENV HAZELCAST_SERVICE_NAME=games-hazelcast KUBERNETES_NAMESPACE=games

RUN echo 'create-jdbc-connection-pool --datasourceclassname org.postgresql.ds.PGConnectionPoolDataSource \
                  --restype javax.sql.ConnectionPoolDataSource \
                  --property PortNumber=26257:User=testuser:ServerName=cockroachdb:Password=123456789:ApplicationName=Lottoland:DatabaseName=games \
                  CockroachDB' >> ${POSTBOOT_COMMANDS} && \
    echo "create-jdbc-resource --connectionpoolid CockroachDB jdbc/cockroach" >> ${POSTBOOT_COMMANDS}

COPY --chown=payara:payara ROOT.war ${DEPLOY_DIR}
COPY --chown=payara:payara postgresql.jar ${PAYARA_PATH}/glassfish/domains/${PAYARA_DOMAIN}/lib
