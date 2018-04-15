## 查询当前最大的code值
#macro($opGetMaxCode())
SELECT * FROM sms_template ORDER BY code DESC LIMIT 1
#end