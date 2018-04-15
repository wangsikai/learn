## 班级教材数据统计
#macro($ymGetByClassAndTextbook(classId,code))
SELECT t.* FROM diagno_class t WHERE t.class_id = :classId AND t.textbook_code = :code
#end

## 班级教材数据统计
#macro($ymGetByClassAndTextbooks(classId,codes))
SELECT t.* FROM diagno_class t WHERE t.class_id = :classId AND t.textbook_code in (:codes)
#end

## 全国平均
#macro($ymAvg())
SELECT t.textbook_code, avg(t.do_count_month) as do_count_month, avg(t.right_count_month) as right_count_month FROM diagno_class t
GROUP BY t.textbook_code
#end