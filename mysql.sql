drop table ods_weblog_origin;
create table ods_weblog_origin(
valid varchar(255),
remote_addr varchar(255),
remote_user varchar(255),
time_local varchar(255),
request varchar(255),
status varchar(255),
body_bytes_sent varchar(255),
http_referer varchar(1023),
http_user_agent varchar(255)
);

drop table ods_weblog_detail;
create table ods_weblog_detail
(
valid           varchar(255),
remote_addr     varchar(255),
remote_user     varchar(255),
time_local      varchar(255),
daystr          varchar(255),
timestr         varchar(255),
month           varchar(255),
day             varchar(255),
hour            varchar(255),
request         varchar(255),
status          varchar(255),
body_bytes_sent varchar(255),
http_referer    varchar(1023),
ref_host        varchar(255),
ref_path        varchar(255),
ref_query       varchar(1023),
ref_query_id    varchar(255),
http_user_agent varchar(255)
 );

drop table ods_click_pageviews;
create table ods_click_pageviews
(
session         varchar(255),
remote_addr     varchar(255),
remote_user     varchar(255),
time_local      varchar(255),
request         varchar(255),
visit_step      varchar(255),
page_duration   varchar(255),
http_referer    varchar(1023),
http_user_agent varchar(255),
body_bytes_sent varchar(255),
status          varchar(255)
);

create table dw_user_dsct_history (
day varchar(255),
ip         varchar(255)
);

create table dw_pvs_refhost_topn_h (
hour      varchar(255),
toporder  varchar(255),
ref_host  varchar(255),
ref_host_cnts  varchar(255)
);

create table dw_pvs_referer_h
(
referer_url  varchar(1023),
referer_host varchar(255),
month        varchar(255),
day          varchar(255),
hour         varchar(255),
pv_referer_cnt bigint
);

create table dw_hotpages_pvs_d
(
day varchar(255),
url varchar(255),
pvs varchar(255)
);

create table click_stream_visit 
(
sessionvarchar(255),
remote_addrvarchar(255),
intime     varchar(255),
outtime    varchar(255),
inpage     varchar(255),
outpage    varchar(255),
referal    varchar(1023),
pagevisits  int
);
