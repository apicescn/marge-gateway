
# 1.API服务列表

# 2.IP访问控制
限制IP访问

# 3.日志管理
访问日志系统

# 4.API文档
swagger和静态文档

# 5.密钥管理
oauth服务管理clientId和clientSecret，如果clientId和clientSecret提供外部使用，则gateway也保存一份，通过这个
clientId和clientSecret获取jwt token，并转化为sessionKey存储在数据库中，并且使用缓存查询。

- gateway表设计：clientId/clientSecret,sessionKey/jwt
- gateway管理页面获取oauth的clientId和clientSecret数据
- 生成sessionKey
- 数据库表DAO查询加redis缓存

密钥管理需求:客户端通过clientId和clientSecret来通过gateway获取jwt token以能够获取api访问的权限;现需要解决如下两个问题
- clientId和clientSecret该怎样获取;
- jwt token需要以sessionKey的方式返回给客户端.

## 针对问题一有以下三种实现方案
- 通过和Auth公用同一数据库,以直接查询数据库的方式获取
- Auth提供接口供gateway查询
- gateway单独维护一份数据库,相当于auth库clientId和clientSecret等信息的冗余方案
那么第一种方案违背了微服务的规范,第二种方案影响到速度,每次做个api访问,都会和auth网络请求,一来延迟,二来与auth强相关联,
在此选择第三种方案

## 针对问题二的解决方案
由gateway直接通过clientId和clientSecret生成sessionKey返回给客户端,客户端后续通过在请求头加sessionKey访问api,gateway将sessionKey转化成
jwt token替换掉请求头以正确访问服务. 这样做的好处是sessionKey的生成直接通过gateway生成,对于后续的增删改差都维护在gateway中,我们可以集成redis
做数据缓存,例如设置过期时间以天为单位,同时将clientId,clientSecret,sessionKey,jwt token持久化在数据库中,这样既不强依赖于redis,增加运维的难度
,也不会影响到查询的速度.

## gateway中维护的数据库信息如何与Auth中保持一致
- 通过写gateway管理页面,点击某个按钮以同步更新Auth数据到gateway数据库中
- 允许容错,如果Auth中clientSecret信息发生变化,gateway并没有同步过来,那么可能发生在如下两种情况


**1.此前gateway已经通过clientId和clientSecret获取到token,然后在token有效期内,Auth库中此clientSecret发生变化,那么并不会影响客户端对服务的访问**

**2.不在上述情况范围内,当Auth库中clientSecret发生变化时,gateway再讲老的clientId和clientSecret去过去token时将会失败,那这时可以出发一次数据同步并在此获取token即可**


# 6.限流
zuul限流组件，针对恶意刷接口的进行限流(待完善需求) https://github.com/marcosbarbero/spring-cloud-zuul-ratelimit

# 7.动态路由
(待完善需求)

# 8.其他
和淘宝TOP接口一样，使用method进行接口路由，需要通过method进行路由，路由信息保存在gateway库中，appId和method接口可以进行绑定，做网关
层面的接口权限控制，对于后端接口安全级别较高的，还需要通过token控制后端接口是否有访问权限。
按照上面的实现方式，接口文档如何实现。swagger文档是以uri为基础，gateway的文档是以method为基础。


网关页面设计参考：
Chris Richardson大神的微服务gateway: http://microservices.io/patterns/apigateway.html

两种网关的对比：https://www.cnblogs.com/savorboard/p/api-gateway.html

konga：https://github.com/pantsel/konga/blob/master/README.md

saluki: https://github.com/linking12/saluki/tree/develop/saluki-gateway

aliyun gateway: https://www.aliyun.com/product/apigateway?spm=a2c4g.11186623.5.1.7wBgNJ

aliyun gateway签名说明:https://help.aliyun.com/document_detail/29475.html?spm=a2c4g.11186623.4.5.zRWGSb

jhipster api gateway: https://www.jhipster.tech/api-gateway

Spring Cloud Zuul中路由配置细节: https://blog.csdn.net/u012702547/article/details/78399406

springcloud(十一)：服务网关Zuul高级篇: https://blog.csdn.net/u011820505/article/details/79373594

数据库存储动态路由：https://github.com/wdxxs2z/zuul-enhance-spring-boot-starter

数据库存储动态路由：https://github.com/wangqifox/blog/blob/ffd8b858435c1c021e5578e76c7fe5ea612005d4/Spring-Cloud/Zuul%E6%8E%A2%E7%A9%B6(%E4%B8%89)%E2%80%94%E2%80%94Zuul%E5%8A%A8%E6%80%81%E8%B7%AF%E7%94%B1%E7%9A%84%E5%AE%9E%E7%8E%B0.md

Spring Cloud Zuul网关 Filter、熔断、重试、高可用的使用方式：https://blog.csdn.net/ityouknow/article/details/79215698

流量控制策略：https://help.aliyun.com/document_detail/29482.html?spm=a2c4g.11174283.4.8.lp3u0N