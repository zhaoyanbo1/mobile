drop table if exists "health_questionnaire";
create table "health_questionnaire" (
                                        health_questionnaire_id integer primary key autoincrement,
                                        user_info_user_info_id_1 integer not null,
                                        adl integer,
                                        mobility_out integer,
                                        falls integer,
                                        weight_loss integer,
                                        diseases text,
                                        pa_minutes integer,
                                        pa_willingness integer,
                                        flu_vaccine integer,
                                        polypharmacy integer,
                                        social integer,
                                        fv_serves integer,
                                        total_score integer,
                                        risk_level text,
                                        answers_json text,
                                        creation_time text,
                                        update_time text,
                                        version integer
);