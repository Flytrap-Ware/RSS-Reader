
#!/bin/bash

echo "🏁 Script Start."
echo "👉 Pulling github repository..."
git pull origin release

echo "👉 Pulling backend Docker image..."
cd ..
cat token/TOKEN2.txt | docker login ghcr.io -u crtEvent --password-stdin
docker pull ghcr.io/flytraphub/rss-reader:release

echo "👉 Starting Docker Compose..."
cd RSS-Reader/
sudo docker-compose up -d

echo "👉 blue-green deploy.sh run..."
sh deploy.sh

echo "👉 Cleaning up unused Docker images..."
sudo docker image prune -a -f

echo "🫡  Script execution completed."
