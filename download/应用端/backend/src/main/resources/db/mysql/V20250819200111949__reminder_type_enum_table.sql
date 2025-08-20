drop table if exists `reminder_type_enum`;
create table `reminder_type_enum` (
	reminder_type_enum_id	INT	 not null auto_increment,
	type_name	varchar(512)	not null,
	primary key (reminder_type_enum_id)
);insert into	reminder_type_enum	(type_name,reminder_type_enum_id)	values	("用药提醒","1");
insert into	reminder_type_enum	(type_name,reminder_type_enum_id)	values	("活动提醒","2");
insert into	reminder_type_enum	(type_name,reminder_type_enum_id)	values	("饮食提醒","3");
