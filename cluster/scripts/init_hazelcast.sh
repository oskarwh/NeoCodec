#!/bin/bash

helm repo add hazelcast https://hazelcast-charts.s3.amazonaws.com/
helm repo update

kubectl create namespace hazelcast
helm install hz-hazelcast hazelcast/hazelcast -n hazelcast

#*) Forward port from POD:
#     $ export POD=$(kubectl get pods --namespace hazelcast -l "app.kubernetes.io/name=hazelcast,role=hazelcast" -o jsonpath="{.items[0].metadata.name}")
#     $ kubectl port-forward --namespace hazelcast $POD 5701:5701
#  *) In Hazelcast Client configure:
#     clientConfig.getNetworkConfig().setSmartRouting(false);
#     clientConfig.getNetworkConfig().addAddress("127.0.0.1:5701");
#
