drop table if exists `health_questionnaire`;
create table `health_questionnaire` (
                                        health_questionnaire_id int not null auto_increment,
                                        user_info_user_info_id_1 int not null,
                                        adl tinyint,
                                        mobility_out tinyint,
                                        falls tinyint,
                                        weight_loss tinyint,
                                        diseases varchar(255),
                                        pa_minutes tinyint,
                                        pa_willingness tinyint,
                                        flu_vaccine tinyint,
                                        polypharmacy tinyint,
                                        social tinyint,
                                        fv_serves tinyint,
                                        total_score int,
                                        risk_level varchar(32),
                                        answers_json text,
                                        creation_time datetime,
                                        update_time datetime,
                                        version int,
                                        primary key (health_questionnaire_id)
);