### 1.参数加载
exe_hive="/root/apps/hive/bin/hive"
# if [ $# -eq 1 ]
# then
#     day_01=`date --date="${1}" +%Y-%m-%d`
# else
#     day_01=`date -d'-1 day' +%Y-%m-%d`
# fi
day_01=`date +%Y-%m-%d`
syear=`date --date=$day_01 +%Y`
smonth=`date --date=$day_01 +%m`
sday=`date --date=$day_01 +%d`


TARGET_DB=db_weblog
SOURCE_TABLE=ods_weblog_origin
TARGET_TABLE=ods_weblog_detail
i=1

### 2.定义执行HQL
HQL="
insert into table $TARGET_DB.$TARGET_TABLE partition(datestr='$day_01')
select c.valid, c.remote_addr,c.remote_user,c.time_local,
substring(c.time_local, 0, 10) as daystr, 
substring(c.time_local, 12) as tmstr, 
substring(c.time_local, 6, 2) as month, 
substring(c.time_local, 9, 2) as day, 
substring(c.time_local, 11, 3) as hour, 
c.request, c.status, c.body_bytes_sent,c.http_referer,c.ref_host, 
c.ref_path, c.ref_query, c.ref_query_id, c.http_user_agent from 
(select a.valid, a.remote_addr, a.remote_user, a.time_local, a.request, 
a.status, a.body_bytes_sent, a.http_referer, a.http_user_agent, b.ref_host, 
b.ref_path, b.ref_query, b.ref_query_id from $TARGET_DB.$SOURCE_TABLE a 
lateral view parse_url_tuple(a.http_referer, 'HOST', 'PATH', 'QUERY', 'QUERY:id') 
b as ref_host, ref_path, ref_query, ref_query_id) c;
"

echo "------------- Executing query $i ----------------------"
echo $HQL
((i++))

#执行hql
$exe_hive -e "$HQL"

SOURCE_TABLE=ods_weblog_detail
TARGET_TABLE=dw_pvs_referer_h
HQL = "
insert into table $TARGET_DB.$TARGET_TABLE partition(datestr='$day_01')
select http_referer, ref_host, month, day, hour, count(1) as pv_referer_cnt
from $TARGET_DB.$SOURCE_TABLE
group by http_referer, ref_host, month, day, hour
having ref_host is not null
order by hour asc, day asc, month asc, pv_referer_cnt desc;
"

echo "------------- Executing query $i ----------------------"
echo $HQL
((i++))

#执行hql
$exe_hive -e "$HQL"


SOURCE_TABLE=ods_weblog_detail
TARGET_TABLE=dw_ref_host_visit_cnts_h
HQL = "
insert into table $TARGET_DB.$TARGET_TABLE partition(datestr='$day_01')
select ref_host, month, day, hour, count(1) as ref_host_cnts from $TARGET_DB.$SOURCE_TABLE group by ref_host, month, day, hour
having ref_host is not null
order by hour asc, day asc, month asc, ref_host_cnts desc;
"

echo "------------- Executing query $i ----------------------"
echo $HQL
((i++))

#执行hql
$exe_hive -e "$HQL"



SOURCE_TABLE=dw_ref_host_visit_cnts_h
TARGET_TABLE=dw_pvs_refhost_topn_h
HQL = "
insert into table $TARGET_DB.$TARGET_TABLE partition(datestr='$day_01')
select t.hour, t.od, t.ref_host, t.ref_host_cnts from 
(select ref_host, ref_host_cnts, concat(month, day, hour) as hour, 
row_number() over(partition by concat(month, day, hour)order by ref_host_cnts desc) 
as od from $TARGET_DB.$SOURCE_TABLE) t where od <= 3;
"

echo "------------- Executing query $i ----------------------"
echo $HQL
((i++))

#执行hql
$exe_hive -e "$HQL"




SOURCE_TABLE=ods_weblog_detail
TARGET_TABLE=dw_hotpages_pvs_d
HQL = "
insert into table $TARGET_DB.$TARGET_TABLE
select '$day_01', a.request, a.request_counts from 
(select request as request, count(request) as request_counts 
from $TARGET_DB.$SOURCE_TABLE where datestr='$day_01' 
group by request having request is not null) a
order by a.request_counts desc limit 10;
"

echo "------------- Executing query $i ----------------------"
echo $HQL
((i++))

#执行hql
$exe_hive -e "$HQL"




SOURCE_TABLE=ods_weblog_detail
TARGET_TABLE=dw_user_dstc_ip_h
HQL = "
insert into table $TARGET_DB.$TARGET_TABLE
select remote_addr, count(1) as pvs, concat(month, day, hour) as hour
from $TARGET_DB.$SOURCE_TABLE
where datestr = '$day_01'
group by concat(month, day, hour), remote_addr;
"

echo "------------- Executing query $i ----------------------"
echo $HQL
((i++))

#执行hql
$exe_hive -e "$HQL"


SOURCE_TABLE=ods_weblog_detail
TARGET_TABLE=dw_user_dsct_history
HQL = "
insert into table $TARGET_DB.$TARGET_TABLE partition(datestr='$day_01')
select tmp.day as day, tmp.today_addr as new_ip from (
select today.day as day, today.remote_addr as today_addr, old.ip as old_addr 
from (select distinct remote_addr as remote_addr, '$day_01' 
as day from $TARGET_DB.$SOURCE_TABLE where daystr='$day_01') today 
left outer join
$TARGET_DB.$TARGET_TABLE old
on today.remote_addr=old.ip)
tmp
where tmp.old_addr is null;
"

echo "------------- Executing query $i ----------------------"
echo $HQL
((i++))

#执行hql
$exe_hive -e "$HQL"
