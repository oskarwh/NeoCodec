cd ../locust
source /venv/bin/activate

echo "Starting first test"
for i in 1 2 5 10 20
do
    for j in {1..10}
    do
        echo "Run $j"
        python3 convert.py videos/Big_Buck_Bunny_1080_10s_${i}MB.mp4 out/out.avi -csv >> /dev/null
    done
done