# Libra-Fortune

[Libra-Fortune](https://github.com/kuretru/Libra-Fortune) 是一个面向多人共同使用的家庭账本。它关注的不是单次记账本身，而是把家庭里的账本、成员、账户、分类、标签和统计分析串在一起，让日常收支能被持续记录、核对和复盘。

## 核心功能

* 共同记账：支持多人围绕同一个账本记录家庭收支。账目可以按成员、分类、标签、币种、账户和备注组织，也可以用明细拆分处理更复杂的消费场景。
* 账户核对：系统可以维护现金、银行卡、储值账户和支付平台等账户，并通过余额快照追踪资产变化，方便把流水记录和真实余额对上。
* 统计复盘：统计面板会按时间、分类、标签、成员等维度聚合账目，帮助观察长期消费结构。分析结果可以继续钻取回原始流水，便于核对明细。

## 技术栈

- 前端：Umi Max、Ant Design Pro、Ant Design
- 后端：Spring Boot、MyBatis-Plus
- 基础组件：[Galaxy-Microservices](https://github.com/kuretru/Galaxy-Microservices)

## 开发

### 项目结构

```text
Libra-Fortune/
├── Libra-Fortune-Api/      # 后端服务
├── Libra-Fortune-Web/      # 前端应用
├── Galaxy-Microservices/   # 后端基础组件
├── docs/                   # 项目文档
└── scripts/                # 辅助脚本
```

* 参考各个模块本身的README.md

## 部署

* 导入`scripts/sql`下的所有文件
* 配置文件参考`Libra-Fortune-Api/libra-fortune-api/src/main/resources/config/application-production.yml`
* 镜像启动配置参考`scripts/docker/compose.yaml`
