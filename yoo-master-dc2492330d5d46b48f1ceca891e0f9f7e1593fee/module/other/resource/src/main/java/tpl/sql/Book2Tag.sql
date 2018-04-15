##根据bookVersionId获取书本tag
#macro($listBook2Tags(bookVersionId))
select t.* from book_2_tag t where t.book_version_id=:bookVersionId
#end