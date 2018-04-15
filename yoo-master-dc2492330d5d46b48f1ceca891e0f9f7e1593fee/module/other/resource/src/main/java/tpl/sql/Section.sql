#macro($findLastestCode(pcode, textbookCode))
SELECT * FROM section t WHERE
1=1
#if(textbookCode)
AND t.textbook_code = :textbookCode
#end
#if(pcode)
AND t.pcode = :pcode
#end
ORDER BY t.code DESC LIMIT 1
#end

#macro($findByOrTextbookCode(pcode, textbookCode, level))
SELECT * FROM section t WHERE
1=1
#if(textbookCode)
AND t.textbook_code = :textbookCode
#end
#if(pcode)
AND t.pcode = :pcode
#end
#if(level)
AND t.level = :level
#end
ORDER BY t.sequence ASC, t.code ASC
#end

## 根据教材代码获取章节列表
#macro($findByTextbookCodeAndName(textBookCode,name))
SELECT * FROM section WHERE textbook_code = :textBookCode 
#if(name)
	and name like :name
#end
ORDER BY level ASC,code ASC
#end

## 根据版本代码获取章节列表
#macro($findByTextbookCategoryCode(codes,textbookCategoryCode))
SELECT s.* FROM section s
 inner join textbook t on t.code=s.textbook_code and t.category_code=:textbookCategoryCode and s.code in (:codes)
 ORDER BY level ASC,code ASC
#end
