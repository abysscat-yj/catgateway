# CatGateway 业务网关
分布式业务网关（参考 Spring Cloud Gateway 实现）

## 当前进展
* 添加基于 Spring Webflux 的 Web 框架
* 通过 RouterFunction 机制来构造访问 endpoint
* 添加 GatewayHandler，初步实现RPC服务网关
* 通过 SimpleUrlHandlerMapping 注册 gatewayWebHandler（主流 webHandler 实现的优化版）
* 增加 plugin chain 机制，将访问下游服务的过程抽象为 plugin 实现
