## 通过班级获取对应的知识点正确率
#macro($getStuKnowpointStat(studentId,classId))
SELECT a.*,b.right_rate classrate,c.name FROM homework_student_class_knowpoint_stat a 
INNER JOIN  homework_class_knowpoint_stat b ON a.class_id =b.class_id AND a.knowpoint_code = b.knowpoint_code
INNER JOIN knowledge_point c ON a.knowpoint_code = c.code
AND a.student_id = :studentId AND a.class_id = :classId
#end

## 获取某个学生某个班级里面知识点最低的五个
#macro($getStuLowKnowpointStat(studentId,classId))
SELECT kpc.id as knowpointCardId,a.knowpoint_code,b.name,a.right_rate,(a.right_num+a.wrong_num) do_count 
FROM homework_student_class_knowpoint_stat a 
INNER JOIN knowledge_point b ON a.knowpoint_code = b.code
LEFT JOIN knowledge_point_card kpc on kpc.knowpoint_code = a.knowpoint_code 
AND  kpc.del_status=0 and kpc.check_status=2
where a.class_id = :classId AND a.student_id = :studentId
GROUP BY b.code
ORDER BY right_rate ASC,do_count DESC,b.code ASC,b.code ASC Limit 0,5
#end



