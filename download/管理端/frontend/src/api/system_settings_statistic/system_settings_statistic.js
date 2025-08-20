// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/systemSettingsStatistics";

export default {
system_settings_statistic_3692371e_count(data){
  return service({
    url: BASE_API + "/system_settings_statistic_3692371e_count",
    method: "post",
    data:data
  });
},
system_settings_statistic_278f24b9_count(data){
  return service({
    url: BASE_API + "/system_settings_statistic_278f24b9_count",
    method: "post",
    data:data
  });
},
system_settings_statistic_80d1d596_count(data){
  return service({
    url: BASE_API + "/system_settings_statistic_80d1d596_count",
    method: "post",
    data:data
  });
},
};
