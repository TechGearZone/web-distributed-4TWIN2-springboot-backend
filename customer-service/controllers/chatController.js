const ChatSession = require('../models/ChatSession');

exports.startChat = async (req, res) => {
    try {
        const session = new ChatSession({ userId: req.body.userId });
        await session.save();
        res.status(201).json(session);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
};

exports.sendMessage = async (req, res) => {
    try {
        const session = await ChatSession.findById(req.params.sessionId);
        session.messages.push({
            sender: req.body.sender,
            message: req.body.message
        });
        await session.save();
        res.json(session);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
};

exports.getChat = async (req, res) => {
    try {
        const session = await ChatSession.findById(req.params.sessionId);
        res.json(session);
    } catch (err) {
        res.status(404).json({ error: 'Chat not found' });
    }
};
