<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<w:document xmlns:wpc="http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas" xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006" xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships" xmlns:m="http://schemas.openxmlformats.org/officeDocument/2006/math" xmlns:v="urn:schemas-microsoft-com:vml" xmlns:wp14="http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing" xmlns:wp="http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing" xmlns:w10="urn:schemas-microsoft-com:office:word" xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main" xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml" xmlns:w15="http://schemas.microsoft.com/office/word/2012/wordml" xmlns:wpg="http://schemas.microsoft.com/office/word/2010/wordprocessingGroup" xmlns:wpi="http://schemas.microsoft.com/office/word/2010/wordprocessingInk" xmlns:wne="http://schemas.microsoft.com/office/word/2006/wordml" xmlns:wps="http://schemas.microsoft.com/office/word/2010/wordprocessingShape" mc:Ignorable="w14 w15 wp14">
	<w:body>
		<#if showQuestion>
		<w:p w:rsidR="00A77B3E" w:rsidRPr="00720C64" w:rsidRDefault="00443CDA" w:rsidP="00720C64">
			<w:pPr>
				<w:spacing w:before="240" w:after="280" w:afterAutospacing="1"/>
				<w:jc w:val="center"/>
				<w:rPr>
					<w:rFonts w:ascii="Cambria Math" w:eastAsia="宋体" w:hAnsi="Cambria Math" w:cs="宋体" w:hint="eastAsia"/>
					<w:b/>
					<w:sz w:val="30"/>
					<w:szCs w:val="30"/>
					<w:lang w:eastAsia="zh-CN"/>
				</w:rPr>
			</w:pPr>
			<w:r w:rsidRPr="00720C64">
				<w:rPr>
					<w:rFonts w:ascii="Cambria Math" w:eastAsia="宋体" w:hAnsi="Cambria Math" w:cs="宋体"/>
					<w:b/>
					<w:sz w:val="30"/>
					<w:szCs w:val="30"/>
					<w:lang w:eastAsia="zh-CN"/>
				</w:rPr>
				<!-- MD 试卷标题 -->
				<w:t>${title}</w:t>
			</w:r>
		</w:p>
		<w:p w:rsidR="00A77B3E" w:rsidRPr="00720C64" w:rsidRDefault="00443CDA" w:rsidP="00720C64">
			<w:pPr>
				<w:spacing w:before="240" w:after="280" w:afterAutospacing="1"/>
				<w:jc w:val="center"/>
				<w:rPr>
					<w:rFonts w:ascii="Cambria Math" w:eastAsia="宋体" w:hAnsi="Cambria Math" w:cs="宋体" w:hint="eastAsia"/>
					<w:sz w:val="21"/>
					<w:lang w:eastAsia="zh-CN"/>
				</w:rPr>
			</w:pPr>
			<#if showTime??>
			<w:r w:rsidRPr="00720C64">
				<w:rPr>
					<w:rFonts w:ascii="Cambria Math" w:eastAsia="宋体" w:hAnsi="Cambria Math" w:cs="宋体"/>
					<w:sz w:val="21"/>
					<w:lang w:eastAsia="zh-CN"/>
				</w:rPr>
				<!-- MD 时长 -->
				<w:t>时长：${time}</w:t>
			</w:r>
			<w:r w:rsidRPr="00720C64">
				<w:rPr>
					<w:rFonts w:ascii="Cambria Math" w:eastAsia="宋体" w:hAnsi="Cambria Math" w:cs="宋体"/>
					<w:sz w:val="21"/>
					<w:lang w:eastAsia="zh-CN"/>
				</w:rPr>
				<w:t xml:space="preserve">                </w:t>
			</w:r>
			</#if>
			<w:r w:rsidRPr="00720C64">
				<w:rPr>
					<w:rFonts w:ascii="Cambria Math" w:eastAsia="宋体" w:hAnsi="Cambria Math" w:cs="宋体"/>
					<w:sz w:val="21"/>
					<w:lang w:eastAsia="zh-CN"/>
				</w:rPr>
				<!-- MD 总分 -->
				<w:t>总分：${totalScore}</w:t>
			</w:r>
		</w:p>
		
		<!-- MD 外层题型循环开始 -->
		<#list topics as topic>
		<w:p w:rsidR="00642350" w:rsidRDefault="00642350" w:rsidP="00642350">
			<w:pPr>
				<w:spacing w:before="240"/>
				<w:jc w:val="center"/>
				<w:rPr>
					<w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cs="宋体"/>
					<w:b/>
					<w:sz w:val="30"/>
					<w:lang w:eastAsia="zh-CN"/>
				</w:rPr>
			</w:pPr>
		</w:p>
		<w:p w:rsidR="00A77B3E" w:rsidRPr="00720C64" w:rsidRDefault="00443CDA" w:rsidP="003A6F29">
			<w:pPr>
				<w:spacing w:before="240" w:after="280" w:afterAutospacing="1"/>
				<w:rPr>
					<w:rFonts w:ascii="Cambria Math" w:eastAsia="宋体" w:hAnsi="Cambria Math" w:cs="宋体" w:hint="eastAsia"/>
					<w:b/>
					<w:lang w:eastAsia="zh-CN"/>
				</w:rPr>
			</w:pPr>
			<w:r w:rsidRPr="00720C64">
				<w:rPr>
					<w:rFonts w:ascii="Cambria Math" w:eastAsia="宋体" w:hAnsi="Cambria Math" w:cs="宋体"/>
					<w:b/>
					<w:lang w:eastAsia="zh-CN"/>
				</w:rPr>
				<!-- MD 题型标题 -->
				<w:t>${topic.title}</w:t>
			</w:r>
		</w:p>
		<!-- MD 题目循环开始 -->
		<#list topic.questions as question>		
			<!-- MD 题目内容 -->
			${question.content}
			<#if question.items??>
				${question.items}
			</#if>
			<w:p></w:p>
			<w:p></w:p>
		</#list>
		
		</#list>
		<!-- MD /外层题型循环结束 end-->
		</#if>
		
		<#if showAnswer>
		<w:p w:rsidR="00495156" w:rsidRPr="008B31A5" w:rsidRDefault="00443CDA" w:rsidP="008B31A5">
			<w:pPr>
				<w:spacing w:before="240" w:after="280" w:afterAutospacing="1"/>
				<w:jc w:val="center"/>
				<w:rPr>
					<w:rFonts w:ascii="Cambria Math" w:eastAsia="宋体" w:hAnsi="Cambria Math" w:cs="宋体" w:hint="eastAsia"/>
					<w:b/>
					<w:sz w:val="30"/>
					<w:szCs w:val="30"/>
					<w:lang w:eastAsia="zh-CN"/>
				</w:rPr>
			</w:pPr>
			<#if showQuestion>
			<w:r>
				<w:rPr>
					<w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cs="宋体"/>
					<w:sz w:val="21"/>
					<w:lang w:eastAsia="zh-CN"/>
				</w:rPr>
				<w:br w:type="page"/>
			</w:r>
			</#if>
			<w:r w:rsidR="00495156" w:rsidRPr="008B31A5">
				<w:rPr>
					<w:rFonts w:ascii="Cambria Math" w:eastAsia="宋体" w:hAnsi="Cambria Math" w:cs="宋体"/>
					<w:b/>
					<w:sz w:val="30"/>
					<w:szCs w:val="30"/>
					<w:lang w:eastAsia="zh-CN"/>
				</w:rPr>
				<w:lastRenderedPageBreak/>
				<!-- MD 试卷标题 -->
				<w:t>${title}</w:t>
			</w:r>
		</w:p>
		<w:p w:rsidR="00495156" w:rsidRPr="002F771D" w:rsidRDefault="00443CDA" w:rsidP="008B31A5">
			<w:pPr>
				<w:spacing w:before="240" w:after="280" w:afterAutospacing="1"/>
				<w:jc w:val="center"/>
				<w:rPr>
					<w:rFonts w:ascii="Cambria Math" w:eastAsia="宋体" w:hAnsi="Cambria Math" w:cs="宋体" w:hint="eastAsia"/>
					<w:b/>
					<w:sz w:val="30"/>
					<w:szCs w:val="30"/>
					<w:lang w:eastAsia="zh-CN"/>
				</w:rPr>
			</w:pPr>
			<w:r w:rsidRPr="002F771D">
				<w:rPr>
					<w:rFonts w:ascii="Cambria Math" w:eastAsia="宋体" w:hAnsi="Cambria Math" w:cs="宋体"/>
					<w:b/>
					<w:sz w:val="30"/>
					<w:szCs w:val="30"/>
					<w:lang w:eastAsia="zh-CN"/>
				</w:rPr>
				<w:t>试卷答案</w:t>
			</w:r>
		</w:p>
		<w:p w:rsidR="002F771D" w:rsidRDefault="002F771D" w:rsidP="008958C7">
			<w:pPr>
				<w:spacing w:before="240" w:after="280" w:afterAutospacing="1"/>
				<w:rPr>
					<w:rFonts w:ascii="Cambria Math" w:eastAsia="宋体" w:hAnsi="Cambria Math" w:cs="宋体" w:hint="eastAsia"/>
					<w:b/>
					<w:lang w:eastAsia="zh-CN"/>
				</w:rPr>
			</w:pPr>
		</w:p>
		
		<!-- MD 外层题型循环开始 -->
		<#list topics as topic>
		<w:p w:rsidR="008958C7" w:rsidRPr="008958C7" w:rsidRDefault="008958C7" w:rsidP="008958C7">
			<w:pPr>
				<w:spacing w:before="240" w:after="280" w:afterAutospacing="1"/>
				<w:rPr>
					<w:rFonts w:ascii="Cambria Math" w:eastAsia="宋体" w:hAnsi="Cambria Math" w:cs="宋体" w:hint="eastAsia"/>
					<w:b/>
					<w:lang w:eastAsia="zh-CN"/>
				</w:rPr>
			</w:pPr>
			<w:r w:rsidRPr="00720C64">
				<w:rPr>
					<w:rFonts w:ascii="Cambria Math" w:eastAsia="宋体" w:hAnsi="Cambria Math" w:cs="宋体"/>
					<w:b/>
					<w:lang w:eastAsia="zh-CN"/>
				</w:rPr>
				<!-- MD 题型标题 -->
				<w:t>${topic.title}</w:t>
			</w:r>
		</w:p>
		<!-- MD 题目循环开始 -->
		<#list topic.questions as question>		
			<!-- MD 答案 -->
			${question.answers}
			<w:p></w:p>
		</#list>
		<!-- MD /题目循环结束 end-->
		
		</#list>
		<!-- MD /外层题型循环结束 end-->
		</#if>
		
		<w:sectPr w:rsidR="008958C7" w:rsidRPr="008458BC" w:rsidSect="00B51059">
			<w:pgSz w:w="23811" w:h="16837"/>
			<w:pgMar w:top="1440" w:right="1800" w:bottom="1440" w:left="1800" w:header="708" w:footer="708" w:gutter="0"/>
			<w:cols w:num="2" w:space="708"/>
			<w:docGrid w:linePitch="360"/>
		</w:sectPr>
	</w:body>
</w:document>