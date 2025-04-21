const express = require('express');
const router = express.Router();
const controller = require('../controllers/ticketController');

router.post('/tickets', controller.createTicket);
router.get('/tickets/:id', controller.getTicketById);
router.get('/tickets/user/:userId', controller.getTicketsByUser);
router.put('/tickets/:id/reply', controller.addReply);
router.patch('/tickets/:id/status', controller.updateStatus);
router.get('/tickets', controller.getAllTickets);

module.exports = router;
