FROM registry.cn-beijing.aliyuncs.com/daodao-bot/nginx:1.27.3
VOLUME /tmp
COPY ./dist /usr/share/nginx/html
COPY nginx.default.conf /etc/nginx/conf.d/default.conf
COPY nginx.start.sh /usr/local/bin/nginx.start.sh
RUN chmod +x /usr/local/bin/nginx.start.sh
EXPOSE 80
# ENTRYPOINT ["nginx", "-g", "daemon off;"]
ENTRYPOINT ["/usr/local/bin/nginx.start.sh"]
