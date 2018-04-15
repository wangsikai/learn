<w:r>
	<w:rPr>
		<w:rFonts w:hint="eastAsia"/>
		<w:noProof/>
	</w:rPr>
	<w:drawing>
		<wp:inline distT="0" distB="0" distL="0" distR="0">
			<wp:extent cx="${width}" cy="${height}"/>
			<wp:effectExtent l="0" t="0" r="0" b="0"/>
			<wp:docPr id="${sequence}" name="图片 ${rid}"/>
			<wp:cNvGraphicFramePr>
				<a:graphicFrameLocks noChangeAspect="1" xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main"/>
			</wp:cNvGraphicFramePr>
			<a:graphic xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main">
				<a:graphicData uri="http://schemas.openxmlformats.org/drawingml/2006/picture">
					<pic:pic xmlns:pic="http://schemas.openxmlformats.org/drawingml/2006/picture">
						<pic:nvPicPr>
							<pic:cNvPr id="${sequence}" name="${name}"/>
							<pic:cNvPicPr/>
						</pic:nvPicPr>
						<pic:blipFill>
							<a:blip r:embed="${rid}" />
							<a:stretch>
								<a:fillRect/>
							</a:stretch>
						</pic:blipFill>
						<pic:spPr>
							<a:xfrm>
								<a:off x="0" y="0"/>
								<a:ext cx="${width}" cy="${height}"/>
							</a:xfrm>
							<a:prstGeom prst="rect">
								<a:avLst/>
							</a:prstGeom>
						</pic:spPr>
					</pic:pic>
				</a:graphicData>
			</a:graphic>
		</wp:inline>
	</w:drawing>
</w:r>