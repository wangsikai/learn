## 查询学生组卷
#macro($queryCustomExampaperStudent(clazzID,key0,bt,et,studentID))
 select t.* from custom_exampaper_student t
 inner join custom_exampaper e on e.id=t.custom_exampaper_id
 #if(key0)
 AND e.name like :key0
 #end
 #if(bt)
 AND e.open_at>=:bt
 #end
 #if(et)
 AND e.open_at<=:et
 #end
 where t.student_id=:studentID AND t.status != 2
#if(clazzID)
 AND t.class_id=:clazzID
#end
 ORDER BY t.id DESC
#end

## 获得截止时间之后的新数据数量
#macro($countNewDatas(cutTime,studentID))
 select count(t.id) from custom_exampaper_student t
 where t.student_id=:studentID AND t.create_at>:cutTime
#end

## 通过学生组卷ID找到组卷题型分类集合
#macro($findCustomExampaperTopicByStudent(customExampaperStudentID))
 SELECT t.* FROM custom_exampaper_topic t
 INNER JOIN custom_exampaper_student s ON s.custom_exampaper_id=t.custom_exampaper_id AND s.id=:customExampaperStudentID
 order by t.sequence ASC
#end