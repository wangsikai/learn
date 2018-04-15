package com.lanking.uxb.service.user.api.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.uxb.service.user.api.ParentService;

@Service("parentService")
@Transactional(readOnly = true)
public class ParentServiceImpl extends ParentUserServiceImpl implements ParentService {
}
