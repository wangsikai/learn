package com.lanking.cloud.ex;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.IllegalFormatException;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedRuntimeException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.lanking.cloud.ex.code.StatusCode;
import com.lanking.cloud.ex.code.core.CoreExceptionCode;

/**
 * 平台异常抽象类
 * 
 * @since 2.5.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年11月17日
 */
public abstract class AbstractException extends RuntimeException implements StatusCode {

	private static final long serialVersionUID = 8470428153437028199L;

	private static Logger logger = LoggerFactory.getLogger(AbstractException.class);

	private static Properties cfgMsg;

	private int code = SUCCEED;
	private Object args[];

	public AbstractException() {
		super();
	}

	public AbstractException(String message) {
		super(message);
	}

	public AbstractException(Throwable cause) {
		super(cause);
		code = parseCode(cause);
	}

	public AbstractException(String message, Throwable cause) {
		super(message, cause);
		code = parseCode(cause);
	}

	public AbstractException(int code, Object... args) {
		this();
		this.code = code;
		this.args = args;
	}

	public AbstractException(String message, int code, Object... args) {
		this(message);
		this.code = code;
		this.args = args;
	}

	public AbstractException(Throwable cause, int code, Object... args) {
		this(cause);
		this.code = code;
		this.args = args;
	}

	public AbstractException(String message, Throwable cause, int code, Object... args) {
		this(message, cause);
		this.code = code;
		this.args = args;
	}

	public int getCode() {
		return code;
	}

	public Object[] getArgs() {
		return args;
	}

	@Override
	public String getMessage() {
		String message = getCfgMessage(code, args);
		if (message != null) {
			return message;
		}
		return buildMessage(code, super.getMessage(), getCause());
	}

	@Override
	public String toString() {
		return getMessage();
	}

	static String getCfgMessage(int code, Object[] args) {
		if (cfgMsg == null) {
			// load config message
			cfgMsg = new Properties();
			try {
				Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath*:/error");
				for (Resource resource : resources) {
					cfgMsg.putAll(PropertiesLoaderUtils.loadProperties(new EncodedResource(resource, "UTF8")));
				}
			} catch (IOException e) {
				logger.error("load error msg config error:", e);
			}
		}

		String errorMsg = null;
		String msg = cfgMsg.getProperty("error." + code);
		if (msg != null) {
			String regex = "\\[\\d+\\]";
			Pattern pattern = Pattern.compile(regex);
			try {
				Matcher matcher = pattern.matcher(msg);
				StringBuffer sb = new StringBuffer();
				int i = 0;
				while (matcher.find()) {
					matcher.appendReplacement(sb, args[i].toString());
					i++;
				}
				matcher.appendTail(sb);
				errorMsg = sb.toString();
			} catch (Exception e) {
				logger.warn("get config message error", e);
			}
		}
		return errorMsg;
	}

	static int parseCode(Throwable cause) {
		if (cause instanceof AbstractException) {
			return ((AbstractException) cause).getCode();
		} else if (cause instanceof NullPointerException) {
			return CoreExceptionCode.NPE_EX;
		} else if (cause instanceof IOException) {
			return CoreExceptionCode.IO_EX;
		} else if (cause instanceof SQLException) {
			return CoreExceptionCode.DB_EX;
		} else if (cause instanceof UnsupportedOperationException) {
			return CoreExceptionCode.UN_SUPPORTED_OPERATION_EX;
		} else if (cause instanceof IllegalArgumentException) {
			return cause instanceof IllegalFormatException ? CoreExceptionCode.ILLEGAL_ARG_FORMAT_EX
					: CoreExceptionCode.ILLEGAL_ARG_EX;
		}
		return CoreExceptionCode.SERVER_EX;
	}

	static String buildMessage(int code, String defaultMessage, Throwable cause) {
		StringBuilder messageSb = new StringBuilder("errorcode:");
		messageSb.append(code);
		if (defaultMessage != null) {
			messageSb.append("; ");
			messageSb.append(defaultMessage);
		}
		if (cause != null) {
			Set<Throwable> visitedExceptions = new HashSet<Throwable>();
			Throwable tmpEx = cause;
			do {
				messageSb.append(" -> ");
				messageSb.append(cause);
				visitedExceptions.add(tmpEx);
				tmpEx = tmpEx.getCause();
			} while (!(tmpEx == null || visitedExceptions.contains(tmpEx) || tmpEx instanceof AbstractException || tmpEx instanceof NestedRuntimeException));
		}
		return messageSb.toString();
	}
}
