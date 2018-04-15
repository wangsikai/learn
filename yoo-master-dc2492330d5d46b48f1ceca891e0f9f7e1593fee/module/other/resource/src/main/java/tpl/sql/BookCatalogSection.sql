## 根据书本版本查询目录章节对应关系
#macro($findByBookVersion(bookVersionId))
select t.* from book_catalog_section t
where t.book_version_id=:bookVersionId
order by t.section_code ASC, sequence ASC
#end

## 根据书本版本及教材查询目录章节对应关系
#macro($findByCatalog(bookVersionId, catalogId))
select t.* from book_catalog_section t
where t.book_version_id=:bookVersionId and t.book_catalog_id=:catalogId
#end

## 更新同一个教材章节下的目录顺序
#macro($updateSequence(textbookCode, sectionCode, sequence))
update book_catalog_section set sequence=sequence+1
where textbook_code=:textbookCode and section_code=:sectionCode
#end

## 删除教辅书本目录对应的章节关系
#macro($deleteCatalogRelation(bookVersionId, catalogId))
delete from book_catalog_section where book_version_id=:bookVersionId and book_catalog_id=:catalogId
#end

## 删除教辅书本目录对应的章节关系
#macro($deleteCatalogsRelation(bookVersionId, catalogIds))
delete from book_catalog_section where book_version_id=:bookVersionId and book_catalog_id in (:catalogIds)
#end

## 删除教辅书本目录对应的章节关系
#macro($deleteCatalogRelationBySection(textbookCode, sectionCode))
delete from book_catalog_section where textbook_code=:textbookCode and section_code=:sectionCode
#end

## 删除教辅书本目录对应的章节关系
#macro($deleteCatalogRelationByBookVersion(bookVersionId))
delete from book_catalog_section where book_version_id=:bookVersionId
#end
