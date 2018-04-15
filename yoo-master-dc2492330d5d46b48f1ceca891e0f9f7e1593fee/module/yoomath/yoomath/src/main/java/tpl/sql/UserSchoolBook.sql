##查询发布状态的学校图书
#macro($userSelectedBook(userId))
SELECT usb.book_id,usb.id FROM user_school_book usb where status=0 and user_id=:userId
#end

##查询用户已选择的图书
#macro($getUserBook(bookIds,userId))
SELECT * FROM user_school_book WHERE user_id=:userId
#if(bookIds)
 AND book_id in (:bookIds) 
#end
#end

##更改书本状态 
#macro($changeBookStatus(ids,updateAt,userId,status))
UPDATE user_school_book SET status=:status,update_at=:updateAt where  book_id in(:ids) and user_id=:userId
#end
