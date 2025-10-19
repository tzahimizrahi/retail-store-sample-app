const express = require('express');
const app = express();

app.use(express.json());

const reviews = {};

app.get('/reviews/:productId', (req, res) => {
  const productId = req.params.productId;
  res.json(reviews[productId] || []);
});

app.post('/reviews/:productId', (req, res) => {
  const productId = req.params.productId;
  if (!reviews[productId]) reviews[productId] = [];
  const reviewText = req.body.text || req.body.comment || '';
  const review = {
    text: reviewText,
    comment: reviewText,
    rating: req.body.rating || 5,
    user: req.body.user || req.body.author || 'Anonymous',
    timestamp: new Date().toISOString()
  };
  reviews[productId].push(review);
  res.status(201).json({ message: 'Review added' });
});

app.get('/health', (req, res) => res.json({ status: 'ok' }));

const PORT = process.env.PORT || 80;
app.listen(PORT, () => console.log(`Reviews service on port ${PORT}`));
