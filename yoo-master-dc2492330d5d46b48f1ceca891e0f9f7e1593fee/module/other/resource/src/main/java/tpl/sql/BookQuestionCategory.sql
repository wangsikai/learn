## 获得目录章节习题分类结构
#macro($queryQuestionCategory(bookCatalogId))
select * from book_question_category where book_section_id=:bookCatalogId order by id ASC
#end

## 获得目录章节习题分类结构
#macro($queryQuestionCategorys(bookCatalogIds))
select * from book_question_category where book_section_id in (:bookCatalogIds)
#end

## 获取习题个数
#macro($getQuestionCount(questionCategoryId))
select count(1) from book_question where book_question_catalog_id =:questionCategoryId
#end

## 获取习题个数
#macro($mgetQuestionCount(questionCategoryIds))
select count(1) as qcount,book_question_catalog_id as id from book_question where book_question_catalog_id in (:questionCategoryIds)
group by book_question_catalog_id
#end

## 清空结构
#macro($clearQuestionCategory(bookCatalogId, questionCategoryId, questionIds))
update book_question set book_question_catalog_id = null 
where book_catalog_id=:bookCatalogId 
#if(questionCategoryId)
 and book_question_catalog_id=:questionCategoryId
#end
#if(questionIds)
 and question_id in (:questionIds)
#end
#end

## 删除章节目录下的所有分组
#macro($deleteByBookCatalogId(bookCatalogId))
delete from book_question_category where book_section_id=:bookCatalogId
#end

## 更新结构
#macro($updateQuestionCategory(bookCatalogId, questionCategoryId, questionIds))
update book_question 
 #if(questionCategoryId == 0)
 	 set book_question_catalog_id=0
 #end
 #if(questionCategoryId != 0)
 	 set book_question_catalog_id=:questionCategoryId
 #end
 where book_catalog_id=:bookCatalogId and question_id in (:questionIds)
#end