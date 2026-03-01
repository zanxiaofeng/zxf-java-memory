# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Java Memory 研究项目 — 用于演示和测试各种 JVM 内存泄漏场景、OOM 场景及对象内存布局分析的 Spring Boot 应用。这是一个学习/实验性项目，**故意包含有缺陷的代码**来触发内存问题。

## Build & Run

- **JDK**: 21
- **框架**: Spring Boot 3.5.11 + Maven
- **构建**: `./mvnw clean package` 或 `mvn clean package`
- **运行 Web 应用**: `mvn spring-boot:run` (默认端口 8080)
- **推荐 JVM 参数**: `-XX:+UseG1GC -Xms256M -Xmx1024M -XshowSettings -XX:+PrintFlagsFinal -XX:NativeMemoryTracking=detail`
- **独立 main 类**（OOM/JOL 测试）直接运行，不走 Spring Boot

## Architecture

### 包结构

- `controller/` — REST 端点，触发各种内存场景
  - `LeakController` (`/leak/*`) — 内存泄漏演示入口
  - `AController` (`/a/*`), `BController` (`/b/*`) — 缓存压力测试（不同采样频率）
- `leak/` — 各类内存泄漏实现（StaticReference、UnclosedResources、InnerClass、ThreadLocal、HashAndEquals）
- `oom/` — OOM 场景的独立 main 程序（HeapSpace、DirectMemory、ThreadStackSpace）
- `jol/` — 使用 JOL (Java Object Layout) 分析对象内存布局的独立 main 程序
- `jdbc/` — Oracle JDBC/UCP 连接池相关测试
- `service/` — StringCacheService、BeanCacheService（缓存服务）、ObjectSizeFetcher
- `util/` — MemoryMonitor（MXBean/jmap/cgroup 内存监控）、DebugUtils

### 关键依赖

- Oracle JDBC (`ojdbc8` + `ucp` + `ons`) — Oracle 数据库连接
- Apache PDFBox — PDF 处理测试
- JOL (`jol-core`) — Java 对象内存布局分析
- Lombok — 使用 `@Slf4j` 等注解

### 设计要点

- `MemoryMonitor` 在应用启动后自动以 150 秒间隔采集 Heap/Non-Heap/Buffer Pool/cgroup 内存信息
- `leak/` 包中的类是**故意有缺陷的** — 它们演示真实的内存泄漏模式，修改时需保留其泄漏行为
- `oom/` 和 `jol/` 包含独立的 `main()` 方法，需单独运行，不是 Spring Boot 的一部分

## API Endpoints

| Endpoint | 用途 |
|---|---|
| `GET /leak/gc` | 触发 GC 并记录前后内存 |
| `GET /leak/byStaticReference` | 静态引用泄漏 |
| `GET /leak/byUnclosedResources` | 未关闭资源泄漏 |
| `GET /leak/byHashAndEqualsNotImplemented` | 缺少 hashCode/equals 的泄漏 |
| `GET /leak/byInnerClass?count=N` | 内部类持有外部类引用泄漏 |
| `GET /leak/byThreadLocal?release=false` | ThreadLocal 泄漏 |
| `GET /a/cache/string`, `/b/cache/string` | 字符串缓存压力 |
| `GET /a/cache/bean`, `/b/cache/bean` | Bean 缓存压力 |
