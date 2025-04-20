const express = require('express');
const router = express.Router();
const controller = require('../controllers/chatController');

router.post('/chats/start', controller.startChat);
router.post('/chats/:sessionId/message', controller.sendMessage);
router.get('/chats/:sessionId', controller.getChat);

module.exports = router;
