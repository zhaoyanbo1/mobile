// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/activityRecommendationStatistics";

export default {
activity_recommendation_activity_time_datetime_statistic_886e9306_count(data){
  return service({
    url: BASE_API + "/activity_recommendation_activity_time_datetime_statistic_886e9306_count",
    method: "post",
    data:data
  });
},
activity_recommendation_activity_time_datetime_statistic_d3b0c288_count(data){
  return service({
    url: BASE_API + "/activity_recommendation_activity_time_datetime_statistic_d3b0c288_count",
    method: "post",
    data:data
  });
},
activity_recommendation_activity_time_datetime_statistic_470f405b_count(data){
  return service({
    url: BASE_API + "/activity_recommendation_activity_time_datetime_statistic_470f405b_count",
    method: "post",
    data:data
  });
},
activity_recommendation_creation_time_datetime_statistic_08c7391b_count(data){
  return service({
    url: BASE_API + "/activity_recommendation_creation_time_datetime_statistic_08c7391b_count",
    method: "post",
    data:data
  });
},
activity_recommendation_creation_time_datetime_statistic_ef3081ce_count(data){
  return service({
    url: BASE_API + "/activity_recommendation_creation_time_datetime_statistic_ef3081ce_count",
    method: "post",
    data:data
  });
},
activity_recommendation_creation_time_datetime_statistic_d08da80b_count(data){
  return service({
    url: BASE_API + "/activity_recommendation_creation_time_datetime_statistic_d08da80b_count",
    method: "post",
    data:data
  });
},
};
