## 根据班级id及知识点列表查找数据
#macro($findByCodesAndClass(classId,codes))
SELECT t.* FROM diagno_class_kp t WHERE t.class_id = :classId AND t.knowpoint_code IN :codes GROUP BY t.class_id, t.knowpoint_code
#end

## 查找薄弱知识点
#macro($findByWeakDatas(classId,limit,bt,kps))
SELECT t.* FROM diagno_class_kp t WHERE t.class_id = :classId AND t.knowpoint_code > 1000000000 
 AND (t.right_count + 1)/(t.do_count + 2) <= 0.6 and right_rate is not null 
 #if(bt)
 AND t.create_at >:bt
 #end
 #if(kps)
 AND t.knowpoint_code in :kps
 #end
 ORDER BY t.right_rate ASC, t.do_count DESC 
 #if(limit)
  LIMIT :limit
 #end
#end

## 查找班级所有薄弱知识点
#macro($findAllByWeakDatas(classId))
SELECT t.* FROM diagno_class_kp t WHERE t.class_id = :classId
#end

## 智能组卷查询薄弱知识点
#macro($smartWeakDatas(classId,textbookCode,limit))
SELECT distinct a.* FROM diagno_class_kp a INNER JOIN knowledge_section b 
ON a.knowpoint_code =b.knowledge_code AND b.section_code LIKE :textbookCode
AND a.class_id = :classId AND a.right_rate < 100
ORDER BY a.right_rate ASC, a.do_count DESC limit 0,:limit
#end

## 智能组卷查询练习数较少的知识点 
#macro($smartBalanceDatas(classId,textbookCode,limit))
SELECT distinct a.* FROM diagno_class_kp a INNER JOIN knowledge_section b 
ON a.knowpoint_code =b.knowledge_code AND b.section_code LIKE :textbookCode
AND a.class_id = :classId AND a.do_count >0
#if(limit)
	ORDER BY a.do_count ASC limit 0,:limit
#end
#end

## 根据班级id查询数据
#macro($findByCodes(classId,codes))
select * from diagno_class_kp
where class_id = :classId
	#if(codes)
		and knowpoint_code in :codes
	#end
#end

## 根据班级id查询数据
#macro($findSectionMasterList(classId))
	SELECT b.section_code,c.level,SUM(do_count) doCount,SUM(right_count) rightCount FROM diagno_class_kp a 
  INNER JOIN knowledge_section b ON a.knowpoint_code = b.knowledge_code 
  INNER JOIN section c ON b.section_code = c.code
  WHERE CHAR_LENGTH(a.knowpoint_code) =10 AND a.class_id = :classId
  GROUP BY b.section_code
#end

## 根据班级id查询对应大章节底下对应做题数
#macro($getSectionDoCountMap(classId,sectionCodes))
	SELECT count(1) count0,LEFT(b.section_code,10) section_code FROM diagno_class_kp a 
  INNER JOIN knowledge_section b ON a.knowpoint_code = b.knowledge_code 
  INNER JOIN section c ON b.section_code = c.code and  left(b.section_code,10) in :sectionCodes
  WHERE CHAR_LENGTH(a.knowpoint_code) =10 AND a.class_id = :classId and a.do_count > 0
  group by left(b.section_code,10)
#end

## 通过章节code获取薄弱知识点列表
#macro($getWeakKpListBySectionCode(classId,sectionCode))
	SELECT a.knowpoint_code FROM diagno_class_kp a 
  INNER JOIN knowledge_section b ON a.knowpoint_code = b.knowledge_code 
  WHERE CHAR_LENGTH(a.knowpoint_code) =10 AND a.class_id = :classId AND b.section_code = :sectionCode
  AND (a.right_count + 1)/(a.do_count + 2) <= 0.6 AND a.right_rate IS NOT NULL
#end

## 查找薄弱知识点
#macro($findDiagnosticDatas(classId,limit,bt))
SELECT t.* FROM diagno_class_kp t 
WHERE t.class_id = :classId
 #if(bt)
 AND t.update_at >:bt
 #end
 ORDER BY t.update_at DESC 
 #if(limit)
  LIMIT :limit
 #end
#end
