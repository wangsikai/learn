##根据书本ID 获取用户书本列表
#macro(getUserSchoolBookByBookId(bookId))
SELECT * FROM user_school_book WHERE book_id=:bookId and status=0
#end

##根据userSchoolBookIDs 更改状态
#macro(updateUserSchoolBookStatus(ids,status))
UPDATE user_school_book SET status=:status WHERE id IN (:ids)
#end

##更新用户书本表状态
#macro($updateUserSchoolBook(schoolBookId,status))
UPDATE user_school_book SET status=:status WHERE school_book_id =:schoolBookId
#end