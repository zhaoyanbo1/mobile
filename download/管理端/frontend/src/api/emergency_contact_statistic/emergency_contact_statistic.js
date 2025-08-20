// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/emergencyContactStatistics";

export default {
emergency_contact_statistic_8ed9134a_count(data){
  return service({
    url: BASE_API + "/emergency_contact_statistic_8ed9134a_count",
    method: "post",
    data:data
  });
},
emergency_contact_statistic_e46df142_count(data){
  return service({
    url: BASE_API + "/emergency_contact_statistic_e46df142_count",
    method: "post",
    data:data
  });
},
emergency_contact_statistic_efb671cc_count(data){
  return service({
    url: BASE_API + "/emergency_contact_statistic_efb671cc_count",
    method: "post",
    data:data
  });
},
};
