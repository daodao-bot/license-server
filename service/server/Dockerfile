FROM registry.cn-beijing.aliyuncs.com/daodao-bot/openjdk:21
VOLUME /tmp
EXPOSE 80
COPY ./target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

