# Libra-Fortune-API

* 后端项目使用Java25+Gradle+Spring Boot+MyBatisPlus技术栈
* 其中包`java/com/kuretru/web/libra/{controller/entity/mapper/service}`是旧的代码，无需修改，等待彻底迁移完成后下线
* 目录结构采用，领域模型+三层分层，具体如下
  * metadata/account/ledger，目前有三个领域
    * entity：POJO对象
      * data：DO对象，和数据表一一对应
      * transfer：DTO对象，基本和DO一一对应
      * query：Query对象，大部分和DTO一一对应，少部分简单模块可能没有
      * mapper：EntityMapper，用于DO和DTO之间转换，使用mapstruct框架
    * controller：暴露API
    * service：核心业务逻辑
    * mapper：repository层
* 大部分基建都在Galaxy-Microservice下的各个子包里，该项目可以专心业务实现，如果你觉得基建有需要修改/完善的地方，先不要修改，告诉我你的规划，由我来决定要不要修改，因为基建同时还被其他项目使用着
* API风格使用RESTful风格，但可以酌情简化
* 需要排序的DO(含有sequence字段)，其对应的DTO不需要暴露sequence
