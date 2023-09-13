#!/bin/bash

#Local Information
APP_NAME="game-api"
BACKUP_PATH="/data/GameServer/backups/ROOT"
TOMCAT_PATH="/data/GameServer/apache-tomcat-8.5.53"
TEMP_PATH="/data/GameServer/temp"
CUR_DATE=$(date +%Y%m%d_%H%M%S)
WAR_FILE="wechat-game-api-0.0.1-SNAPSHOT.war"


#Deploy war file check

#Temp directory check and make
if [ ! -d $TEMP_PATH ]
then
  echo "make temp directory : $TEMP_PATH"
  mkdir $TEMP_PATH
fi

#Clean temp directory
echo "clean temp directory : ${TEMP_PATH}"
rm -rf ${TEMP_PATH}/*

#Copy war file to temp directory
echo "download new app for temp"
cp /data/GameServer/${WAR_FILE} ${TEMP_PATH}/

if [ ! -e ${TEMP_PATH}/${WAR_FILE} ]
then
  echo "copy fail. not exist war file [${TEMP_PATH}/${WAR_FILE}]"
  echo "check the file ${WAR_FILE} is exist in path /data/GameServer/${WAR_FILE}"
fi

#Xxtract war file
echo "unzip ${TEMP_PATH}/${WAR_FILE} file to ${TEMP_PATH}/ROOT"
mkdir ${TEMP_PATH}/ROOT
unzip ${TEMP_PATH}/${WAR_FILE} -d ${TEMP_PATH}/ROOT > /dev/null 2>&1



#backup old app
echo "backup old app : ${BACKUP_PATH}"
if [ ! -d ${BACKUP_PATH} ]
then
  echo "mkdir back path : ${BACKUP_PATH}"
  mkdir -p ${BACKUP_PATH}
fi

rm -rf `ls ${BACKUP_PATH} -t  |tail -n +11`
mkdir ${BACKUP_PATH}/${CUR_DATE}
cp -r ${TOMCAT_PATH}/webapps/ROOT/* ${BACKUP_PATH}/${CUR_DATE}/

#service stop
echo "stop service ${APP_NAME}"
sh $TOMCAT_PATH/bin/shutdown.sh

#sleep 1 sec
sleep 3

#temp new app
echo "temp new app : ${TEMP_PATH}/ROOT >> ${TOMCAT_PATH}/ROOT"
rm -rf ${TOMCAT_PATH}/webapps/ROOT
mv ${TEMP_PATH}/ROOT ${TOMCAT_PATH}/webapps/ROOT

#service start
echo "start service ${APP_NAME}"
sh $TOMCAT_PATH/bin/startup.sh