# How to get java process info
- `jinfo <pid> #Print all info(Java System Properties, VM Flags, VM Arguments)`


# How to set a named VM flag
- `jinfo -flag <+|-><flag-name> <pid>` # enable/disable
- `jinfo -flag <flag-name>=<value> <pid>` # set value


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
- `jmap -dump:live,format=b,file=/path/to/dumpfile.hprof <pid>`
- `jcmd <pid> GC.heap_dump /path/to/dumpfile.hprof`
- `java -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/path/to/dumpfile.hprof ...`
- By GUI JVisualVM


# How to Analyze the Heap Dump
- Eclipse Memory Analyzer(MAT)
- YourKit Java Profiler
- IBM HEAP Analyzer
- jhat <options> <heap-dump-file>(http://localhost:7000)


# Type of Memory Leaks in Java
- Memory Leak Through static Fields
- Through Unclosed Resources
- Inner Classes That Reference Outer Classes
- Through finalize() Methods
- Interned Strings
- Using ThreadLocals


## OOM
- Java虚拟机（JVM）在遇到内存溢出错误（OOM，OutOfMemoryError）时，并不一定会立即终止执行。OOM是Error类的一个子类，通常表示一种严重的运行时问题，它标志着JVM在尝试分配内存但无法满足请求时的错误状态。
- 尽管如此，JVM会将这个错误传递给当前正在运行的代码，给予应用程序一个机会去捕获并处理这个异常。如果应用程序确实捕获了这个错误，理论上它可能会尝试继续执行，但在实际操作中并不推荐这样做，因为这通常意味着程序已经处于不稳定的状态。
## OOM Killer
- OOM Killer是Linux内核在面对内存不足时的自我保护机制, 它会在系统内存不足时自动选择并终止内存使用最多的进程来释放内存，防止系统崩溃。
- 在容器化环境中，它的行为需要被理解和管理，以确保系统的稳定性和应用程序的性能。
- 在Kubernetes中，每个Pod都有自己的cgroup，用于限制和监控资源使用。当Pod的内存使用量超过其cgroup的限制时，OOM Killer可能会被触发，选择并终止该Pod中的一个或多个容器以释放内存。然而，这并不意味着整个Pod都会被杀掉，因为Pod中的其他容器可能并未超过内存限制。


## Java Options
- Standard options that are guaranteed to be supported by all JVM implementations out there. Usually, these options are used for everyday actions such as –classpath, -cp, –version, and so on
- Extra options that are not supported by all JVM implementations and are usually subject to change. These options start with -X
- Moreover, some of those additional options are more advanced and begin with -XX.


## Container oom-killed
#### 查看内存使用量
- docker stats
#### Cgroup V1
- The memory limit of the current container
- '$ cat /sys/fs/cgroup/memory/memory.limit_in_bytes'
- The actual memory usage of the current container
- '$ cat /sys/fs/cgroup/memory/memory.usage_in_bytes'
- The max memory usage of the current container
- '$ cat /sys/fs/cgroup/memory/memory.max_usage_in_bytes'
- The memory&swap limit of the current container
- '$ cat /sys/fs/cgroup/memory/memory.memsw.limit_in_bytes'
- The actual memory&swap usage of the current container
- '$ cat /sys/fs/cgroup/memory/memory.memsw.usage_in_bytes'
- '$ cat /sys/fs/cgroup/memory/memory.numa_stat'
- '$ cat /sys/fs/cgroup/memory/memory.oom_control'
- '$ cat /sys/fs/cgroup/memory/memory.pressure_level'
- '$ cat /sys/fs/cgroup/memory/memory.stat'
#### Cgroup V2
- The memory limit of the current container
- '$ cat /sys/fs/cgroup/memory.max'
- The actual memory usage of the current
- '$ cat /sys/fs/cgroup/memory.current'
- The max memory usage of the current container
- '$ cat /sys/fs/cgroup/memory.peak'
- '$ cat /sys/fs/cgroup/memory.stat'


## JVM OOM
#### java.lang.OutOfMemoryError - Java heap space overflow
- Java heap space overflow – This error is thrown when the heap space does not have enough space to store the newly created object. This is usually caused by memory leaks or improper heap size settings. For memory leaks, you need to use memory monitoring software to find the leaked code in the program, and the heap size can be modified using parameters (such as-Xms and-Xmx).
#### java.lang.OutOfMemoryError - PermGen space/Metaspace overflow**
- PermGen space/Metaspace overflow – The objects that permanent generation stores include class information and constants. The JDK 1.8 uses Metaspace to replace the permanent generation. This error is usually reported because the number of classes loaded is too large or the size is too big. You can modify the-XX:MaxPermSize or-XX:MaxMetaspaceSize to expand the PermGen space/Metaspace.
#### java.lang.OutOfMemoryError - Unable to create a new native thread
- Unable to create a new native thread. Each Java thread needs to occupy a certain amount of memory space. When the JVM sends a request to the underlying operating system to create a new native thread, such an error mentioned above will be reported if there aren't enough resources to be allocated. Possible causes are insufficient native memory, the number of threads exceeding the limit of the maximum number of threads in the operating system caused by thread leak, ulimit, or the number of threads exceeding the kernel.pid_max. You need to upgrade resources, limit the size of the thread pool, and reduce the size of the thread stack.


## Native Memory Tracking (NMT)
- JDK8 introduces the Native Memory Tracking (NMT) feature that tracks the internal memory usage of the JVM. 
- Use the NativeMemoryTracking (NMT) to understand the JVM memory usage of your application. NMT can track the memory usage of the JVM. In tests, NMT can be used to figure out the approximate distribution of the memory used by the program JVM as a reference for memory capacity configuration. 
- By default, NMT is turned off and on using the JVM parameter: -XX:NativeMemoryTracking=[off | summary | detail], After NMT is enabled, you can use the jcmd command to print the JVM memory usage.
- jcmd <pid> VM.native_memory [summary | detail] [scale=MB]


## Set memory size of JVM
#### Option 1
- `-Xms<size> set initial Java heap size`
- `-Xmx<size> set maximum Java heap size`
- `-Xss<size> set java thread stack size`
- `-Xmn<size> set eden zone size`
- `-XX:MetaspaceSize set initial meta space size`
- `-XX:MaxMetaspaceSize set maximum meta space size`
#### Option 2(For container env, jdk10+)
- `-XX:+UseContainerSupport` #默认开启
- `-XX:InitialRAMPercentage=50.0`
- `-XX:MinRAMPercentage=80.0`
- `-XX:MaxRAMPercentage=60.0`

## Set memory size of Non-JVM
- `-XX:MaxDirectMemorySize=512M`


## How to kill java process after OOM
- `-XX:+CrashOnOutOfMemoryError`
- `-XX:OnOutOfMemoryError="/path/to/oomKillAndRestart.sh"`

## JVM setting
- java <OPTIONS> -XshowSettings <-version | entrypoint>
- java <OPTIONS> -XshowSettings:vm <-version | entrypoint>

## Global flags
- `java -XX:+PrintFlagsInitial`
- `java -XX:+PrintFlagsFinal <-version | entrypoint>`
- `java -XX:+PrintCommandLineFlags <-version | entrypoint>`
- `java -XX:+PrintFlagsFinal -version | grep HeapSize`

# JDK troubleshooting guide
- https://docs.oracle.com/javase/8/docs/technotes/guides/troubleshoot/
- https://docs.oracle.com/javase/9/troubleshoot/title.htm
- https://docs.oracle.com/en/java/javase/17/troubleshoot/troubleshooting-guide.pdf
- https://docs.oracle.com/en/java/javase/22/troubleshoot/troubleshooting-guide.pdf
