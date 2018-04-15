package com.lanking.uxb.service.latex.api;

import java.io.File;
import java.util.List;

/**
 * latex 相关服务.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年12月11日
 */
public interface LatexService {

	/**
	 * 查找/生成本地latex图片文件.
	 * 
	 * @param latex
	 * @param md5Latex
	 * @param color
	 * @return
	 */
	File storeLatexImage(String latex, String md5Latex, String color);

	String mml2Latex(String mml);

	List<String> mml2Latex(List<String> mmls);

	/**
	 * mml里面包含多个mml编码，中间可能混着中文等数据,如：<br>
	 * 
	 * <pre>
	 * 	痛苦<math xmlns="http://www.w3.org/1998/Math/MathML">
	 * 		<mstyle displaystyle="true">
	 * 			<mn>77</mn><mo>×</mo><mo>+</mo><mi>f</mi><mi>f</mi>
	 * 		</mstyle>
	 *    </math>
	 * </pre>
	 * 
	 * @param mml
	 * @return
	 */
	String multiMml2Latex(String mml);

	List<String> multiMml2Latex(List<String> mmls);
}
