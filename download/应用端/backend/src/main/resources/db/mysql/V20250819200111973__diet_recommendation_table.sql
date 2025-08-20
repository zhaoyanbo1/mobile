drop table if exists `diet_recommendation`;
create table `diet_recommendation` (
	diet_recommendation_id	INT	 not null auto_increment,
	title	varchar(512)	not null,
	difficulty	varchar(512)	,
	required_time	varchar(512)	,
	creation_time	DATETIME	,
	primary key (diet_recommendation_id)
);insert into	diet_recommendation	(creation_time,difficulty,diet_recommendation_id,required_time,title)	values	("2024-05-01 08:00:00","简单","1","30分钟","低糖杂粮粥");
insert into	diet_recommendation	(creation_time,difficulty,diet_recommendation_id,required_time,title)	values	("2024-05-02 09:00:00","中等","2","45分钟","清蒸鲈鱼");
insert into	diet_recommendation	(creation_time,difficulty,diet_recommendation_id,required_time,title)	values	("2024-05-03 10:00:00","简单","3","15分钟","蔬菜沙拉");
insert into	diet_recommendation	(creation_time,difficulty,diet_recommendation_id,required_time,title)	values	("2024-05-04 11:00:00","中等","4","60分钟","低盐鸡汤");
insert into	diet_recommendation	(creation_time,difficulty,diet_recommendation_id,required_time,title)	values	("2024-05-05 12:00:00","简单","5","10分钟","燕麦水果碗");
insert into	diet_recommendation	(creation_time,difficulty,diet_recommendation_id,required_time,title)	values	("2024-05-06 13:00:00","中等","6","40分钟","红烧茄子");
