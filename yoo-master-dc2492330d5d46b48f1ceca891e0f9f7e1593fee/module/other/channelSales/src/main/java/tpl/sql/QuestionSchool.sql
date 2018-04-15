## 根据学校id查找数据
#macro($csFindBySchool(schoolId))
SELECT t.* FROM question_school t WHERE t.school_id = :schoolId AND t.status = 0
#end