// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/dietRecommendationStatistics";

export default {
diet_recommendation_creation_time_datetime_statistic_d9cff743_count(data){
  return service({
    url: BASE_API + "/diet_recommendation_creation_time_datetime_statistic_d9cff743_count",
    method: "post",
    data:data
  });
},
diet_recommendation_creation_time_datetime_statistic_24a31926_count(data){
  return service({
    url: BASE_API + "/diet_recommendation_creation_time_datetime_statistic_24a31926_count",
    method: "post",
    data:data
  });
},
diet_recommendation_creation_time_datetime_statistic_6372a03f_count(data){
  return service({
    url: BASE_API + "/diet_recommendation_creation_time_datetime_statistic_6372a03f_count",
    method: "post",
    data:data
  });
},
};
