// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/healthQuestionnaireStatistics";

export default {
health_questionnaire_statistic_e3d3f9c6_count(data){
  return service({
    url: BASE_API + "/health_questionnaire_statistic_e3d3f9c6_count",
    method: "post",
    data:data
  });
},
health_questionnaire_statistic_b3a63b11_count(data){
  return service({
    url: BASE_API + "/health_questionnaire_statistic_b3a63b11_count",
    method: "post",
    data:data
  });
},
health_questionnaire_statistic_2228e20a_count(data){
  return service({
    url: BASE_API + "/health_questionnaire_statistic_2228e20a_count",
    method: "post",
    data:data
  });
},
health_questionnaire_creation_time_datetime_statistic_88df0f9b_count(data){
  return service({
    url: BASE_API + "/health_questionnaire_creation_time_datetime_statistic_88df0f9b_count",
    method: "post",
    data:data
  });
},
health_questionnaire_creation_time_datetime_statistic_f09877db_count(data){
  return service({
    url: BASE_API + "/health_questionnaire_creation_time_datetime_statistic_f09877db_count",
    method: "post",
    data:data
  });
},
health_questionnaire_creation_time_datetime_statistic_498a7150_count(data){
  return service({
    url: BASE_API + "/health_questionnaire_creation_time_datetime_statistic_498a7150_count",
    method: "post",
    data:data
  });
},
health_questionnaire_update_time_datetime_statistic_a433a769_count(data){
  return service({
    url: BASE_API + "/health_questionnaire_update_time_datetime_statistic_a433a769_count",
    method: "post",
    data:data
  });
},
health_questionnaire_update_time_datetime_statistic_b1d5cba1_count(data){
  return service({
    url: BASE_API + "/health_questionnaire_update_time_datetime_statistic_b1d5cba1_count",
    method: "post",
    data:data
  });
},
health_questionnaire_update_time_datetime_statistic_9bad8d9e_count(data){
  return service({
    url: BASE_API + "/health_questionnaire_update_time_datetime_statistic_9bad8d9e_count",
    method: "post",
    data:data
  });
},
};
