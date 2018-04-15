## 试卷点击量查询
#macro($indexFindPaperCount(examPaperIds,dayOfN,nDay))
select t.* from exam_paper_count t where t.exam_paper_id in (:examPaperIds)  and t.n_day =:nDay and t.day_of_n=:dayOfN
#end

