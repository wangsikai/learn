##获取目录下对应的分类
#macro(findListByCatalogId(bookCatalogId))
 SELECT * FROM book_question_category WHERE book_section_id = :bookCatalogId
#end

##批量获取目录下对应的分类
#macro(mgetByCatalogIds(bookCatalogIds))
 SELECT * FROM book_question_category WHERE book_section_id in :bookCatalogIds
#end