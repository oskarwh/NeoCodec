kubectl delete -f middleware.yaml 
kubectl delete -f converter.yaml

kubectl exec -it pulsar-broker-2 -n pulsar -- /bin/bash ./bin/pulsar-admin topics delete persistent://public/inout/output-topic --force
exit



