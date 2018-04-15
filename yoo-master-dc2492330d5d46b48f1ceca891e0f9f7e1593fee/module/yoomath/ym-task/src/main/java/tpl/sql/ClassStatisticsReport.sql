##查询
#macro($taskQuery(subjectCode,classId,calDate,classIds))
select t.* from class_statistics_report t where t.subject_code=:subjectCode
#if(classId)
	AND t.class_id=:classId
#end
#if(classIds)
	AND t.class_id in (:classIds)
#end
#if(calDate)
	AND t.cal_date=:calDate
#end
#end

##查询知识点分析情况
#macro($getKnowpointAnalysis(clazzId,textbookCode,startTime,endTime))
select LEFT(d.section_code,10) section_code,d.knowledge_code,sum(b.wrong_count) wrong_count,sum(b.right_count) right_count from homework a
inner join homework_question b on a.id = b.homework_id 
	and a.homework_class_id = :clazzId AND a.STATUS = 3 and a.right_rate is not null 
	and a.start_time >= :startTime and a.start_time < :endTime
inner join question_knowledge c on b.question_id = c.question_id
inner join knowledge_section d on d.knowledge_code = c.knowledge_code and d.section_code like :textbookCode
group by LEFT(d.section_code,10),d.knowledge_code
order by LEFT(d.section_code,10)
#end

##查询章节分析情况
#macro($getSectionAnalysis(clazzId,textbookCode,startTime,endTime))
select d.section_code,sum(b.wrong_count) wrong_count,sum(b.right_count) right_count from homework a
inner join homework_question b on a.id = b.homework_id 
	and a.homework_class_id = :clazzId AND a.STATUS = 3 and a.right_rate is not null 
	and a.start_time >= :startTime and a.start_time < :endTime
inner join question_knowledge c on b.question_id = c.question_id
inner join knowledge_section d on d.knowledge_code = c.knowledge_code and d.section_code like :textbookCode
group by d.section_code
order by LEFT(d.section_code,12) asc
#end
