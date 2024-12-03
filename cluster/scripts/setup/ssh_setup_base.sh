for i in {001..004}; do
  ssh ceti-vm-$i 'bash -s' < setup_base.sh
done

