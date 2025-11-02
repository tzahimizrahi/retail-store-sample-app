# Demo Setup Instructions

## Demo Context
- **Audience**: New DevOps engineer learning about retail store application mono-repo project
- **Purpose**: Help them learn about the project and assist with deploying/updating app components on Kubernetes cluster

## AWS Configuration
- **Profile**: eks_mcp_demo
- **Account**: 377881603156
- **Region**: us-west-2

## Kubernetes Configuration
- **Cluster Name**: eks-mcp-workshop
- **Cluster ARN**: arn:aws:eks:us-west-2:377881603156:cluster/eks-mcp-workshop
- **Node**: 1 inf1.xlarge instance with Karpenter management

## Application Status
- **Total Pods**: 18 pods running
- **Application Services**: UI, Catalog, Carts, Orders, Checkout, Assets
- **Databases**: MySQL (catalog/orders), DynamoDB (carts), Redis (checkout), RabbitMQ
- **Infrastructure**: Karpenter, CoreDNS, AWS VPC CNI, Kube-proxy

## Application Access
- **Ingress Name**: ui (in ui namespace)
- **External DNS**: Check ingress with `kubectl -n ui get ingress` to get current ALB address
- **Expected format**: k8s-ui-ui-XXXXXXXX-YYYYYYYY.us-west-2.elb.amazonaws.com

## Key Guidelines
- Always use EKS MCP server tools for Kubernetes operations
- Focus on educational aspects for new DevOps engineers
- Help with component deployment and updates
- Explain retail store application architecture and patterns
- Use the established development guidelines from the project

## Kubernetes Deployment Best Practices

### CRITICAL: Never Use Direct kubectl Commands for Updates
**Always use manifest files for all Kubernetes operations**

#### ❌ WRONG Approach:
```bash
# Never do this
kubectl set image deployment/ui ui=new-image:tag -n ui
kubectl patch configmap ui -n ui --type merge -p '{"data":{"KEY":"value"}}'
kubectl scale deployment/ui --replicas=3 -n ui
```

#### ✅ CORRECT Approach:
1. **Export current manifest** (if not already saved)
2. **Edit the manifest file**
3. **Apply the updated manifest**

### UI Deployment and ConfigMap Locations

**UI Deployment Manifest**: 
- Helm Template: `/src/ui/chart/templates/deployment.yaml`
- Applied manifests can be exported from cluster

**UI ConfigMap Manifest**:
- Helm Template: `/src/ui/chart/templates/configmap.yml`
- Applied manifests can be exported from cluster

### Workflow for Updates

#### Step 1: Export Current Manifest (if needed)
```bash
kubectl get deployment ui -n ui -o yaml > ui-deployment.yaml
kubectl get configmap ui -n ui -o yaml > ui-configmap.yaml
```

#### Step 2: Edit Manifest File
Edit the YAML file with required changes:
- Update image tags
- Add/modify environment variables
- Change ConfigMap data
- Adjust resource limits

#### Step 3: Apply Updated Manifest
```bash
# Using EKS MCP tools
apply_yaml(
    yaml_path="/absolute/path/to/ui-deployment.yaml",
    cluster_name="eks-workshop",
    namespace="ui"
)
```

### Example: Updating UI Image

```yaml
# ui-deployment.yaml
spec:
  template:
    spec:
      containers:
      - name: ui
        image: 377881603156.dkr.ecr.us-west-2.amazonaws.com/retail-store-ui:v1.0.4-reviews
        # ... rest of container spec
```

### Example: Updating UI ConfigMap

```yaml
# ui-configmap.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: ui
  namespace: ui
data:
  RETAIL_UI_ENDPOINTS_CATALOG: http://catalog.catalog.svc:80
  RETAIL_UI_ENDPOINTS_CARTS: http://carts.carts.svc:80
  RETAIL_UI_ENDPOINTS_ORDERS: http://orders.orders.svc:80
  RETAIL_UI_ENDPOINTS_CHECKOUT: http://checkout.checkout.svc:80
  RETAIL_UI_ENDPOINTS_REVIEWS: http://reviews.reviews.svc:80  # NEW
```

### Why Manifest-Based Approach?

1. **Version Control**: Manifests can be committed to Git
2. **Reproducibility**: Same manifest produces same result
3. **Auditability**: Clear history of what changed
4. **Rollback**: Easy to revert to previous manifest
5. **Documentation**: Manifest serves as documentation
6. **GitOps Ready**: Enables GitOps workflows

### When kubectl is Acceptable

Only use kubectl for:
- **Read operations**: `kubectl get`, `kubectl describe`, `kubectl logs`
- **Debugging**: `kubectl exec`, `kubectl port-forward`
- **Status checks**: `kubectl rollout status`
- **Testing**: Temporary curl pods for endpoint testing

## Connectivity Status
✅ AWS Profile: Connected (admin user)
✅ Kubernetes Cluster: Connected (1 node available)
✅ Application: All 18 pods running successfully
