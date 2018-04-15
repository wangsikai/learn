package com.lanking.uxb.service.thirdparty.weixin.response;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 微信消息.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2016年1月5日
 */
@XmlRootElement(name = "xml")
public class WXMessage {
	private String toUserName;

	private String fromUserName;

	private String createTime;

	private String msgType;

	private String content;

	private String msgId;

	private String picUrl;

	private String mediaId;

	private String format;

	private String recognition;

	private String thumbMediaId;

	private String location_X;

	private String location_Y;

	private String scale;

	private String label;

	private String title;

	private String description;

	private String url;

	private String event;

	private String eventKey;

	// 图文消息使用
	private String articleCount; // 图文个数
	private List<WXArticle> articles; // 图文集合

	@XmlElement(name = "ToUserName")
	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	@XmlElement(name = "FromUserName")
	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	@XmlElement(name = "CreateTime")
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@XmlElement(name = "MsgType")
	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	@XmlElement(name = "Content")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@XmlElement(name = "MsgId")
	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	@XmlElement(name = "PicUrl")
	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	@XmlElement(name = "MediaId")
	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	@XmlElement(name = "Format")
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	@XmlElement(name = "Recognition")
	public String getRecognition() {
		return recognition;
	}

	public void setRecognition(String recognition) {
		this.recognition = recognition;
	}

	@XmlElement(name = "ThumbMediaId")
	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

	@XmlElement(name = "Location_X")
	public String getLocation_X() {
		return location_X;
	}

	public void setLocation_X(String location_X) {
		this.location_X = location_X;
	}

	@XmlElement(name = "Location_Y")
	public String getLocation_Y() {
		return location_Y;
	}

	public void setLocation_Y(String location_Y) {
		this.location_Y = location_Y;
	}

	@XmlElement(name = "Scale")
	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	@XmlElement(name = "Label")
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@XmlElement(name = "Title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@XmlElement(name = "Description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlElement(name = "Url")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@XmlElement(name = "Event")
	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	@XmlElement(name = "EventKey")
	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

	@XmlElement(name = "ArticleCount")
	public String getArticleCount() {
		return articleCount;
	}

	public void setArticleCount(String articleCount) {
		this.articleCount = articleCount;
	}

	@XmlElementWrapper(name = "Articles")
	@XmlElement(name = "item")
	public List<WXArticle> getArticles() {
		return articles;
	}

	public void setArticles(List<WXArticle> articles) {
		this.articles = articles;
	}
}
