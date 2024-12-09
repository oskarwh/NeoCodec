for i in {002..004}; do
 kubectl drain ceti-vm-$i --ignore-daemonsets
 kubectl delete node ceti-vm-$i
 ssh ceti-vm-$i sudo kubeadm reset -y
 ssh ceti-vm-$i sudo ip link delete cni0 
done

sudo kubeadm reset

rm -rf $HOME/.kube || true

sudo systemctl restart kubelet
sudo systemctl restart containerd
sudo rm -rf /var/lib/cni/
sudo rm -rf /var/lib/kubelet/*
sudo rm -rf /etc/cni/
ifconfig cni0 down
ifconfig flannel.1 down
ifconfig docker0 down
