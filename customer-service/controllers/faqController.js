const FAQ = require('../models/FAQ');

exports.getAllFaqs = async (req, res) => {
    try {
        const faqs = await FAQ.find();
        res.json(faqs);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
};

exports.createFaq = async (req, res) => {
    try {
        const faq = new FAQ(req.body);
        await faq.save();
        res.status(201).json(faq);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
};

exports.deleteFaq = async (req, res) => {
    try {
        await FAQ.findByIdAndDelete(req.params.id);
        res.json({ message: 'FAQ deleted' });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
};
