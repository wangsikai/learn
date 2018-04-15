##查询旧统计数据
#macro($getKnowPoint(code))
SELECT SUM(c1) total,SUM(c2) pass,SUM(c3) nopass,SUM(c4) editing ,SUM(c5) onepass ,meta_code FROM  (
		(SELECT COUNT(a.question_id) AS c1,0 AS c2,0 AS c3,0 AS c4,0 AS c5,meta_code FROM question_metaknow a
		INNER JOIN question b ON a.`question_id` = b.id and b.status !=5 and b.same_show_id IS NULL
		 GROUP BY meta_code)
		 UNION 
		 (SELECT 0 AS c1,COUNT(a.question_id) AS c2,0 AS c3,0 AS c4,0 AS c5,meta_code FROM question_metaknow a
		INNER JOIN question b ON a.`question_id` = b.id AND b.status =2 and b.same_show_id IS NULL
		 GROUP BY meta_code
		 )
		 UNION
		 (SELECT 0 AS c1,0 AS c2,COUNT(a.question_id) AS c3,0 AS c4,0 AS c5,meta_code FROM question_metaknow a
		INNER JOIN question b ON a.`question_id` = b.id AND b.STATUS =3 and b.same_show_id IS NULL
		 GROUP BY meta_code
		 )
		 UNION
		 (SELECT 0 AS c1,0 AS c2,0 AS c3,COUNT(a.question_id) AS c4,0 AS c5,meta_code FROM question_metaknow a
		INNER JOIN question b ON a.`question_id` = b.id AND b.STATUS =0 and b.same_show_id IS NULL
		 GROUP BY meta_code
		 )
		 UNION
		 (SELECT 0 AS c1,0 AS c2,0 AS c3,0 AS c4,COUNT(a.question_id) AS c5,meta_code FROM question_metaknow a
		INNER JOIN question b ON a.`question_id` = b.id AND b.STATUS =4 and b.same_show_id IS NULL
		 GROUP BY meta_code
		 )
	) tt where meta_code in :code GROUP BY meta_code
#end


##查询新统计数据
#macro($getNewKnowPoint(code))
SELECT SUM(c1) total,SUM(c2) pass,SUM(c3) nopass,SUM(c4) editing ,SUM(c5) onepass ,knowledge_code FROM  (
		(SELECT COUNT(a.question_id) AS c1,0 AS c2,0 AS c3,0 AS c4,0 AS c5,knowledge_code FROM question_knowledge a
		INNER JOIN question b ON a.`question_id` = b.id and b.status !=5 and b.same_show_id IS NULL
		 GROUP BY knowledge_code)
		 UNION 
		 (SELECT 0 AS c1,COUNT(a.question_id) AS c2,0 AS c3,0 AS c4,0 AS c5,knowledge_code FROM question_knowledge a
		INNER JOIN question b ON a.`question_id` = b.id AND b.status =2 and b.same_show_id IS NULL
		 GROUP BY knowledge_code
		 )
		 UNION
		 (SELECT 0 AS c1,0 AS c2,COUNT(a.question_id) AS c3,0 AS c4,0 AS c5,knowledge_code FROM question_knowledge a
		INNER JOIN question b ON a.`question_id` = b.id AND b.STATUS =3 and b.same_show_id IS NULL
		 GROUP BY knowledge_code
		 )
		 UNION
		 (SELECT 0 AS c1,0 AS c2,0 AS c3,COUNT(a.question_id) AS c4,0 AS c5,knowledge_code FROM question_knowledge a
		INNER JOIN question b ON a.`question_id` = b.id AND b.STATUS =0 and b.same_show_id IS NULL
		 GROUP BY knowledge_code
		 )
		 UNION
		 (SELECT 0 AS c1,0 AS c2,0 AS c3,0 AS c4,COUNT(a.question_id) AS c5,knowledge_code FROM question_knowledge a
		INNER JOIN question b ON a.`question_id` = b.id AND b.STATUS =4 and b.same_show_id IS NULL
		 GROUP BY knowledge_code
		 )
	) tt where knowledge_code in :code GROUP BY knowledge_code
#end

##删除统计表
#macro($deleteknowledgeQuestionStat(version))
	delete from knowledge_question_stat 
	#if(version)
	where version = :version
	#end
#end

