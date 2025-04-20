const express = require('express');
const app = express();
const port = 3000;
const mongoose = require('mongoose');
const http = require('http');
const socketIo = require('socket.io');
const ChatSession = require('./models/ChatSession');
const ticketRoutes = require('./routes/ticketRoutes');
const chatRoutes = require('./routes/chatRoutes');
const faqRoutes = require('./routes/faqRoutes');
const eurekaClient = require('./eureka-client');
const server = http.createServer(app);
const io = socketIo(server, {
  cors: { origin: '*' }
});

mongoose.connect('mongodb://localhost:27017/customer-service');

io.on('connection', (socket) => {
  console.log('New client connected:', socket.id);

  socket.on('join', ({ sessionId }) => {
    socket.join(sessionId);
    console.log(`Socket ${socket.id} joined room ${sessionId}`);
  });

  socket.on('sendMessage', async ({ sessionId, sender, message }) => {
    // Store in DB
    const session = await ChatSession.findById(sessionId);
    session.messages.push({ sender, message });
    await session.save();

    io.to(sessionId).emit('newMessage', { sender, message });
  });

  socket.on('disconnect', () => {
    console.log('Client disconnected:', socket.id);
  });
});

app.use(express.json());

app.use('/api/customers', ticketRoutes);
app.use('/api/customers', chatRoutes);
app.use('/api/customers', faqRoutes);

server.listen(port, () => {
  console.log(`Customer service listening on port ${port}`);
  eurekaClient.start((error) => {
    console.log(error || 'Eureka client registered');
  });
});
