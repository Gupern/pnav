#!/bin/bash
# 运行方式： sh ./start.sh

# 进入项目启动目录，含有lib, resources, *.jar
# cd xxx

# 上传最新jar包，如果使用xshell，则用rz命令
# rz

# 赋权
chmod -R 755 *.jar

# 杀死旧进程 一个
old_pid=`ps -ef | grep java | grep application-prod.properties | head -n 1 | awk '{print $2}'`
kill -9 $old_pid

# 启动新进程
filename=`ls -lt | grep jar | head -n 1 | awk '{print $9}'`
nohup java -jar $filename --spring.config.location=resources/application-prod.properties > app.log 2&>1 &

# 打印日志
tail -fn 300 app.log