## 根据教材代码获取章节列表
#macro($findByTextbookCode(textBookCode,textBookCodes,levelParam))
SELECT * FROM section WHERE
1=1
#if(textBookCode)
AND textbook_code = :textBookCode
#end
#if(textBookCodes)
AND textbook_code IN (:textBookCodes)
#end
#if(levelParam)
AND level = :levelParam
#end
ORDER BY level ASC,code ASC
#end

#macro($getPSection(code))
select * from section where code=(select pcode from section where code=:code)
#end

#macro($findAllSectionName(codes))
select s.* from section s where s.code in (:codes)
#end

#macro($findNextSection(code,textbookCode))
select * from section n where n.code not in (
  select t.pcode from section t where t.textbook_code = :textbookCode
) and n.textbook_code = :textbookCode
#if(code)
  and RPAD(n.code, 14, 0) > RPAD(:code, 14, 0) AND n.code <> :code
#end
order by RPAD(n.code, 14, 0) asc limit 1
#end

## 根据code查找到此章节下所有子章节
#macro($findSectionChildren(code))
SELECT t.code FROM section t WHERE t.code LIKE :code
#end

## 根据章节code查询此章节下面所有叶子节点
#macro($findChildrenLeaf(code))
SELECT t.code FROM section t WHERE t.code LIKE :code AND NOT EXISTS (
  SELECT * FROM section m WHERE m.pcode = t.code
)
#end

## 根据教材码查询此章节下所有叶子节点
#macro($findLeafByTextbook(code))
SELECT t.code FROM section t WHERE t.textbook_code = :code AND NOT EXISTS (
  SELECT * FROM section m WHERE m.pcode = t.code
)
#end

## 根据章节码查询是否本章综合与测试
#macro($findntegrateSectionCode(code))
select * from section where code= :code
and name = '本章综合与测试'
and level = 2
#end