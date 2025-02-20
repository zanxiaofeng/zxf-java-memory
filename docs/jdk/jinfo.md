# 介绍
- jinfo 是 JDK 提供的命令行工具，用于查看和动态修改运行中的 Java 进程的 JVM 参数和系统属性。

# 基本语法
- jinfo [options] <pid>

# 常用选项
- `-flags` 显示进程的 JVM 参数（如 `-Xmx`, `-XX:MaxMetaspaceSize` 等）。
- `-sysprops` 显示所有系统属性（等同于 `System.getProperties()`）。
- `-flag <name>` 查看某个具体 JVM 参数的值。
- `-flag [+/-]<name>` **启用或禁用**某个布尔类型的 JVM 参数（如 `PrintGCDetails`）。
- `-flag <name>=<value>` **修改**某个 JVM 参数的值（仅支持动态可写的参数）。
