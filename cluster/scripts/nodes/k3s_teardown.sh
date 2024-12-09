for i in {002..004}; do
	ssh ceti-vm-$i sudo systemctl restart kubelet
	ssh ceti-vm-$i k3s-agent-uninstall.sh
done

k3s-uninstall.sh
sudo systemctl restart kubelet
