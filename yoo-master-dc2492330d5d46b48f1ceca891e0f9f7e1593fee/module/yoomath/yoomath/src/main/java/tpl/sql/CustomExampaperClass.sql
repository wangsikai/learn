## 根据组卷id查找班级列表
#macro($findByPaper(paperId))
SELECT t.class_id FROM custom_exampaper_class t WHERE t.custom_exampaper_id = :paperId
#end

## 根据多个组卷查找数据
#macro($findByPapers(paperIds))
SELECT t.* FROM custom_exampaper_class t WHERE t.custom_exampaper_id IN :paperIds
#end