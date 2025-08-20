// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/userInfoStatistics";

export default {
user_info_registration_date_datetime_statistic_e1bf3d5d_count(data){
  return service({
    url: BASE_API + "/user_info_registration_date_datetime_statistic_e1bf3d5d_count",
    method: "post",
    data:data
  });
},
user_info_registration_date_datetime_statistic_683a2401_count(data){
  return service({
    url: BASE_API + "/user_info_registration_date_datetime_statistic_683a2401_count",
    method: "post",
    data:data
  });
},
user_info_registration_date_datetime_statistic_1e61d94d_count(data){
  return service({
    url: BASE_API + "/user_info_registration_date_datetime_statistic_1e61d94d_count",
    method: "post",
    data:data
  });
},
user_info_last_login_date_datetime_statistic_10ddf510_count(data){
  return service({
    url: BASE_API + "/user_info_last_login_date_datetime_statistic_10ddf510_count",
    method: "post",
    data:data
  });
},
user_info_last_login_date_datetime_statistic_65a6ff33_count(data){
  return service({
    url: BASE_API + "/user_info_last_login_date_datetime_statistic_65a6ff33_count",
    method: "post",
    data:data
  });
},
user_info_last_login_date_datetime_statistic_d6db3a40_count(data){
  return service({
    url: BASE_API + "/user_info_last_login_date_datetime_statistic_d6db3a40_count",
    method: "post",
    data:data
  });
},
};
