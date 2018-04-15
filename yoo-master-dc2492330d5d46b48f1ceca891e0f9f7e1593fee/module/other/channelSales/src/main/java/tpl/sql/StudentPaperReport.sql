## 根据记录ID查询学生报告数据
#macro($listByRecord(recordId))
SELECT t.* FROM student_paper_report t WHERE t.record_id=:recordId
#end