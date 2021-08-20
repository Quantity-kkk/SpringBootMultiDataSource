# SpringBootMultiDataSource
## 简介
本项目为Demo项目，主要包含如下两个模块：
* nonjta-jpa
* jta-jpa  

nonjta为springboot+jpa+hikari多数据源+jdbcTemplate+且带jpa独立事务的项目配置示例，并编写了demo测试不同方式操作数据库的事务情况。  
jta-jpa则在nonjta基础上引入了jta-atomikos，并编写了demo测试不同方式操作数据库的事务情况。


## 配置详情
**nonjta-jpa:**
* springboot 2.3.2.RELEASE 
* hikari springboot自带数据库连接池 
* jdbcTemplate 跟随springboot版本
* jpa 跟随springboot版本
* 多数据源，都是mysql，其他类型数据库自行扩充

**jta-jpa**
* springboot 2.3.2.RELEASE 
* AtomikosDataSource springboot自带数据库连接池 
* jdbcTemplate 跟随springboot版本
* jpa 跟随springboot版本
* jta Atomikos
* 多数据源，都是mysql，其他类型数据库自行扩充

**备注：**  
* 本项目使用的SpringBoot版本为2.3.2.RELEASE，在较低版本的springboot中配置会出现无法找到jtaPlatform的问题，无法配置成功；
* 使用jta的时候，需要配套使用AtomikosDataSourceBean数据库连接池，否则jdbcTemplate无法正常回滚。

## tips
* @Transactional注解需要添加在public方法上；
* 在多数据源多事务管理器的情况下，@Transactional需要主动指定正确的事务管理器否则无法正确回滚；
