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

import com.kuafu.web.mapper.ReminderItemMapper;
import com.kuafu.web.entity.ReminderItem;
import com.kuafu.web.service.IReminderItemService;
import lombok.extern.slf4j.Slf4j;

/**
 * <p> 提醒事项 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Slf4j
@Service("ReminderItem")
public class ReminderItemServiceImpl extends ServiceImpl<ReminderItemMapper, ReminderItem> implements IReminderItemService {


}
