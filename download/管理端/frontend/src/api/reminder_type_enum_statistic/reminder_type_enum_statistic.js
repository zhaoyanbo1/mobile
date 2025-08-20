// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/reminderTypeEnumStatistics";

export default {
reminder_type_enum_reminder_type_enum_id_status_statistic_e178f79f_count(data){
  return service({
    url: BASE_API + "/reminder_type_enum_reminder_type_enum_id_status_statistic_e178f79f_count",
    method: "post",
    data:data
  });
},
reminder_type_enum_reminder_type_enum_id_status_statistic_4133ff9f_count(data){
  return service({
    url: BASE_API + "/reminder_type_enum_reminder_type_enum_id_status_statistic_4133ff9f_count",
    method: "post",
    data:data
  });
},
reminder_type_enum_reminder_type_enum_id_status_statistic_3179cc92_count(data){
  return service({
    url: BASE_API + "/reminder_type_enum_reminder_type_enum_id_status_statistic_3179cc92_count",
    method: "post",
    data:data
  });
},
};
