## 获取用户某个教材下的每日练设置
#macro($zyFindByTextbookCode(userId,textbookCode))
SELECT * FROM daily_practice_settings WHERE user_id = :userId AND textbook_code = :textbookCode
#end