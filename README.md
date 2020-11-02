# Project_Backend

电子商城服务端

* 2020-10-29：  
    * Mybatis-generator：
        * @`pom.xml`: 调整mybatis-generator`<plugin>`标签位置
        * +`generatorConfig.xml`
        * +`datasource.properties`
        * +`java/pojo/*`
        * +`java/dao/*`
        * +`resources/mappers/*`
        * @`mappers/*`: 优化时间戳配置
* 2020-10-31
    * resources:
        * +`applicationContext.xml`: spring主配置文件
        * +`applicationContext-datasource.xml`: spring数据库相关配置
        * @`datasource.properties`: 增加连接池相关参数
        * +`dispatcher-servlet.xml`
        * @`web.xml`
        * +`logback.xml`: logback配置文件
* 2020-11-2
    * java:
        * +`UserController.java`: 登录功能
        * +`IUserService.java`: 登录功能
        * +`IUserServiceImpl.java`: 登录功能
        * @`UserMapper.java`: 用户名查询，用户名、密码查询
        * !+`ServerResponse`: 高可复用服务响应对象
        * +`ResponseCode.java`: 服务响应状态
        * +`Const.java`: 常量类
    * resources:  
        * @`UserMapper.xml`: 用户名查询，用户名、密码查询  
        * @`logback.xml`: 修改日志位置
        