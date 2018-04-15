## 科举考试,根据阶段和老师查询对应的作业
#macro($list(code,type,userId,tag,room))
SELECT * FROM imperial_exam_homework 
WHERE activity_code = :code and type =:type and user_id = :userId
#if(tag)
	AND tag = :tag
#end
#if(room)
	AND room = :room
#end
ORDER BY tag desc
#end

## 科举考试,查询下发情况
#macro($countHk(code,type,userId,flag))
SELECT count(*) FROM imperial_exam_homework a
inner join homework b on a.homework_id = b.id
WHERE a.activity_code = :code and a.type =:type and a.user_id = :userId
#if(flag == 1)
	and b.status != 3
#end
#if(flag == 2)
	and b.status = 3
#end
#end

## 科举考试,查询下发情况,关联tag
#macro($countHkByTag(code,type,userId,flag,tag,room))
SELECT count(*) FROM imperial_exam_homework a
inner join homework b on a.homework_id = b.id
WHERE a.activity_code = :code and a.type =:type and a.user_id = :userId
#if(flag == 1)
	and b.status != 3
#end
#if(flag == 2)
	and b.status = 3
#end
#if(tag)
	and a.tag = :tag
#end
#if(room)
	and a.room = :room
#end
#end

## 科举考试,查询下发情况,关联tag
#macro($existIssueByTag(code,type,userId,tag,endTime))
SELECT count(*) FROM imperial_exam_homework a
inner join homework b on a.homework_id = b.id
WHERE a.activity_code = :code and a.type =:type and a.user_id = :userId
	and b.status = 3
#if(tag)
	and a.tag = :tag
#end
and b.issue_at IS NOT NULL
and b.issue_at < :endTime
#end
