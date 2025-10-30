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

## Connectivity Status
✅ AWS Profile: Connected (admin user)
✅ Kubernetes Cluster: Connected (1 node available)
✅ Application: All 18 pods running successfully
