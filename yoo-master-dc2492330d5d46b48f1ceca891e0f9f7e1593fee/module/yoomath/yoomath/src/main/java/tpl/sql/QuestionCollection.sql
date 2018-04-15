## 知识点对应题目收藏数量
#macro($statisKnowPointCollect(subjectCode,userId,qtCodes))
  SELECT COUNT(a.question_id) cou,a.meta_code FROM question_metaknow a
  INNER JOIN question_collection b ON a.question_id = b.question_id AND b.subject_code =:subjectCode and user_id=:userId 
  #if(qtCodes)
     and b.type_code not in (:qtCodes)
  #end
  GROUP BY a.meta_code
#end

##获取单个收藏
#macro($getQuestionCollect(questionId,userId))
  select * from question_collection where user_id=:userId and question_id=:questionId
#end

##获取多个收藏
#macro($getQuestionCollects(questionIds,userId))
  select * from question_collection where user_id=:userId and question_id in :questionIds
#end

## 章节码对应题目收藏数量
#macro($statisSectionCollect(textbookCode,userId,qtCodes,version))
	SELECT COUNT(b.question_id) cou,b.section_code FROM question_collection a 
	INNER JOIN question_section b ON a.question_id = b.question_id and b.textbook_code =:textbookCode and a.user_id=:userId 
	#if(qtCodes)
		and a.type_code not in (:qtCodes)
	#end
	#if(version == 1)
	 	and b.v1 = 1
	#end
	#if(version == 2)
	 	and b.v2 = 1
	#end
	GROUP BY b.section_code
#end

##我的收藏列表
#macro($queryCollection(typeCode,textbookCode,sort,sectionCode,categoryCode,metaknowCode,leDif,reDif,userId,typeCodes,keystr))
  SELECT a.* FROM question_collection a 
  	#if(textbookCode)
    INNER JOIN question_section b ON a.question_id = b.question_id AND b.textbook_code = :textbookCode
    #end
    #if(metaknowCode)
    INNER JOIN question_metaknow c ON a.question_id = c.question_id AND c.meta_code like :metaknowCode
    #end
    #if(keystr)
    INNER JOIN	question d ON a.question_id = d.question_id AND CONCAT(d.content,d.choice_a,d.choice_b,d.choice_c,d.choice_d,d.choice_e,d.choice_f) LIKE :keystr
    #end
    WHERE a.user_id =:userId
    #if(sectionCode)
    AND b.section_code like :sectionCode
    #end
   	#if(typeCode)
   	AND a.type_code = :typeCode
   	#end
   	#if(typeCodes)
   	AND a.type_code in (:typeCodes)
   	#end
   	#if(leDif)
   	AND a.difficulty >= :leDif
   	#end
   	#if(reDif)
   	AND a.difficulty < :reDif
   	#end
   	order by create_at
   	#if(sort ==0)
   	ASC
   	#end
   	#if(sort ==1)
   	DESC
   	#end
#end

## 统计教材题目收藏数量
#macro($statisTextbookCollect(textbookCodes,userId,qtCodes,version))
SELECT COUNT(b.question_id) cou,b.textbook_code 
FROM question_collection a INNER JOIN question_section b ON a.question_id = b.question_id 
WHERE b.textbook_code IN (:textbookCodes) AND a.user_id = :userId
#if(qtCodes)
	AND a.type_code not in (:qtCodes)
#end
#if(version == 1)
	and b.v1 = 1
#end
#if(version == 2)
	and b.v2 = 1
#end
GROUP BY b.textbook_code
#end

## 统计用户收藏数量
#macro($zyCount(userId))
SELECT COUNT(id) FROM question_collection WHERE user_id = :userId
#end

## 游标方式查询搜索题目
#macro($zyQueryCollectionByCursor(userId,keyword))
SELECT a.* FROM question_collection a INNER JOIN question b ON a.question_id = b.id
WHERE user_id = :userId
#if(keyword)
 AND b.content LIKE :keyword
#end
#if(next)
	AND a.id < :next
#end
ORDER BY a.id DESC
#end


## 知识点对应题目收藏数量
#macro($zyGetNewKnowpointCollectCount(subjectCode,userId,qtCodes))
  SELECT COUNT(a.question_id) count,a.knowledge_code FROM question_knowledge a
  INNER JOIN question_collection b ON a.question_id = b.question_id AND b.subject_code =:subjectCode and user_id=:userId 
  #if(qtCodes)
     and b.type_code not in (:qtCodes)
  #end
  GROUP BY a.knowledge_code
#end

## 统计每个教材题目收藏数量
#macro($statisEveryTextbookCollect(teacherId,categoryCode))
SELECT
	COUNT(DISTINCT(t1.question_id)) cou,
	t2.textbook_code
FROM
	question_collection t1
INNER JOIN question_section t2 ON t1.question_id = t2.question_id
WHERE
	t1.user_id = :teacherId
AND t2.v2 = 1
AND t2.textbook_code LIKE :categoryCode
GROUP BY
	t2.textbook_code
#end

## 游标方式查询搜索题目
#macro($zyQueryCollectionByCursor2(userId,typeCodes,leDifficulty,reDifficulty,textBookCode,sectionCodes,searchInSection,sectionCodes,textBookCode))
SELECT DISTINCT a.* FROM question_collection a INNER JOIN question b ON a.question_id = b.id
#if(searchInSection) 
INNER JOIN question_section qs ON qs.question_id = a.question_id AND qs.v2 = 1
	#if(sectionCodes)
AND qs.section_code IN (:sectionCodes)
	#end
	#if(textBookCode)
AND qs.textbook_code =:textBookCode
	#end
#end
WHERE user_id = :userId
#if(typeCodes)
	AND a.type_code in (:typeCodes)
#end
#if(leDifficulty)
	#if(leftOpen==true)
	AND a.difficulty > :leDifficulty
	#end
	#if(leftOpen==false)
	AND a.difficulty >= :leDifficulty
	#end
#end
#if(reDifficulty)
	#if(rightOpen==true)
	AND a.difficulty < :reDifficulty
	#end
	#if(rightOpen==false)
	AND a.difficulty <= :reDifficulty
	#end
#end
#if(next)
	AND a.id < :next
#end
ORDER BY a.id DESC
#end

## 取最后一条收藏题目的textbookCode
#macro($queryLastTextbookCode(userId, categoryCode))
SELECT qs.textbook_code
FROM question_collection a,question_section qs
where a.question_id = qs.question_id
AND a.user_id = :userId
AND qs.textbook_code LIKE :categoryCode
AND qs.v2 = 1
ORDER BY a.create_at DESC
LIMIT 1;
#end

## 查询章节数据
#macro($zyCollectGetSectionCode(teacherId,textbookCode))
SELECT
	t2.section_code
FROM
	question_collection t1
INNER JOIN question_section t2 ON t1.question_id = t2.question_id
WHERE
	t2.v2 = 1
AND t1.user_id = :teacherId
AND t2.textbook_code = :textbookCode
#end

## 游标方式查询搜索题目
#macro($zyQueryCollectionByCursor2Count(userId,typeCodes,leDifficulty,reDifficulty,textBookCode,sectionCodes,searchInSection,sectionCodes,textBookCode))
SELECT count(distinct a.id) FROM question_collection a INNER JOIN question b ON a.question_id = b.id
#if(searchInSection) 
INNER JOIN question_section qs ON qs.question_id = a.question_id AND qs.v2 = 1
	#if(sectionCodes)
AND qs.section_code IN (:sectionCodes)
	#end
	#if(textBookCode)
AND qs.textbook_code =:textBookCode
	#end
#end
WHERE user_id = :userId
#if(typeCodes)
	AND a.type_code in (:typeCodes)
#end
#if(leDifficulty)
	#if(leftOpen==true)
	AND a.difficulty > :leDifficulty
	#end
	#if(leftOpen==false)
	AND a.difficulty >= :leDifficulty
	#end
#end
#if(reDifficulty)
	#if(rightOpen==true)
	AND a.difficulty < :reDifficulty
	#end
	#if(rightOpen==false)
	AND a.difficulty <= :reDifficulty
	#end
#end
#end