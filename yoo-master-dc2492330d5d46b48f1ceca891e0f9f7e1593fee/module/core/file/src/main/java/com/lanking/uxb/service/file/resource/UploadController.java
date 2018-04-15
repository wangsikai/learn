package com.lanking.uxb.service.file.resource;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.domain.base.file.File;
import com.lanking.cloud.domain.base.file.FileType;
import com.lanking.cloud.domain.base.file.Space;
import com.lanking.cloud.domain.base.file.api.CropModel;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.sdk.util.Charsets;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.file.ex.FileException;

/**
 * 上传的相关接口
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年11月18日
 * 
 */
@RestController
@RequestMapping({ "f/up", "fs/up" })
public class UploadController extends FileBaseController {

	private Logger logger = LoggerFactory.getLogger(UploadController.class);

	@SuppressWarnings("deprecation")
	@RequestMapping(value = "{space}", method = RequestMethod.POST)
	public void upload(@PathVariable("space") String name,
			@RequestParam(value = "left", required = false) Integer left,
			@RequestParam(value = "top", required = false) Integer top,
			@RequestParam(value = "w", required = false) Integer w,
			@RequestParam(value = "h", required = false) Integer h,
			@RequestParam(value = "crop", required = false, defaultValue = "false") Boolean crop,
			@RequestParam(value = "style", required = false) String style,
			@RequestParam(value = "base64Size", required = false) Long base64Size,
			@RequestParam(value = "base64Type", required = false) String base64Type,
			@RequestParam(value = "base64Data", required = false) String base64Data,
			@RequestParam(value = "url", required = false) String url, HttpServletRequest request,
			HttpServletResponse response) {
		Space space = spaceService.getSpace(name);
		File file = new File();
		file.setSpace(space);
		file.setPreview(space.isPreview());
		file.setCrop(crop);
		file.setCropModel(new CropModel(left, top, w, h));
		file.setBase64Size(base64Size);
		file.setBase64Type(base64Type);
		file.setBase64Data(base64Data);
		file.setUrl(url);
		Value value = null;
		try {
			defaultFileHandle.preUpload(file, request, response);
			defaultFileHandle.postUpload(file, request, response);
			defaultFileHandle.afterUpload(file);
			value = new Value(fileConverter.to(file));
		} catch (FileException e) {
			logger.error("upload fail:", e);
			value = new Value(e.getCode(), e.getMessage());
		} catch (AbstractException e) {
			logger.error("upload fail:", e);
			value = new Value(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("upload fail:", e);
			value = new Value(new ServerException());
		}
		Map<String, Object> map = new HashMap<String, Object>(5);
		map.put("status", JSON.toJSONString(value));
		map.put("ret_code", value.getRet_code());
		map.put("ret_msg", value.getRet_msg());
		map.put("ret", value.getRet());
		map.put("ieRet", URLEncoder.encode(JSON.toJSONString(value)));
		response.setCharacterEncoding(Charsets.UTF8);
		String callback = request.getParameter("callback");
		try {
			if (StringUtils.isBlank(callback)) {
				response.getWriter().write(JSON.toJSONString(map));
			} else {
				response.getWriter().print("<script>" + callback + "(" + JSON.toJSONString(map) + ")</script>");
			}
		} catch (Exception e) {
			logger.error("response write error:", e);
		}
	}

	@SuppressWarnings("deprecation")
	@RequestMapping(value = "space/{space}/type/{type}", method = RequestMethod.POST)
	public void upload(@PathVariable("space") String name, @PathVariable("type") int type,
			@RequestParam(value = "left", required = false) Integer left,
			@RequestParam(value = "top", required = false) Integer top,
			@RequestParam(value = "w", required = false) Integer w,
			@RequestParam(value = "h", required = false) Integer h,
			@RequestParam(value = "crop", required = false, defaultValue = "false") Boolean crop,
			@RequestParam(value = "style", required = false) String style,
			@RequestParam(value = "base64Size", required = false) Long base64Size,
			@RequestParam(value = "base64Type", required = false) String base64Type,
			@RequestParam(value = "base64Data", required = false) String base64Data,
			@RequestParam(value = "url", required = false) String url, HttpServletRequest request,
			HttpServletResponse response) {
		Space space = spaceService.getSpace(name);
		File file = new File();
		file.setSpace(space);
		file.setPreview(space.isPreview());
		file.setType(FileType.findByValue(type));
		file.setCrop(crop);
		file.setCropModel(new CropModel(left, top, w, h));
		file.setBase64Size(base64Size);
		file.setBase64Type(base64Type);
		file.setBase64Data(base64Data);
		file.setUrl(url);
		Value value = null;
		try {
			defaultFileHandle.preUpload(file, request, response);
			defaultFileHandle.postUpload(file, request, response);
			defaultFileHandle.afterUpload(file);
			value = new Value(fileConverter.to(file));
		} catch (FileException e) {
			logger.error("upload fail:", e);
			value = new Value(e.getCode(), e.getMessage());
		} catch (AbstractException e) {
			logger.error("upload fail:", e);
			value = new Value(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("upload fail:", e);
			value = new Value(new ServerException());
		}
		Map<String, Object> map = new HashMap<String, Object>(5);
		map.put("status", JSON.toJSONString(value));
		map.put("ret_code", value.getRet_code());
		map.put("ret_msg", value.getRet_msg());
		map.put("ret", value.getRet());
		map.put("ieRet", URLEncoder.encode(JSON.toJSONString(value)));
		response.setCharacterEncoding(Charsets.UTF8);
		String callback = request.getParameter("callback");
		try {
			if (StringUtils.isBlank(callback)) {
				response.getWriter().write(JSON.toJSONString(map));
			} else {
				response.getWriter().print("<script>" + callback + "(" + JSON.toJSONString(map) + ")</script>");
			}
		} catch (Exception e) {
			logger.error("response write error:", e);
		}
	}

	@SuppressWarnings("deprecation")
	@RequestMapping(value = "base64/{space}", method = RequestMethod.POST)
	public void uploadBase64(@PathVariable("space") String name, @RequestBody String data, HttpServletRequest request,
			HttpServletResponse response) {
		Space space = spaceService.getSpace(name);
		File file = new File();
		file.setSpace(space);
		file.setPreview(space.isPreview());
		file.setCrop(false);
		file.setCropModel(new CropModel());
		JSONObject jsonObject = JSONObject.parseObject(data);
		file.setBase64Size(jsonObject.getLong("base64Size"));
		file.setBase64Type(jsonObject.getString("base64Type"));
		file.setBase64Data(jsonObject.getString("base64Data"));
		Value value = null;
		try {
			defaultFileHandle.preUpload(file, request, response);
			defaultFileHandle.postUpload(file, request, response);
			defaultFileHandle.afterUpload(file);
			value = new Value(fileConverter.to(file));
		} catch (FileException e) {
			logger.error("upload fail:", e);
			value = new Value(e.getCode(), e.getMessage());
		} catch (AbstractException e) {
			logger.error("upload fail:", e);
			value = new Value(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("upload fail:", e);
			value = new Value(new ServerException());
		}
		Map<String, Object> map = new HashMap<String, Object>(5);
		map.put("status", JSON.toJSONString(value));
		map.put("ret_code", value.getRet_code());
		map.put("ret_msg", value.getRet_msg());
		map.put("ret", value.getRet());
		map.put("ieRet", URLEncoder.encode(JSON.toJSONString(value)));
		response.setCharacterEncoding(Charsets.UTF8);
		String callback = request.getParameter("callback");
		try {
			if (StringUtils.isBlank(callback)) {
				response.getWriter().write(JSON.toJSONString(map));
			} else {
				response.getWriter().print("<script>" + callback + "(" + JSON.toJSONString(map) + ")</script>");
			}
		} catch (Exception e) {
			logger.error("response write error:", e);
		}

	}
}
