const express = require('express');
const app = express();
const port = 3000;

const eurekaClient = require('./eureka-client');

app.get('/api/customers', (req, res) => {
  res.json([{ id: 1, name: 'John Doe' }]);
});

app.listen(port, () => {
  console.log(`Customer service listening on port ${port}`);
  eurekaClient.start((error) => {
    console.log(error || 'Eureka client registered');
  });
});

