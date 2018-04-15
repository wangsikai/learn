## 获取活动的消息发送日志
#macro($findMessageLogs(activityCode))
select t.* from imperial_exam_activity_message_log t where t.activity_code=:activityCode order by t.create_time ASC
#end