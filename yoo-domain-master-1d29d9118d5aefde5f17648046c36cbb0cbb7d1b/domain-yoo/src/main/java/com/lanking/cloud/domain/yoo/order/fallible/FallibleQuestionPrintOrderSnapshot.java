package com.lanking.cloud.domain.yoo.order.fallible;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 错题打印订单快照
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "fallible_question_print_order_snapshot")
public class FallibleQuestionPrintOrderSnapshot extends FallibleQuestionPrintOrderBaseInfo {

	private static final long serialVersionUID = -5763623372357199967L;

	/**
	 * 错题打印订单ID
	 */
	@Column(name = "fallible_question_print_order_id")
	private long fallibleQuestionPrintOrderId;

	public long getFallibleQuestionPrintOrderId() {
		return fallibleQuestionPrintOrderId;
	}

	public void setFallibleQuestionPrintOrderId(long fallibleQuestionPrintOrderId) {
		this.fallibleQuestionPrintOrderId = fallibleQuestionPrintOrderId;
	}

}
