# Example project: kafka-driven accounting

## Prerequisites
### Database
To run the application you need a postgres or cockroachDB database running.
There must be a database `lotto` available. The user/server information is located in the `src/main/docker/postbootcommands.asadmin` file.

### Kafka
A kafka cluster is required (local dev cluster is sufficient). 

## Getting started

Path to jar:  
target/payara-micro.jar

Specify the correct location of the cockroachDB or postgres db in the file:  
`postbootcommands.asadmin`

VM options (define the kafka server... in this case just the local dev cluster):  
`-Dkafka.servers=localhost:9092`

Program arguments:  
`--deploy kafka-rar.rar --addJars postgresql.jar --deploy ROOT.war --port 8080 --postbootcommandfile ../src/main/docker/postbootcommands.asadmin`

Set the working directory to the maven target folder

Do a maven install before running it.

## Create a player

Execute the request defined in the `register.http` file to register the user 'John Doe'

## Send some accounting events

The kafka message body has to be in JSON format with the following content:  
`{"id": "YOUR_TRANSACTION_ID", "playerId": "jdo", "payable": 0, "nonPayable": 250, "description": "just a test"}`

## Responses

The responses are delivered in 2 different topics:
- `accountUpdates` for successful transactions
- `transactionFailures` for failures during processing

