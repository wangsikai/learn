package com.lanking.uxb.service.fallible.api.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.uxb.service.fallible.api.StudentFallibleQuestionService;

@Transactional(readOnly = true)
@Service
public class StudentFallibleQuestionServiceImpl implements StudentFallibleQuestionService {

}
