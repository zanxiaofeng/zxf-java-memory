# 介绍
jcmd 是 JDK 自带的命令行工具，用于向正在运行的 Java 进程发送诊断命令，支持查看 JVM 状态、生成堆转储、监控性能等操作。它的功能整合了 'jps'，`jmap`、`jstack`、`jinfo` 等传统工具，且语法更简洁统一。

# 基本语法
- jcmd [options] <pid|main-class> <command> [arguments]

# 常用命令
- jcmd -l #列出所有 Java 进程
- jcmd <pid> help #查看进程支持的命令
- jcmd <pid> VM.info #查看系统属性（替代 `jinfo`）
- jcmd <pid> VM.version #查看系统属性（替代 `jinfo`）
- jcmd <pid> VM.system_properties #查看系统属性（替代 `jinfo -sysprops`）
- jcmd <pid> VM.flags  #查看 JVM 参数（替代 `jinfo -flags`）
- jcmd <pid> VM.set_flag PrintGCDetails true #开启打印 GC 详情
- jcmd <pid> VM.set_flag TraceClassUnloading false # 关闭类卸载日志
- jcmd <pid> Thread.print # 生成线程转储（替代 `jstack`）
- jcmd <pid> VM.native_memory [summary|detail] #查看 Native Memory 分配，需 JVM 启动时开启 Native Memory Tracking
- jcmd <pid> GC.heap_dump filename=heapdump.hprof 生成堆转储（替代 `jmap`）
- jcmd <pid> GC.heap_info #查看堆内存使用
- jcmd <pid> GC.run #触发 Full GC
- jcmd <pid> GC.class_histogram 查看类直方图（替代 `jmap -histo`）
- jcmd <pid> JFR.start name=myrecording duration=60s filename=profile.jfr #启动 JFR 记录（持续 60 秒，保存到 profile.jfr）
- jcmd <pid> JFR.check # 查看正在进行的 JFR 记录
- jcmd <pid> JFR.stop name=myrecording filename=profile.jfr # 停止并转储记*录*
- jcmd <pid> Compiler.codecache #查看编译后的方法信息*
