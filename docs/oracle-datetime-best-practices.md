# Oracle Date Time 类型在 Java 中的最佳实践

> 基于 `JdbcTimeTests.java` 和 `JDBCDataInitializer.java` 的代码分析总结。

## 1. Oracle 四种日期时间类型

| Oracle 类型 | 存储内容 | 精度 | 推荐Java类型 |
|---|---|---|---|
| `DATE` | 年月日时分秒 | 秒级 | `LocalDateTime` 或 `java.sql.Timestamp` |
| `TIMESTAMP(6)` | 年月日时分秒+纳秒 | 纳秒级 | `LocalDateTime` |
| `TIMESTAMP(6) WITH TIME ZONE` | 时间戳+时区偏移 | 纳秒级 | `ZonedDateTime` / `OffsetDateTime` |
| `TIMESTAMP(6) WITH LOCAL TIME ZONE` | 时间戳（转为DB时区存储） | 纳秒级 | `ZonedDateTime` / `OffsetDateTime` |

## 2. 读取（Oracle → Java）最佳实践

**优先使用 `resultSet.getObject(col, Class)` 指定目标类型**，避免依赖默认的 Oracle 驱动映射：

```java
// 推荐：明确指定目标类型
LocalDateTime ldt = resultSet.getObject("CL_DATE", LocalDateTime.class);
LocalDateTime ts = resultSet.getObject("CL_TIMESTAMP", LocalDateTime.class);
ZonedDateTime tstz = resultSet.getObject("CL_TIMESTAMP_TZ", ZonedDateTime.class);
ZonedDateTime tsltz = resultSet.getObject("CL_TIMESTAMP_LTZ", ZonedDateTime.class);
```

**默认映射行为**（不指定类型时）需注意：

- `DATE` 列 → 默认返回 `java.sql.Timestamp`（不是 `java.sql.Date`）
- `TIMESTAMP` 列 → 默认返回 `oracle.sql.TIMESTAMP`
- `TIMESTAMP WITH TIME ZONE` 列 → 默认返回 `oracle.sql.TIMESTAMPTZ`
- `TIMESTAMP WITH LOCAL TIME ZONE` 列 → 默认返回 `oracle.sql.TIMESTAMPLTZ`

## 3. 写入（Java → Oracle）最佳实践

### 方式一：使用 `OracleType` 显式指定目标 Oracle 类型（推荐）

```java
preparedStatement.setObject(1, now, OracleType.DATE);
preparedStatement.setObject(2, now, OracleType.TIMESTAMP);
preparedStatement.setObject(3, now, OracleType.TIMESTAMP_WITH_TIME_ZONE);
preparedStatement.setObject(4, now, OracleType.TIMESTAMP_WITH_LOCAL_TIME_ZONE);
```

### 方式二：让驱动自动推断（了解其规则）

| Java 类型 | Oracle 自动映射为 |
|---|---|
| `java.sql.Timestamp` | `TIMESTAMP` (type=180) |
| `java.sql.Date` | `DATE` (type=12) |
| `LocalDate` | `TIMESTAMP` (type=180) |
| `LocalDateTime` | `TIMESTAMP` (type=180) |
| `ZonedDateTime` | `TIMESTAMP WITH TIME ZONE` (type=181) |
| `OffsetDateTime` | `TIMESTAMP WITH TIME ZONE` (type=181) |

**关键点**：`LocalDate` 和 `LocalDateTime` 自动映射为 `TIMESTAMP` 而非 `DATE`，只有 `java.sql.Date` 才映射为 `DATE`。

## 4. 时区管理

```java
// 设置 Session 时区（影响 TIMESTAMP WITH LOCAL TIME ZONE 的转换）
((OracleConnection) connection).getSessionTimeZone();  // 查看当前
// ALTER SESSION SET TIME_ZONE = '+07:00'              // 修改

// 查看 DB 和 Session 时区
// SELECT DBTIMEZONE, SESSIONTIMEZONE FROM DUAL;
```

**时区规则**：

- `DATE` / `TIMESTAMP`：无时区概念，存什么就是什么
- `TIMESTAMP WITH TIME ZONE`：存储时保留了时区偏移信息
- `TIMESTAMP WITH LOCAL TIME ZONE`：存储时转为 DB 时区，读取时转为 Session 时区

## 5. 核心建议

1. **使用 `java.time` 包**（`LocalDateTime`, `ZonedDateTime`, `OffsetDateTime`），避免使用旧的 `java.sql.Timestamp` / `java.util.Date`
2. **读取时始终指定目标 Class**，用 `getObject(col, LocalDateTime.class)` 而非 `getObject(col)` 后强转
3. **写入时显式指定 `OracleType`**，避免驱动自动推断带来的意外映射
4. **涉及跨时区场景使用 `WITH TIME ZONE` 类型**，纯本地场景用 `TIMESTAMP`
5. **不要用 `java.sql.Date`** 代表 Oracle `DATE`（它会截断时间部分），用 `LocalDateTime` 更安全
