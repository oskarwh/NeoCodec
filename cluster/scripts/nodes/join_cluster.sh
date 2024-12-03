for i in {002..004}; do
	ssh ceti-vm-$i sudo kubeadm reset
	ssh ceti-vm-$i sudo kubeadm join 10.201.0.20:6443 --token q2h0lu.nslh8l8xbh9na7mf --discovery-token-ca-cert-hash sha256:0215eb8454f8899df25441372204c1ac3d40b5d4a6eb15f75f9198de00eb52a6
done
