const mongoose = require('mongoose');

const chatSessionSchema = new mongoose.Schema({
    userId: String,
    messages: [{
        sender: String,
        message: String,
        createdAt: { type: Date, default: Date.now }
    }],
    createdAt: { type: Date, default: Date.now }
});

module.exports = mongoose.model('ChatSession', chatSessionSchema);
