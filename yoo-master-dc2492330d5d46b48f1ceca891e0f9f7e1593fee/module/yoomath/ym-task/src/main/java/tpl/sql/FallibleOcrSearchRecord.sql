## 按照fileId查询
#macro($taskFindByFileId(fileId))
SELECT * FROM fallible_ocr_search_record WHERE file_id = :fileId
#end