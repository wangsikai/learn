## 根据试卷id查询主题
#macro($zyFindByPaper(paperId))
SELECT t.* FROM exam_paper_topic t WHERE t.exam_paper_id = :paperId ORDER BY t.sequence ASC
#end

## 根据试卷id查询可导出的试卷分类（只取单选、填空、简答）
#macro($zyFindExportByPaper(paperId))
SELECT t.* FROM exam_paper_topic t 
 WHERE t.exam_paper_id = :paperId AND t.type in (0,2,3) ORDER BY t.type ASC
#end