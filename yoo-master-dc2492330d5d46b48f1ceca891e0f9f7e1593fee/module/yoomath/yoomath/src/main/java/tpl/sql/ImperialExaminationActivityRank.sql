## 获取老师最优排名数据,分数相同,时间短优先
#macro($queryBest(code,type,userId))
SELECT * FROM imperial_exam_activity_rank
WHERE user_id = :userId AND activity_code =:code AND TYPE =:type and do_time is not null
ORDER BY manual_score DESC,do_time ASC LIMIT 1
#end

## 获取老师排名列表
#macro(queryTopList(code,type,limit))
SELECT
	t1.manual_score,t1.do_time,t1.score,t1.user_id,ff.homework_id,ff.clazz_id 
FROM
	imperial_exam_activity_rank t1
INNER JOIN (
	SELECT
		t3.user_id,
		MIN(t3.do_time) min_do_time,
		t3.manual_score AS max_score
	FROM
		(
			SELECT
				t0.*
			FROM
				imperial_exam_activity_rank t0
			INNER JOIN (
				SELECT
					user_id,
					MAX(manual_score) AS max_score
				FROM
					imperial_exam_activity_rank
				WHERE
					activity_code = :code
				AND type = :type
				GROUP BY user_id
			) t ON t0.user_id = t.user_id
			AND t0.manual_score = t.max_score
			WHERE
				t0.activity_code = :code
			AND t0.type = :type
		) t3
	GROUP BY
		t3.user_id
) t2 ON t1.user_id = t2.user_id
INNER JOIN imperial_exam_homework ff on ff.id = t1.activity_homework_id
AND t1.manual_score = t2.max_score and t1.do_time = t2.min_do_time
WHERE
	t1.activity_code = :code
AND t1.type = :type
GROUP BY t1.user_id
ORDER BY t1.manual_score DESC,t1.do_time ASC
LIMIT :limit
#end

## 获取当前老师最佳班级在当前阶段的排名
#macro($getRank(code,type,userId,score,doTime))
select count(*) from
	(
		SELECT a.* FROM (
			SELECT t.* FROM imperial_exam_activity_rank t where t.activity_code =:code AND t.TYPE =:type
			ORDER BY t.manual_score DESC,t.do_time ASC
		) a
		GROUP BY a.user_id
	) ff
	WHERE ff.manual_score > :score OR (ff.manual_score = :score AND ff.do_time < :doTime)
#end

## 获取老师排名列表
#macro($queryTopList2(code,type,limit,room))
SELECT
	t1.manual_score,t1.do_time,t1.score,t1.user_id,ff.homework_id,ff.clazz_id,t1.tag 
FROM
	imperial_exam_activity_rank t1
INNER JOIN (
	SELECT
		t3.user_id,
		MIN(t3.do_time) min_do_time,
		t3.manual_score AS max_score
	FROM
		(
			SELECT
				t0.*
			FROM
				imperial_exam_activity_rank t0
			INNER JOIN (
				SELECT
					user_id,
					MAX(manual_score) AS max_score
				FROM
					imperial_exam_activity_rank
				WHERE
					activity_code = :code
				AND type = :type
				AND room = :room
				GROUP BY user_id
			) t ON t0.user_id = t.user_id
			AND t0.manual_score = t.max_score
			WHERE
				t0.activity_code = :code
			AND t0.type = :type
			AND t0.room = :room
		) t3
	GROUP BY
		t3.user_id
) t2 ON t1.user_id = t2.user_id
INNER JOIN imperial_exam_homework ff on ff.id = t1.activity_homework_id
AND t1.manual_score = t2.max_score and t1.do_time = t2.min_do_time
WHERE
	t1.activity_code = :code
AND t1.type = :type
AND t1.room = :room
GROUP BY t1.user_id
ORDER BY t1.manual_score DESC,t1.do_time ASC
LIMIT :limit
#end

## 获取当前老师最佳班级在当前阶段的排名
#macro($getRank2(code,type,userId,score,doTime,room))
select count(*) from
	 (
		SELECT * FROM (
			SELECT
				t0.*
			FROM
				imperial_exam_activity_rank t0
			INNER JOIN (
				SELECT
					user_id,
					MAX(manual_score) AS max_score,
					do_time
				FROM
					imperial_exam_activity_rank
				WHERE
					activity_code = :code
				AND type = :type
				AND room = :room
				GROUP BY user_id
			) t ON t0.user_id = t.user_id
			AND t0.manual_score = t.max_score
			WHERE
				t0.activity_code = :code
			AND t0.type = :type
			ORDER BY max_score DESC,t.do_time ASC 
		 ) a GROUP BY a.user_id
	) ff
	WHERE ff.manual_score > :score OR (ff.manual_score = :score AND ff.do_time < :doTime)
#end
