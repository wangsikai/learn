#macro($initFindFallibleQuestions())
select t.* from teacher_fallible_question t
 INNER JOIN question q on q.id=t.question_id
 INNER JOIN teacher tea ON tea.id=t.teacher_id AND q.subject_code=t.subject_code
 where t.right_rate <= 50 and t.do_num > 0 and t.create_at > '2017-7-1' and t.create_at < '2018-1-10'
 order by t.teacher_id ASC,q.phase_code ASC
#end