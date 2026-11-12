# 长安逸动 2018 车机安装 EadoPlay：纯小白完整教程

> 适用车型：2018 款长安逸动原厂 Android 4.4.2 车机。
>
> 适用对象：完全不懂 ADB、安卓命令和编程，希望先完成简单准备，再把电脑安装过程交给 AI 的车主。
>
> 本教程不需要 root，不破解系统，也不修改 `/system`。

## 先看一遍完整流程

请先把电脑端需要的工具和文件准备齐全，再去车机上操作。正确顺序是：

```text
准备双 USB-A 公对公数据线和 Windows 电脑
→ 打开能够操作电脑终端的 AI
→ 用户或 AI 下载并解压 Platform-Tools
→ 下载 EadoPlay APK，并复制 APK 完整路径
→ 确认 adb.exe 和 APK 都已准备好
→ 车辆停稳并保持车机供电
→ 输入 *#518200#* 打开 USB 调试
→ 输入 *#518121#* 切换 Device 模式
→ 用双 USB-A 公对公数据线连接车机和电脑
→ 在车机上允许这台电脑进行 USB 调试
→ 把本文的完整安装提示词交给 AI
→ AI 检查连接、安装并验证 EadoPlay
→ 输入 *#518120#* 恢复 USB Host 模式
```

未破解的原厂车机会拒绝 U 盘点击安装和普通 `adb install`。本教程会让 AI 在确认车机 ADB 连接和工程权限正常后，直接使用本车已验证的 `/data/app` 安装方式绕过厂商普通安装鉴权。

如果你的车机已经破解到能够直接点击 APK 安装，那么直接通过 U 盘安装即可，不需要使用这份教程。

---

## 第一部分：先在电脑旁完成全部准备工作

这部分全部完成以后，才需要进入车辆操作车机。

## 一、准备电脑、线材和 AI

你需要：

1. 一台 Windows 电脑；
2. 一根支持数据传输的**双 USB-A 公对公数据线**；
3. EadoPlay 的 APK 安装包；
4. 一个能够操作这台电脑终端的 AI，例如具备本机终端操作能力的 Codex。

普通网页聊天机器人如果只能回答问题，不能操作电脑上的 PowerShell 或终端，就不能真正替你下载安装。它只能把命令显示出来，让你自己执行。

### 数据线必须选对

本车调试使用的是两头均为 USB-A 公头的公对公数据线：

```text
车机 USB-A 接口 ←→ USB-A 公头——数据线——USB-A 公头 ←→ 电脑 USB-A 接口
```

购买或寻找线材时，应同时满足这些关键词：

- USB-A 对 USB-A；
- 双 A 口；
- 公对公；
- 支持数据传输。

下面这些线不能替代：

- USB-A 转 Type-C；
- USB-A 转 Lightning；
- USB-A 转 Micro-USB；
- 只有供电线路、不支持数据传输的双 A 口供电线。

线材能供电不代表能够传数据。最终应以电脑运行 `adb devices` 能否看到车机为准。

## 二、准备 ADB（Platform-Tools）

ADB 是电脑控制安卓车机的官方调试工具，包含在 Google 官方的 Android SDK Platform-Tools 中。无需安装完整的 Android Studio。

准备完成后的目标路径是：

```text
C:\platform-tools\adb.exe
```

### 方法 A：自己从 Google 官方页面下载

1. 打开：

   [Google 官方 Platform-Tools 下载页面](https://developer.android.com/tools/releases/platform-tools?hl=zh-cn)

2. 选择 Windows 版本；
3. 接受协议并下载 `platform-tools-latest-windows.zip`；
4. 在 ZIP 文件上点击鼠标右键，选择“全部解压”；
5. 把解压后的整个 `platform-tools` 文件夹放到 C 盘根目录；
6. 确认存在：

   ```text
   C:\platform-tools\adb.exe
   ```

如果最终路径变成：

```text
C:\platform-tools\platform-tools\adb.exe
```

说明多套了一层文件夹。把里面那层 `platform-tools` 移到 C 盘根目录即可。

### 方法 B：Google 页面打不开时，让 AI 帮忙下载

国内网络可能无法打开 Google 下载页面。如果你使用的 AI 能操作本机终端并访问网络，可以把下面整段话发送给它：

```text
请帮我在这台 Windows 电脑上准备 Google 官方 Android SDK Platform-Tools。

要求：
1. 先检查 C:\platform-tools\adb.exe、Android SDK 常见目录和系统 PATH 中是否已经存在可用的 adb.exe。
2. 如果已有 ADB，先告诉我实际路径和版本，不要重复下载。
3. 如果没有，只允许从 Google 官方地址下载：
   https://dl.google.com/android/repository/platform-tools-latest-windows.zip
4. 不要使用网盘、论坛、软件下载站、来历不明的国内镜像或“魔改 ADB 工具箱”。
5. 将 ZIP 下载到 Windows 下载目录，再解压到 C:\platform-tools。
6. 最终确认 C:\platform-tools\adb.exe 真实存在。
7. 运行 C:\platform-tools\adb.exe version，并告诉我版本信息。
8. 检查 adb.exe 的 Windows 数字签名；如果系统能读取签名，应确认签名有效，发布者与 Google 相符。
9. 如果 Google 官方地址也无法访问，请停止并告诉我下载失败，不要擅自改用第三方来源。
10. 完成后告诉我：是否原本已有 ADB、实际 adb.exe 路径、ADB 版本、下载来源和解压结果。
```

AI 可能拥有与浏览器不同的网络访问能力，因此有时可以代为下载；如果 AI 和电脑都无法访问 Google，它也无法凭空得到官方文件。这种情况下应更换网络后再试，不要自动改用不明来源。

### 检查 ADB 是否准备好

在文件资源管理器中打开：

```text
C:\platform-tools
```

应至少能看到 `adb.exe`。不要只从 ZIP 中单独拖出一个 `adb.exe`，因为 Platform-Tools 还包含它运行时需要的其他文件。

## 三、下载 EadoPlay APK

1. 打开：

   [EadoPlay GitHub Releases](https://github.com/LeoChase913/EadoPlay/releases)

2. 找到要安装的版本；
3. 在该版本的“Assets”或“附件”区域下载扩展名为 `.apk` 的文件；
4. 建议把 APK 放到 Windows 桌面；
5. 不要解压 APK，也不要修改 APK 内部文件。

例如，APK 可能位于：

```text
C:\Users\你的电脑用户名\Desktop\EadoPlay-r29.apk
```

### 复制 APK 的准确路径

1. 在文件资源管理器中找到 APK；
2. 按住键盘上的 `Shift`；
3. 对 APK 点击鼠标右键；
4. 选择“复制文件地址”或“复制为路径”；
5. 把路径粘贴到记事本中保存。

复制结果一般类似：

```text
"C:\Users\lzc\Desktop\EadoPlay-r29.apk"
```

引号可以保留。不要只告诉 AI“APK 在桌面”，应提供完整路径。

## 四、确认电脑端准备工作已经完成

在开始操作车机以前，逐项确认：

- [ ] 已准备双 USB-A 公对公数据线，并确认支持数据传输；
- [ ] 已准备能够操作本机终端的 AI；
- [ ] `C:\platform-tools\adb.exe` 真实存在；
- [ ] EadoPlay APK 已下载且没有解压；
- [ ] 已复制 APK 的完整路径；
- [ ] AI 下载 ADB 时使用的是 Google 官方来源。

建议在记事本中提前填好：

```text
ADB 路径：C:\platform-tools\adb.exe
APK 路径：________________________________
车机型号：2018 款长安逸动
车机系统：Android 4.4.2
EadoPlay 包名：com.eadoplay.carplay
```

到这里全部确认无误后，再进入车辆开始操作车机。

---

## 第二部分：开始操作车机

## 五、车辆停稳并保持车机供电

1. 把车辆停在安全位置；
2. 保持车机稳定供电；
3. 安装过程中不要驾驶车辆；
4. 在 AI 确认安装和验证完成以前，不要熄火、断开车机电源或拔数据线。

## 六、打开车机 USB 调试

这一步必须由车主在车机上完成。

1. 打开车机拨号界面；
2. 输入：

   ```text
   *#518200#*
   ```

3. 进入安卓设置；
4. 找到“开发者选项”或“USB 调试”；
5. 确认“USB 调试”已经打开；
6. 如果出现风险提示，阅读后确认开启。

USB 调试已经开启时，不需要反复关闭再打开。

> 这些工程代码只在本项目对应的 2018 款长安逸动原厂车机上验证过，不保证适用于其他车型或其他固件。

## 七、把车机 USB 切换成 Device 模式

电脑通过 ADB 控制车机时，车机必须作为被电脑识别的安卓设备，也就是 Device 模式。

1. 回到车机拨号界面；
2. 输入：

   ```text
   *#518121#*
   ```

3. 等待车机完成 USB 模式切换。

Device 模式用于电脑调试车机。Host 模式用于车机读取 U 盘或直接控制其他 USB 设备，两者不是一回事。

## 八、用双 USB-A 公对公数据线连接电脑

1. 把数据线一头插入车机 USB-A 接口；
2. 把另一头直接插入电脑 USB-A 接口；
3. 第一次连接时不要经过 USB 集线器、扩展坞、转接头或延长坞；
4. 暂时不要拔线。

连接关系应为：

```text
车机 USB-A 接口
→ 双 USB-A 公对公数据线
→ 电脑 USB-A 接口
```

## 九、允许这台电脑调试车机

车机可能弹出“是否允许 USB 调试”，并显示一串 RSA 密钥指纹。

此时：

1. 勾选“始终允许使用这台计算机进行调试”；
2. 点击“允许”或“确定”；
3. 不要点击“取消”。

如果没有立刻弹窗，先继续到下一步。AI 运行 `adb devices` 后，授权弹窗可能才会出现。

如果以前已经勾选过“始终允许”，车机可能不会再次弹窗。

---

## 第三部分：把安装交给 AI

## 十、把完整安装提示词发送给 AI

打开能够操作这台电脑终端的 AI，把本文“附录一”的完整提示词复制给它。

发送前必须把提示词中的：

```text
【把这里替换成实际 APK 完整路径】
```

替换成第三节复制的真实 APK 路径。

## 十一、AI 操作时用户需要做什么

AI 开始操作后，请留在车机旁边：

1. 如果车机弹出 USB 调试授权，立即勾选“始终允许”并点击允许；
2. 如果 AI 说设备为 `unauthorized`，检查车机屏幕上的授权弹窗；
3. 如果 AI 说没有发现设备，检查 Device 模式、双 USB-A 公对公数据线和电脑接口；
4. 如果 AI 提议 root、修改 `/system`、关闭安全服务、删除其他应用或运行不明脚本，拒绝执行；
5. 如果 AI 提议卸载 EadoPlay 或清除数据，先了解是否会丢失设置，不要直接同意；
6. 安装过程中不要拔线、熄火或让车机断电。

AI 应逐步告诉你结果。实际返回与预期不同时，它应停下来分析，不能自动执行更激进的命令。

## 十二、AI 正常应执行的流程

你不需要理解全部命令，但可以用下面的顺序判断 AI 是否按教程操作：

### 1. 检查电脑文件

确认：

```text
C:\platform-tools\adb.exe
你提供的 EadoPlay APK 完整路径
```

两个文件都真实存在。

### 2. 检查 ADB 和车机连接

AI 应运行：

```powershell
& 'C:\platform-tools\adb.exe' version
& 'C:\platform-tools\adb.exe' devices -l
```

车机状态必须明确显示为：

```text
device
```

其他状态：

- 列表为空：电脑还没有找到车机；
- `unauthorized`：车机尚未允许这台电脑调试；
- `offline`：ADB 连接异常，需要重新启动 ADB 或重新插线。

设备不是 `device` 时，AI 不应继续安装。

### 3. 检查工程 ADB 写入权限

AI 应查看 `adb shell id`，再通过创建后立即删除一个名称明确的测试文件，确认 `/data/app` 可写。

本项目对应的原厂车机具备所需工程权限。这个测试用于确认当前连接和权限状态正常。

如果返回 `Permission denied` 或结果不确定，AI 必须停止，不能擅自 root、破解系统或关闭车机安全服务。

### 4. 绕过厂商鉴权并安全写入 APK

确认权限正常后，AI 应：

1. 查询车机是否已经安装 EadoPlay；
2. 已安装时，读取旧 APK 的准确路径；
3. 首次安装时使用 `/data/app/eadoplay.apk`；
4. 把新 APK 上传到 `/data/local/tmp/EadoPlay-new.apk`；
5. 先复制为 `/data/app/.eadoplay.apk.tmp` 临时文件；
6. 复制完成后设置本车已验证的文件所有者和权限；
7. 在同一分区内原子改名为正式 APK；
8. 执行 `sync` 并等待安卓系统扫描登记；
9. 删除本次安装产生的临时文件。

先写完整临时文件再原子改名，可以避免系统扫描到尚未复制完成的半个 APK。

AI 不得使用通配符删除文件，不得执行 `rm -rf /data/app`，不得删除无法确认属于 EadoPlay 的文件。

### 5. 验证是否真正安装成功

AI 应检查：

- 包名是否为 `com.eadoplay.carplay`；
- `pm path` 是否能找到正式 APK；
- 实际 `versionName` 和 `versionCode`；
- 应用是否能够正常启动；
- 上传到临时目录的 APK 是否已经清理。

只有这些检查完成，安装才算结束。

---

## 第四部分：安装完成后的操作

## 十三、在车机上检查 EadoPlay

1. 在车机桌面找到 EadoPlay 图标；
2. 点击图标，确认应用能够打开；
3. 进入设置查看版本号；
4. 第一次使用时，按照软件界面完成 iPhone 蓝牙配对和无线连接设置。

图标出现但应用一打开就退出时，应让 AI 抓取启动日志分析。不要反复覆盖 APK。

## 十四、r29 设置自动连接

先完成一次正常的无线连接，然后进入：

```text
EadoPlay → 设置 → 连接 → 自动连接
```

根据需要开启：

- “打开 EadoPlay 时连接”；
- “打开 EadoPlay 时开启车机热点”。

后者仅用于车机热点模式，使用原车已经保存的热点名称和密码。无线日常使用不需要电脑持续连接，也不需要网络 ADB。

## 十五、恢复 USB Host 模式并收尾

确认 EadoPlay 已安装、版本正确并能够打开后：

1. 让 AI 确认 `/data/local/tmp/EadoPlay-new.apk` 等本次临时文件已经删除；
2. 保存本次使用的 APK 文件和版本号；
3. 回到车机拨号界面；
4. 输入：

   ```text
   *#518120#*
   ```

5. 等待车机恢复 USB Host 模式；
6. 断开电脑数据线；
7. 平时不调试时可以关闭 USB 调试，以后升级软件时再开启。

切换回 Host 模式不会卸载已经安装的 EadoPlay。

---

## 第五部分：按发生阶段排查问题

## 十六、电脑准备阶段的问题

### Google 页面打不开

使用第二节“方法 B”的提示词，让能够操作本机终端并访问网络的 AI 尝试从 Google 官方直链下载。如果官方直链仍失败，应更换网络，不要自动使用不明镜像。

### 提示找不到 `adb`

让 AI 使用完整路径：

```text
C:\platform-tools\adb.exe
```

同时检查是否错误解压成：

```text
C:\platform-tools\platform-tools\adb.exe
```

### APK 可以放进 platform-tools 吗

可以，但没有必要。APK 可以放在桌面或下载目录，只要把准确的完整路径提供给 AI。

## 十七、车机连接阶段的问题

### 车机能通电或电脑有反应，但 ADB 看不到设备

依次检查：

1. 是否输入 `*#518121#*` 切换到 Device 模式；
2. USB 调试是否开启；
3. 是否使用双 USB-A 公对公数据线；
4. 线材是否真的支持数据传输；
5. 是否直接插入电脑 USB-A 接口；
6. 是否插错车机 USB 接口；
7. Windows 设备管理器是否存在未识别的 Android 或 ADB 设备；
8. 更换电脑 USB-A 接口后重新执行 `adb devices -l`。

### 设备显示 `unauthorized`

查看车机屏幕，找到 USB 调试授权弹窗，勾选“始终允许”并点击允许。弹窗可能被其他车机界面挡住。

### 设备显示 `offline`

让 AI 重新启动 ADB 服务，再重新插拔数据线。如果仍然离线，检查线材和 USB 模式。

## 十八、安装阶段的问题

### 为什么不使用 U 盘或 `adb install`

未破解的原厂车机会通过厂商鉴权拒绝普通第三方应用安装。APK 位于桌面、U 盘或车机内部存储，并不会改变鉴权结果。因此本文确认工程 ADB 权限后，直接使用 `/data/app` 安装流程。

### `/data/app` 显示 `Permission denied`

停止安装。这通常表示当前 ADB 连接、授权或工程权限状态不符合预期。不要为了继续而 root、修改系统或关闭安全服务。

### 提示签名不一致或版本异常

不要直接卸载。卸载可能清除设置和配对信息。先让 AI 核对旧版和新版的包名、签名及版本，再由用户决定是否备份后卸载。

### 安装后没有立刻出现图标

先等待 10 至 20 秒，让 Android Package Manager 扫描 APK。AI 应使用 `pm path` 和 `dumpsys package` 检查。确认系统仍未登记后，再考虑重启车机。

### 图标存在但应用打不开

让 AI 抓取与 `AndroidRuntime`、`FATAL EXCEPTION`、`EadoPlay` 和 `UnsatisfiedLinkError` 有关的日志。安装成功不代表 APK 一定兼容当前系统或硬件。

---

## 附录一：复制给 AI 的完整安装提示词

把“APK 路径”替换成自己的真实路径，再把整段内容发送给能够操作本机终端的 AI：

```text
我要在我本人拥有并已授权调试的 2018 款长安逸动 Android 4.4.2 原厂车机上安装 EadoPlay。

我已经亲自完成：
1. 电脑上已准备 C:\platform-tools\adb.exe；
2. 已下载 EadoPlay APK；
3. 在车机拨号界面输入 *#518200#*，打开 USB 调试；
4. 输入 *#518121#*，把车机 USB 切换到 Device 模式；
5. 已使用两头均为 USB-A 公头、且支持数据传输的双 USB-A 公对公数据线，将车机直接连接到这台 Windows 电脑；
6. 如果车机出现 USB 调试授权，我会勾选“始终允许”并点击允许。

ADB 路径：
C:\platform-tools\adb.exe

APK 完整路径：
【把这里替换成实际 APK 完整路径】

EadoPlay 包名：
com.eadoplay.carplay

请直接协助我完成检查、安装和验证，并严格遵守以下规则：

1. 先做只读检查，确认 adb.exe 和 APK 文件真实存在，并告诉我结果。
2. 使用 ADB 完整路径运行 adb version 和 adb devices -l。
3. 只有设备状态明确为 device 才继续。若列表为空、显示 unauthorized 或 offline，停止安装并告诉我应该检查什么。
4. 不要执行普通 adb install。该未破解原厂车机会被厂商鉴权拒绝，请直接使用下面的工程 ADB 流程。
5. 运行 adb shell id，记录当前身份，不要擅自 root。
6. 使用名称明确、可立即删除的临时测试文件，检查 /data/app 是否可写。测试文件必须在同一条命令中删除，不要碰其他文件。
7. 如果写入测试返回 Permission denied 或结果不确定，立即停止。禁止 root、解锁 Bootloader、修改 /system、关闭厂商安全服务或使用提权工具。
8. 确认 /data/app 可写后，运行 pm path com.eadoplay.carplay，查询旧版及其准确 APK 路径。
9. 已有版本时，只能把 pm path 返回且明确位于 /data/app/ 的路径作为替换目标。首次安装使用 /data/app/eadoplay.apk。目标不在 /data/app/ 时必须停止。
10. 将电脑 APK 推送到 /data/local/tmp/EadoPlay-new.apk，并确认传输成功。
11. 停止 EadoPlay 进程，把临时目录中的 APK 复制为 /data/app/.eadoplay.apk.tmp。
12. 复制完整后设置本车已验证的所有者 system.system 和权限 0644，再在同一分区原子改名为第 9 步确认的正式目标，并执行 sync。
13. 禁止使用通配符；禁止执行 rm -rf；禁止删除 /data/app 目录；禁止删除其他应用；禁止修改 /system；禁止覆盖任何无法确认属于 EadoPlay 的路径。
14. 未经我明确同意，不要卸载 EadoPlay，不要清除应用数据，不要删除 /data/data/com.eadoplay.carplay。签名或版本异常时先说明影响，等待我决定。
15. 写入后等待 Android Package Manager 扫描。使用 pm path 和 dumpsys package 核对 codePath、versionName 和 versionCode。
16. 验证成功后，只删除 /data/local/tmp/EadoPlay-new.apk 和本次流程创建的残留临时文件，然后执行 sync。
17. 尝试启动 EadoPlay，确认桌面图标和启动结果。启动失败时先抓取相关日志，不要反复覆盖安装。
18. 每完成一个阶段都用通俗中文告诉我结果。实际返回与预期不同就停止分析，不要自动执行更激进或不可逆的命令。
19. 不要输出、上传或保存与本次安装无关的个人数据、账号信息、手机配对数据或密钥。

完成后请汇报：
- ADB 设备状态；
- /data/app 工程权限检查结果；
- 是否完成厂商鉴权绕过安装；
- 最终 APK 路径；
- 实际 versionName 和 versionCode；
- 应用是否成功启动；
- 临时文件是否已经清理。
```

## 附录二：没有能操作终端的 AI 怎么办

如果 AI 只能聊天，不能操作本机终端，它无法真正替你下载或安装。可以：

1. 换用明确支持本地终端操作，并且你已授权访问 Platform-Tools 和 APK 路径的 AI；
2. 找懂 ADB 的人现场协助，并把本文交给对方作为操作边界。

不要把车机调试权限交给来历不明的远程控制软件，也不要运行“一键 root”或“全自动破解”脚本。

## 附录三：最终检查清单

### 电脑准备完成

- [ ] 双 USB-A 公对公数据线支持数据传输；
- [ ] 能操作本机终端的 AI 已打开；
- [ ] `C:\platform-tools\adb.exe` 已存在并可运行；
- [ ] Platform-Tools 来自 Google 官方来源；
- [ ] EadoPlay APK 已下载且未解压；
- [ ] APK 完整路径已复制。

### 车机准备完成

- [ ] 车辆已停稳并保持车机供电；
- [ ] 已输入 `*#518200#*` 并打开 USB 调试；
- [ ] 已输入 `*#518121#*` 并切换 Device 模式；
- [ ] 车机已通过双 USB-A 公对公数据线连接电脑；
- [ ] 已允许这台电脑进行 USB 调试。

### 安装和收尾完成

- [ ] AI 确认设备状态为 `device`；
- [ ] AI 确认 `/data/app` 工程权限正常；
- [ ] AI 已核对实际版本和 APK 路径；
- [ ] EadoPlay 能够正常启动；
- [ ] 本次临时文件已经清理；
- [ ] 已输入 `*#518120#*` 恢复 USB Host 模式；
- [ ] 已安全断开电脑数据线。
