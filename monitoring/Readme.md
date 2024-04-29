## Kafka Cluster Monitoring

1. Install Docker on your system.

2. Clone the project directory.

3. Please update the Advertised IP address in docker compose files before deployment. 

4. Run the following command:
`docker compose up`

5. Use the port 3000 to access the grafana dashboard. (default user/password - admin/admin)

6. Add the Prometheus data source which is listerning in port 9090.

7. import the [Dashboard](./dashboard/kafka-overview.json) after updating the prometheus uuid in json.

External reference for the setup:

https://gsfl3101.medium.com/kafka-kraft-monitoring-with-prometheus-and-grafana-1994ef272f48

