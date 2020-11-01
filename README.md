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
        