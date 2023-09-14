const express = require('express');
const Kafka = require('node-rdkafka');

const app = express();
const port = process.env.PORT || 3000;

// Kafka producer configuration
const producer = new Kafka.Producer({
  'metadata.broker.list': 'localhost:9094', // Replace with your Kafka broker address
  'dr_cb': true, // Enable delivery report callbacks
  'queue.buffering.max.ms': 10, // Send messages every 10 milliseconds
  'queue.buffering.max.messages': 100000, // Buffer up to 1000 messages
  'sasl.username': 'user', // Replace with your Kafka username
  'sasl.password': 'password', // Replace with your Kafka password
});

// Connect to the Kafka broker
producer.connect();

// Handle errors
producer.on('event.error', (err) => {
  console.error('Kafka producer error:', err);
});

// Middleware to parse JSON requests
app.use(express.json());

// API endpoint to send messages to Kafka
app.post('/logs/log-events', async (req, res) => {
  const { topic, message } = req.body;

  if (!topic || !message) {
    return res.status(400).json({ error: 'Missing topic or message in the request body' });
  }

  try {

    // Produce the JSON message to the specified Kafka topic
    producer.produce(
      topic,
      null, // Partition (null for random)
      Buffer.from(JSON.stringify(message)), // Message payload
      null, // Key (null for no key)
      Date.now(), // Timestamp
      null, // Headers
    )
    producer.flush()
    // console.log(`JSON message sent to topic ${topic}`);
    res.json({ message: 'JSON message sent to Kafka successfully' });
  } catch (error) {
    console.error('Error producing message:', error);
    res.status(500).json({ error: 'Failed to produce message to Kafka' });
  }
});

// Start the Express server
app.listen(port, () => {
  console.log(`Express server is running on port ${port}`);
});
