#IOS镜像
FROM openjdk:8-jre-alpine

ADD simhei.ttf /usr/share/fonts/

ENV LANG en_US.UTF-8

#安装程序
RUN echo "http://mirrors.aliyun.com/alpine/v3.9/main" > /etc/apk/repositories \
    && echo "http://mirrors.aliyun.com/alpine/v3.9/community" >> /etc/apk/repositories \
    && apk update upgrade \
    && apk add --no-cache libc6-compat gcompat procps curl bash tzdata ttf-dejavu fontconfig \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone

ENV APOLLO="-Dapollo.meta=http://192.168.8.6:8083"
ENV JVM_OPTS="-Xms512m -Xmx512m"
ENV HEAP_DUMP_OPTS="-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/work/logs/"
ENV LANG="en_US.UTF-8"
ENV LANGUAGE="en"
ENV TZ="Asia/Shanghai"

WORKDIR /work
COPY kgms.jar  /work/
ENTRYPOINT java ${APOLLO} ${JVM_OPTS} ${HEAP_DUMP_OPTS}  -jar /work/kgms.jar
