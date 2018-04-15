<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<w:document xmlns:wpc="http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas" xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006" xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships" xmlns:m="http://schemas.openxmlformats.org/officeDocument/2006/math" xmlns:v="urn:schemas-microsoft-com:vml" xmlns:wp14="http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing" xmlns:wp="http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing" xmlns:w10="urn:schemas-microsoft-com:office:word" xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main" xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml" xmlns:w15="http://schemas.microsoft.com/office/word/2012/wordml" xmlns:wpg="http://schemas.microsoft.com/office/word/2010/wordprocessingGroup" xmlns:wpi="http://schemas.microsoft.com/office/word/2010/wordprocessingInk" xmlns:wne="http://schemas.microsoft.com/office/word/2006/wordml" xmlns:wps="http://schemas.microsoft.com/office/word/2010/wordprocessingShape" mc:Ignorable="w14 w15 wp14">
	<w:body>
		<w:p w:rsidR="006B187A" w:rsidRDefault="00115385" w:rsidP="00FA068F">
			<w:pPr>
				<w:jc w:val="center"/>
				<w:rPr>
					<w:b/>
					<w:sz w:val="32"/>
					<w:szCs w:val="32"/>
				</w:rPr>
			</w:pPr>
			<w:r w:rsidRPr="007078DF">
				<w:rPr>
					<w:rFonts w:hint="eastAsia"/>
					<w:b/>
					<w:sz w:val="32"/>
					<w:szCs w:val="32"/>
				</w:rPr>
				<w:t>${title}</w:t>
			</w:r>
		</w:p>
		<w:p w:rsidR="00055D12" w:rsidRDefault="00055D12"/>
		<#list qtypes as qtype>
					<w:p></w:p>
					<w:p>
						<w:r>
							<w:rPr>
								<w:b/>
							</w:rPr>
							<w:t>${qtype.title}</w:t>
						</w:r>
					</w:p>
					<#list qtype.questions as question>
					<w:p></w:p>
					${question.content}
					<#if question.items??>
						${question.items}
					</#if>
					</#list>
		</#list>
		<w:p w:rsidR="00055D12" w:rsidRDefault="00055D12" w:rsidP="002420B5"/>
		<w:sectPr w:rsidR="00055D12">
			<w:pgSz w:w="11906" w:h="16838"/>
			<w:pgMar w:top="1440" w:right="1800" w:bottom="1440" w:left="1800" w:header="851" w:footer="992" w:gutter="0"/>
			<w:cols w:space="425"/>
			<w:docGrid w:type="lines" w:linePitch="312"/>
		</w:sectPr>
	</w:body>
</w:document>