## 查找子题
#macro($getSubQuestions(parentId,parentIds))
SELECT * FROM question WHERE del_status=0
	#if(parentId)
	AND parent_id = :parentId
	#end
	#if(parentIds)
	AND parent_id IN (:parentIds)
	#end
	ORDER BY sequence ASC
#end

## 根据code查找题目
#macro($zyFindByCode(code))
SELECT * FROM question WHERE status = 2 AND del_status = 0 AND sub_flag = 0 AND code = :code
#end