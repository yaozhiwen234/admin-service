
# 基础镜像使用java
FROM java:8
# 作者
MAINTAINER yzw <2718486004@qq.com>
# VOLUME 指定了临时文件目录为/tmp。
# 其效果是在主机 /var/lib/docker 目录下创建了一个临时文件，并链接到容器的/tmp
VOLUME /tmp

#一般情况下，我们的宿主机服务器的时区都是在东八区，而docker容器里却是在零时区。这样就导致了时间差了8个小时，所以需要把容器的时间也设置成东八区。
RUN echo "Asia/Shanghai" > /etc/timezone
RUN dpkg-reconfigure -f noninteractive tzdata


# 将jar包添加到容器中并更名为app.jar
ADD yzw-system-1.0.jar app.jar
# 运行jar包
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
