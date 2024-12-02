sudo kubeadm init --cri-socket unix:///var/run/containerd/containerd.sock  --pod-network-cidr=10.244.0.0/16
kubectl apply -f https://github.com/flannel-io/flannel/releases/latest/download/kube-flannel.yml

