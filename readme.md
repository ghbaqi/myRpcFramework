## 自己实现一个 rpc 框架 ##

### 简介 ###
    基于 netty 的 nio , 具备服务注册和发现 。 通过框架的代理透明化调用

---

### 具备的功能 ###
1. 服务提供者注册到 zookeeper
2. 调用者从注册中心拉取服务 ， 通过框架的代理功能透明化调用提供者
3. ..........

---

### 如何使用 ###
1. 使用上总体与 dubbo 类似 , 也可以仿照示例工程 . 首先下载本项目 ,  修改 provider01 和 consume01 示例工程的配置文件 . 运行并访问消费者的 UserController 接口地址即可调通示例工程
2. 添加依赖  ` <dependency>
            <groupId>com.example.gh.rpc</groupId>
            <artifactId>rpc-core</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>`
3. 服务端 service 添加 @RpcService 注解
4. 客户端 

### 需进一步完善的地方 ###
1. 添加服务端软负载均衡
2. 序列化方式可配置
3. 代理策略优化 cglib , jdk 代理
4. 服务端心跳检测
5. 超时 和 重试策略
