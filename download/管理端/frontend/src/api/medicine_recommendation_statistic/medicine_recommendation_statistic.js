// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/medicineRecommendationStatistics";

export default {
medicine_recommendation_creation_time_datetime_statistic_d4e51f2a_count(data){
  return service({
    url: BASE_API + "/medicine_recommendation_creation_time_datetime_statistic_d4e51f2a_count",
    method: "post",
    data:data
  });
},
medicine_recommendation_creation_time_datetime_statistic_660c8953_count(data){
  return service({
    url: BASE_API + "/medicine_recommendation_creation_time_datetime_statistic_660c8953_count",
    method: "post",
    data:data
  });
},
medicine_recommendation_creation_time_datetime_statistic_fb391654_count(data){
  return service({
    url: BASE_API + "/medicine_recommendation_creation_time_datetime_statistic_fb391654_count",
    method: "post",
    data:data
  });
},
};
