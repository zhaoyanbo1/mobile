drop table if exists `reminder_item`;
create table `reminder_item` (
	reminder_item_id	INTEGER PRIMARY KEY AUTOINCREMENT,
	user_info_user_info_id_1	INTEGER	not null,
	reminder_type_enum_id	INTEGER	not null,
	title	varchar(512)	not null,
	description	varchar(512)	,
	reminder_time	DATETIME	not null,
	is_completed	BOOL	not null,
	medicine_photo_resource_key	varchar(255)		default	'medicine_photo_resource_key',
	medicine_dosage	varchar(512)	,
	location_latitude	FLOAT	,
	location_longitude	FLOAT	,
	location_address	varchar(512)	,
	diet_recipe_id	INTEGER	,
	creation_time	DATETIME	,
	update_time	DATETIME	
);insert into	reminder_item	(creation_time,reminder_time,medicine_dosage,update_time,user_info_user_info_id_1,reminder_item_id,description,reminder_type_enum_id,title,is_completed)	values	("2024-05-30 07:00:00.0 00:00:00.0","2024-06-03 07:00:00.0 00:00:00.0","1片","2024-05-30 07:00:00.0 00:00:00.0","1","1","服用降压药1片","1","早晨服用降压药","false");
insert into	reminder_item	(creation_time,reminder_time,update_time,user_info_user_info_id_1,reminder_item_id,description,diet_recipe_id,reminder_type_enum_id,title,is_completed)	values	("2024-05-28 12:00:00.0 00:00:00.0","2024-06-03 12:00:00.0 00:00:00.0","2024-05-28 12:00:00.0 00:00:00.0","2","2","推荐低糖食谱","1","3","午餐低糖饮食","false");
insert into	reminder_item	(creation_time,location_address,reminder_time,user_info_user_info_id_1,location_latitude,description,title,is_completed,update_time,reminder_item_id,reminder_type_enum_id,location_longitude)	values	("2024-05-30 18:00:00.0 00:00:00.0","四川省成都市武侯区人民公园","2024-06-03 18:00:00.0 00:00:00.0","3","30.6586","参加社区公园太极活动","社区太极活动","false","2024-05-30 18:00:00.0 00:00:00.0","3","2","104.0665");
insert into	reminder_item	(creation_time,reminder_time,medicine_dosage,update_time,user_info_user_info_id_1,reminder_item_id,description,reminder_type_enum_id,title,is_completed)	values	("2024-05-31 20:00:00.0 00:00:00.0","2024-06-03 20:00:00.0 00:00:00.0","2次","2024-05-31 20:00:00.0 00:00:00.0","4","4","吸入剂2次","1","晚上吸入哮喘药","false");
insert into	reminder_item	(creation_time,reminder_time,medicine_dosage,update_time,user_info_user_info_id_1,reminder_item_id,description,reminder_type_enum_id,title,is_completed)	values	("2024-06-01 07:00:00.0 00:00:00.0","2024-06-03 07:00:00.0 00:00:00.0","1片","2024-06-01 07:00:00.0 00:00:00.0","5","5","服用阿司匹林1片","1","早晨服用心脏药","true");
insert into	reminder_item	(creation_time,reminder_time,update_time,user_info_user_info_id_1,reminder_item_id,description,diet_recipe_id,reminder_type_enum_id,title,is_completed)	values	("2024-06-02 19:00:00.0 00:00:00.0","2024-06-03 19:00:00.0 00:00:00.0","2024-06-02 19:00:00.0 00:00:00.0","6","6","推荐低脂食谱","2","3","晚餐低脂饮食","false");
