create table if not exists db_weblog.ods_weblog_origin(
valid string,
remote_addr string,
remote_user string,
time_local string,
request string,
status string,
body_bytes_sent string,
http_referer string,
http_user_agent string)
partitioned by (datestr string)
row format delimited
fields terminated by '\001';

create table if not exists db_weblog.ods_click_pageviews(
session string,
remote_addr string,
remote_user string,
time_local string,
request string,
visit_step string,
page_duration string,
http_referer string,
http_user_agent string,
body_bytes_sent string,
status string)
partitioned by (datestr string)
row format delimited
fields terminated by '\001';

create table if not exists db_weblog.click_stream_visit(
session string,
remote_addr string,
inTime string,
outTime string,
inPage string,
outPage string,
referal string,
pageVisits int)
partitioned by (datestr string);

drop table ods_weblog_detail;
create table ods_weblog_detail(
valid string,
remote_addr string,
remote_user string,
time_local string,
daystr string,
timestr string,
month string,
day string,
hour string,
request string,
status string,
body_bytes_sent string,
http_referer string,
ref_host string,
ref_path string,
ref_query string,
ref_query_id string,
http_user_agent string
)
partitioned by (datestr string);

create table dw_pvs_referer_h(
referer_url string,
referer_host string,
month string,
day string,
hour string,
pv_referer_cnt bigint
) 
partitioned by (datestr string);

create table dw_ref_host_visit_cnts_h(
ref_host string,
month string,
day string,
hour string,
ref_host_cnts bigint
) 
partitioned by (datestr string);

create table dw_pvs_refhost_topn_h(
hour string,
toporder string,
ref_host string,
ref_host_cnts string
) 
partitioned by (datestr string);


create table dw_hotpages_pvs_d(
day string, 
url string, 
pvs string);

create table dw_user_dstc_ip_h (
remote_addr string,
pvs bigint,
hour string);

create table dw_user_dsct_history(
day string,
ip string)
partitioned by (datestr string);
