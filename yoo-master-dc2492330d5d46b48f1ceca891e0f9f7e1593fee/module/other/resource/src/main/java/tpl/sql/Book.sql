##根据商品获取书本
#macro($getFromProduct(productId))
SELECT * FROM book where product_id=:productId
#end

##查询书本
#macro($queryBooks(vendorId, key, createId, status, createBt, createEt, phaseCode, subjectCode, textbookCategoryCode, 
textbookCode, sectionCode, resourceCategoryCode, bookCode, name, publishBt, publishEt, orderType, direction))
SELECT distinct(t.book_id) FROM book_version t
 INNER JOIN book b on b.id = t.book_id and b.vendor_id=:vendorId and b.status != 2
 INNER JOIN textbook_category tc ON tc.code=t.textbook_category_code
 LEFT JOIN textbook tb ON tb.code=t.textbook_code
 LEFT JOIN book_history h on h.book_id=b.id AND h.type=3
 where t.status != 5
 #if(key)
 AND (t.name LIKE :key OR t.isbn LIKE :key)
 #end
 #if(bookCode)
 AND t.book_id =:bookCode
 #end
 #if(createId)
 AND t.create_id =:createId
 #end
 #if(status)
 AND t.status =:status
 #end
 #if(createBt)
 AND t.create_at >=:createBt
 #end
 #if(createEt)
 AND t.create_at<:createEt
 #end
 #if(phaseCode)
 AND t.phase_code =:phaseCode
 #end
 #if(subjectCode)
 AND t.subject_code =:subjectCode
 #end
 #if(textbookCategoryCode)
 AND t.textbook_category_code =:textbookCategoryCode
 #end
 #if(textbookCode)
 AND t.textbook_code =:textbookCode
 #end
 #if(sectionCode)
 AND t.section_codes like :sectionCode
 #end
 #if(resourceCategoryCode)
 AND t.resource_category_code =:resourceCategoryCode
 #end
 #if(name)
 AND t.name like :name
 #end
 #if(publishBt && publishEt)
 AND h.create_at>=:publishBt AND h.create_at<=:publishEt
 #end
 #if(orderType == 1)
  order by CONVERT(t.name USING gbk)
  #if(direction == 'asc') ASC #end
  #if(direction == 'desc') DESC #end
  ,CONVERT(tc.name USING gbk) ASC,CONVERT(tb.name USING gbk) ASC
 #elseif(orderType == 2)
  order by CONVERT(tc.name USING gbk)
  #if(direction == 'asc') ASC #end
  #if(direction == 'desc') DESC #end
  ,CONVERT(t.name USING gbk) ASC,CONVERT(tb.name USING gbk) ASC
 #elseif(orderType == 3)
  order by CONVERT(tb.name USING gbk)
  #if(direction == 'asc') ASC #end
  #if(direction == 'desc') DESC #end
  ,CONVERT(tc.name USING gbk) ASC,CONVERT(t.name USING gbk) ASC
 #elseif(orderType == 4)
  order by h.create_at
  #if(direction == 'asc') ASC #end
  #if(direction == 'desc') DESC #end
 #else
 order by t.create_at DESC
 #end
#end

##查询书本整体统计
#macro($queryBooksCounts(vendorId))
SELECT COUNT(v.id) AS counts,v.phase_code AS pcode,v.subject_code AS scode FROM book_version v
 INNER JOIN book b ON b.id=v.book_id AND b.status != 2 AND b.vendor_id=:vendorId
 WHERE v.main_flag = 1 AND v.status < 4
 GROUP BY v.phase_code,v.subject_code
#end

##查询学科书本统计
#macro($subjectBooksCounts(vendorId, subjectCode))
SELECT COUNT(bv.id)AS counts,bv.status as status FROM book_version bv
 INNER JOIN book b ON b.id=bv.book_id AND b.status != 2
 WHERE b.vendor_id=:vendorId AND bv.status < 4
 #if(subjectCode)
 AND bv.subject_code=:subjectCode 
 #end
 GROUP BY bv.status
#end
