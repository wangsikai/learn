package com.lanking.uxb.service.honor.api.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.uxb.service.honor.api.PointLogService;

@Transactional(readOnly = true)
@Service
public class PointLogServiceImpl implements PointLogService {
}
