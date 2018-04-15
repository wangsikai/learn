package com.lanking.uxb.zycon.nationalDayActivity01.resource;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01H5;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01H5PVUV;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.zycon.nationalDayActivity01.api.ZycNationalDayActivity01H5PVUVService;
import com.lanking.uxb.zycon.nationalDayActivity01.api.ZycNationalDayActivity01StuService;
import com.lanking.uxb.zycon.nationalDayActivity01.api.ZycNationalDayActivity01TeaService;
import com.lanking.uxb.zycon.nationalDayActivity01.form.ZycNationalDayActivity01Form;

/**
 * 国庆活动接口
 *
 * @author peng.zhao
 * @since yoomath V1.4.7
 */
@RestController
@RequestMapping(value = "/zyc/nda01")
public class ZycNationalDayActivity01Controller {

	@Autowired
	private ZycNationalDayActivity01StuService nationalDayActivity01StuService;
	@Autowired
	private ZycNationalDayActivity01H5PVUVService nationalDayActivity01H5PVUVService;
	@Autowired
	private ZycNationalDayActivity01TeaService nationalDayActivity01TeaService;

	/**
	 * 查询学生排名
	 *
	 * @return Value
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "stuRanking", method = { RequestMethod.GET, RequestMethod.POST })
	public Value stuRanking(ZycNationalDayActivity01Form form, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int pageSize) {
		Page<Map> stuInfo = nationalDayActivity01StuService.queryNdaStuRank(form, P.index(page, pageSize));

		VPage<Map> vp = new VPage<>();
		int tPage = (int) (stuInfo.getTotalCount() + pageSize - 1) / pageSize;
		vp.setPageSize(pageSize);
		vp.setCurrentPage(page);
		vp.setTotalPage(tPage);
		vp.setTotal(stuInfo.getTotalCount());
		vp.setItems(stuInfo.getItems());

		return new Value(vp);
	}

	/**
	 * 查询访问页面数据统计
	 *
	 * @param stuPvuv
	 *            年月日拼接的整数，记录全部时此字段为0，如：20170922
	 * @param type
	 *            TEACHER:教师 STUDENT:学生
	 * @return Value
	 */
	@RequestMapping(value = "viewPvuv", method = { RequestMethod.GET, RequestMethod.POST })
	public Value viewPvuv(Long viewAt, UserType type) {
		if (type == null) {
			return new Value(new MissingArgumentException());
		}
		if (viewAt == null) {
			viewAt = 0L;
		}

		Map<String, Object> data = new HashMap<String, Object>();
		if (UserType.TEACHER == type) {
			NationalDayActivity01H5PVUV intro = nationalDayActivity01H5PVUVService
					.getH5PVUV(NationalDayActivity01H5.TEA_INTRO, viewAt);
			NationalDayActivity01H5PVUV rank = nationalDayActivity01H5PVUVService
					.getH5PVUV(NationalDayActivity01H5.TEA_RANK, viewAt);
			if (intro == null) {
				intro = new NationalDayActivity01H5PVUV();
				intro.setPv(0);
				intro.setUv(0);
			}
			if (rank == null) {
				rank = new NationalDayActivity01H5PVUV();
				rank.setPv(0);
				rank.setUv(0);
			}
			data.put("intro", intro);
			data.put("rank", rank);
		} else if (UserType.STUDENT == type) {
			NationalDayActivity01H5PVUV intro = nationalDayActivity01H5PVUVService
					.getH5PVUV(NationalDayActivity01H5.STU_INTRO, viewAt);
			NationalDayActivity01H5PVUV rank = nationalDayActivity01H5PVUVService
					.getH5PVUV(NationalDayActivity01H5.STU_RANK, viewAt);
			if (intro == null) {
				intro = new NationalDayActivity01H5PVUV();
				intro.setPv(0);
				intro.setUv(0);
			}
			if (rank == null) {
				rank = new NationalDayActivity01H5PVUV();
				rank.setPv(0);
				rank.setUv(0);
			}
			data.put("intro", intro);
			data.put("rank", rank);
		}

		return new Value(data);
	}

	/**
	 * 查询教师排名
	 *
	 * @return Value
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "teaRanking", method = { RequestMethod.GET, RequestMethod.POST })
	public Value teaRanking(ZycNationalDayActivity01Form form, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int pageSize) {
		Page<Map> teaInfo = nationalDayActivity01TeaService.queryNdaTeaRank(form, P.index(page, pageSize));
		VPage<Map> vp = new VPage<>();
		int tPage = (int) (teaInfo.getTotalCount() + pageSize - 1) / pageSize;
		vp.setPageSize(pageSize);
		vp.setCurrentPage(page);
		vp.setTotalPage(tPage);
		vp.setTotal(teaInfo.getTotalCount());
		vp.setItems(teaInfo.getItems());

		return new Value(vp);
	}
}
