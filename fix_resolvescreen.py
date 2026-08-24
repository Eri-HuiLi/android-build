#!/usr/bin/python3
F = '/root/gh-workbench/yunx-ctfile/app/src/main/kotlin/com/yunx/app/ui/screens/ResolveScreen.kt'
src = open(F, encoding='utf-8').read()

old = """    SharePlatform.PAN123 -> "123云盘"
}"""

new = """    SharePlatform.PAN123 -> "123云盘"
    SharePlatform.CTFILE -> "城通网盘"
}"""

if old in src:
    src = src.replace(old, new, 1)
    print('CTFILE LABEL ADDED')
else:
    print('!!! NOT FOUND')
open(F, 'w', encoding='utf-8').write(src)