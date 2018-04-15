package com.lanking.uxb.service.session.cache;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import com.lanking.cloud.domain.base.session.DeviceType;
import com.lanking.cloud.domain.base.session.Session;
import com.lanking.cloud.domain.base.session.SessionStatus;
import com.lanking.cloud.domain.base.session.api.SessionConstants;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;
import com.lanking.uxb.service.session.api.SessionActiveLogService;
import com.lanking.uxb.service.session.api.SessionPacket;
import com.lanking.uxb.service.session.api.SessionService;

@Service
public final class SessionCacheService extends AbstractCacheService {

	private Logger logger = LoggerFactory.getLogger(SessionCacheService.class);

	private ValueOperations<String, SessionPacket> spOpt;
	private ZSetOperations<String, String> tokenOpt;

	private static final String SESSIONPACK_KEY = "p";
	private static final String SESSIONSET_KEY = "s";

	private static final int LIMIT = 1000;

	private static long WEB_DDI = 0;
	private static long WEB_ACTIVE_DDI = 0;
	private static long MOBILE_DDI = 0;
	private static long MOBILE_ACTIVE_DDI = 0;
	private static long GUEST_DDI = 0;

	@Autowired
	private SessionService sessionService;
	@Autowired
	private SessionActiveLogService sessionActiveLogService;

	@Override
	public String getNs() {
		return "s";
	}

	@Override
	public String getNsCn() {
		return "会话";
	}

	private String getSessionPacketKey(String token) {
		return assemblyKey(SESSIONPACK_KEY, token);
	}

	public SessionPacket getSessionPacket(String token) {
		return spOpt.get(getSessionPacketKey(token));
	}

	public void setSessionPacket(SessionPacket packet) {
		spOpt.set(getSessionPacketKey(packet.getToken()), packet);
	}

	public void refresh(String token) {
		tokenOpt.add(SESSIONSET_KEY, token, System.currentTimeMillis());
	}

	public long size() {
		Long size = tokenOpt.size(SESSIONSET_KEY);
		return size == null ? 0 : size;
	}

	@SuppressWarnings("unchecked")
	public void sync() {
		long currentTime = System.currentTimeMillis();
		Set<ZSetOperations.TypedTuple<String>> typedTuples;
		long min = 0;
		do {
			typedTuples = tokenOpt.rangeByScoreWithScores(SESSIONSET_KEY, min, currentTime, 0, LIMIT);
			if (typedTuples != null) {
				if (typedTuples.size() == 1 && ((ZSetOperations.TypedTuple<String>) typedTuples.toArray()[0]).getScore()
						.longValue() == min) {
					break;
				}
				long oldMin = min;
				for (ZSetOperations.TypedTuple<String> typedTuple : typedTuples) {
					String token = typedTuple.getValue();
					min = typedTuple.getScore().longValue();
					Session session = sessionService.getSession(token);
					if (session == null) {
						tokenOpt.remove(SESSIONSET_KEY, token);
						getRedisTemplate().delete(getSessionPacketKey(token));
					} else {
						if (session.getStatus() == SessionStatus.TIMEOUT
								|| session.getStatus() == SessionStatus.LOGOUT) {// 如果会话注销或则超时
							if (session.getStartActiveAt() > 0 && session.getActiveAt() > session.getStartActiveAt()) {
								sessionActiveLogService.log(session.getToken(), session.getUserId(),
										session.getDeviceType(), session.getStartActiveAt(), session.getActiveAt());
								session.setActiveTime(
										session.getActiveTime() + (session.getActiveAt() - session.getStartActiveAt()));
								session.setStartActiveAt(0);
								session.setActiveAt(min);
								sessionService.updateSession(session);
							}
						} else {
							boolean timeOut = false;
							boolean unActive = false;
							if (session.getUserId() == SessionConstants.GUEST_ID) {// 未登录
								unActive = min < (currentTime - GUEST_DDI);
								timeOut = min < (currentTime - GUEST_DDI);
							} else {
								if (session.getDeviceType() == DeviceType.WEB) {
									unActive = min < (currentTime - WEB_ACTIVE_DDI);
									timeOut = min < (currentTime - WEB_DDI);
								} else {
									unActive = min < (currentTime - MOBILE_ACTIVE_DDI);
									timeOut = min < (currentTime - MOBILE_DDI);
								}
							}
							if (timeOut) {
								if (session.getStatus() == SessionStatus.ACTIVE
										|| session.getStatus() == SessionStatus.UNACTIVE) {
									session.setEndAt(System.currentTimeMillis());
									session.setStatus(SessionStatus.TIMEOUT);
								}
							} else {
								if (unActive) {// 不活跃
									session.setStatus(SessionStatus.UNACTIVE);
								} else {// 活跃
									session.setStatus(SessionStatus.ACTIVE);
								}
							}
							if (unActive) {// 如果不活跃了则错发活跃log的记录
								if (session.getStartActiveAt() > 0
										&& session.getActiveAt() > session.getStartActiveAt()) {
									sessionActiveLogService.log(session.getToken(), session.getUserId(),
											session.getDeviceType(), session.getStartActiveAt(), session.getActiveAt());
									session.setActiveTime(session.getActiveTime()
											+ (session.getActiveAt() - session.getStartActiveAt()));
									session.setStartActiveAt(min);
									session.setActiveAt(min);
								} else {
									session.setActiveAt(min);
								}
							} else {
								session.setActiveAt(min);
							}
							sessionService.updateSession(session);

							if (unActive || timeOut) {
								tokenOpt.remove(SESSIONSET_KEY, token);
								getRedisTemplate().delete(getSessionPacketKey(token));
							}
						}

						if ((session.getStatus() == SessionStatus.ACTIVE
								|| session.getStatus() == SessionStatus.UNACTIVE) && session.getStartActiveAt() == 0) {
							// 如果开始活跃时间为0，则将此设置为何最后一次活跃时间相同值，主要是为了兼容已经有的数据
							session.setStartActiveAt(session.getActiveAt());
						}
					}
				}
				if (oldMin == min) {
					break;
				}
			}
		} while (!CollectionUtils.isEmpty(typedTuples));

	}

	public void flushWebSession() {
		List<Session> sessions = sessionService.findInvalidWebSession(System.currentTimeMillis() - WEB_DDI,
				Env.getInt("session.flush.fetchCount"));
		if (!CollectionUtils.isEmpty(sessions)) {
			for (Session s : sessions) {
				try {
					if (s.getStatus() != SessionStatus.TIMEOUT && s.getStatus() != SessionStatus.LOGOUT) {
						s.setStatus(SessionStatus.TIMEOUT);
					}
					Double d = tokenOpt.score(SESSIONSET_KEY, s.getToken());
					if (d != null) {
						long lastActive = d.longValue();
						s.setActiveAt(lastActive);
					}
					logger.info("flush session token:[{}] to history...", s.getToken());
				} catch (Exception e) {
					logger.warn("get score from redis fail...");
				}
				try {
					sessionService.flush2History(s);
				} catch (Exception e) {
					sessionService.delHistory(s.getToken());
					sessionService.flush2History(s);
				}
			}
		}
	}

	public void flushMobileSession() {
		List<Session> sessions = sessionService.findInvalidMobileSession(System.currentTimeMillis() - MOBILE_DDI,
				Env.getInt("session.flush.fetchCount"));
		if (!CollectionUtils.isEmpty(sessions)) {
			for (Session s : sessions) {
				try {
					if (s.getStatus() != SessionStatus.TIMEOUT && s.getStatus() != SessionStatus.LOGOUT) {
						s.setStatus(SessionStatus.TIMEOUT);
					}
					Double d = tokenOpt.score(SESSIONSET_KEY, s.getToken());
					if (d != null) {
						long lastActive = d.longValue();
						s.setActiveAt(lastActive);
					}
					logger.info("flush session token:[{}] to history...", s.getToken());
				} catch (Exception e) {
					logger.warn("get score from redis fail...");
				}
				try {
					sessionService.flush2History(s);
				} catch (Exception e) {
					sessionService.delHistory(s.getToken());
					sessionService.flush2History(s);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		spOpt = getRedisTemplate().opsForValue();
		tokenOpt = getRedisTemplate().opsForZSet();
		loadConfig();
	}

	public void loadConfig() {
		WEB_DDI = TimeUnit.valueOf(Env.getDynamicString("session.web.expire.timeunit"))
				.toMillis(Env.getDynamicLong("session.web.expire"));
		WEB_ACTIVE_DDI = TimeUnit.valueOf(Env.getDynamicString("session.web.unactive.timeunit"))
				.toMillis(Env.getDynamicLong("session.web.unactive"));
		MOBILE_DDI = TimeUnit.valueOf(Env.getDynamicString("session.mobile.expire.timeunit"))
				.toMillis(Env.getDynamicLong("session.mobile.expire"));
		MOBILE_ACTIVE_DDI = TimeUnit.valueOf(Env.getDynamicString("session.mobile.unactive.timeunit"))
				.toMillis(Env.getDynamicLong("session.mobile.unactive"));
		GUEST_DDI = TimeUnit.valueOf(Env.getDynamicString("session.guest.expire.timeunit"))
				.toMillis(Env.getDynamicLong("session.guest.expire"));
	}
}
