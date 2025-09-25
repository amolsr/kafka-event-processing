const express = require('express');
require('./config/kafka');

const app = express();
const port = process.env.PORT || 3000;

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
    await producer.send({
      topic: topic,
      messages: [message],
    });
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
