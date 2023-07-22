# How to generate Heap Dump

## Generating a Heap Dump on demand with jmap
- `jmap -dump:[live] format=b file=/path/to/dumpfile.hprof <pid>`

## Generating a Heap Dump on demand with jcmd
- `jcmd <pid> GC.heap_dump /path/to/dumpfile.hprof`

## Automatically generating a Heap Dump on OutOfMemoryError
- `java -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/path/to/dumpfile.hprof ...`

## Other Methods of Generating Heap Dumps
- JVisualVM

# How to Analyzing the Heap Dump
- Eclipse Memory Analyzer(MAT)
- YourKit Java Profiler
- IBM HEAP Analyzer

# Other tools
- jmap -histo <pid>

# Type of Memory Leaks in Java
## Memory Leak Through static Fields
## Through Unclosed Resources
## 
## Inner Classes That Reference Outer Classes
## Through finalize() Methods
## Interned Strings
## Using ThreadLocals
