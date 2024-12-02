for i in {002..004}; do
 ssh ceti-vm-001 sudo kubectl drain ceti-vm-$i --ignore-daemonsets
done

for i in {002..004}; do
  ssh ceti-vm-001 sudo kubectl delete node ceti-vm-$i
done

