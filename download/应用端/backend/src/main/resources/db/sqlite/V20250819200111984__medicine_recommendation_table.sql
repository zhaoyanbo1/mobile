drop table if exists `medicine_recommendation`;
create table `medicine_recommendation` (
	medicine_recommendation_id	INTEGER PRIMARY KEY AUTOINCREMENT,
	title	varchar(512)	not null,
	usage_guide	varchar(512)	,
	nearby_pharmacy_info	varchar(512)	,
	creation_time	DATETIME	
);insert into	medicine_recommendation	(creation_time,medicine_recommendation_id,usage_guide,title,nearby_pharmacy_info)	values	("2024-05-01 08:00:00.0 00:00:00.0","1","每日早晨服用1片，避免空腹。注意监测血压变化。","降压药A","成都市武侯区药店A库存充足");
insert into	medicine_recommendation	(creation_time,medicine_recommendation_id,usage_guide,title,nearby_pharmacy_info)	values	("2024-05-02 09:00:00.0 00:00:00.0","2","每日注射，根据血糖水平调整剂量。注意保存于冰箱。","胰岛素注射液","成都市锦江区药店B有货");
insert into	medicine_recommendation	(creation_time,medicine_recommendation_id,usage_guide,title,nearby_pharmacy_info)	values	("2024-05-03 10:00:00.0 00:00:00.0","3","按医嘱服用，避免长期大量使用。","止痛药B","成都市青羊区药店C库存有限");
insert into	medicine_recommendation	(creation_time,medicine_recommendation_id,usage_guide,title,nearby_pharmacy_info)	values	("2024-05-04 11:00:00.0 00:00:00.0","4","哮喘发作时使用，每次2吸。","吸入剂C","成都市武侯区药店D有货");
insert into	medicine_recommendation	(creation_time,medicine_recommendation_id,usage_guide,title,nearby_pharmacy_info)	values	("2024-05-05 12:00:00.0 00:00:00.0","5","预防血栓形成，每日早晨服用1片。","阿司匹林","成都市锦江区药店E库存充足");
insert into	medicine_recommendation	(creation_time,medicine_recommendation_id,usage_guide,title,nearby_pharmacy_info)	values	("2024-05-06 13:00:00.0 00:00:00.0","6","每日补充，促进钙吸收。","维生素D","成都市青羊区药店F有货");
