## 统计某个教师标签
#macro($findTagsByUserId(teacherId))
SELECT * 
FROM teachersday_activity_01_tea_tag 
WHERE teacher_id = :teacherId
ORDER BY num DESC
#end

## 通过老师和tagcode获取次数等信息
#macro($getTagNum(teacherId,tagCode))
SELECT * 
FROM teachersday_activity_01_tea_tag 
WHERE teacher_id = :teacherId and code_tag = :tagCode
#end