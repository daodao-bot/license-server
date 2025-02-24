FROM registry.cn-beijing.aliyuncs.com/daodao-bot/nginx:1.27.3
VOLUME /tmp
COPY nginx.default.conf /etc/nginx/conf.d/default.conf
COPY ./dist /usr/share/nginx/html
EXPOSE 80
ENTRYPOINT ["nginx", "-g", "daemon off;"]
