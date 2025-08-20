drop table if exists `emergency_contact`;
create table `emergency_contact` (
	emergency_contact_id	INTEGER PRIMARY KEY AUTOINCREMENT,
	user_info_user_info_id_1	INTEGER	not null,
	name	varchar(512)	not null,
	phone_number	VARCHAR(64)	not null
);insert into	emergency_contact	(user_info_user_info_id_1,emergency_contact_id,name,phone_number)	values	("1","1","李女士","13800138001");
insert into	emergency_contact	(user_info_user_info_id_1,emergency_contact_id,name,phone_number)	values	("2","2","王先生","13900139002");
insert into	emergency_contact	(user_info_user_info_id_1,emergency_contact_id,name,phone_number)	values	("3","3","张女士","13700137003");
insert into	emergency_contact	(user_info_user_info_id_1,emergency_contact_id,name,phone_number)	values	("4","4","赵先生","13600136004");
insert into	emergency_contact	(user_info_user_info_id_1,emergency_contact_id,name,phone_number)	values	("5","5","孙女士","13500135005");
insert into	emergency_contact	(user_info_user_info_id_1,emergency_contact_id,name,phone_number)	values	("6","6","周先生","13400134006");
