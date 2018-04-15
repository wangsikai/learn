## 获取未登录过APP的渠道教师用户的电话集合
#macro($getMobilesFromNotAPPTeacher())
SELECT a.mobile FROM account a
 INNER JOIN USER u ON u.account_id=a.id AND u.user_type=1 AND u.user_channel_code != 10000
 AND u.import0=1
 INNER JOIN teacher t ON t.id=u.id AND t.phase_code=2
 WHERE a.mobile IS NOT NULL
 AND NOT EXISTS
 (
 SELECT sh.id FROM session_history sh WHERE sh.account_id=a.id AND (sh.device_type = 1 OR sh.device_type = 3)
 )
 AND NOT EXISTS
 (
 SELECT sh.id FROM session sh WHERE sh.account_id=a.id AND (sh.device_type = 1 OR sh.device_type = 3)
)
#end