drop table if exists `user_info`;
create table `user_info` (
	user_info_id	INTEGER PRIMARY KEY AUTOINCREMENT,
	phone_number	VARCHAR(64)	not null,
	username	varchar(512)	,
	password	VARCHAR(64)	not null,
	registration_date	DATETIME	,
	last_login_date	DATETIME	
);insert into	user_info	(password,registration_date,phone_number,last_login_date,user_info_id,username)	values	("Passw0rd!23","2023-01-10 08:30:00.0 00:00:00.0","13800138000","2024-06-01 09:00:00.0 00:00:00.0","1","李大爷");
insert into	user_info	(password,registration_date,phone_number,last_login_date,user_info_id,username)	values	("SecurePwd#45","2023-02-15 14:20:00.0 00:00:00.0","13900139001","2024-05-28 18:15:00.0 00:00:00.0","2","王老太");
insert into	user_info	(password,registration_date,phone_number,last_login_date,user_info_id,username)	values	("MyPwd789$","2023-03-05 10:00:00.0 00:00:00.0","13700137002","2024-05-30 16:45:00.0 00:00:00.0","3","张老汉");
insert into	user_info	(password,registration_date,phone_number,last_login_date,user_info_id,username)	values	("Pwd!23456","2023-04-12 11:10:00.0 00:00:00.0","13600136003","2024-05-31 08:30:00.0 00:00:00.0","4","赵奶奶");
insert into	user_info	(password,registration_date,phone_number,last_login_date,user_info_id,username)	values	("Pwd#98765","2023-05-20 09:50:00.0 00:00:00.0","13500135004","2024-06-01 07:55:00.0 00:00:00.0","5","孙叔叔");
insert into	user_info	(password,registration_date,phone_number,last_login_date,user_info_id,username)	values	("Pwd2024$%","2023-06-01 13:00:00.0 00:00:00.0","13400134005","2024-06-02 10:10:00.0 00:00:00.0","6","周阿姨");
