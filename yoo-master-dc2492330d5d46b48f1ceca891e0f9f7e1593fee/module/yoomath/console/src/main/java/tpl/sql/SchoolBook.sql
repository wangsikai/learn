## 查询学校书本
#macro($getSchoolBook(schoolId,bookId))
select * from school_book where school_id =:schoolId and book_id=:bookId
#end


## 查询学校书本
#macro($getSbList(bookId))
select * from school_book where book_id =:bookId and status = 0
#end

## 批量查询学校书本
#macro($mgetSbList(bookIds))
select * from school_book where book_id in (:bookIds) and status = 0
#end

##通过书本id 删除对应的学校书本
#macro($delSchoolBookByBookId(bookId))
update school_book set status = 2 where book_id = :bookId
#end

## 查询学校书本
#macro($getBookCount(schoolId))
select count(id) from school_book where school_id =:schoolId and status = 0
#end


## 查询学校书本
#macro($mgetBookCount(schoolIds))
select count(id) count1,school_id from school_book where school_id in(:schoolIds) and status = 0 group by school_id
#end


## 更新学校对应书本状态
#macro($updateSchoolBook(schoolId,status))
update school_book  set status = :status where school_id = :schoolId
#end


## 查询学校对应的书本
#macro($getBookIdsBySchool(schoolId))
select book_id from school_book where status = 0 and school_id = :schoolId
#end

##根据书本 学校ID拼串查询 schoolbook
#macro($findBySchoolAndBooK(unionIds))
SELECT t.id,t.book_id FROM (SELECT id,book_id,CONCAT(school_id,book_id) AS union_id FROM school_book  ) t  WHERE t.union_id IN (:unionIds)
#end

