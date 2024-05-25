  #!/bin/sh

  IS_GREEN=$(docker ps | grep green) # 현재 실행중인 App이 green인지 확인

  DEFAULT_CONF="./nginx/conf.d/nginx.conf"

  docker-compose up -d balancer
  docker-compose up -d redis

  if [ -z $IS_GREEN  ];then # blue라면
  	echo "### BLUE => GREEN ###"
  	echo "1. get green image"
  	docker-compose pull green # 이미지 받아서
  	echo "1-1. blue container down"
    docker-compose stop blue
  	echo "2. green container up"
  	docker-compose up -d green # 컨테이너 실행
  	while [ 1 = 1 ]; do
  		echo "3. green health check..."
  		sleep 3
  		REQUEST=$(docker exec balancer curl http://green:8080) # green으로 request
  		if [ -n "$REQUEST" ]; then # 서비스 가능하면 break
  			echo "health check success"
  			break ;
  		fi
  	done;
  	sed -i 's/blue/green/g' $DEFAULT_CONF # nginx가 green을 가리키도록 변경
  	echo "4. reload nginx"
  	docker exec balancer nginx -s reload
  else
  	echo "### GREEN => BLUE ###"
  	echo "1. get blue image"
  	docker-compose pull blue
  	echo "1-1. green container down"
    docker-compose stop green
  	echo "2. blue container up"
  	docker-compose up -d blue
  	while [ 1 = 1 ]; do
  		sleep 3
  		echo "3. blue health check..."
  		REQUEST=$(docker exec balancer curl http://blue:8080)
  		if [ -n "$REQUEST" ]; then
  				echo "health check success"
  				break ;
  		fi
      done;
  	sed -i 's/green/blue/g' $DEFAULT_CONF
  	echo "4. reload nginx"
  	docker exec balancer nginx -s reload
  fi
