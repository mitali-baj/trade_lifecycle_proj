We are building a trade execution and lifecycle Straight Through Processing (STP) platform. We will build this in microsercives architecture. Each microservice will be build using spring boot and containerized using docker. In development it will not have any load balancer or API gateway.
A database server will be running in a docker container. Use postgresql.
All services will be communicating via Kafka running on a kafka server which is deployed using docker container. So output of the service will be published on the respective Kafka topic. Before putting on the topic, it will log the trade life cycle state in database along with all information of data.
Following are the services - 
1. Trade Capture Service - It will get basic trade data like ticker, quantity, price, buy/sell via an API from client front end. This service will create a unique trdeId. this is an entry point for STP. This service will only produce an event on Kafka.
2. Trade Enrichment Service - This service will listen to a kafka topics for trades. Following is the enrichment process.
    a. Consume trade information from kafka
    b. Add Clients static data
    c. Add trade reference data. for example currency code, instrument data, rating etc and other necessary ref data. Pull the data from a postgresql table.
    d. Add market data like current market price
    e. Push the enriched data to llifecycle state db.
    f. Publish enriched record on Kafka topic.

Lets build these two services. you will create 
1. docker containers images for all the components including kafka, postgresql and microservices with necessary dependencies.
2. Write the code for microservices and expose API endpoint
3. A build script for the code
4. A windows cmd script to lauch the application compnents
5. A test file that sends test trade.
