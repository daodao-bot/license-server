#!/bin/sh
if [ -z "$LICENSE_SERVER" ]; then
  export LICENSE_SERVER=http://localhost:8080
fi
sed -i "s|\${LICENSE_SERVER}|${LICENSE_SERVER}|g" /etc/nginx/conf.d/default.conf
nginx -g 'daemon off;'
