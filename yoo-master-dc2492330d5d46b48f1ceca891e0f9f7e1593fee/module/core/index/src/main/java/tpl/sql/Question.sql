## 查找子题
#macro($getSubQuestionsForIndex(parentId))
SELECT * FROM question WHERE parent_id = :parentId and del_status = 0 ORDER BY sequence ASC
#end

## 查找子题
#macro($mgetSubQuestions(parentIds))
SELECT * FROM question WHERE parent_id in(:parentIds) and del_status = 0
#end

## 分页找大题
#macro($indexQueryByPage())
SELECT * FROM question WHERE del_status = 0 and sub_flag = 0
#end

## 分页找大题（相似题使用）
## 2017-04-26，去除非校本题目的限制
#macro($indexSimilarQueryByPage())
SELECT * FROM question WHERE del_status = 0 AND sub_flag = 0 AND TYPE != 6 AND status !=5
 AND (subject_code = 102 OR subject_code = 202 OR subject_code = 302)
#end

## 分页找2017-03-28之后新录入的已通过的题（相似题索引使用）
## 2017-05-09，原重复题功能，将后续添加的题做处理
#macro($indexSimilarNewQueryByPage())
SELECT q.* FROM question q WHERE q.del_status = 0 AND q.sub_flag = 0 AND q.type != 6 AND q.status = 2
 AND (q.subject_code = 102 OR q.subject_code = 202 OR q.subject_code = 302)
 AND q.create_at > '2017-03-28'  AND q.school_id = 0 AND q.same_show IS NULL
#end

## 查询当前实际个数
#macro($indexDataCount())
SELECT count(id) FROM question WHERE del_status = 0 and sub_flag = 0
#end