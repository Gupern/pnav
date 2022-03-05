# PNAV后台

#### 介绍
PNAV后台代码仓库，使用spring cloud框架

#### 软件架构
- 按官网建议，小项目按模块来分，而不是按功能来分，更方便开发，详见: `https://docs.spring.io/spring-boot/docs/2.4.4/reference/htmlsingle/#using-boot-locating-the-main-class`
```
- main.java.com.gupern.pnav.h5: H5端的接口
    - H5Controller.java: H5端控制层
    - H5Service.java： H5端服务层
    - H5ServiceImpl.java： H5端实现层
    - H5Pojo***.java: H5端的各种Object，Pojo可为Dto、Vo、Dao、Pojo，具体见下方约定
    - H5Mapper***.java: H5端的各种Mapper
- main.java.com.gupern.pnav.pc: PC端的接口
    - PcController.java: Pc端控制层
    - PcService.java： Pc端服务层
    - PcServiceImpl.java： Pc端实现层
    - PcPojo***.java: Pc端的各种Object，Pojo可为Dto、Vo、Dao、Pojo，具体见下方约定，Pojo放前面，方便文件排序
    - PcMapper***.java: H5端的各种Mapper，Mapper放前面，方便文件排序
- main.resources.mapper: 存放Mybatis Mapper XML的文件夹
    - H5Mapper******.xml，Mapper放前面，方便文件排序
    - PcMapper******.xml，Mapper放前面，方便文件排序
- main.resources.application-{env}.properties: 配置文件
```

#### 约定
- dao：数据库访问的object
- dto：接收前端的object
- vo：返回给前端的view object
- pojo：其他无法区分的object

#### db信息
db名: pnav

## 命令
- 使用maven package后，lib和jar会分离，只有第一次需要上传lib包，后续如果没有引用其他模块，就只需要更新jar包即可

#### 打包后运行命令，需指定properties文件
`java -jar .\pnav-1.0-SNAPSHOT.jar  --spring.config.location=resources/application-{env}.properties`
#### IDEA工具配置参数
在启动时配置工作目录和profile.active=dev
![idea配置图片](doc/idea-config.png)
#### linux启动命令，指定生产配置文件，并输出日志文件到pnav.log并后台运行
`nohup java -jar pnav-1.0-SNAPSHOT.jar --spring.config.location=resources/application-prod.properties > pnav.log 2&>1 &`
#### 查看日志
`tail -fn 300 pnav.log`
#### curl请求本地应用
`curl localhost:8080/client/list/1/2`


