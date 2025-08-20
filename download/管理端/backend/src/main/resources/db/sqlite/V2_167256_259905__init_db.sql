drop table if exists `system_config`;
create table `system_config` (
 	 `id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `name` 	 varchar(512) not null  ,
	 `chinese_name` 	 varchar(512)   ,
	 `description` 	 varchar(512)   ,
	 `content` 	 TEXT   ,
	 `remark` 	 varchar(512)   ,
	 `type` 	 varchar(512)   
 );


drop table if exists `user_info`;
create table `user_info` (
 	 `user_info_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `phone_number` 	 VARCHAR(64) not null  ,
	 `username` 	 varchar(512)   ,
	 `password` 	 VARCHAR(64) not null  ,
	 `registration_date` 	 DATETIME   ,
	 `last_login_date` 	 DATETIME   
 );
insert into `user_info` (`phone_number`, `username`, `password`, `registration_date`, `last_login_date` ) values ("13800138000","李大爷","Passw0rd!23","2025-01-01 00:00:00","2025-01-01 00:00:00" );
insert into `user_info` (`phone_number`, `username`, `password`, `registration_date`, `last_login_date` ) values ("13900139001","王老太","SecurePwd#45","2025-01-01 00:00:00","2025-01-01 00:00:00" );
insert into `user_info` (`phone_number`, `username`, `password`, `registration_date`, `last_login_date` ) values ("13700137002","张老汉","MyPwd789$","2025-01-01 00:00:00","2025-01-01 00:00:00" );
insert into `user_info` (`phone_number`, `username`, `password`, `registration_date`, `last_login_date` ) values ("13600136003","赵奶奶","Pwd!23456","2025-01-01 00:00:00","2025-01-01 00:00:00" );
insert into `user_info` (`phone_number`, `username`, `password`, `registration_date`, `last_login_date` ) values ("13500135004","孙叔叔","Pwd#98765","2025-01-01 00:00:00","2025-01-01 00:00:00" );
insert into `user_info` (`phone_number`, `username`, `password`, `registration_date`, `last_login_date` ) values ("13400134005","周阿姨","Pwd2024$%","2025-01-01 00:00:00","2025-01-01 00:00:00" );

drop table if exists `health_questionnaire`;
create table `health_questionnaire` (
 	 `health_questionnaire_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `user_info_user_info_id_1` 	 INTEGER not null  ,
	 `name` 	 varchar(512) not null  ,
	 `age` 	 INTEGER not null  ,
	 `residence` 	 varchar(512)   ,
	 `chronic_disease` 	 varchar(512)   ,
	 `allergy_history` 	 varchar(512)   ,
	 `common_medication` 	 varchar(512)   ,
	 `diet_preference` 	 varchar(512)   ,
	 `exercise_frequency` 	 varchar(512)   ,
	 `creation_time` 	 DATETIME   ,
	 `update_time` 	 DATETIME  DEFAULT (CURRENT_TIMESTAMP) ,
	 `version` 	 INTEGER   
 );
insert into `health_questionnaire` (`user_info_user_info_id_1`, `name`, `age`, `residence`, `chronic_disease`, `allergy_history`, `common_medication`, `diet_preference`, `exercise_frequency`, `creation_time`, `update_time`, `version` ) values ("1","李大爷","68","独居","高血压","花粉过敏","降压药","清淡","每天","2025-01-01 00:00:00","2025-01-01 00:00:00","3" );
insert into `health_questionnaire` (`user_info_user_info_id_1`, `name`, `age`, `residence`, `chronic_disease`, `allergy_history`, `common_medication`, `diet_preference`, `exercise_frequency`, `creation_time`, `update_time`, `version` ) values ("2","王老太","72","与家人同住","糖尿病","无","胰岛素","低糖","每周3次","2025-01-01 00:00:00","2025-01-01 00:00:00","2" );
insert into `health_questionnaire` (`user_info_user_info_id_1`, `name`, `age`, `residence`, `chronic_disease`, `allergy_history`, `common_medication`, `diet_preference`, `exercise_frequency`, `creation_time`, `update_time`, `version` ) values ("3","张老汉","75","与配偶同住","关节炎","青霉素","止痛药","高蛋白","偶尔","2025-01-01 00:00:00","2025-01-01 00:00:00","1" );
insert into `health_questionnaire` (`user_info_user_info_id_1`, `name`, `age`, `residence`, `chronic_disease`, `allergy_history`, `common_medication`, `diet_preference`, `exercise_frequency`, `creation_time`, `update_time`, `version` ) values ("4","赵奶奶","70","养老院","无","无","无","低盐","每天","2025-01-01 00:00:00","2025-01-01 00:00:00","1" );
insert into `health_questionnaire` (`user_info_user_info_id_1`, `name`, `age`, `residence`, `chronic_disease`, `allergy_history`, `common_medication`, `diet_preference`, `exercise_frequency`, `creation_time`, `update_time`, `version` ) values ("5","孙叔叔","65","与子女同住","冠心病","阿司匹林","心脏药","均衡","每周5次","2025-01-01 00:00:00","2025-01-01 00:00:00","2" );
insert into `health_questionnaire` (`user_info_user_info_id_1`, `name`, `age`, `residence`, `chronic_disease`, `allergy_history`, `common_medication`, `diet_preference`, `exercise_frequency`, `creation_time`, `update_time`, `version` ) values ("6","周阿姨","69","独居","哮喘","尘螨","吸入剂","低脂","每天","2025-01-01 00:00:00","2025-01-01 00:00:00","1" );

drop table if exists `reminder_item`;
create table `reminder_item` (
 	 `reminder_item_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `user_info_user_info_id_1` 	 INTEGER not null  ,
	 `reminder_type_enum_id` 	 INTEGER not null  ,
	 `title` 	 varchar(512) not null  ,
	 `description` 	 varchar(512)   ,
	 `reminder_time` 	 DATETIME not null  ,
	 `is_completed` 	 BOOL not null  ,
	 `medicine_photo_resource_key` 	 VARCHAR(255)   ,
	 `medicine_dosage` 	 varchar(512)   ,
	 `location_latitude` 	 FLOAT   ,
	 `location_longitude` 	 FLOAT   ,
	 `location_address` 	 varchar(512)   ,
	 `diet_recipe_id` 	 INTEGER   ,
	 `creation_time` 	 DATETIME   ,
	 `update_time` 	 DATETIME  DEFAULT (CURRENT_TIMESTAMP) 
 );
insert into `reminder_item` (`user_info_user_info_id_1`, `reminder_type_enum_id`, `title`, `description`, `reminder_time`, `is_completed`, `medicine_photo_resource_key`, `medicine_dosage`, `location_latitude`, `location_longitude`, `location_address`, `diet_recipe_id`, `creation_time`, `update_time` ) values ("1","1","早晨服用降压药","服用降压药1片","2025-01-01 00:00:00","","0","1片","","","","","2025-01-01 00:00:00","2025-01-01 00:00:00" );
insert into `static_resources` (`resource_path`, `related_table_name`, `related_table_key`, `relate_table_column_name`) values ('https://www.codeflying.net/preview/medicine_photo.jpg','reminder_item', '1', 'medicine_photo');
insert into `reminder_item` (`user_info_user_info_id_1`, `reminder_type_enum_id`, `title`, `description`, `reminder_time`, `is_completed`, `medicine_photo_resource_key`, `medicine_dosage`, `location_latitude`, `location_longitude`, `location_address`, `diet_recipe_id`, `creation_time`, `update_time` ) values ("2","3","午餐低糖饮食","推荐低糖食谱","2025-01-01 00:00:00","","0","","","","","1","2025-01-01 00:00:00","2025-01-01 00:00:00" );
insert into `reminder_item` (`user_info_user_info_id_1`, `reminder_type_enum_id`, `title`, `description`, `reminder_time`, `is_completed`, `medicine_photo_resource_key`, `medicine_dosage`, `location_latitude`, `location_longitude`, `location_address`, `diet_recipe_id`, `creation_time`, `update_time` ) values ("3","2","社区太极活动","参加社区公园太极活动","2025-01-01 00:00:00","","0","","30.6586","104.0665","四川省成都市武侯区人民公园","","2025-01-01 00:00:00","2025-01-01 00:00:00" );
insert into `reminder_item` (`user_info_user_info_id_1`, `reminder_type_enum_id`, `title`, `description`, `reminder_time`, `is_completed`, `medicine_photo_resource_key`, `medicine_dosage`, `location_latitude`, `location_longitude`, `location_address`, `diet_recipe_id`, `creation_time`, `update_time` ) values ("4","1","晚上吸入哮喘药","吸入剂2次","2025-01-01 00:00:00","","0","2次","","","","","2025-01-01 00:00:00","2025-01-01 00:00:00" );
insert into `static_resources` (`resource_path`, `related_table_name`, `related_table_key`, `relate_table_column_name`) values ('https://www.codeflying.net/preview/medicine_photo.jpg','reminder_item', '4', 'medicine_photo');
insert into `reminder_item` (`user_info_user_info_id_1`, `reminder_type_enum_id`, `title`, `description`, `reminder_time`, `is_completed`, `medicine_photo_resource_key`, `medicine_dosage`, `location_latitude`, `location_longitude`, `location_address`, `diet_recipe_id`, `creation_time`, `update_time` ) values ("5","1","早晨服用心脏药","服用阿司匹林1片","2025-01-01 00:00:00","True","0","1片","","","","","2025-01-01 00:00:00","2025-01-01 00:00:00" );
insert into `static_resources` (`resource_path`, `related_table_name`, `related_table_key`, `relate_table_column_name`) values ('https://www.codeflying.net/preview/medicine_photo.jpg','reminder_item', '5', 'medicine_photo');
insert into `reminder_item` (`user_info_user_info_id_1`, `reminder_type_enum_id`, `title`, `description`, `reminder_time`, `is_completed`, `medicine_photo_resource_key`, `medicine_dosage`, `location_latitude`, `location_longitude`, `location_address`, `diet_recipe_id`, `creation_time`, `update_time` ) values ("6","3","晚餐低脂饮食","推荐低脂食谱","2025-01-01 00:00:00","","0","","","","","2","2025-01-01 00:00:00","2025-01-01 00:00:00" );

drop table if exists `reminder_type_enum`;
create table `reminder_type_enum` (
 	 `reminder_type_enum_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `type_name` 	 varchar(512) not null  
 );
insert into `reminder_type_enum` (`type_name` ) values ("用药提醒" );
insert into `reminder_type_enum` (`type_name` ) values ("活动提醒" );
insert into `reminder_type_enum` (`type_name` ) values ("饮食提醒" );

drop table if exists `activity_recommendation`;
create table `activity_recommendation` (
 	 `activity_recommendation_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `title` 	 varchar(512) not null  ,
	 `activity_time` 	 DATETIME not null  ,
	 `location_latitude` 	 FLOAT not null  ,
	 `location_longitude` 	 FLOAT not null  ,
	 `location_address` 	 varchar(512) not null  ,
	 `suitable_people` 	 varchar(512)   ,
	 `creation_time` 	 DATETIME   
 );
insert into `activity_recommendation` (`title`, `activity_time`, `location_latitude`, `location_longitude`, `location_address`, `suitable_people`, `creation_time` ) values ("社区太极拳课程","2025-01-01 00:00:00","30.6586","104.0665","四川省成都市武侯区人民公园","老年人","2025-01-01 00:00:00" );
insert into `activity_recommendation` (`title`, `activity_time`, `location_latitude`, `location_longitude`, `location_address`, `suitable_people`, `creation_time` ) values ("老年舞蹈班","2025-01-01 00:00:00","30.6634","104.0702","四川省成都市锦江区文化馆","退休老人","2025-01-01 00:00:00" );
insert into `activity_recommendation` (`title`, `activity_time`, `location_latitude`, `location_longitude`, `location_address`, `suitable_people`, `creation_time` ) values ("书法兴趣小组","2025-01-01 00:00:00","30.66","104.065","四川省成都市青羊区图书馆","所有老人","2025-01-01 00:00:00" );
insert into `activity_recommendation` (`title`, `activity_time`, `location_latitude`, `location_longitude`, `location_address`, `suitable_people`, `creation_time` ) values ("健康讲座","2025-01-01 00:00:00","30.659","104.068","四川省成都市武侯区社区中心","中老年人","2025-01-01 00:00:00" );
insert into `activity_recommendation` (`title`, `activity_time`, `location_latitude`, `location_longitude`, `location_address`, `suitable_people`, `creation_time` ) values ("老年游泳班","2025-01-01 00:00:00","30.6575","104.067","四川省成都市锦江区体育馆","老年人","2025-01-01 00:00:00" );
insert into `activity_recommendation` (`title`, `activity_time`, `location_latitude`, `location_longitude`, `location_address`, `suitable_people`, `creation_time` ) values ("广场舞比赛","2025-01-01 00:00:00","30.661","104.069","四川省成都市青羊区广场","退休老人","2025-01-01 00:00:00" );

drop table if exists `diet_recommendation`;
create table `diet_recommendation` (
 	 `diet_recommendation_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `title` 	 varchar(512) not null  ,
	 `difficulty` 	 varchar(512)   ,
	 `required_time` 	 varchar(512)   ,
	 `creation_time` 	 DATETIME   
 );
insert into `diet_recommendation` (`title`, `difficulty`, `required_time`, `creation_time` ) values ("低糖杂粮粥","简单","30分钟","2025-01-01 00:00:00" );
insert into `diet_recommendation` (`title`, `difficulty`, `required_time`, `creation_time` ) values ("清蒸鲈鱼","中等","45分钟","2025-01-01 00:00:00" );
insert into `diet_recommendation` (`title`, `difficulty`, `required_time`, `creation_time` ) values ("蔬菜沙拉","简单","15分钟","2025-01-01 00:00:00" );
insert into `diet_recommendation` (`title`, `difficulty`, `required_time`, `creation_time` ) values ("低盐鸡汤","中等","60分钟","2025-01-01 00:00:00" );
insert into `diet_recommendation` (`title`, `difficulty`, `required_time`, `creation_time` ) values ("燕麦水果碗","简单","10分钟","2025-01-01 00:00:00" );
insert into `diet_recommendation` (`title`, `difficulty`, `required_time`, `creation_time` ) values ("红烧茄子","中等","40分钟","2025-01-01 00:00:00" );

drop table if exists `medicine_recommendation`;
create table `medicine_recommendation` (
 	 `medicine_recommendation_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `title` 	 varchar(512) not null  ,
	 `usage_guide` 	 varchar(512)   ,
	 `nearby_pharmacy_info` 	 varchar(512)   ,
	 `creation_time` 	 DATETIME   
 );
insert into `medicine_recommendation` (`title`, `usage_guide`, `nearby_pharmacy_info`, `creation_time` ) values ("降压药A","每日早晨服用1片，避免空腹。注意监测血压变化。","成都市武侯区药店A库存充足","2025-01-01 00:00:00" );
insert into `medicine_recommendation` (`title`, `usage_guide`, `nearby_pharmacy_info`, `creation_time` ) values ("胰岛素注射液","每日注射，根据血糖水平调整剂量。注意保存于冰箱。","成都市锦江区药店B有货","2025-01-01 00:00:00" );
insert into `medicine_recommendation` (`title`, `usage_guide`, `nearby_pharmacy_info`, `creation_time` ) values ("止痛药B","按医嘱服用，避免长期大量使用。","成都市青羊区药店C库存有限","2025-01-01 00:00:00" );
insert into `medicine_recommendation` (`title`, `usage_guide`, `nearby_pharmacy_info`, `creation_time` ) values ("吸入剂C","哮喘发作时使用，每次2吸。","成都市武侯区药店D有货","2025-01-01 00:00:00" );
insert into `medicine_recommendation` (`title`, `usage_guide`, `nearby_pharmacy_info`, `creation_time` ) values ("阿司匹林","预防血栓形成，每日早晨服用1片。","成都市锦江区药店E库存充足","2025-01-01 00:00:00" );
insert into `medicine_recommendation` (`title`, `usage_guide`, `nearby_pharmacy_info`, `creation_time` ) values ("维生素D","每日补充，促进钙吸收。","成都市青羊区药店F有货","2025-01-01 00:00:00" );

drop table if exists `emergency_contact`;
create table `emergency_contact` (
 	 `emergency_contact_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `user_info_user_info_id_1` 	 INTEGER not null  ,
	 `name` 	 varchar(512) not null  ,
	 `phone_number` 	 VARCHAR(64) not null  
 );
insert into `emergency_contact` (`user_info_user_info_id_1`, `name`, `phone_number` ) values ("1","李女士","13800138001" );
insert into `emergency_contact` (`user_info_user_info_id_1`, `name`, `phone_number` ) values ("2","王先生","13900139002" );
insert into `emergency_contact` (`user_info_user_info_id_1`, `name`, `phone_number` ) values ("3","张女士","13700137003" );
insert into `emergency_contact` (`user_info_user_info_id_1`, `name`, `phone_number` ) values ("4","赵先生","13600136004" );
insert into `emergency_contact` (`user_info_user_info_id_1`, `name`, `phone_number` ) values ("5","孙女士","13500135005" );
insert into `emergency_contact` (`user_info_user_info_id_1`, `name`, `phone_number` ) values ("6","周先生","13400134006" );

drop table if exists `system_settings`;
create table `system_settings` (
 	 `system_settings_id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `user_info_user_info_id_1` 	 INTEGER not null  ,
	 `reminder_volume` 	 INTEGER   ,
	 `font_size` 	 INTEGER   ,
	 `questionnaire_exported` 	 BOOL   
 );
insert into `system_settings` (`user_info_user_info_id_1`, `reminder_volume`, `font_size`, `questionnaire_exported` ) values ("1","80","18","True" );
insert into `system_settings` (`user_info_user_info_id_1`, `reminder_volume`, `font_size`, `questionnaire_exported` ) values ("2","70","20","" );
insert into `system_settings` (`user_info_user_info_id_1`, `reminder_volume`, `font_size`, `questionnaire_exported` ) values ("3","75","18","True" );
insert into `system_settings` (`user_info_user_info_id_1`, `reminder_volume`, `font_size`, `questionnaire_exported` ) values ("4","60","22","" );
insert into `system_settings` (`user_info_user_info_id_1`, `reminder_volume`, `font_size`, `questionnaire_exported` ) values ("5","85","18","True" );
insert into `system_settings` (`user_info_user_info_id_1`, `reminder_volume`, `font_size`, `questionnaire_exported` ) values ("6","70","19","" );

drop table if exists `dynamic_api_setting`;
create table `dynamic_api_setting` (
 	 `id` 	 INTEGER PRIMARY KEY AUTOINCREMENT ,
	 `key_name` 	 VARCHAR(512)   ,
	 `description` 	 VARCHAR(512)   ,
	 `url` 	 VARCHAR(512)   ,
	 `token` 	 VARCHAR(512)   ,
	 `app_id` 	 VARCHAR(512)   ,
	 `api_key` 	 VARCHAR(512)   ,
	 `api_secret` 	 VARCHAR(512)   ,
	 `method` 	 VARCHAR(512)   ,
	 `body_type` 	 VARCHAR(512)   ,
	 `body_template` 	 VARCHAR(512)   ,
	 `header` 	 VARCHAR(512)   ,
	 `auth_type` 	 VARCHAR(512)   ,
	 `protocol` 	 VARCHAR(512)   ,
	 `data_path` 	 VARCHAR(512)   ,
	 `data_type` 	 VARCHAR(512)   
 );
insert into `dynamic_api_setting` (`key_name`, `description`, `url`, `token`, `app_id`, `api_key`, `api_secret`, `method`, `body_type`, `body_template`, `header`, `auth_type`, `protocol`, `data_path`, `data_type` ) values ("word2pic","AI文本生成图片","http://1.94.150.175:8087/api/word2pic","sk-zOFNmxCWi5GpMyrvWt3KRVqrj2LtRaipS/sYSnIBbJs=","","","","post","template",'{"input":"${{text}}"}','{"Authorization": "Bearer ${{token}}", "Content-Type": "application/json"}',"codeflying","http","","" );
insert into `dynamic_api_setting` (`key_name`, `description`, `url`, `token`, `app_id`, `api_key`, `api_secret`, `method`, `body_type`, `body_template`, `header`, `auth_type`, `protocol`, `data_path`, `data_type` ) values ("pic2word","AI图片识别理解","http://1.94.150.175:8087/api/pic2word","sk-zOFNmxCWi5GpMyrvWt3KRVqrj2LtRaipS/sYSnIBbJs=","","","","post","template",'{"input":"请详细描述一下图中有什么？如果图片中是文本，请将文本内容原样输出。","content":"${{content}}"}','{"Authorization": "Bearer ${{token}}", "Content-Type": "application/json"}',"codeflying","http","","" );
insert into `dynamic_api_setting` (`key_name`, `description`, `url`, `token`, `app_id`, `api_key`, `api_secret`, `method`, `body_type`, `body_template`, `header`, `auth_type`, `protocol`, `data_path`, `data_type` ) values ("text2text","AI文本生成文本","http://1.94.150.175:8087/llm/chat-block","sk-zOFNmxCWi5GpMyrvWt3KRVqrj2LtRaipS/sYSnIBbJs=","","","","post","","",'{"Authorization": "Bearer ${{token}}", "Content-Type": "application/json"}',"codeflying","http","","" );
insert into `dynamic_api_setting` (`key_name`, `description`, `url`, `token`, `app_id`, `api_key`, `api_secret`, `method`, `body_type`, `body_template`, `header`, `auth_type`, `protocol`, `data_path`, `data_type` ) values ("tts","文本生成语音","http://1.94.150.175:8087/api/text2speech","sk-zOFNmxCWi5GpMyrvWt3KRVqrj2LtRaipS/sYSnIBbJs=","","","","post","template",'{"content":"${{text}}"}','{"Authorization": "Bearer ${{token}}", "Content-Type": "application/json"}',"codeflying","http","","" );
insert into `dynamic_api_setting` (`key_name`, `description`, `url`, `token`, `app_id`, `api_key`, `api_secret`, `method`, `body_type`, `body_template`, `header`, `auth_type`, `protocol`, `data_path`, `data_type` ) values ("amap_geo","高德-将详细的结构化地址转换为经纬度坐标","http://1.94.150.175:8087/mcp/amap?method=maps_geo","sk-zOFNmxCWi5GpMyrvWt3KRVqrj2LtRaipS/sYSnIBbJs=","","","","post","template",'{"city":"${{city}}","address":"${{address}}"}','{"Authorization": "Bearer ${{token}}", "Content-Type": "application/json"}',"codeflying","http","$.data.content[0].text","json" );
insert into `dynamic_api_setting` (`key_name`, `description`, `url`, `token`, `app_id`, `api_key`, `api_secret`, `method`, `body_type`, `body_template`, `header`, `auth_type`, `protocol`, `data_path`, `data_type` ) values ("amap_regeocode","高德-经纬度坐标转换为行政区划地址信息","http://1.94.150.175:8087/mcp/amap?method=maps_regeocode","sk-zOFNmxCWi5GpMyrvWt3KRVqrj2LtRaipS/sYSnIBbJs=","","","","post","template",'{"location":"${{location}}"}','{"Authorization": "Bearer ${{token}}", "Content-Type": "application/json"}',"codeflying","http","$.data.content[0].text","json" );
insert into `dynamic_api_setting` (`key_name`, `description`, `url`, `token`, `app_id`, `api_key`, `api_secret`, `method`, `body_type`, `body_template`, `header`, `auth_type`, `protocol`, `data_path`, `data_type` ) values ("amap_direction_driving","高德-驾车路径规划","http://1.94.150.175:8087/mcp/amap?method=maps_direction_driving","sk-zOFNmxCWi5GpMyrvWt3KRVqrj2LtRaipS/sYSnIBbJs=","","","","post","template",'{"origin":"${{origin}}","destination":"${{destination}}"}','{"Authorization": "Bearer ${{token}}", "Content-Type": "application/json"}',"codeflying","http","$.data.content[0].text","json" );

drop table if exists `login`;

    create table `login` (
        `login_id`      INTEGER PRIMARY KEY AUTOINCREMENT,
        `relevance_id`  VARCHAR ,
        `password`      VARCHAR ,
        `user_name`     VARCHAR ,
        `relevance_table`       VARCHAR ,
        `phone_number`  VARCHAR ,
        `wx_open_id`    VARCHAR
    );
    
    insert into `login` (`relevance_id`,`password`,`user_name`,`relevance_table`,`phone_number`,`wx_open_id` ) values ("","$2a$10$bOrRjyHCMT2DUnXoVuFWNO1dSd6Zj/fyjS0TETmcD1ZXlR/BGklc2","admin","","admin","" );
    
    insert into `login` (`relevance_id`,`password`,`user_name`,`relevance_table`,`phone_number`,`wx_open_id` ) values ("1","$2a$10$bOrRjyHCMT2DUnXoVuFWNO1dSd6Zj/fyjS0TETmcD1ZXlR/BGklc2","test","userInfo","18852718858","" );
