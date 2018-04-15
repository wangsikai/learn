##通过订单id查询错题本导出信息
#macro($getByOrderId(fallibleQuestionPrintOrderId))
	select * from student_fallible_export_record 
	where fallible_question_print_order_id = :fallibleQuestionPrintOrderId
#end

##通过订单id批量查询错题本导出信息
#macro($mgetByOrderIds(fallibleQuestionPrintOrderIds))
	select * from student_fallible_export_record 
	where fallible_question_print_order_id in :fallibleQuestionPrintOrderIds
#end
