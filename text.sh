#! /bin/bash
n=`netstat -lntp | grep ':80' | wc -l`

if [ $n -eq 0 ]; then
    echo "It not listen port 80"
curl 'https://oapi.dingtalk.com/robot/send?access_token=838d067198fb09d82fa037d578072d0c89932d49635551c2781b83b134538c63' -H 'Content-Type: application/json' -d '{"msgtype": "text", "text": {"content": "80端口监控未登录！\n"}}'

else
    ser=`netstat -lntp | grep ':80 ' | awk -F '/' '{print $NF}'`
    echo "It is listening port 80, and the server is $ser"
curl 'https://oapi.dingtalk.com/robot/send?access_token=838d067198fb09d82fa037d578072d0c89932d49635551c2781b83b134538c63' -H 'Content-Type: application/json' -d '{"msgtype": "text", "text": {"content": "80端口监控登录！\n"}}'
fi
