package com.kuafu.web.service.impl;

import java.util.List;
import com.kuafu.common.util.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuafu.common.domin.PageRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafu.web.mapper.SystemSettingsAllMapper;
import com.kuafu.web.entity.SystemSettingsAll;
import com.kuafu.web.service.ISystemSettingsAllService;
import org.springframework.util.SerializationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p> 系统设置 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Slf4j
@Service("SystemSettingsAll")
public class SystemSettingsAllServiceImpl extends ServiceImpl<SystemSettingsAllMapper, SystemSettingsAll> implements ISystemSettingsAllService {
@Autowired
private SystemSettingsAllMapper taskInfoAllMapper;


@Override
public List<SystemSettingsAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<SystemSettingsAll> queryWrapper) {

        LambdaQueryWrapper<SystemSettingsAll> pageQueryWrapper = getPageQueryWrapper(pageRequest, queryWrapper);
        final List<String> other_search_condition = pageRequest.getOther_search_condition();
        if (other_search_condition!=null){
        for (String s : other_search_condition) {
        if (StringUtils.isNotEmpty(s)){
        pageQueryWrapper.apply(s);
        }
        }
        }
        long current = pageRequest.getCurrent();
        long pageSize = pageRequest.getPageSize();
        long newCurrent = (current - 1) * pageSize;
        pageQueryWrapper.last("limit " + newCurrent + "," + pageSize);
        return taskInfoAllMapper.selectListNew(pageQueryWrapper);

        }

  @Override
public List<SystemSettingsAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<SystemSettingsAll> queryWrapper,boolean isPage) {

                LambdaQueryWrapper<SystemSettingsAll> pageQueryWrapper = getPageQueryWrapper(pageRequest, queryWrapper);
                final List<String> other_search_condition = pageRequest.getOther_search_condition();
                if (other_search_condition!=null){
                        for (String s : other_search_condition) {
                                if (StringUtils.isNotEmpty(s)){
                                        pageQueryWrapper.apply(s);
                                }
                        }
                }
                if (isPage){
                        long current = pageRequest.getCurrent();
                        long pageSize = pageRequest.getPageSize();
                        long newCurrent = (current - 1) * pageSize;
                        pageQueryWrapper.last("limit " + newCurrent + "," + pageSize);
                }

                return taskInfoAllMapper.selectListNew(pageQueryWrapper);

        }


@Override
public long selectCount(PageRequest pageRequest, LambdaQueryWrapper<SystemSettingsAll> queryWrapper) {
        LambdaQueryWrapper<SystemSettingsAll> pageQueryWrapper = getPageQueryWrapper(pageRequest, queryWrapper);

        final List<String> other_search_condition = pageRequest.getOther_search_condition();
        if (other_search_condition!=null){
        for (String s : other_search_condition) {
                pageQueryWrapper.apply(s);
                }
        }

        return taskInfoAllMapper.selectCount(queryWrapper);
        }



@Override
public IPage pageNew(IPage<SystemSettingsAll> page, PageRequest pageRequest, LambdaQueryWrapper<SystemSettingsAll> queryWrapper) {
        page.setCurrent(pageRequest.getCurrent());
        page.setSize(pageRequest.getPageSize());
        page.setRecords(selectListNew(pageRequest, queryWrapper));
        page.setTotal(selectCount(pageRequest, queryWrapper));
        return page;
        }



private <T> LambdaQueryWrapper<T> getPageQueryWrapper(PageRequest pageRequest, LambdaQueryWrapper<T> queryWrapper) {
final byte[] serialize = SerializationUtils.serialize(queryWrapper);
        return (LambdaQueryWrapper<T>) SerializationUtils.<LambdaQueryWrapper>deserialize(serialize);
        }

}
