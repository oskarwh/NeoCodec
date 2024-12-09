curl -sfL https://get.k3s.io | K3S_KUBECONFIG_MODE="644" sh -
TOKEN=$(sudo cat /var/lib/rancher/k3s/server/node-token)
echo $TOKEN

#mkdir -p $HOME/.kube
#sudo cp /etc/rancher/k3s/k3s.yaml $HOME/.kube/config
#sudo chown $(id -u):$(id -g) $HOME/.kube/config


for i in {002..004}; do
	#ssh ceti-vm-$i sudo systemctl restart kubelet
	ssh ceti-vm-$i curl -sfL https://get.k3s.io | K3S_URL=https://ceti-vm-001:6443 K3S_TOKEN='$TOKEN' sh -
done
