#!/bin/sh
# certbot после успешного renew трогает /etc/letsencrypt/renewed (см. --deploy-hook
# в docker-compose.yml). nginx сам не подхватывает обновлённые файлы сертификата —
# держит их в памяти процесса, поэтому нужен явный `nginx -s reload`. Этот скрипт
# раз в 5 минут проверяет mtime маркера и релоадит nginx только если он изменился.
set -e

nginx -g 'daemon off;' &
NGINX_PID=$!
trap 'kill -TERM "$NGINX_PID"; wait "$NGINX_PID"; exit 0' TERM INT

LAST_RELOAD=0
while kill -0 "$NGINX_PID" 2>/dev/null; do
  if [ -f /etc/letsencrypt/renewed ]; then
    MTIME=$(stat -c %Y /etc/letsencrypt/renewed 2>/dev/null || echo 0)
    if [ "$MTIME" != "$LAST_RELOAD" ]; then
      nginx -s reload
      LAST_RELOAD=$MTIME
    fi
  fi
  sleep 300
done
wait "$NGINX_PID"
