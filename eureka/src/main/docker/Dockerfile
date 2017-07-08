#使用daocloud的java8镜像
FROM daocloud.io/library/java:8
#镜像创建人
MAINTAINER sxt i_sxtian@3songhu.com
#附加卷
VOLUME /tmp
#添加jar包
ADD eureka-*.jar app.jar
#修改jar包日期
RUN bash -c "touch app.jar"
#并指定端口号
EXPOSE 8761
#环境变量
ENV TZ Asia/Shanghai
ENV RESERVED_MEGABYTES 0
ENV JAVA_OPTS "-Xss128k -XX:NewRatio=4 -XX:SurvivorRatio=4 -XX:MaxPermSize=16m -XX:MaxTenuringThreshold=0 -Djava.security.egd=file:/dev/./urandom "
#设置时区
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
#复制脚步
COPY entrypoint.sh /entrypoint.sh
#脚步可执行
RUN chmod 777 /entrypoint.sh
#运行脚步
CMD ["/entrypoint.sh"]
