drop table if exists `activity_recommendation`;
create table `activity_recommendation` (
	activity_recommendation_id	INTEGER PRIMARY KEY AUTOINCREMENT,
	title	varchar(512)	not null,
	activity_time	DATETIME	not null,
	location_latitude	FLOAT	not null,
	location_longitude	FLOAT	not null,
	location_address	varchar(512)	not null,
	suitable_people	varchar(512)	,
	creation_time	DATETIME	
);insert into	activity_recommendation	(activity_time,creation_time,location_address,suitable_people,location_latitude,title,activity_recommendation_id,location_longitude)	values	("2024-06-05 07:30:00.0 00:00:00.0","2024-05-01 08:00:00.0 00:00:00.0","四川省成都市武侯区人民公园","老年人","30.6586","社区太极拳课程","1","104.0665");
insert into	activity_recommendation	(activity_time,creation_time,location_address,suitable_people,location_latitude,title,activity_recommendation_id,location_longitude)	values	("2024-06-06 09:00:00.0 00:00:00.0","2024-05-02 09:00:00.0 00:00:00.0","四川省成都市锦江区文化馆","退休老人","30.6634","老年舞蹈班","2","104.0702");
insert into	activity_recommendation	(activity_time,creation_time,location_address,suitable_people,location_latitude,title,activity_recommendation_id,location_longitude)	values	("2024-06-07 14:00:00.0 00:00:00.0","2024-05-03 10:00:00.0 00:00:00.0","四川省成都市青羊区图书馆","所有老人","30.66","书法兴趣小组","3","104.065");
insert into	activity_recommendation	(activity_time,creation_time,location_address,suitable_people,location_latitude,title,activity_recommendation_id,location_longitude)	values	("2024-06-08 10:00:00.0 00:00:00.0","2024-05-04 11:00:00.0 00:00:00.0","四川省成都市武侯区社区中心","中老年人","30.659","健康讲座","4","104.068");
insert into	activity_recommendation	(activity_time,creation_time,location_address,suitable_people,location_latitude,title,activity_recommendation_id,location_longitude)	values	("2024-06-09 16:00:00.0 00:00:00.0","2024-05-05 12:00:00.0 00:00:00.0","四川省成都市锦江区体育馆","老年人","30.6575","老年游泳班","5","104.067");
insert into	activity_recommendation	(activity_time,creation_time,location_address,suitable_people,location_latitude,title,activity_recommendation_id,location_longitude)	values	("2024-06-10 18:00:00.0 00:00:00.0","2024-05-06 13:00:00.0 00:00:00.0","四川省成都市青羊区广场","退休老人","30.661","广场舞比赛","6","104.069");
