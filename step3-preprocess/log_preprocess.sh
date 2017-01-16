#!/bin/bash

#
# ===========================================================================
# program name		:     
# function			:     move files from flume to hdfs for preprocess
# input parameter	:     running date
# source path 		:     /data/weblog/preprocess/input/2017-01-15  
# target path 		:     /data/weblog/preprocess/output/2017-01-15  
#   author			:     Gengwu
# date				:	  2017-1-15
# version			:     v1.0
# code review		:     
# changer			:
# change date		:
# change reason		:
# change list		: 
# ===========================================================================



#set java env
export JAVA_HOME=/usr/local/jdk1.8.0_101
export JRE_HOME=${JAVA_HOME}/jre
export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib
export PATH=${JAVA_HOME}/bin:$PATH

#set hadoop env
export HADOOP_HOME=/root/apps/hadoop-2.7.3
export PATH=${HADOOP_HOME}/bin:${HADOOP_HOME}/sbin:$PATH


#清洗程序类名
preprocess_class="preprocess.WeblogPreprocess"

#待处理日志存放的目录
log_pre_input=/data/weblog/preprocess/input

#预处理输出结果(raw)目录
log_pre_output=/data/weblog/preprocess/output


#获取时间信息
# day_01=`date -d'-1 day' +%Y-%m-%d`
day_01=`date +%Y-%m-%d`
syear=`date --date=$day_01 +%Y`
smonth=`date --date=$day_01 +%m`
sday=`date --date=$day_01 +%d`


#读取日志文件的目录，判断是否有当日待处理的目录
files=`hadoop fs -ls $log_pre_input | grep $day_01 | wc -l`
if [ $files -gt 0 ]; then
hdfs dfs -rm -r $log_pre_output
#提交mr任务job运行
echo "running..    hadoop jar weblog.jar $preprocess_class $log_pre_input/$day_01 /$log_pre_output/$day_01"
hadoop jar weblog.jar $preprocess_class $log_pre_input/$day_01 $log_pre_output/$day_01
fi

#如果失败
#发送邮件或短信，人为来干预



