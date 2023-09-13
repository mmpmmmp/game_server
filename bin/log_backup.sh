#!/bin/bash


APP_HOST=$(hostname)
APP_NAME="nginx"

DATE=$(date "+%Y-%m-%d %H:%M:%S")
echo ""
echo "------------------------------------------------"
echo "[$DATE]"
echo "[$APP_HOST] $APP_NAME log bakcup start"

#S3 Infommation
BASE_DIR=$(dirname $(readlink -f $0))
BUCKET_NAME="log-backup-1256929896"
BACKUP_PATH="wre-prd-01/game-api/nginx/"

#TARGET_YEAR=`date +%Y`
#TARGET_MONTH=`date +%m`
#TARGET_DAY=`date +%d`

clean_list=(
"/home/wre/logs/nginx/:4"
)

stop_backup() {
    echo "[$APP_HOST] $APP_NAME log bakcup stop"
    exit;
}

remove_expired_logs() {
    TARGET_INFO_STR=$1
    TARGET_INFO_ARR=(${TARGET_INFO_STR//:/ })
    TARGET_DIR=${TARGET_INFO_ARR[0]}
    TARGET_DATE=${TARGET_INFO_ARR[1]}

    # check target directory
    if [ ! -d ${TARGET_DIR} ];then
        echo "ERROR : No such directory (TARGET__DIR)"
        stop_backup
    fi

    #check logs files
    CHK_FILE=`find ${TARGET_DIR} -type f -name "gameapi*"  -mtime +${TARGET_DATE} | wc -l`
    if [ ${CHK_FILE} -eq 0 ];then
        echo "INFO : There is no file to backup"
        stop_backup
    fi

    for T_FILE in `find ${TARGET_DIR} -type f -name "gameapi*" -mtime +${TARGET_DATE}`; do
        echo "move $T_FILE to bucket: /${BACKUP_PATH}"
        #aws s3 mv ${T_FILE} s3://${BUCKET_NAME}/${BACKUP_PATH}/${APP_HOST}/${APP_NAME}/${TARGET_YEAR}/${TARGET_MONTH}/${TARGET_DAY}/
	coscmd upload ${T_FILE} /${BACKUP_PATH}
	mv ${T_FILE} /home/wre/logs-backup/
    done
}


#tomcat log
echo "clear tomcat log and backup log"
find /home/wre/logs/tomcat/ -type f -mtime +5 -exec rm -f {} \;
find /home/wre/logs-backup/ -type f -mtime +8 -exec rm -f {} \;
echo "done"


for dirname in "${clean_list[@]}"; do
    echo "$dirname"
    remove_expired_logs $dirname
done

