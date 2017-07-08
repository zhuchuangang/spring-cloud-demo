批量删除镜像
```
docker rmi -f $(docker images | awk '/^<none>/ { print $3 }')
docker rmi -f $(docker images | awk '/^zcg/ { print $3 }')
```

# 1 erueka

# 2 ribbon

# 3 hystrix
# 3.1 服务端调用超时

# 3.2 继承HystrixCommand实现（同步和异步）

# 3.3 添加标注实现（同步和异步）


