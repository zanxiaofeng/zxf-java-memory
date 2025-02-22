以下是使用 `jstat` 监控 Java 进程内存使用情况的命令详解及输出示例：

---

### **1. 基本命令格式**
```bash
jstat -gc <进程PID> [刷新间隔(ms)] [刷新次数]
```
- **示例**：
  ```bash
  jstat -gc 12345 1000 5 # 监控 PID=12345 的进程，每秒刷新一次，共输出5次
  ```

---

### **2. 快速获取 Java 进程 PID**
使用 `jps` 命令列出所有 Java 进程及其 PID：
```bash
jps -l
```
输出示例：
```
12345 com.example.MainClass
67890 sun.tools.jps.Jps
```

---

### **3. `jstat -gc` 输出列详解**
执行命令后，输出如下格式的表格（不同 JDK 版本列名可能微调）：

| **列名** | **全称** | **说明** |
|----------|-------------------------|-------------------------------------------------------------------------|
| `S0C` | Survivor 0 Capacity | 第1个 Survivor 区的容量 (KB) |
| `S1C` | Survivor 1 Capacity | 第2个 Survivor 区的容量 (KB) |
| `S0U` | Survivor 0 Utilization | 第1个 Survivor 区已使用量 (KB) |
| `S1U` | Survivor 1 Utilization | 第2个 Survivor 区已使用量 (KB) |
| `EC` | Eden Capacity | Eden 区的容量 (KB) |
| `EU` | Eden Utilization | Eden 区已使用量 (KB) |
| `OC` | Old Capacity | 老年代容量 (KB) |
| `OU` | Old Utilization | 老年代已使用量 (KB) |
| `MC` | Metaspace Capacity | 元空间（方法区）的容量 (KB) |
| `MU` | Metaspace Utilization | 元空间已使用量 (KB) |
| `CCSC` | Compressed Class Space Capacity | 压缩类空间容量（启用压缩指针时有效）(KB) |
| `CCSU` | Compressed Class Space Utilization | 压缩类空间已使用量 (KB) |
| `YGC` | Young GC Count | 新生代 GC 次数 |
| `YGCT` | Young GC Time | 新生代 GC 总耗时（秒） |
| `FGC` | Full GC Count | Full GC 次数 |
| `FGCT` | Full GC Time | Full GC 总耗时（秒） |
| `GCT` | Total GC Time | 所有 GC 总耗时（秒） |

---

### **4. 输出示例**
```plaintext
 S0C S1C S0U S1U EC EU OC OU MC MU CCSC CCSU YGC YGCT FGC FGCT GCT   
10240 10240 0.0 0.0 81920 32768 204800 51200 48640 47234 6144 5678 10 0.250 2 0.500 0.750
```
- **解读**：
    - **新生代**：Eden 区容量 `EC=81920KB`，已使用 `EU=32768KB`（约 32MB）。
    - **老年代**：容量 `OC=204800KB`，已使用 `OU=51200KB`（约 50MB）。
    - **元空间**：容量 `MC=48640KB`，已使用 `MU=47234KB`。
    - **GC 统计**：发生 10 次 Young GC（耗时 0.25s），2 次 Full GC（耗时 0.5s），总 GC 时间 0.75s。

---

### **5. 其他常用 `jstat` 选项**
| **选项** | **说明** |
|---------------|-------------------------------------------------------------------------|
| `-gcutil` | 显示各内存区域使用百分比（更直观） |
| `-gccapacity` | 显示各内存区域容量及其限制（如 `NGCMN` 新生代最小容量，`NGCMX` 最大容量） |
| `-gcmetacapacity` | 显示元空间容量统计 |

**示例**：
```bash
jstat -gcutil 12345 # 输出内存使用率百分比
```
输出示例：
```
  S0 S1 E O M CCS YGC YGCT FGC FGCT GCT   
  0.00 99.80 25.43 45.67 97.33 92.45 10 0.250 2 0.500 0.750
```

---

### **6. 关键分析场景**
- **内存泄漏**：若 `OU`（老年代使用量）持续增长且 Full GC（`FGC`）频繁，可能存在内存泄漏。
- **Young GC 频繁**：若 `YGC` 快速增加且 `EU` 接近 `EC`，可能需要增大新生代（`-Xmn`）。
- **元空间不足**：若 `MU` 接近 `MC`，需调整 `-XX:MaxMetaspaceSize`。

通过定期运行 `jstat` 并观察趋势，可快速定位内存问题并优化 JVM 参数。