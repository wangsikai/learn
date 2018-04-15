###通过关键字和学科查找知识点
#macro($findAll(subject_code,code,version))
select * from knowledge_question_stat where 1=1
#if(subject_code)
 and subject_code = :subject_code
#end
#if(code)
 and knowpoint_code in :code
#end
#if(version)
 and version = :version
#end
#end