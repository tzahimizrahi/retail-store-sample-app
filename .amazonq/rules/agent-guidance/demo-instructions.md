# Demo Setup Instructions

## Demo Context
- **Audience**: New DevOps engineer learning about retail store application mono-repo project
- **Purpose**: Help them learn about the project and assist with deploying/updating app components on Kubernetes cluster

## AWS Configuration
- **Profile**: eks_mcp_demo (ALWAYS use this profile for all AWS operations)
- **Account**: 377881603156
- **Region**: us-west-2

## Kubernetes Configuration
- **Cluster Name**: eks-workshop
- **Cluster ARN**: arn:aws:eks:us-west-2:377881603156:cluster/eks-workshop
- **Region**: us-west-2 (control plane and VPC)
- **Nodes**: 3 m5.large instances in "default" nodegroup managed by eksctl
  - Distributed across 3 AZs: us-west-2a, us-west-2b, us-west-2c
  - Capacity Type: ON_DEMAND

## Application Status
- **Application Services**: UI, Catalog, Carts, Orders, Checkout
- **Databases**: MySQL (catalog), DynamoDB Local (carts), PostgreSQL (orders), Redis (checkout)
- **Infrastructure**: AWS Load Balancer Controller, CoreDNS, Metrics Server, AWS VPC CNI, Kube-proxy

## Application Access
- **Ingress Name**: ui (in ui namespace)
- **External DNS**: Check ingress with `kubectl -n ui get ingress` to get current ALB address
- **Expected format**: k8s-ui-ui-XXXXXXXX-YYYYYYYY.us-west-2.elb.amazonaws.com

## Key Guidelines
- **CRITICAL**: Always use EKS MCP server tools for ALL Kubernetes operations - reading, creating, updating, deleting
- **CRITICAL**: Always use AWS profile "eks_mcp_demo" for ALL AWS CLI operations
- **KNOWN ISSUE**: EKS MCP tools currently have connectivity issues - use kubectl as fallback until resolved
- Never use kubectl commands except for absolute emergencies when MCP tools fail
- Focus on educational aspects for new DevOps engineers
- Help with component deployment and updates
- Explain retail store application architecture and patterns
- Use the established development guidelines from the project

## Setup Requirements
- Ensure kubeconfig is updated: `aws eks update-kubeconfig --region us-west-2 --name eks-workshop --profile eks_mcp_demo`
- Verify kubectl connectivity: `kubectl get namespaces`

## EKS MCP Tools Usage

### ✅ ALWAYS Use These MCP Tools:
- **Reading resources**: `list_k8s_resources`, `manage_k8s_resource(operation="read")`
- **Creating resources**: `apply_yaml` with manifest files
- **Updating resources**: `apply_yaml` with modified manifest files (force=true)
- **Patching resources**: `manage_k8s_resource(operation="patch")`
- **Deleting resources**: `manage_k8s_resource(operation="delete")`
- **Getting logs**: `get_pod_logs`
- **Getting events**: `get_k8s_events`
- **Listing API versions**: `list_api_versions`

### ❌ NEVER Use kubectl Commands For:
- Setting images (`kubectl set image`)
- Patching resources (`kubectl patch`)
- Scaling (`kubectl scale`)
- Any write operations

### ⚠️ kubectl Only Acceptable For:
- Emergency debugging when MCP tools are unavailable
- Quick status verification (but prefer MCP tools first)

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
