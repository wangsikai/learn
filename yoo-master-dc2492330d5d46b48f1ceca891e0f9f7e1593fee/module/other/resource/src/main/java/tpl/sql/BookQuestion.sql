##获得目录题目统计
#macro($getQuestionNum(bookCatalogId))
select COUNT(t.id) from book_question t where t.book_catalog_id=:bookCatalogId
#end

##获得目录题目集合
#macro($getQuestionFromCatalog(questionId, sequence, bookCatalogId, bookVersionId))
select t.* from book_question t where 1=1
#if(bookCatalogId)
 AND t.book_catalog_id=:bookCatalogId
#end
#if(questionId)
 AND t.question_id=:questionId
#end
#if(sequence)
 AND t.sequence=:sequence
#end
#if(bookVersionId)
 AND t.book_version_id=:bookVersionId
#end
#end

##获得目录题目集合
#macro($getQuestionFromVersion(questionId, sequence, bookVersionId))
select t.* from book_question t where t.book_version_id=:bookVersionId
#if(questionId)
 AND t.question_id=:questionId
#end
#if(sequence)
 AND t.sequence=:sequence
#end
#end

##更新删除某题后的题目顺序
#macro($updateDeleteSequence(bookCatalogId, sequence))
update book_question set sequence=sequence-1 where sequence>:sequence
#end

##查找书本中是否包含指定编号的题目
#macro($findHasQuestionCode(bookVersionId, questionCodes))
SELECT q.code FROM book_question t INNER JOIN question q ON q.id=t.question_id AND q.code in (:questionCodes)
 WHERE t.book_version_id=:bookVersionId
#end

##获得目录节点最大的目录序列
#macro($getMaxSequence(bookCatalogId))
select MAX(sequence) from book_question where book_catalog_id=:bookCatalogId
#end

##查询题目
#macro($query(bookVersionId, bookCatalogIds, keys, checkStatus, questionType, notag, questionTags, code, v3flag, bookQuestionCategoryId, nobqc))
select q.* from book_question t
 inner join question q on q.id = t.question_id
 #if(keys)
 AND (q.content like :keys OR q.choice_a like :keys OR q.choice_b like :keys OR q.choice_c like :keys OR q.choice_d like :keys
 OR q.choice_e like :keys OR q.choice_f like :keys OR q.hint like :keys OR q.analysis like :keys)
 #end
 #if(code)
 AND q.code=:code
 #end
 #if(checkStatus)
 AND q.status=:checkStatus
 #end
 #if(questionType)
 AND q.type=:questionType
 #end
 left join book_catalog c on c.id = t.book_catalog_id
 #if(v3flag)
 LEFT JOIN question_knowledge_review qkr ON qkr.question_id=t.question_id
 LEFT JOIN question_knowledge_sync qks ON qks.question_id=t.question_id
 #end
 where t.book_version_id=:bookVersionId and q.del_status != 2
 #if(bookCatalogIds)
 AND t.book_catalog_id in (:bookCatalogIds)
 #end
 #if(bookQuestionCategoryId)
  AND t.book_question_catalog_id=:bookQuestionCategoryId
 #end
 #if(nobqc)
  AND (t.book_question_catalog_id is null or t.book_question_catalog_id = 0)
 #end
 #if(notag == 1)
  AND (t.book_catalog_id is null or t.book_catalog_id = 0)
 #end
 #if(questionTags)
  AND EXISTS 
  (
   SELECT qt.id FROM question_2_tag qt WHERE qt.tag_code IN (:questionTags) AND qt.question_id=t.question_id
  )
 #end
 #if(v3flag)
  AND qkr.question_id IS NULL AND qks.question_id IS NULL
 #end
 order by t.create_at DESC
#end

##获得书本版本中各个状态的题目数
#macro($getStausCounts(bookVersionId))
SELECT COUNT(t.id) as count,q.status as status FROM book_question t INNER JOIN question q ON q.id = t.question_id
 WHERE t.book_version_id=:bookVersionId GROUP BY q.status
#end

##获得书本版本中各个状态的题目数
#macro($getBookversionQuestionCounts(bookVersionIds))
SELECT COUNT(t.id) AS count,q.status AS status,t.book_version_id as id FROM book_question t 
 INNER JOIN question q ON q.id = t.question_id
 WHERE t.book_version_id IN (:bookVersionIds)
 GROUP BY q.status,t.book_version_id
#end

## 替换习题
#macro($changeQuestion(bookVersionId, oldQuestionId, newQuestionId))
update book_question set question_id=:newQuestionId where book_version_id=:bookVersionId and question_id=:oldQuestionId
#end

## V3知识点转换-查询未转换书本习题个数（去除重复题）
#macro($findVersionNoV3Counts(bookVersionIds))
select count(1) as cont,t.book_version_id from book_question t
inner join question q on q.id=t.question_id and q.status=2 and q.del_status=0 AND q.same_show_id IS NULL
LEFT JOIN question_knowledge_review kr ON kr.question_id=t.question_id
LEFT JOIN question_knowledge_sync ks ON ks.question_id=t.question_id
WHERE kr.question_id IS NULL and ks.question_id is null and t.book_version_id in (:bookVersionIds)
group by t.book_version_id
#end

## V3知识点转换-查询未转换书本习题个数（去除重复题）
#macro($findVersionNoV3Count(bookVersionId))
select count(1) from book_question t
inner join question q on q.id=t.question_id and q.status=2 and q.del_status=0 AND q.same_show_id IS NULL
LEFT JOIN question_knowledge_review kr ON kr.question_id=t.question_id
LEFT JOIN question_knowledge_sync ks ON ks.question_id=t.question_id
WHERE kr.question_id IS NULL and ks.question_id is null and t.book_version_id=:bookVersionId
#end

## V3知识点转换-查询未转换目录习题个数（去除重复题）
#macro($findCatalogNoV3Counts(bookVersionId, catalogIds))
select count(1) as cont,t.book_catalog_id from book_question t
inner join question q on q.id=t.question_id and q.status=2 and q.del_status=0 AND q.same_show_id IS NULL
LEFT JOIN question_knowledge_review kr ON kr.question_id=t.question_id
LEFT JOIN question_knowledge_sync ks ON ks.question_id=t.question_id
WHERE kr.question_id IS NULL and ks.question_id is null and t.book_version_id=:bookVersionId
#if(catalogIds)
 and t.book_catalog_id in (:catalogIds)
#end
group by t.book_catalog_id
#end

##获得书本版本中各个状态的题目数
#macro($countCatalog(bookCatalogId))
select count(*) from book_question t 
 where t.book_catalog_id = :bookCatalogId
#end