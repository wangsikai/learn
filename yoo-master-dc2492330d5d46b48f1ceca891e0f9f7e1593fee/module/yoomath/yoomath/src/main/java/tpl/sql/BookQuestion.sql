##书本章节目录下所有题目ID
#macro($listQuestionsInCatalog(bookCatalogId))
SELECT distinct(bq.question_id) FROM book_question bq 
INNER JOIN question q ON bq.question_id = q.id AND q.status=2 AND q.del_status=0 and q.type in (1,3,5)
WHERE bq.book_catalog_id=:bookCatalogId ORDER BY bq.sequence ASC,bq.id ASC
#end

##书本章节目录下所有题目ID
#macro($mQueryQuestionByCatalog(bookCatalogId, filterType,questionType,diff1,diff2))
SELECT bq.* FROM book_question bq 
INNER JOIN question q ON bq.question_id = q.id AND q.status=2 AND q.del_status=0 and q.type in (1,3,5)
WHERE bq.book_catalog_id = :bookCatalogId 
#if(filterType)
	AND q.type != :filterType
#end
#if(questionType)
	and q.type = :questionType
#end
#if(diff1)
	and q.difficulty >= :diff1
#end
#if(diff2)
	and q.difficulty < :diff2
#end
#if(next)
	AND bq.id > :next
#end
ORDER BY bq.id ASC
#end

##获取分类下题目
#macro($findQuestionByBookCategoryId(bookCategoryId))
SELECT question_id FROM book_question where book_question_catalog_id = :bookCategoryId
#end

##批量获取分类下题目
#macro($findQuestionByBookCategoryIds(bookCategoryIds,filterType,questionType,diff1,diff2))
SELECT bq.* FROM book_question bq 
INNER JOIN question q ON bq.question_id = q.id AND q.status=2 AND q.del_status=0 and q.type in (1,3,5)
WHERE bq.book_question_catalog_id in :bookCategoryIds 
#if(filterType)
	AND q.type != :filterType
#end
#if(questionType)
	and q.type = :questionType
#end
#if(diff1)
	and q.difficulty >= :diff1
#end
#if(diff2)
	and q.difficulty < :diff2
#end
#if(next)
	AND bq.id > :next
#end
ORDER BY bq.id ASC
#end