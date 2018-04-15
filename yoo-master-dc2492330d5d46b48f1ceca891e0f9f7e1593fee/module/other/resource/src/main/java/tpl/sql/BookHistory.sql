##获得指定版本通过的历史记录
#macro($mgetTimeByPublish(bookIds))
select MAX(t.create_at) as ct,t.book_id as bookid from book_history t where t.book_id in (:bookIds) AND type=3 group by t.book_id
#end