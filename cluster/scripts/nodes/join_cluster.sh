for i in {002..004}; do
	ssh ceti-vm-$i sudo kubeadm reset
	ssh ceti-vm-$i sudo kubeadm join 10.201.0.20:6443 --token ihs9st.qcuqvvk3mkqj5to3 --discovery-token-ca-cert-hash sha256:e6e5662bf47ace1aa4586756ac00c599f67125cc5eeeb9832bb20a4540b65acb    
done
