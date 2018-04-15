##获取书本对应的标签
#macro(getByBookVersionId(versionId))
 SELECT * FROM book_2_tag WHERE book_version_id = :versionId
#end


##批量获取书本对应的标签
#macro(mgetByBookVersionIds(versionIds))
 SELECT * FROM book_2_tag WHERE book_version_id in (:versionIds)
#end