# 数据库设计

## ER图

```mermaid
erDiagram
    user {
        Long id PK
        UUID uuid
        Instant create_time
        Instant update_time
        String nickname
        String avatar
        UUID gemini_id
        Instant last_login
    }

    ledger_tag {
        Long id PK
        UUID uuid
        Instant create_time
        Instant update_time
        UUID user_id FK
        String name
    }
    user ||--o{ ledger_tag : "拥有"

    payment_channel {
        Long id PK
        UUID uuid
        Instant create_time
        Instant update_time
        UUID user_id FK
        String name
    }
    user ||--|{ payment_channel : "拥有"

    ledger {
        Long id PK
        UUID uuid
        Instant create_time
        Instant update_time
        UUID owner_id FK
        String name
        Short type
        String remark
    }
    user ||--|| ledger : "拥有"

    ledger_member {
        Long id PK
        UUID uuid
        Instant create_time
        Instant update_time
        UUID ledger_id FK
        UUID user_id FK
    }
    ledger ||--|{ ledger_member : ""
    user ||--|| ledger_member : ""

    ledger_category {
        Long id PK
        UUID uuid
        Instant create_time
        Instant update_time
        UUID ledger_id FK
        String name
    }
    ledger ||--|{ ledger_category : ""

    ledger_entry {
        Long id PK
        UUID uuid
        Instant create_time
        Instant update_time
        UUID ledger_id FK
        UUID category_id FK
        LocalDate date
        String name
        Long total
        String currency_type
        String remark
    }
    ledger ||--o{ ledger_entry : ""

    ledger_entry_detail {
        Long id PK
        UUID uuid
        Instant create_time
        Instant update_time
        UUID entry_id FK
        UUID user_id FK
        UUID payment_channel_id FK
        Short funded_ratio
        Long amount
    }
    ledger_entry ||--o{ ledger_entry_detail : ""
    user ||--|| ledger_entry_detail : ""
    payment_channel ||--|| ledger_entry_detail : ""

    ledger_entry_tag {
        Long id PK
        UUID uuid
        Instant create_time
        UUID entry_id FK
        UUID tag_id FK
    }
    ledger_entry ||--o{ ledger_entry_tag : ""
    ledger_tag ||--|| ledger_entry_tag : ""
```

## 账本表(ledger)

| 列名     |   长度   | 用途                                                           |
| :------- | :------: | :------------------------------------------------------------- |
| name     |    32    | 名称                                                           |
| remark   |    64    | 备注                                                           |
| owner_id |          | 账本管理员                                                     |
| type     | smallint | 账本类型： &8 = 8 -> 合作账本, &1 = 1 普通账本, &2 =2 理财账本 |

## 账本用户关联表(co_ledger_user)

仅合作账本会关联用户

| 列名        | 长度  | 用途     |
| :---------- | :---: | :------- |
| ledger_id   |       | 账本ID   |
| user_id     |       | 用户ID   |
| is_writable |       | 是否可改 |

## 账本条目分类表(ledger_category)

| 列名      | 长度  | 用途   |
| :-------- | :---: | :----- |
| ledger_id |       | 账本ID |
| name      |  16   | 名称   |

## 账本条目标签表(ledger_tag)

| 列名    | 长度  | 用途   |
| :------ | :---: | :----- |
| user_id |       | 用户ID |
| name    |  16   | 名称   |

## 账本条目表(ledger_entry)

| 列名        | 长度  | 用途     |
| :---------- | :---: | :------- |
| ledger_id   |       | 账本ID   |
| category_id |       | 分类ID   |
| date        |       | 产生时间 |
| amount      |       | 金额     |
| remark      |  64   | 备注     |

## 帐本条目标签关联表(ledger_entry_tag)

| 列名     | 长度  | 用途   |
| :------- | :---: | :----- |
| entry_id |       | 条目ID |
| tag_id   |       | 标签ID |

## 合作账本条目组成表(co_ledger_entry_)

| 列名     | 长度  | 用途   |
| :------- | :---: | :----- |
| entry_id |       | 条目ID |
| user_id  |       | 用户ID |
| amount   |       | 金额   |

## 用户表(system_user)

| 列名     | 长度  | 用途    |
| :------- | :---: | :------ |
| username |  16   | 用户名  |
| password |  36   | 密码    |
| salt     |  36   | 盐      |
| nickname |  16   | 昵称    |
| avatar   |  64   | 头像URL |

## 理财条目表(financial_entry)

| 列名      | 长度  | 用途     |
| :-------- | :---: | :------- |
| ledger_id |       | 账本ID   |
| date      |       | 产生时间 |
| amount    |       | 金额     |
| remark    |  64   | 备注     |
