#macro($zycFindByAccount(accountId))
SELECT t.* FROM user u, teacher t WHERE u.id = t.id AND u.account_id = :accountId
#end

#macro($zycFindByAccountNameOrMobileOrEmail(loginValue))
SELECT t.* FROM teacher t INNER JOIN user u ON u.id = t.id
INNER JOIN account a ON a.id = u.account_id
WHERE a.name = :loginValue or a.mobile = :loginValue or a.email = :loginValue
#end

##分页获取teacher
#macro($zycGetAllByPage())
SELECT * FROM teacher WHERE id < :next ORDER BY id DESC
#end

##查询老师的总数
#macro($zycGetTeachersCount())
	select count(*) from teacher
#end

##分批查询老师
#macro($zycGetTeachers(start,num))
	select * from teacher limit :start,:num
#end

## 更新老师名字
#macro($updateName(id, name))
UPDATE teacher SET name = :name WHERE id = :id
#end