#macro($getSchoolIdsByBookId(bookid))
SELECT t.school_id FROM school_book t WHERE t.book_id=:bookid AND t.status=0
#end