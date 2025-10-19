# Reviews Microservice

Simple Node.js Express API for product reviews with in-memory storage.

## Features

- GET/POST `/reviews/:productId` endpoints
- In-memory storage (no database)
- ConfigMap-based Kubernetes deployment
- Health check endpoint at `/health`

## Local Development

```bash
npm install
npm start
```

Server runs on port 80 (or PORT environment variable).

## API Endpoints

### Get Reviews
```bash
GET /reviews/:productId
```

Returns array of reviews for the product.

### Add Review
```bash
POST /reviews/:productId
Content-Type: application/json

{
  "rating": 5,
  "user": "John Doe",
  "text": "Great product!"
}
```

### Health Check
```bash
GET /health
```

## Kubernetes Deployment

Deploy to cluster:
```bash
kubectl apply -f k8s-deployment.yaml
```

The deployment uses:
- **Namespace**: `reviews`
- **Init container**: Copies code from ConfigMap and runs `npm install`
- **Base image**: `node:18-alpine`
- **Service**: ClusterIP on port 80

## Review Data Format

Reviews are stored with the following fields:
- `user`: Reviewer name (defaults to "Anonymous")
- `text`: Review text
- `comment`: Same as text (for UI compatibility)
- `rating`: 1-5 stars (defaults to 5)
- `timestamp`: ISO 8601 timestamp

## Integration

The UI service connects to reviews at:
```
http://reviews.reviews.svc:80
```

Configure in UI ConfigMap:
```yaml
RETAIL_UI_ENDPOINTS_REVIEWS: http://reviews.reviews.svc:80
```
