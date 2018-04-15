## 查询历史列表
#macro($zyQuery(userId,textbookCode))
SELECT * FROM daily_practise t WHERE t.user_id = :userId
AND t.practise_id = 0
#if(next)
AND t.id < :next
#end
#if(textbookCode)
AND t.textbook_code = :textbookCode
#end
ORDER BY id DESC
#end

## 最新的一条练习记录
#macro($zyFindLatest(userId,textbookCode))
SELECT * FROM daily_practise t WHERE t.user_id = :userId
AND  t.practise_id = 0 AND t.textbook_code = :textbookCode ORDER BY t.create_at DESC LIMIT 1
#end

## 此教材已经练习了多少天
#macro($zyGetPractiseDays(userId,textbookCode,isFindTotal))
SELECT count(*) FROM daily_practise t WHERE t.user_id = :userId AND t.textbook_code = :textbookCode AND t.practise_id = 0
#if(isFindTotal==false)
AND t.update_at IS NOT NULL
#end
#end
