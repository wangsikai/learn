## 查询活动
#macro($TaskQueryActivity())
select t.* from  holiday_activity_02 t order by t.create_at desc limit 10
#end