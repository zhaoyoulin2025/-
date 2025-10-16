package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.Message;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageMapper extends BaseMapper<Message> {
    int insert(Message message);
    List<Message> selectByReceiverAndStatus(Message message);
    int markAsRead(Long id);
}
