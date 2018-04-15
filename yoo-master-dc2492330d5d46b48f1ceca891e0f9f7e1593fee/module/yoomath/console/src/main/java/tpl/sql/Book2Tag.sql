#macro($findByBookVersionId(bookVersionId))
	select * from book_2_tag where book_version_id = :bookVersionId
#end