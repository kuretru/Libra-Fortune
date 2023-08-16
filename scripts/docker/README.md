# Docker部署

```shell
mkdir -p /home/nginx/libra.i5zhen.com/public
mkdir -p /home/nginx/libra.i5zhen.com/log

mkdir -p /home/docker/libra-fortune
cd /home/docker/libra-fortune
wget -O compose.yaml https://github.com/kuretru/Libra-Fortune/raw/main/scripts/docker/compose.yaml
wget -O environment https://github.com/kuretru/Libra-Fortune/raw/main/scripts/docker/environment

docker compose up -d
```
