sudo apt-get remove --purge kubelet kubeadm kubectl -y
sudo apt-get autoremove -y
sudo systemctl disable kubelet
sudo systemctl stop kubelet

sudo systemctl disable containerd
sudo systemctl stop containerd

sudo apt-get install iptables -y
sudo sysctl --system
sudo iptables -F
sudo iptables -t nat -F
sudo iptables -t mangle -F
sudo iptables -X
