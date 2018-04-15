## 查询活动报名用户
#macro($TaskQueryActivityUser(code,room,category))
select t.* from imperial_exam_activity_user t where t.activity_code =:code
#if(room)
and t.room =:room
#end
#if(category)
and t.textbook_category_code =:category
#end
#end