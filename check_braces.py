#!/usr/bin/python3
F = '/root/gh-workbench/yunx-ctfile/app/src/main/kotlin/com/yunx/app/ui/MainScreen.kt'
lines = open(F, encoding='utf-8').read().split('\n')

depth = 0
in_block_comment = False
min_line = None
for i, line in enumerate(lines, 1):
    j = 0
    n = len(line)
    while j < n:
        c = line[j]
        if in_block_comment:
            if line.startswith('*/', j):
                in_block_comment = False
                j += 2
            else:
                j += 1
            continue
        if c == '/' and j + 1 < n and line[j+1] == '/':
            break  # 行注释
        if c == '/' and j + 1 < n and line[j+1] == '*':
            in_block_comment = True
            j += 2
            continue
        if c == '"':
            # 跳过字符串（含转义）
            j += 1
            while j < n:
                if line[j] == '\\':
                    j += 2
                    continue
                if line[j] == '"':
                    break
                j += 1
            j += 1
            continue
        if c == "'":
            j += 1
            while j < n:
                if line[j] == '\\':
                    j += 2
                    continue
                if line[j] == "'":
                    break
                j += 1
            j += 1
            continue
        if c == '{':
            depth += 1
        elif c == '}':
            depth -= 1
            if depth < 0 and min_line is None:
                min_line = i
        j += 1
    # 记录每50行的深度快照
    if i % 100 == 0 or i == len(lines):
        print(f'line {i}: depth={depth}')

print('FINAL depth =', depth)
print('negative at line:', min_line)