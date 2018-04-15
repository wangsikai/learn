## 查询活动
#macro($TaskQueryActivity())
select t.* from  imperial_exam_activity t where  t.status =0 and t.period = 2
order by t.create_at desc limit 10
#end