## 试卷点击量增加
#macro($updatePaperCount(examPaperId,dayOfN,nDay))
update exam_paper_count t set t.click_count=if((t.update_at=CURDATE() or t.n_day=0),t.click_count+1,1),t.update_at=CURDATE() where t.exam_paper_id=:examPaperId and t.n_day=:nDay and t.day_of_n=:dayOfN 
#end

## 试卷点击量查询
#macro($findPaperCount(examPaperIds,dayOfN,nDay))
select t.exam_paper_id as id,sum(t.click_count) as count from exam_paper_count t where t.exam_paper_id in (:examPaperIds)  and t.n_day =:nDay
#if(dayOfN)
and t.day_of_n=:dayOfN
#end
#end

