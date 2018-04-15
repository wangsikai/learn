## 通过问题标识查询对应章节和教材编号
#macro($getQuestionSectionList(questionId))
SELECT * FROM question_section WHERE question_id = :questionId AND v2 = 1
#end

## 通过批量问题标识查询对应章节和教材编号
#macro($mgetQuestionSectionList(questionIds))
SELECT * FROM question_section WHERE v2 = 1 AND question_id in (:questionIds)
#end

## 通过问题标识查询对应章节和教材编号（v全部）
#macro($getQuestionSectionListAllv(questionId))
SELECT * FROM question_section WHERE question_id = :questionId 
#end

## 通过批量问题标识查询对应章节和教材编号
#macro($mgetQuestionSectionListAllv(questionIds))
SELECT * FROM question_section WHERE  question_id in (:questionIds)
#end