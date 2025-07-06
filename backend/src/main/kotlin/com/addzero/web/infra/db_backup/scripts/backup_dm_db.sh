#!/bin/bash



dbName=SYSDBA
dbPwd= SYSDBA
dbIp=127.0.0.1
DM_HOME="/data/dameng/dmdbms"
BACKUP_HOME="/data/dameng/backup"
dbPort=5236


# 备份操作
# 获取当前日期作为备份目录名
DATE=$(date +%Y%m%d%H%M%S)
FILENAME="data%U.dmp"
LOG_NAME="data%U.log"
DBURL="$dbName/$dbPwd@$dbIp:$dbPort"
FILE_PATH="$BACKUP_HOME/$DATE"

# 创建备份目录
mkdir -p "$FILE_PATH"

cd "$DM_HOME/bin/"
echo "./dexp "$DBURL" file="$FILE_PATH/$FILENAME" log="$FILE_PATH/$LOG_NAME" FULL=Y FILESIZE=10240m COMPRESS=Y PARALLEL=4 TABLE_PARALLEL=2 TABLE_POOL=3"
./dexp "$DBURL" file="$FILE_PATH/$FILENAME" log="$FILE_PATH/$LOG_NAME" FULL=Y FILESIZE=10240m COMPRESS=Y PARALLEL=4 TABLE_PARALLEL=2 TABLE_POOL=3


# 删除早期文件，防止文件占用资源
# 定义要检查的目录和最大允许的子目录数量
MAX_BACKUP_NUM=5

# 获取子目录数量，排除父目录
subdir_count=$(find "$BACKUP_HOME" -mindepth 1 -maxdepth 1 -type d | wc -l)

# 如果子目录数量大于5
if [ $subdir_count -gt $MAX_BACKUP_NUM ]; then
    # 获取所有子目录并按创建时间排序（升序，最早的在前）
    sorted_dirs=$(find "$BACKUP_HOME" -mindepth 1 -maxdepth 1 -type d -exec stat -c '%w %n' {} + | sort -n)
	echo $sorted_dirs
    # 获取并删除最早的子目录
    oldest_dir=$(echo "$sorted_dirs" | head -n 1 | awk '{print $2}')
    rm -rf "$oldest_dir"
    echo "Deleted the oldest directory: $oldest_dir"
else
    echo "The number of subdirectories is less than or equal to 5."
fi

# 可以选择在备份完成后发送邮件通知等操作
# ...
