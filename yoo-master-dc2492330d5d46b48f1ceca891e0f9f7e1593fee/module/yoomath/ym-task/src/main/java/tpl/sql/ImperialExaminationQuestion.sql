## 查询活动的题目
#macro($TaskQueryActivityQuestion(code,type,tag,room,category))
select t.* from imperial_exam_2_question t where t.activity_code =:code
#if(tag)
and t.tag =:tag
#end
#if(room)
and t.room =:room
#end
#if(category)
and t.textbook_category_code =:category
#end
and t.type =:type order by t.sequence
#end