## 根据章节练习id查找此练习下所有的题目
#macro($mgetListByPractise(practiseId))
SELECT t.* FROM section_practise_question t WHERE t.practise_id = :practiseId
#end