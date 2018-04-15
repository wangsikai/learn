##查找非删除的单个书本版本
#macro($listIndexBookVersion(bookId))
select t.* from book_version t where t.book_id=:bookId AND t.status!=5
#end

##查找非删除的多个书本版本
#macro($mListIndexBookVersion(bookIds))
select t.* from book_version t where t.book_id in (:bookIds) AND status!=5
#end