const express = require('express');
const router = express.Router();
const controller = require('../controllers/faqController');

router.get('/faqs', controller.getAllFaqs);
router.post('/faqs', controller.createFaq); // Only admin should access
router.delete('/faqs/:id', controller.deleteFaq); // Only admin should access

module.exports = router;
