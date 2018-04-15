## 根据code查找班级
#macro($zyFindByCode(code))
SELECT * FROM homework_class WHERE code = :code  AND status=0
#end

## 根据code计算班级数量
#macro($zyCountByCode(code))
SELECT count(id) FROM homework_class WHERE code = :code
#end

## 根据name计算班级数量
#macro($zyCountByName(name, teacherId))
SELECT count(id) FROM homework_class WHERE name = :name AND status = 0 AND teacher_id = :teacherId
#end

## 查询
#macro($zyQuery(teacherId,status,size))
SELECT * FROM homework_class WHERE teacher_id = :teacherId
#if(status)
 AND status = :status
#end
#if(status==0)
ORDER BY id DESC
#end
#if(status==1)
ORDER BY update_at DESC
#end
#if(size)
 limit :size
#end
#end

##增加学生人数
#macro($zyIncrStudentNum(id,delta,updateAt))
UPDATE homework_class SET student_num = student_num + :delta,update_at = :updateAt WHERE id = :id
#end

##查找History记录数
#macro($zyHistoryCount(teacherId))
SELECT COUNT(*) FROM homework_class t WHERE t.teacher_id = :teacherId AND t.status = 1
#end

##查找Current记录数
#macro($zyCurrentCount(teacherId))
SELECT COUNT(*) FROM homework_class t WHERE t.teacher_id = :teacherId AND t.status = 0
#end

## 锁定
#macro($zyLock(id,teacherId))
UPDATE homework_class 
SET lock_status = 1 
WHERE teacher_id = :teacherId AND status = 0 AND lock_status = 0 AND id = :id
#end

## 解锁
#macro($zyUnlock(id,teacherId))
UPDATE homework_class 
SET lock_status = 0 
WHERE teacher_id = :teacherId AND status = 0 AND lock_status = 1 AND id = :id
#end

##根据第三方查找班级
#macro($findByFromCode(frm, codes, teacherId))
SELECT * FROM homework_class where clazz_from=:frm and from_code in (:codes)
#if(teacherId)
 AND teacher_id =:teacherId
#end
#end

##根据第三方更新教师的ID
#macro($updateTeacherByFromCode(frm, fromCode, teacherId))
UPDATE homework_class set teacher_id =:teacherId where clazz_from=:frm and from_code=:fromCode
#end

##获取指定班级的已经下发作业的平均完成率
#macro($getMapAVGComplete(clazzIds))
SELECT AVG(h.commit_count/h.distribute_count) AS avgs,h.homework_class_id AS cid FROM homework h
 WHERE h.del_status=0 AND h.status=3
 GROUP BY h.homework_class_id HAVING h.homework_class_id IN (:clazzIds)
#end

## 批量更新指定的班级进度
#macro($updateBookProcess(classIds,bookVersionId,bookCataId))
UPDATE homework_class SET book_version_id = :bookVersionId, book_cata_id = :bookCataId WHERE id IN :classIds
#end

## 清空教师教辅设置
#macro($clearBookSetting(userId))
UPDATE homework_class SET book_version_id = null, book_cata_id = null WHERE teacher_id = :userId AND status = 0
#end

## 重置教辅
#macro($resetBookSetting(bookIds,userId))
UPDATE homework_class SET book_version_id = null, book_cata_id = null WHERE teacher_id = :userId AND status = 0
AND book_version_id IN ( SELECT id FROM book_version WHERE book_id IN (:bookIds) )
#end

## 根据id列表查找有效班级
#macro($findEnableClass(ids))
SELECT DISTINCT t.* FROM homework_class t WHERE t.id IN :ids AND t.status = 0
#end

## 根据用户id查找此用户的渠道商下的学校信息
#macro($zyFindUserChannelSchoolClass(userId,key))
SELECT t.* FROM homework_class t
INNER JOIN user u ON u.id = t.teacher_id
INNER JOIN teacher te ON te.id = u.id,
(
  SELECT m.user_channel_code, s.school_id FROM user m INNER JOIN student s ON s.id = m.id WHERE m.id = :userId
) z
WHERE u.user_channel_code = z.user_channel_code  AND t.status = 0 AND u.status = 0 AND te.school_id = z.school_id
#if(key)
AND
(
  u.name LIKE :key
  OR
  t.name LIKE :key
  OR
  t.code LIKE :key
)
#end
#end

## 非校级用户根据教师手机号码或者code进行查询
#macro($zyFindByCodeOrMobile(key))
SELECT t.* FROM homework_class t
INNER JOIN user u ON t.teacher_id = u.id
INNER JOIN account a ON a.id = u.account_id
WHERE
(t.code = :key OR a.mobile = :key) AND t.status = 0 AND u.status = 0
#end

## 需要确认
#macro($zyNeedConfirm(id,teacherId))
UPDATE homework_class 
SET need_confirm = 1,lock_status = 0
WHERE teacher_id = :teacherId AND status = 0  AND id = :id
#end

## 不需要确认
#macro($zyNotNeedConfirm(id,teacherId))
UPDATE homework_class 
SET need_confirm = 0,lock_status = 0
WHERE teacher_id = :teacherId AND status = 0 AND id = :id
#end

## 查询
#macro($listClazzsOrderByStuNum(teacherId,status,size))
SELECT * FROM homework_class WHERE teacher_id = :teacherId
#if(status)
 AND status = :status
#end
order by student_num desc
#end

## 查询
#macro($zyAllCount(teacherId,status))
SELECT count(*) FROM homework_class WHERE teacher_id = :teacherId
#if(status)
 AND status = :status
#end
#end