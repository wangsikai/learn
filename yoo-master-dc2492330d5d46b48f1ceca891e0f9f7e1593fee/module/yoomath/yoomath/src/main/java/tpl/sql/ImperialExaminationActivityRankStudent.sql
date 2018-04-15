## 获取学生最优排名数据,分数相同,时间短优先
#macro($queryBest(code,type,userId))
SELECT * FROM imperial_exam_activity_rank_student
WHERE user_id = :userId AND activity_code =:code AND TYPE =:type and do_time is not null
ORDER BY manual_score DESC,do_time ASC LIMIT 1
#end

## 获取学生排名列表
#macro($queryTopList(code,type,limit))
SELECT
	t1.manual_score,t1.do_time,t1.score,t1.user_id,ff.homework_id,ff.clazz_id,t1.tag 
FROM
	imperial_exam_activity_rank_student t1
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
				imperial_exam_activity_rank_student t0
			INNER JOIN (
				SELECT
					user_id,
					MAX(manual_score) AS max_score
				FROM
					imperial_exam_activity_rank_student
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
INNER JOIN imperial_exam_homework_student ff on ff.id = t1.activity_homework_id
AND t1.manual_score = t2.max_score and t1.do_time = t2.min_do_time
INNER JOIN student_homework sh ON t1.user_id = sh.student_id and ff.homework_id = sh.homework_id
WHERE
	t1.activity_code = :code
AND t1.type = :type
GROUP BY t1.user_id
ORDER BY t1.manual_score DESC,t1.do_time ASC,sh.submit_at ASC
LIMIT :limit
#end

## 获取当前学生最佳班级在当前阶段的排名
#macro($getRank(code,type,userId,score,doTime,submitAt))
SELECT
	count(*)
FROM
	imperial_exam_activity_rank_student t1
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
				imperial_exam_activity_rank_student t0
			INNER JOIN (
				SELECT
					user_id,
					MAX(manual_score) AS max_score
				FROM
					imperial_exam_activity_rank_student
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
INNER JOIN imperial_exam_homework_student ff on ff.id = t1.activity_homework_id
AND t1.manual_score = t2.max_score and t1.do_time = t2.min_do_time
INNER JOIN student_homework sh ON t1.user_id = sh.student_id and ff.homework_id = sh.homework_id
WHERE
	t1.activity_code = :code
	AND t1.type = :type
	AND t1.manual_score > :score 
	OR (t1.manual_score = :score AND t1.do_time < :doTime)
	#if(submitAt)
	OR (t1.manual_score = :score AND t1.do_time = :doTime AND sh.submit_at < :submitAt)
	#end
#end
