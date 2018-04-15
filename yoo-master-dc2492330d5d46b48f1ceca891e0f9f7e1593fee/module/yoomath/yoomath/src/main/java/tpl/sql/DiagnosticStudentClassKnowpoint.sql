##查询不同等级的知识统计情况
#macro($queryListByLevel(length,classId,studentId))
SELECT * FROM diagno_stu_class_kp where class_id =:classId and student_id = :studentId 
#if(length)
	and CHAR_LENGTH(knowpoint_code) =:length
#end
#end

##
#macro($getClassKp(code,classId,studentId))
SELECT * FROM diagno_stu_class_kp where class_id =:classId and student_id = :studentId and knowpoint_code =:code
#end

##根据学生班级/当前所选知识专项或小专题查询对应的知识点或知识专项的统计信息
#macro($queryknowListBySpecial(studentId,classId,pcode,level))
SELECT a.* FROM diagno_stu_class_kp a
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

##根据知识专项查询薄弱的知识点统计情况(去掌握情况低于60%的)
#macro($queryWeakListBySpecial(classId,studentId,startTime,endTime,codes))
SELECT a.* FROM diagno_stu_class_kp a 
WHERE a.class_id =:classId AND a.student_id = :studentId 
AND CHAR_LENGTH(a.knowpoint_code) =10 AND (right_count+1)/(do_count+2) <= 0.6
#if(startTime)
	AND a.create_at >= :startTime
#end
#if(endTime)
	AND a.create_at < :endTime
#end
#if(codes)
	AND a.knowpoint_code in :codes
#end
ORDER BY a.right_rate,a.do_count DESC
#end

##班级平均
#macro($getClassAvgRightRateByPcode(classId,pcode,level))
SELECT ROUND(AVG(a.right_rate),2) classrate,a.knowpoint_code  FROM diagno_stu_class_kp a
#if(level=='2')
INNER JOIN knowledge_system b
#end
#if(level=='3')
INNER JOIN knowledge_point b
#end
ON a.knowpoint_code = b.code AND b.pcode=:pcode AND class_id = :classId
GROUP BY a.knowpoint_code 
#end


##根据教材查询学生小专题
#macro($querySamllTopicList(studentId,classId,codes))
SELECT a.* FROM diagno_stu_class_kp a 
WHERE a.student_id =:studentId AND a.class_id = :classId AND a.knowpoint_code in (:codes)
ORDER BY a.knowpoint_code
#end

##小专题班级平均
#macro($querySmallTopicClassRateList(classId,codes))
SELECT ROUND(AVG(a.right_rate),2) classrate,a.knowpoint_code FROM diagno_stu_class_kp a 
WHERE  a.class_id = :classId AND a.knowpoint_code in (:codes)
GROUP BY a.knowpoint_code
#end

#macro($queryByKnowledge(code,studentId))
SELECT a.* FROM diagno_stu_class_kp a
WHERE a.student_id =:studentId AND a.knowpoint_code = :code
ORDER BY a.knowpoint_code
#end

##获取学生做题情况
#macro($getSectionDoCountMap(sectionCodes,studentId,classId))
	SELECT SUM(a.do_count) docount,SUM(a.right_count) rightcount,LEFT(b.section_code,10) section_code 
	FROM diagno_stu_class_kp a 
  INNER JOIN knowledge_section b ON a.knowpoint_code = b.knowledge_code and left(b.section_code,10) in :sectionCodes
  INNER JOIN section c on b.section_code = c.code and c.name != '本章综合与测试'
  WHERE CHAR_LENGTH(a.knowpoint_code) =10 AND a.student_id = :studentId and a.do_count > 0
  #if(classId)
  		and a.class_id = :classId
  	#end
   GROUP BY left(b.section_code,10)
#end

##获取章节下知识点情况
#macro($getKpDataBySectioncodes(sectionCodes,studentId,classId))
	   SELECT a.knowpoint_code,SUM(a.do_count) docount,SUM(a.right_count) rightcount,b.section_code
		FROM diagno_stu_class_kp a 
  	INNER JOIN knowledge_section b ON a.knowpoint_code = b.knowledge_code AND b.section_code IN :sectionCodes
  	WHERE CHAR_LENGTH(a.knowpoint_code) =10 AND a.student_id = :studentId AND a.do_count > 0 
  	#if(classId)
  		and a.class_id = :classId
  	#end
   	GROUP BY a.knowpoint_code,b.section_code
   	ORDER BY b.section_code
#end
