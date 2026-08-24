#!/usr/bin/python3
F = '/root/gh-workbench/yunx-ctfile/app/src/main/kotlin/com/yunx/app/ui/MainScreen.kt'
src = open(F, encoding='utf-8').read()

old = """            onDismiss = { showCfileConfig = false }
        )
    }

    // 首次下载引导：加入「忽略电池优化」白名单（锁屏保持下载生效的前提）"""

new = """            onDismiss = { showCfileConfig = false }
        )
    }
    }

    // 首次下载引导：加入「忽略电池优化」白名单（锁屏保持下载生效的前提）"""

if old in src:
    src = src.replace(old, new, 1)
    print('BRACE RESTORED')
else:
    print('!!! NOT FOUND')
open(F, 'w', encoding='utf-8').write(src)