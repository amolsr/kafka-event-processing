const { Kafka } = require('kafkajs')
const fs = require('fs')

// This creates a client instance that is configured to connect to the Kafka broker provided by
// the environment variable KAFKA_BOOTSTRAP_SERVER
const kafka = new Kafka({
    clientId: 'topic_test',
    brokers: ['10.120.11.172:9094'],
    logLevel: 2
})

const producer = kafka.producer()

producer.on('producer.connect', () => {
    console.log(`KafkaProvider: connected`);
});
producer.on('producer.disconnect', () => {
    console.log(`KafkaProvider: could not connect`);
});
producer.on('producer.network.request_timeout', (payload) => {
    console.log(`KafkaProvider: request timeout ${payload.clientId}`);
});

const run = async () => {
    await producer.connect();
};

run().catch(console.error);

module.exports = producer;