# How to get java process info
- `jinfo <pid> #Print all info(Java System Properties, VM Flags, VM Arguments)`


# How to enable/disable the named VM flag
- `jinfo -flag <+|-> <name>`


# How to get Heap info
- `jmap -heap <pid>`
- `jmap -histo <pid>`


# How to trigger full gc
- `jcmd <pid> GC.run`
- `jmap -histo:live <pid>`
- `jmap -dump:live,format=b,file=/path/to/dumpfile.hprof <pid>`
- `jconsole`


# How to monitor GCs
## `jstat -gcutil <pid> 1000`
- S0, Survivor0使用百分比
- S1, Survivor1使用百分比
- E, Eden区使用百分比
- O, 老年代使用百分比
- M, 元数据区使用百分比
- CCS, 压缩使用百分比
- YGC, 年轻代垃圾回收次数
- YGCT, 年轻代垃圾回收消耗时间
- FGC, Full GC垃圾回收次数
- FGCT, Full GC垃圾回收收消耗时间
- GCT, 垃圾回收收消耗时间
## `jstat -gc <pid> 1000`
- S0C, Survivor0大小
- S0U, Survivor0已使用大小
- S1C, Survivor1大小
- S1U, Survivor1已使用大小
- EC, Eden区大小
- EU, Eden区已使用大小
- OC, 老年代大小
- OU, 老年代已使用大小
- MC, 元数据区大小
- MU, 元数据区已使用大小

# How to generate Heap Dump
## Generating a Heap Dump on demand with jmap
- `jmap -dump:live,format=b,file=/path/to/dumpfile.hprof <pid>`
## Generating a Heap Dump on demand with jcmd
- `jcmd <pid> GC.heap_dump /path/to/dumpfile.hprof`
## Automatically generating a Heap Dump on OutOfMemoryError
- `java -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/path/to/dumpfile.hprof ...`
## Other Methods of Generating Heap Dumps
- JVisualVM


# How to Analyze the Heap Dump
- Eclipse Memory Analyzer(MAT)
- YourKit Java Profiler
- IBM HEAP Analyzer
- jhat <options> <heap-dump-file>(http://localhost:7000)


# Type of Memory Leaks in Java
## Memory Leak Through static Fields
## Through Unclosed Resources
## Inner Classes That Reference Outer Classes
## Through finalize() Methods
## Interned Strings
## Using ThreadLocals

## OOM
- Java虚拟机（JVM）在遇到内存溢出错误（OOM，OutOfMemoryError）时，并不一定会立即终止执行。OOM是Error类的一个子类，通常表示一种严重的运行时问题，它标志着JVM在尝试分配内存但无法满足请求时的错误状态。
- 尽管如此，JVM会将这个错误传递给当前正在运行的代码，给予应用程序一个机会去捕获并处理这个异常。如果应用程序确实捕获了这个错误，理论上它可能会尝试继续执行，但在实际操作中并不推荐这样做，因为这通常意味着程序已经处于不稳定的状态。
## OOM Killer
- OOM Killer是Linux内核在面对内存不足时的自我保护机制, 它会在系统内存不足时自动选择并终止内存使用最多的进程来释放内存，防止系统崩溃。
- 在容器化环境中，它的行为需要被理解和管理，以确保系统的稳定性和应用程序的性能。
- 在Kubernetes中，每个Pod都有自己的cgroup，用于限制和监控资源使用。当Pod的内存使用量超过其cgroup的限制时，OOM Killer可能会被触发，选择并终止该Pod中的一个或多个容器以释放内存。然而，这并不意味着整个Pod都会被杀掉，因为Pod中的其他容器可能并未超过内存限制。

## Memory parameters of JVM
- Initial heap size
- Maximum heap size
- Eden zone size
- Metaspace size
- Stack size

## How to set mem size of java
### Option 1
- `-Xms<size> set initial Java heap size`
- `-Xmx<size> set maximum Java heap size`
- `-Xss<size> set java thread stack size`
- `-Xmn<size> set eden zone size`
- `-XX:MetaspaceSize set initial meta space size`
- `-XX:MaxMetaspaceSize set maximum meta space size`
### Option 2(For container env)
- -XX:InitialRAMPercentage=50.0
- -XX:MinRAMPercentage=80.0
- -XX:MaxRAMPercentage=60.0

## How to kill java process after OOM
- `-XX:+CrashOnOutOfMemoryError`
- `-XX:OnOutOfMemoryError="/path/to/oomKillAndRestart.sh"`

## How to display JVM setting
- java <OPTIONS> -XshowSettings:vm -version
## Get Default HeapSize
- `java -XX:+PrintFlagsFinal -version | grep HeapSize`