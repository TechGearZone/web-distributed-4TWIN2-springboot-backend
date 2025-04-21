const Ticket = require('../models/Ticket');

exports.createTicket = async (req, res) => {
    try {
        const ticket = new Ticket(req.body);
        await ticket.save();
        res.status(201).json(ticket);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
};

exports.getTicketById = async (req, res) => {
    try {
        const ticket = await Ticket.findById(req.params.id);
        res.json(ticket);
    } catch (err) {
        res.status(404).json({ error: 'Ticket not found' });
    }
};

exports.getTicketsByUser = async (req, res) => {
    try {
        const tickets = await Ticket.find({ userId: req.params.userId });
        res.json(tickets);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
};

exports.addReply = async (req, res) => {
    try {
        const ticket = await Ticket.findById(req.params.id);
        ticket.replies.push({
            sender: req.body.sender,
            message: req.body.message
        });
        ticket.updatedAt = new Date();
        await ticket.save();
        res.json(ticket);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
};

exports.updateStatus = async (req, res) => {
    try {
        const ticket = await Ticket.findByIdAndUpdate(req.params.id, { status: req.body.status, updatedAt: new Date() }, { new: true });
        res.json(ticket);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
};

exports.getAllTickets = async (req, res) => {
    try {
        const tickets = await Ticket.find(); // or whatever your data source is
        res.json(tickets);
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
};
