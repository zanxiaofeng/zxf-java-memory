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


# Get Default HeapSize
- `java -XX:+PrintFlagsFinal -version | grep HeapSize`

# Type of Memory Leaks in Java
## Memory Leak Through static Fields
## Through Unclosed Resources
## Inner Classes That Reference Outer Classes
## Through finalize() Methods
## Interned Strings
## Using ThreadLocals
