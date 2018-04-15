## 查询书本(公开>对指定学校公开>不公开,然后按时间倒序)
#macro($queryBook(keystr,subjectCode,resourceCategoryCode,status,categroyCode,textbookCode,sectionCode,statusfilter,schoolId,subjects))
SELECT a.*,FIND_IN_SET(b.open_status,'1,2,0') temp FROM book_version a
INNER JOIN book b ON a.book_id = b.id
WHERE a.textbook_code IS NOT NULL AND a.main_flag = 1 AND a.status = 2 AND a.subject_code in(:subjects) and b.status = 0
	#if(keystr)
		AND (a.name LIKE :keystr or a.isbn LIKE :keystr or b.id LIKE :keystr)
	#end
	#if(subjectCode)
		AND a.subject_code = :subjectCode
	#end
	#if(resourceCategoryCode)
		AND a.resource_category_code = :resourceCategoryCode
	#end
	#if(status)
		AND b.open_status = :status
	#end
	#if(categroyCode)
		AND a.textbook_category_code = :categroyCode
	#end
	#if(textbookCode)
		AND a.textbook_code = :textbookCode
	#end
	#if(sectionCode)
		AND a.section_code = :sectionCode
	#end
	#if(statusfilter)
		AND b.open_status not in(1)
	#end
	#if(schoolId)
		AND a.book_id not in(select c.book_id from school_book c where c.status =0 and school_id =:schoolId)
	#end
	ORDER BY temp,b.open_at desc
#end


## 查询学校书本
#macro($getBookBySchool(schoolId,phaseCode))
SELECT b.* FROM school_book a
INNER JOIN book_version b ON a.book_id = b.book_id AND b.main_flag =1 AND b.status = 2
WHERE a.school_id =:schoolId AND a.status = 0
	#if(phaseCode)
	AND b.phase_code = :phaseCode
	#end
order by a.create_at desc
#end
