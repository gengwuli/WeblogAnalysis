#!/bin/bash
cur_date=`date +%Y-%m-%d`

mysql_connect=jdbc:mysql://172.16.249.139/db_weblog
mysql_db_username=root
mysql_db_password=root

for i in ods_weblog_detail ods_click_pageviews \
dw_user_dstc_ip_h dw_user_dsct_history dw_ref_host_visit_cnts_h \
dw_pvs_refhost_topn_h dw_pvs_referer_h dw_hotpages_pvs_d click_stream_visit; do
	echo "-----------------Exporting $i ---------------------"
	table_name=$i
	hdfs_dir=/user/hive/warehouse/db_weblog.db/$i/datestr=$cur_date/
	$SQOOP_HOME/bin/sqoop export \
	--connect "$mysql_connect" \
	--username "$mysql_db_username" \
	--password "$mysql_db_password" \
	--table "$table_name" \
	--fields-terminated-by '\001' \
	--export-dir $hdfs_dir 
done
