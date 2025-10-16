package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.domain.UserClock;
import com.ruoyi.system.mapper.UserClockMapper;
import com.ruoyi.system.service.UserClockService;
import org.springframework.stereotype.Service;

@Service
public class UserClockImpl extends ServiceImpl<UserClockMapper, UserClock> implements UserClockService {
}
