## 查询排行榜前x名
#macro($findTopN(topn))
SELECT *
FROM national_day_activity_01_tea
ORDER BY score DESC, homework_count DESC, commit_rate DESC, user_id ASC 
LIMIT :topn
#end

## 查询指定用户的参与信息
#macro($findTeaByUser(userId))
select * from (
SELECT
    tea.*,@rownum \:=@rownum + 1 AS rownum
FROM
    (
        SELECT
            *
        FROM
            national_day_activity_01_tea
        ORDER BY score DESC, homework_count DESC, commit_rate DESC, user_id ASC 
    ) AS tea,
    (SELECT @rownum \:=0) r) t
WHERE t.user_id = :userId
#end