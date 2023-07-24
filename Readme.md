## Kafka Consumer Producer using Python

1. Install Docker on your system.

2. Clone the project directory.

3. Run the following command:
`docker compose up`

4. Run Jupyter Lab using the following command:
`docker run --net=host -p 10001:8888 jupyter/scipy-notebook:2023-02-28 -v ./notebooks:/home/jovyan`

5. Open localhost:10001.

6. Run the producer script, then the consumer script.