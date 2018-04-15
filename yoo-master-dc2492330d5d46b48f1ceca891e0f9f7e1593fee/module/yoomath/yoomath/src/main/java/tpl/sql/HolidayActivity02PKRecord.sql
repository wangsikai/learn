## 查询当前用户的所有pk记录
#macro($findAllPkRecords(code,userId))
SELECT * FROM holiday_activity_02_pkrecord WHERE activity_code = :code and user_id = :userId
#end

## 随机获取一个不等于该用户的pk记录
#macro($getRandomPkRecords(code,userId))
SELECT * FROM holiday_activity_02_pkrecord
WHERE id >= ((
(SELECT MAX(id) FROM holiday_activity_02_pkrecord WHERE activity_code = :code and user_id != :userId and power is not null)-
(SELECT MIN(id) FROM holiday_activity_02_pkrecord WHERE activity_code = :code and user_id != :userId and power is not null)
) * RAND() + (SELECT MIN(id) FROM holiday_activity_02_pkrecord WHERE activity_code = :code and user_id != :userId and power is not null) 
)
and user_id != :userId
and activity_code = :code
and power is not null
LIMIT 1000
#end

## 查询当前用户的所有pk记录
#macro($getAllPkRecordBySize(code,userId,size))
SELECT p.id as id,
p.pk_at as pkAt,
p.power as power,
u.name as pkName
FROM holiday_activity_02_pkrecord p
INNER JOIN USER u ON p.pk_user_id = u.id
WHERE activity_code = :code 
AND user_id = :userId
AND p.power IS NOT NULL
ORDER BY p.pk_at DESC
#if(size)
LIMIT :size
#end
#end