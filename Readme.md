Setup
=====

```shell
apt update
apt install certbot nodejs npm nginx
npm install -g angular-http-server
certbot
```

## HTTP -> HTTPS

1. Configure Nginx

2. Replace the contents of `/etc/nginx/sites-enabled/default` with the following

```
server {
    server_name _;

    location / {
        alias /root;
        index index.html;
        try_files $uri $uri/ index.html =404;
    }
}
```

3. Finally, restart Nginx

```shell
service nginx restart
```

Run
===

```shell
nohup angular-http-server -p 8081 > log.txt 2> errors.txt < /dev/null &
PID=$!
echo $PID > pid.txt
```
