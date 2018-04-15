## 获取用户所有互动
#macro($query(userId))
	select * from interaction where teacher_id = :userId order by create_at desc
#end

## 获取首页互动,只显示10条,不显示忽略的，不显示不在首页显示的
#macro($queryIndex(userId))
	select * from interaction where teacher_id = :userId and status not in (3,4) and homepage_show != 0
	order by create_at desc limit 0,10
#end

## 获取学生的荣耀榜(首页)
#macro($queryIndexHonourList(clazzIds))
	select * from interaction where status = 2 
	#if(clazzIds)
		and class_id in (:clazzIds)
	#end
	order by update_at desc limit 0,10
#end

## 获取学生的荣耀榜(非首页)
#macro($queryHonourList(clazzIds,userId))
	select * from interaction where status = 2 
	#if(clazzIds)
		and class_id in (:clazzIds)
	#end
	#if(userId)
		and student_id = :userId
	#end
	order by update_at desc
#end

## 新版本进步，退步，三次没有交作业的数据拉取
#macro($queryIndex2(userId,classId))
	SELECT * FROM 
		(
		SELECT * FROM interaction WHERE teacher_id = :userId AND class_id = :classId
		AND STATUS NOT IN (3,4) AND TYPE IN(2,3) AND p1 IS NOT NULL AND p2 IS NOT NULL
		UNION ALL
		SELECT * FROM interaction WHERE teacher_id = :userId AND class_id = :classId
		AND STATUS NOT IN (3,4) AND TYPE IN(4)
		) t 
	ORDER BY t.create_at DESC LIMIT 0,10
#end
