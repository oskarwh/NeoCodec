for i in {002..004}; do
	ssh ceti-vm-$i sudo kubeadm reset
	ssh ceti-vm-$i sudo kubeadm join 10.201.0.20:6443 --token unm28z.erae4snfjskqburn \
	--discovery-token-ca-cert-hash sha256:b6f0c3b86e29f3a6c6f45bc7296e8a9c5e93ab3a0e2d66e79284abbe18059553    
done
