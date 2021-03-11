# JFinger
基于Spring Cloud的一个后端开发框架 
#使用组件
Spring:	**SpringBoot**    
Cloud:	**Spring Cloud Hoxton.SR3**    
网关:	**Spring Gateway**    
服务注册:  **Alibaba Nacos**    
在线配置:  **Alibaba Nacos**    
JSON:	**FastJson**
数据库:	**Mybatis Plus**   
缓存:	**Redis**   
定时任务:	**Quartz**(集成暂未使用)    

#模块说明
##common:			通用基础模块   
	###common-config		通用配置模块，各微服务引用该模块即可   
	###common-core		通用核心模块，目前只包含日志及字典转换aop   
	###common-entity		通用实体模块，主要是公共实体   
	###common-utils		工具类模块  
##gateway			网关应用
##provider			各微服务 
	###provider-uac		用户中心微服务   
##provider-api			向其他微服务提供的API，其他服务直接引用即可调用   
	###provider-api-uac		用户中心API
