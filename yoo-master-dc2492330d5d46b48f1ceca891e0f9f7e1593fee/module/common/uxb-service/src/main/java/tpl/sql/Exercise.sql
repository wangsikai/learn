## 查询习题页 version 2.0
#macro($queryExercise(createId,gradeCode,name))
SELECT * FROM exercise WHERE status = 0
#if(createId)
 AND create_id = :createId
#end
#if(gradeCode)
 AND grade_code = :gradeCode
#end
#if(name)
 AND name LIKE :name
#end
 AND id < :next ORDER BY id DESC
#end

## 查询习题页 version 2.1
#macro($queryExercisesByTime(createId,gradeCode,name,seque,ownCursor,limitRows))
SELECT * FROM exercise WHERE status = 0
#if(createId)
 AND create_id = :createId
#end
#if(gradeCode)
 AND grade_code = :gradeCode
#end
#if(name)
 AND name LIKE :name
#end
#if(ownCursor)
 #if(seque)
  #if(seque == 'desc')
  AND unix_timestamp(update_at) * 1000 < :ownCursor
  #end
  #if(seque == 'asc')
  AND unix_timestamp(update_at) * 1000 > :ownCursor
  #end
 #end
#end
  ORDER BY update_at
#if(seque)
 #if(seque == 'desc')
  DESC
 #end
#end
LIMIT :limitRows
#end


## 判断老师有没有创建名称为name的习题页
#macro($existName(createId,name))
SELECT count(id) FROM exercise WHERE create_id = :createId AND name = :name
#end

## 查询用户下的所有习题页
#macro($queryTotalExercise(userId))
SELECT COUNT(id) FROM exercise WHERE 1=1
#if(userId)
 AND create_id=:userId
#end
#end