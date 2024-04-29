## Kafka Cluster Setup

1. Install Docker on your system.

2. Clone the project directory.

3. Please update the Advertised IP address in docker compose file before deployment. 

4. Run the following command:
`docker compose up`

## Kafka Consumer Producer

### Using Python Scripts

1. Run Jupyter Lab using the following command for experimentation:
`docker run --net=host -p 10001:8888 jupyter/scipy-notebook:2023-02-28

2. Open localhost:10001.

3. Import the producer script, then the consumer script from [here](./notebooks)

### Using Express App

1. update the config in 

[Link](./monitoring/Readme.md) for setting up the monitoring for kafka cluster.