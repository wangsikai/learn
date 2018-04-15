##查询发布状态的学校图书
#macro($getSchoolBook(textCategoryCode,textbookCode,schoolId))
SELECT b.* FROM book_version b INNER JOIN school_book sb ON sb.book_id  =b.book_id AND sb.school_id=:schoolId AND sb.status=0  
WHERE b.textbook_category_code=:textCategoryCode AND b.textbook_code=:textbookCode
AND b.status=2 AND b.main_flag=1 ORDER BY sb.update_at
#end

##查询发布状态的学校图书
#macro($getSchoolBookCount(textCategoryCode,textbookCode,schoolId))
SELECT count(1) FROM book_version b INNER JOIN school_book sb ON sb.book_id  =b.book_id AND sb.school_id=:schoolId AND sb.status=0  
WHERE b.textbook_category_code=:textCategoryCode AND b.textbook_code=:textbookCode
AND b.status=2 AND b.main_flag=1 
#end


##查询完全公开状态的图书
#macro($getFreeBook(textCategoryCode,textbookCode))
SELECT bv.* FROM  book_version bv  INNER JOIN  book b  ON b.id  =bv.book_id AND  b.open_status=1 
WHERE bv.textbook_category_code=:textCategoryCode AND bv.textbook_code=:textbookCode  AND bv.main_flag=1 AND bv.status=2
ORDER BY b.open_at
#end

##查询完全公开状态的图书统计
#macro($getFreeBookCount(textCategoryCode,textbookCode))
SELECT count(1) FROM  book_version bv  INNER JOIN  book b  ON b.id  =bv.book_id AND  b.open_status=1 
WHERE bv.textbook_category_code=:textCategoryCode AND bv.textbook_code=:textbookCode  AND bv.main_flag=1 AND bv.status=2
#end

##获取用户对应版本教材选择的书本列表
#macro($getUserBookList(categoryCode,textBookCode,userId))
SELECT bv.* FROM book_version  bv INNER JOIN  book b  ON b.id  =bv.book_id AND  b.open_status in (1,2)  INNER JOIN user_school_book usb ON usb.book_id=bv.book_id AND usb.user_id=:userId 
AND usb.status=0 
WHERE bv.textbook_category_code=:categoryCode 
#if(textBookCode)
	AND bv.textbook_code=:textBookCode 
#end
AND bv.main_flag=1 
AND bv.status=2 
GROUP BY usb.book_id 
ORDER BY usb.update_at ASC
#end

## 获取用户对应的版本教材选择图书列表（去除校级图书）
#macro($getUserFreeBookList(categoryCode,textBookCode,userId))
SELECT bv.* FROM book_version  bv INNER JOIN  book b  ON b.id  =bv.book_id AND  b.open_status in (1,2)  INNER JOIN user_school_book usb ON usb.book_id=bv.book_id AND usb.user_id=:userId
AND usb.status=0
WHERE bv.textbook_category_code=:categoryCode AND bv.textbook_code=:textBookCode AND bv.main_flag=1 AND bv.status=2
AND bv.id NOT IN (
  SELECT b.id FROM book_version b INNER JOIN school_book sb ON sb.book_id = b.book_id AND sb.status=0
  WHERE b.textbook_category_code=:categoryCode AND b.textbook_code=:textBookCode
  AND b.status=2 AND b.main_flag=1
)
GROUP BY usb.book_id ORDER BY usb.update_at ASC
#end

## 判断是否存在这样的书本
#macro($existBook(categoryCode,userId,id))
SELECT * FROM book_version  bv INNER JOIN  book b  ON b.id  =bv.book_id AND  b.open_status in (1,2)  LEFT JOIN user_school_book usb ON usb.book_id=bv.book_id AND usb.user_id=:userId
AND usb.status=0
WHERE bv.id = :id AND bv.textbook_category_code=:categoryCode AND bv.main_flag=1 AND bv.status=2 GROUP BY usb.book_id ORDER BY usb.update_at ASC
#end

##查询指定书本类型的学校图书
#macro($getSchoolBookResource(textCategoryCode,textbookCode,schoolId,resourceCategoryCode))
SELECT b.* FROM book_version b INNER JOIN school_book sb ON sb.book_id  =b.book_id AND sb.school_id=:schoolId AND sb.status=0  
WHERE b.textbook_category_code=:textCategoryCode AND b.textbook_code=:textbookCode
#if(resourceCategoryCode)
	AND b.resource_category_code=:resourceCategoryCode 
#end
AND b.status=2 AND b.main_flag=1 ORDER BY sb.update_at DESC
#end

##查询完全公开状态的图书
#macro($getFreeBookResource(textCategoryCode,textbookCode,resourceCategoryCode,resourceCategoryCodes))
SELECT bv.* FROM  book_version bv  INNER JOIN  book b  ON b.id  =bv.book_id AND  b.open_status=1 
WHERE bv.textbook_category_code=:textCategoryCode AND bv.textbook_code=:textbookCode  
#if(resourceCategoryCode)
	AND bv.resource_category_code=:resourceCategoryCode 
#end
#if(resourceCategoryCodes)
	AND bv.resource_category_code in :resourceCategoryCodes 
#end
AND bv.main_flag=1 AND bv.status=2
ORDER BY b.open_at DESC
#end

## 根据bookID获取图书版本
#macro($getBookVersionByBookIds(bookIds))
select bv.* FROM  book_version bv where bv.book_id in (:bookIds) and bv.main_flag=1 and status=2
#end