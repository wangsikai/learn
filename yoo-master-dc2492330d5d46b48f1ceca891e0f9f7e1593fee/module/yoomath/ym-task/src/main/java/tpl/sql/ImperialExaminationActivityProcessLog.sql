## 查询活动阶段日志
#macro($TaskQueryActivityProcessLog(code,process,data))
select t.* from imperial_exam_activity_process_log t where t.activity_code=:code and t.process =:process and t.process_data=:data
#end