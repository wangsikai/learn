##章节和题目数量统计关系
#macro($findQuestionStatByTextbookCode(textbookCode,version))
SELECT CODE,level,pcode,name,SUM(c1) total,SUM(c2) pass,SUM(c3) nopass,SUM(c4) editing,SUM(c5) onepass FROM(
	(SELECT s.code,s.level,s.pcode,s.name,COUNT(t.id) AS c1, 0 AS c2,0 AS c3,0 AS c4,0 AS c5 FROM question t
	INNER JOIN question_section f on t.id = f.question_id and f.textbook_code = :textbookCode
	INNER JOIN section s ON s.code=f.section_code 
	WHERE t.status !=5 AND t.sub_flag !=1 AND t.same_show_id IS NULL
		#if(version == 1)
			and f.v1 = 1
		#end
		#if(version == 2)
			and f.v2 = 1
		#end
	GROUP BY s.code)
	UNION
	(
	SELECT s.code,s.level,s.pcode,s.name,0 AS c1,COUNT(t.id) AS c2,0 AS c3,0 AS c4,0 AS c5 FROM question t
	INNER JOIN question_section f on t.id = f.question_id and f.textbook_code = :textbookCode
	INNER JOIN section s ON s.code=f.section_code 
	WHERE t.`status`=2 AND t.sub_flag !=1 AND t.same_show_id IS NULL
		#if(version == 1)
			and f.v1 = 1
		#end
		#if(version == 2)
			and f.v2 = 1
		#end
	GROUP BY s.code
	)
	UNION
	(
	  SELECT s.code,s.level,s.pcode,s.name,0 AS c1,0 AS c2,COUNT(t.id) AS c3,0 AS c4,0 AS c5 FROM question t
	  INNER JOIN question_section f on t.id = f.question_id and f.textbook_code = :textbookCode
	INNER JOIN section s ON s.code=f.section_code 
	WHERE t.`status`=3 AND t.sub_flag !=1 AND t.same_show_id IS NULL
		#if(version == 1)
			and f.v1 = 1
		#end
		#if(version == 2)
			and f.v2 = 1
		#end
	GROUP BY s.code
	)
	UNION
	(
	  SELECT s.code,s.level,s.pcode,s.name,0 AS c1,0 AS c2,0 AS c3,COUNT(t.id) AS c4,0 AS c5 FROM question t
	  INNER JOIN question_section f on t.id = f.question_id and f.textbook_code = :textbookCode
	INNER JOIN section s ON s.code=f.section_code
	WHERE t.`status`=0 AND t.sub_flag !=1 AND t.same_show_id IS NULL
		#if(version == 1)
			and f.v1 = 1
		#end
		#if(version == 2)
			and f.v2 = 1
		#end
	GROUP BY s.code
	)
	UNION
	(
	  SELECT s.code,s.level,s.pcode,s.name,0 AS c1,0 AS c2,0 AS c3,0 AS c4,COUNT(t.id) AS c5 FROM question t
	  INNER JOIN question_section f on t.id = f.question_id and f.textbook_code = :textbookCode
	INNER JOIN section s ON s.code=f.section_code
	WHERE t.`status`=4 AND t.sub_flag !=1 AND t.same_show_id IS NULL
		#if(version == 1)
			and f.v1 = 1
		#end
		#if(version == 2)
			and f.v2 = 1
		#end
	GROUP BY s.code
	)
) t2 GROUP BY CODE
#end

##根据questionId查找题目所对应的章节
#macro($ymGetByQuestionId(questionId))
SELECT t.* FROM question_section t WHERE t.question_id = :questionId AND t.v2 = 1
#end

##根据quesetionId列表批量查找题目对应的章节数据
#macro($ymGetByQuestionIds(questionIds))
SELECT t.* FROM question_section t WHERE t.question_id IN :questionIds AND t.v2 = 1
#end