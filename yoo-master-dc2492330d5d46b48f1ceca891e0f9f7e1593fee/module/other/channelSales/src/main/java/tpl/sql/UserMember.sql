##获取用户会员信息记录
#macro($csFindByUserIds(userIds))
SELECT * FROM user_member WHERE user_id IN :userIds
#end

##获取用户会员信息记录
#macro($csFindByUserId(userId))
SELECT * FROM user_member WHERE user_id = :userId
#end

## 统计班级会员数量
#macro($csCountByClass(endDate, classIds))
SELECT COUNT(t.id) AS member_num, h.class_id AS id FROM user_member t
INNER JOIN user u ON t.user_id = u.id
INNER JOIN homework_student_class h ON h.student_id = u.id
INNER JOIN account a ON a.id = u.account_id
WHERE h.class_id IN :classIds AND t.end_at >= :endDate AND u.status = 0 AND h.status = 0 AND a.status = 0
GROUP BY h.class_id
#end