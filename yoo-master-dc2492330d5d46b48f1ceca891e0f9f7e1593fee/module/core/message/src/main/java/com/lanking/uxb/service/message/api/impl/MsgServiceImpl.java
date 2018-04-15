package com.lanking.uxb.service.message.api.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.uxb.service.message.api.MsgService;

@Service
@Transactional(readOnly = true)
public class MsgServiceImpl implements MsgService {

}
