package com.lanking.uxb.service.latex.resource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.latex.api.LatexService;

import fmath.conversion.ConvertFromLatexToMathML;

//TODO 待更新后将后面一个url映射删除
@RestController
@RequestMapping({ "f/latex", "fs/latex" })
@RolesAllowed(anyone = true)
public class LatexMathController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final long EXPIRES = 2592000000L;

	private boolean useNginx = false;

	@Autowired
	private LatexService latexService;

	@PostConstruct
	void init() throws Exception {
		useNginx = Env.getBoolean("latex.image.usenginx");
	}

	/**
	 * 通过<ux-mth>标签返回公式图片.
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "img")
	public void img(HttpServletRequest request, HttpServletResponse response) {
		String math = StringUtils.defaultIfBlank(request.getParameter("math"), "");
		String colorStr = StringUtils.defaultIfBlank(request.getParameter("color"), "black");
		math = math.replaceAll("<(/?)ux-mth>", "");
		math = math.replaceAll("\\\\[\\s]*%", "%");
		if (StringUtils.isBlank(math)) {
			return;
		}

		String md5Latex = Codecs.md5Hex(math.getBytes());
		if (math.indexOf("%") != -1 || math.indexOf("&") != -1) {
			// 带%的重新生成
			md5Latex = md5Latex + "171128";
		}
		File file = latexService.storeLatexImage(math, md5Latex, colorStr);

		if (file == null || !file.exists()) {
			return;
		}

		long imsTime = request.getDateHeader("If-Modified-Since");
		if (file.lastModified() > imsTime) {
			response.setStatus(HttpServletResponse.SC_OK);
		}
		if (imsTime + EXPIRES > System.currentTimeMillis()) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return;
		}
		String previousToken = request.getHeader("If-None-Match");
		if (previousToken != null && previousToken.equals(Long.toString(file.lastModified()))) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return;
		}
		response.setHeader("ETag", Long.toString(file.lastModified()));
		response.addDateHeader("Last-Modified", System.currentTimeMillis());
		response.addDateHeader("Expires", System.currentTimeMillis() + EXPIRES);
		response.setStatus(HttpServletResponse.SC_OK);

		response.setHeader("Content-Disposition", "inline; filename=\"" + md5Latex + ".png\"");
		response.setHeader("Content-Type", "image/png");

		if (useNginx) {
			if (file.exists()) {
				String realFilePath = file.getAbsolutePath();
				response.setHeader("X-Accel-Redirect",
						realFilePath.replace(Env.getString("latex.file.store.path"), "/latex"));
			}
		} else {
			OutputStream os = null;
			FileInputStream inputStream = null;
			try {
				os = response.getOutputStream();
				inputStream = new FileInputStream(file);
				ByteArrayOutputStream bops = new ByteArrayOutputStream();
				int data = -1;
				while ((data = inputStream.read()) != -1) {
					bops.write(data);
				}
				os.write(bops.toByteArray());
				os.flush();
				os.close();
				inputStream.close();
			} catch (Exception e) {
				logger.warn("[latex error] math={}", math, e);
			} finally {
				if (os != null) {
					try {
						os.close();
					} catch (IOException e) {
						logger.error(e.getMessage(), e);
					}
				}
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		}
	}

	/**
	 * mml转latex.
	 * 
	 * @param mml
	 * @return
	 */
	@RequestMapping(value = "mml2latex")
	public Value mathMLToLatex(String mml) {
		return new Value(latexService.mml2Latex(mml));
	}

	/**
	 * mml批量转latex.
	 * 
	 * @param mmls
	 * @return
	 */
	@RequestMapping(value = "mml2latexs")
	public Value mathMLToLatexs(String mmls) {
		if (StringUtils.isBlank(mmls)) {
			return new Value(new MissingArgumentException("mmls"));
		}
		JSONArray mmlArray = JSONArray.parseArray(mmls);
		List<String> latexs = new ArrayList<String>(mmlArray.size());
		try {
			for (int i = 0; i < mmlArray.size(); i++) {
				latexs.add(latexService.mml2Latex(mmlArray.getString(i)));
			}
		} catch (RuntimeException e) {
			logger.error("[mml2latexs error] mmls={}", mmls, e);
			return new Value(new ServerException());
		}
		return new Value(latexs);
	}

	/**
	 * latex转mathML
	 * 
	 * @param latex
	 * @return
	 */
	@RequestMapping(value = "latex2mml")
	public Value latexToMathML(String latex) {
		if (StringUtils.isBlank(latex)) {
			return new Value(StringUtils.EMPTY);
		}
		String mml = ConvertFromLatexToMathML.convertToMathML(latex);
		return new Value(mml);
	}
}
