## 查询活动学生作业
#macro(TaskQueryActivityHomeworkStudent(code,type,room,category,tag))
select t.* from  imperial_exam_homework_student t where  t.activity_code=:code
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