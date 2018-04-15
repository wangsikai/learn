## 根据章节码列表查找数据
#macro($findBySectionCodes(codes))
SELECT t.* FROM knowledge_section t WHERE t.section_code IN :codes
#end

## 根据教材码或者章节码查找数据
#macro($findByCode(code))
SELECT t.knowledge_code FROM knowledge_section t WHERE t.section_code LIKE :code GROUP BY t.knowledge_code
#end

## 根据知识点查询所对应章节非重复
#macro($findTextbookByKnowledge(codes))
SELECT s.textbook_code FROM knowledge_section m INNER JOIN section s ON s.code = m.section_code
WHERE m.knowledge_code IN :codes GROUP BY s.textbook_code
#end

## 根据知识点查询所对应章节
#macro($findTextbookByKnowledgeRelation(codes))
SELECT m.knowledge_code,s.textbook_code FROM knowledge_section m INNER JOIN section s ON s.code = m.section_code
WHERE m.knowledge_code IN :codes
#end

## 根据教材码或者章节码查找数据
#macro($findSectionsByMetaknowCodes(knowledgeCodes, textbookCategoryCode,orderBy,limit))
select t.* from section t
#if(textbookCategoryCode)
 inner join textbook tb on tb.code=t.textbook_code and tb.category_code=:textbookCategoryCode
#end
 where exists (
 	select 1 from knowledge_section ks where ks.section_code=t.code and ks.knowledge_code in (:knowledgeCodes)
 )
 #if(orderBy)
 ORDER BY code DESC
 #end
 #if(limit)
 Limit :limit
 #end
#end

## 根据知识点集合查询知识点（同步知识点）章节对应关系（排除本章综合与测试）
#macro($findSectionRelationByKnowledgeCodes(knowledgeCodes))
select t.* from knowledge_section t 
inner join section s on s.code = t.section_code and s.name != '本章综合与测试'
where t.knowledge_code in (:knowledgeCodes)
#end