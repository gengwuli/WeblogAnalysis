#!/bin/bash

#set java env
export JAVA_HOME=/usr/local/jdk1.8.0_101
export JRE_HOME=${JAVA_HOME}/jre
export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib
export PATH=${JAVA_HOME}/bin:$PATH

#set hadoop env
export HADOOP_HOME=/root/apps/hadoop-2.7.3
export PATH=${HADOOP_HOME}/bin:${HADOOP_HOME}/sbin:$PATH



#flume采集生成的日志文件存放的目录
log_flume_dir=/data/flumedata

#预处理程序的工作目录
log_pre_input=/data/weblog/preprocess/input


#获取时间信息
#采集昨天的数据
# day_01=`date -d'-1 day' +%Y-%m-%d`
day_01=`date +%Y-%m-%d`
syear=`date --date=$day_01 +%Y`
smonth=`date --date=$day_01 +%m`
sday=`date --date=$day_01 +%d`


#读取日志文件的目录，判断是否有需要上传的文件 wc count 条目
files=`hadoop fs -ls $log_flume_dir | grep $day_01 | wc -l`
if [ $files -gt 0 ]; then
hdfs dfs -mkdir -p ${log_pre_input} 
hadoop fs -mv ${log_flume_dir}/${day_01} ${log_pre_input}
echo "success moved ${log_flume_dir}/${day_01} to ${log_pre_input} ....."
fi

