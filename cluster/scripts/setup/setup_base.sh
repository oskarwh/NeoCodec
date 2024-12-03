sudo apt-get install wget -y

wget https://github.com/containerd/containerd/releases/download/v1.7.23/containerd-1.7.23-linux-amd64.tar.gz -nv
sudo tar Cxzvf /usr/local containerd-1.7.23-linux-amd64.tar.gz

wget https://github.com/opencontainers/runc/releases/download/v1.1.15/runc.amd64 -nv
sudo install -m 755 runc.amd64 /usr/local/sbin/runc

wget https://github.com/containernetworking/plugins/releases/download/v1.6.0/cni-plugins-linux-amd64-v1.6.0.tgz -nv
sudo mkdir -p /opt/cni/bin
sudo tar Cxzvf /opt/cni/bin cni-plugins-linux-amd64-v1.6.0.tgz

sudo mkdir -p /usr/local/lib/systemd/system/
sudo wget https://raw.githubusercontent.com/containerd/containerd/main/containerd.service -nv
sudo mv containerd.service /usr/local/lib/systemd/system/
ls /usr/local/lib/systemd/system/

sudo rm /etc/systemd/system/containerd.service
sudo systemctl daemon-reload
sudo systemctl enable --now containerd

sudo mkdir -p /etc/containerd/
#sudo mv config.toml /etc/containerd/
sudo touch /etc/containerd/config.toml
sudo chmod 666 /etc/containerd/config.toml
/usr/local/bin/containerd config default > /etc/containerd/config.toml
sudo sed -i 's/SystemdCgroup = false/SystemdCgroup = true/' /etc/containerd/config.toml

# Fix ks libs
sudo apt-get update
sudo apt-get install -y apt-transport-https ca-certificates curl gpg
curl -fsSL https://pkgs.k8s.io/core:/stable:/v1.31/deb/Release.key | sudo gpg --dearmor -o /etc/apt/keyrings/kubernetes-apt-keyring.gpg
echo 'deb [signed-by=/etc/apt/keyrings/kubernetes-apt-keyring.gpg] https://pkgs.k8s.io/core:/stable:/v1.31/deb/ /' | sudo tee /etc/apt/sources.list.d/kubernetes.list
sudo apt-get update
sudo apt-get install -y socat kubelet kubeadm kubectl
sudo apt-mark hold kubelet kubeadm kubectl
sudo systemctl enable --now kubelet
sudo swapoff -a
sudo sysctl net.ipv4.ip_forward=1
sudo systemctl restart containerd
sudo systemctl restart kubelet
sudo sysctl --system

sudo modprobe br_netfilter
sudo sysctl -p /etc/sysctl.conf

