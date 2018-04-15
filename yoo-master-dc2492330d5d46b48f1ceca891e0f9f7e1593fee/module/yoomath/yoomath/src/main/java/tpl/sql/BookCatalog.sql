##书本版本下 所有章节目录
#macro(getBookCatalogs(bookVersionId))
SELECT * FROM book_catalog WHERE book_version_id = :bookVersionId ORDER BY level ASC,sequence ASC
#end

## 获得第一层章节目录
#macro($getLevelOne(bookVersionId))
SELECT * FROM book_catalog WHERE book_version_id = :bookVersionId AND level = 1 ORDER BY sequence ASC, id ASC
#end


##查询教材书本章节(指定教材章节)
#macro($getBookCatalogByVersionAndSection(bookVersionId,textbookCode,sectionCode))
SELECT bc.* FROM book_catalog_section bs 
INNER JOIN book_catalog bc ON bs.book_catalog_id = bc.id
WHERE bs.book_version_id = :bookVersionId 
and bs.textbook_code = :textbookCode 
and bs.section_code =:sectionCode
ORDER BY bs.sequence ASC
#end