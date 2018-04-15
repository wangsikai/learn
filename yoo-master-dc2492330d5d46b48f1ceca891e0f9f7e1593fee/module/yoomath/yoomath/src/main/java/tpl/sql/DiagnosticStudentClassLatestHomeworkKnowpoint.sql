##根据学生班级查询6个薄弱知识点(不包含100%),正确率相等时按做题数多的排
##20170207改取掌握度低于60%的
#macro($queryWeakList(studentId,classId,times,kpCount))
SELECT * FROM diagno_stu_class_latest_hk_kp WHERE student_id =:studentId AND class_id = :classId and right_rate!=100
AND CHAR_LENGTH(knowpoint_code) =10 AND (right_count+1)/(do_count+2) <= 0.6
#if(times)
	AND times = :times
#end
ORDER BY right_rate ASC,do_count DESC
#if(kpCount)
	limit 0,:kpCount
#end
#end

##根据教材查询学生小专题
#macro($querySamllTopicList(studentId,classId,codes))
SELECT a.* FROM diagno_stu_class_latest_hk_kp a 
WHERE a.student_id =:studentId AND a.class_id = :classId AND a.knowpoint_code in (:codes)
ORDER BY a.knowpoint_code
#end

##小专题班级平均
#macro($querySmallTopicClassRateList(classId,codes))
SELECT ROUND(AVG(a.right_rate),0) classrate,a.knowpoint_code FROM diagno_stu_class_latest_hk_kp a 
WHERE  a.class_id = :classId AND a.knowpoint_code in (:codes)
GROUP BY a.knowpoint_code
#end


##根据学生班级/当前所选知识专项或小专题查询对应的知识点或知识专项的统计信息
#macro($queryKnowledgeListByPcode(studentId,classId,pcode,level))
SELECT a.* FROM diagno_stu_class_latest_hk_kp a
#if(level=='2')
INNER JOIN knowledge_system b
#end
#if(level=='3')
INNER JOIN knowledge_point b
#end
ON a.knowpoint_code = b.code and b.pcode=:pcode
WHERE student_id =:studentId AND class_id = :classId
ORDER BY a.knowpoint_code 
#end


##根据学生班级/当前所选知识专项或小专题查询对应的知识点或知识专项的统计信息
#macro($getClassAvgRightRateByPcode(studentId,classId,pcode,level))
SELECT ROUND(AVG(a.right_rate),0) classrate,a.knowpoint_code  FROM diagno_stu_class_latest_hk_kp a
#if(level=='2')
INNER JOIN knowledge_system b
#end
#if(level=='3')
INNER JOIN knowledge_point b
#end
ON a.knowpoint_code = b.code AND b.pcode=:pcode AND class_id = :classId
GROUP BY a.knowpoint_code 
#end
