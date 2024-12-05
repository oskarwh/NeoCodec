sudo kubeadm init --cri-socket unix:///var/run/containerd/containerd.sock  --pod-network-cidr=10.244.0.0/16

mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config

kubectl apply -f https://github.com/flannel-io/flannel/releases/latest/download/kube-flannel.yml

# For longhorn
#kubectl create namespace longhorn-system
#kubectl apply -f https://raw.githubusercontent.com/longhorn/longhorn/v1.7.2/deploy/prerequisite/longhorn-iscsi-installation.yaml -n longhorn-system
#kubectl apply -f https://raw.githubusercontent.com/longhorn/longhorn/v1.7.2/deploy/prerequisite/longhorn-nfs-installation.yaml -n longhorn-system
#helm install longhorn longhorn/longhorn --namespace longhorn-system --create-namespace --version 1.7.2


kubectl create namespace pulsar
kubectl create secret generic kubepromsecret   --from-literal=username=1926166 --from-literal=password='lc_eyJvIjoiMTI4NDc5OCIsIm4iOiJzdGFjay0xMTA0NTg3LWFsbG95LXB1bHNhciIsImsiOiJzNzZiWThKZTM1T3N0NXF0VzFlOTMyVkIiLCJtIjp7InIiOiJwcm9kLWV1LW5vcnRoLTAifX0=' -n pulsar

