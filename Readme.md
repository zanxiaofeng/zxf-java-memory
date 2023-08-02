# How to get Heap info
- jmap -heap <pid>
- jmap -histo <pid>

# How to trigger full gc
- jcmd <pid> GC.run
- jmap -histo:live <pid>
- jmap -dump:live,format=b,file=/path/to/dumpfile.hprof <pid>
- jconsole


# How to monitor GCs
- jstat -gcutil <pid> 1000


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
