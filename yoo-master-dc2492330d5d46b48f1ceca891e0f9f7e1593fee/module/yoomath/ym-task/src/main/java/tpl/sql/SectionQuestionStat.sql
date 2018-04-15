##删除统计表
#macro($deleteStat(version))
	delete from section_question_stat where version = :version
#end