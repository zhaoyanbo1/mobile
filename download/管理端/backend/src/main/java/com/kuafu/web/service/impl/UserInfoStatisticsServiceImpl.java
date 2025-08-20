package com.kuafu.web.service.impl;

import java.util.List;
import org.springframework.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.kuafu.web.mapper.UserInfoStatisticsMapper;
import com.kuafu.web.entity.UserInfo;
import com.kuafu.web.service.IUserInfoStatisticsService;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p> user_info 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Slf4j
@Service("UserInfoStatistics")
public class UserInfoStatisticsServiceImpl extends ServiceImpl<UserInfoStatisticsMapper, UserInfo> implements IUserInfoStatisticsService {
    @Resource
    private UserInfoStatisticsMapper userInfoStaticMapper;

                        @Override
                        public List<Object> user_info_registration_date_datetime_statistic_e1bf3d5d_count(LambdaQueryWrapper queryWrapper) {
                                return userInfoStaticMapper.user_info_registration_date_datetime_statistic_e1bf3d5d_count(queryWrapper);
                                }


                        @Override
                        public List<Object> user_info_registration_date_datetime_statistic_683a2401_count(LambdaQueryWrapper queryWrapper) {
                                return userInfoStaticMapper.user_info_registration_date_datetime_statistic_683a2401_count(queryWrapper);
                                }


                        @Override
                        public List<Object> user_info_registration_date_datetime_statistic_1e61d94d_count(LambdaQueryWrapper queryWrapper) {
                                return userInfoStaticMapper.user_info_registration_date_datetime_statistic_1e61d94d_count(queryWrapper);
                                }


                        @Override
                        public List<Object> user_info_last_login_date_datetime_statistic_10ddf510_count(LambdaQueryWrapper queryWrapper) {
                                return userInfoStaticMapper.user_info_last_login_date_datetime_statistic_10ddf510_count(queryWrapper);
                                }


                        @Override
                        public List<Object> user_info_last_login_date_datetime_statistic_65a6ff33_count(LambdaQueryWrapper queryWrapper) {
                                return userInfoStaticMapper.user_info_last_login_date_datetime_statistic_65a6ff33_count(queryWrapper);
                                }


                        @Override
                        public List<Object> user_info_last_login_date_datetime_statistic_d6db3a40_count(LambdaQueryWrapper queryWrapper) {
                                return userInfoStaticMapper.user_info_last_login_date_datetime_statistic_d6db3a40_count(queryWrapper);
                                }


}
