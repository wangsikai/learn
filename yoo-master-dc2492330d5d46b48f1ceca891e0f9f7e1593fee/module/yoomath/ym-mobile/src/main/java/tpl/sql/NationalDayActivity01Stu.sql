## 查询排行榜前x名
#macro($findNationalDayStuTopN(topn))
SELECT *
FROM national_day_activity_01_stu
ORDER BY right_count DESC, user_id ASC 
LIMIT :topn
#end

## 查询指定用户的参与信息
#macro($findStuByUser(userId))
select * from (
SELECT
    stu.*,@rownum \:=@rownum + 1 AS rownum
FROM
    (
        SELECT
            *
        FROM
            national_day_activity_01_Stu
        ORDER BY right_count DESC, user_id ASC 
    ) AS stu,
    (SELECT @rownum \:=0) r) t
WHERE t.user_id = :userId
#end