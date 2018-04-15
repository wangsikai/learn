package com.lanking.cloud.sdk.util;

import com.vdurmont.emoji.EmojiParser;

/**
 * emoji表情工具类<br>
 * http://apps.timwhitlock.info/emoji/tables/unicode<br>
 * 1. Emoticons ( 1F601 - 1F64F )<br>
 * 2. Dingbats ( 2702 - 27B0 )<br>
 * 3. Transport and map symbols ( 1F680 - 1F6C0 )<br>
 * 4. Enclosed characters ( 24C2 - 1F251 )<br>
 * 5. Uncategorized<br>
 * 6a. Additional emoticons ( 1F600 - 1F636 )<br>
 * 6b. Additional transport and map symbols ( 1F681 - 1F6C5 )<br>
 * 6c. Other additional symbols ( 1F30D - 1F567 ) <br>
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年6月29日
 */
public final class EmojiUtils {

	public static String filterEmoji(String source) {
		return EmojiParser.parseToAliases(source);
	}
}
