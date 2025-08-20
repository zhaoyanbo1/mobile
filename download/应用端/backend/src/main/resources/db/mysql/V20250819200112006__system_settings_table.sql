drop table if exists `system_settings`;
create table `system_settings` (
	system_settings_id	INT	 not null auto_increment,
	user_info_user_info_id_1	INT	not null,
	reminder_volume	INT	,
	font_size	INT	,
	questionnaire_exported	BOOL	,
	primary key (system_settings_id)
);insert into	system_settings	(user_info_user_info_id_1,reminder_volume,system_settings_id,font_size,questionnaire_exported)	values	("1","80","1","18","true");
insert into	system_settings	(user_info_user_info_id_1,reminder_volume,system_settings_id,font_size,questionnaire_exported)	values	("2","70","2","20","false");
insert into	system_settings	(user_info_user_info_id_1,reminder_volume,system_settings_id,font_size,questionnaire_exported)	values	("3","75","3","18","true");
insert into	system_settings	(user_info_user_info_id_1,reminder_volume,system_settings_id,font_size,questionnaire_exported)	values	("4","60","4","22","false");
insert into	system_settings	(user_info_user_info_id_1,reminder_volume,system_settings_id,font_size,questionnaire_exported)	values	("5","85","5","18","true");
insert into	system_settings	(user_info_user_info_id_1,reminder_volume,system_settings_id,font_size,questionnaire_exported)	values	("6","70","6","19","false");
