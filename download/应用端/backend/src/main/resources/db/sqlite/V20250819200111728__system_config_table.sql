drop table if exists `system_config`;
create table `system_config` (
	id	INTEGER PRIMARY KEY AUTOINCREMENT,
	name	varchar(512)	not null,
	chinese_name	varchar(512)	,
	description	varchar(512)	,
	content	TEXT	,
	remark	varchar(512)	,
	type	varchar(512)	
);