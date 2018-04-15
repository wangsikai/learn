##查找未删除的书本版本
#macro($listBookVersion(bookId))
select t.* from book_version t where t.book_id=:bookId AND t.status!=5
#end

##查找未删除的书本版本
#macro($mListBookVersion(bookIds, bookStatus))
select t.* from book_version t where t.book_id in (:bookIds) AND t.status!=5 
#if(bookStatus)
 and t.status=:bookStatus
#end
order by t.create_at ASC
#end

##查找未删除的书本主版本
#macro($mListMainBookVersion(bookIds, bookStatus))
select t.* from book_version t where t.book_id in (:bookIds) AND t.status!=5 AND t.main_flag=1
#if(bookStatus)
 and t.status=:bookStatus
#end
#end