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
1. 添加你自己的暴露服务的接口包 ，配置 zookeeper.address 和 rpc.server.address 
3. 服务端在启动类上添加 **@EnableRpc** 注解启用 rpc 服务，  需要暴露的 service 添加 @RpcService 注解
4. 客户端在启动类上添加 **@EnableRpc** 注解 ， 注入 RpcProxy ， 创建代理对象 eg : `IUserService userService = rpcProxy.create(IUserService.class);`  

### 需进一步完善的地方 ###
1. 添加服务端软负载均衡
2. 序列化方式可配置
3. 代理优化 cglib , jdk 代理 ， 代理创建方式优化， 希望通过注解的方式注入即可
4. 服务端心跳检测
5. 超时 和 重试策略
6. 支持多个微服务  ， 给每个微服务起名称
