const mongoose = require('mongoose');

const ticketSchema = new mongoose.Schema({
    userId: String,
    subject: String,
    description: String,
    status: { type: String, enum: ['open', 'pending', 'resolved'], default: 'open' },
    replies: [{
        sender: String, // 'user' or 'support'
        message: String,
        createdAt: { type: Date, default: Date.now }
    }],
    createdAt: { type: Date, default: Date.now },
    updatedAt: Date
});

module.exports = mongoose.model('Ticket', ticketSchema);
