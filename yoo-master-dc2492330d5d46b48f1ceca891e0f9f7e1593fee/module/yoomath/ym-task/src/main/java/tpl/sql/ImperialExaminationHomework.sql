## 查询活动作业
#macro(TaskQueryActivityHomework(code,type,room,category,tag))
select t.* from  imperial_exam_homework t where  t.activity_code=:code
#if(type)
and t.type =:type
#end
#if(tag)
and t.tag =:tag
#end
#if(room)
and t.room =:room
#end
#if(category)
and t.textbook_category_code =:category
#end
#end