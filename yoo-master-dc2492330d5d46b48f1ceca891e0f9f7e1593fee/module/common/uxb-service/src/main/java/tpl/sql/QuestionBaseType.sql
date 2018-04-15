## 通过题目题型获取科目题型code
#macro($findBaseCodeList(questionCode))
SELECT base_code from question_base_type  where question_code =:questionCode
#end

