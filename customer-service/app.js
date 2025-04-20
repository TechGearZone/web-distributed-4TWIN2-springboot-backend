const express = require('express');
const app = express();
const port = 3000;
const mongoose = require('mongoose');
const ticketRoutes = require('./routes/ticketRoutes');
const chatRoutes = require('./routes/chatRoutes');
const faqRoutes = require('./routes/faqRoutes');

const eurekaClient = require('./eureka-client');

mongoose.connect('mongodb://localhost:27017/customer-service');

app.use('/api/customers', ticketRoutes);
app.use('/api/customers', chatRoutes);
app.use('/api/customers', faqRoutes);

app.listen(port, () => {
  console.log(`Customer service listening on port ${port}`);
  eurekaClient.start((error) => {
    console.log(error || 'Eureka client registered');
  });
});

