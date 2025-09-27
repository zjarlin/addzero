#!/bin/bash

# Dotfiles CLI 工具启动脚本

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../../.." && pwd)"

# 设置JVM参数
JVM_OPTS="-Xmx512m -Dfile.encoding=UTF-8"

# 设置类路径
# 这里需要根据实际构建结果调整
CLASSPATH="$PROJECT_ROOT/lib/tool-jvm/addzero-tool-cli/build/libs/*:$PROJECT_ROOT/lib/tool-jvm/addzero-tool-cli/build/classes/*"

# 运行CLI工具
java $JVM_OPTS -cp "$CLASSPATH" site.addzero.app.MainKt "$@"