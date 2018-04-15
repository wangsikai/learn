#macro($zycFindByAccount(accountId))
SELECT t.* FROM user u, student t where u.id = t.id AND u.account_id = :accountId
#end

##查找班级成员
#macro($queryClazzMember(hkClazzId))
SELECT c.name realname,e.name accountname,f.name schoolname,c.id FROM homework_class a 
INNER JOIN homework_student_class b ON a.id = b.class_id AND a.id = :hkClazzId
INNER JOIN student c ON c.id = b.student_id
INNER JOIN user d ON c.id = d.id
INNER JOIN account e ON d.account_id = e.id
LEFT JOIN school f ON c.school_id = f.id
WHERE b.status = 0
#end

##查询学生
#macro($queryStudent(name))
SELECT c.name accountName,a.name realName,d.name schoolName,a.id FROM student a
INNER JOIN USER b ON a.id = b.id
INNER JOIN account c ON b.account_id = c.id
LEFT JOIN school d ON a.school_id = d.id
#if(name)
	WHERE a.name LIKE :name OR c.name LIKE :name
#end
#end

## 批量查询所有学生数据
#macro($zycQuery())
SELECT t.* FROM student t INNER JOIN user u ON u.id = t.id
WHERE u.status = 0 AND t.id < :next ORDER BY t.id DESC
#end
