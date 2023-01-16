Build
=====

`./gradlew jsBrowserDistribution`

Files are in `build/distributions`

Deploy
=====

```shell
apt update
apt install certbot nodejs npm nginx python3-certbot-nginx
```

## HTTP -> HTTPS

1. Configure Nginx

2. Replace the contents of `/etc/nginx/sites-enabled/default` with the following

```
server {
    server_name _;
    root /root/ui;
    listen 80;

    location / {
        index index.html;
        try_files $uri $uri/ /index.html;
    }
}
```

chmod `/root/ui` with `755`

3. Finally

```shell
certbot --nginx
nginx -t
service nginx restart
```

Run
===

```shell
nohup angular-http-server -p 8081 > log.txt 2> errors.txt < /dev/null &
PID=$!
echo $PID > pid.txt
```
