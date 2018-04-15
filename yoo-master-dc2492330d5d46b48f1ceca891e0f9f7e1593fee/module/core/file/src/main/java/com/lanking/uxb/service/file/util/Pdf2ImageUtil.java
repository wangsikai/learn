package com.lanking.uxb.service.file.util;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

/**
 * pdf 2 image
 * 
 * <pre>
 * http://stackoverflow.com/questions/550129/export-pdf-pages-to-a-series-of-images-in-java
 * </pre>
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年6月29日
 */
public class Pdf2ImageUtil {

	private static Logger logger = LoggerFactory.getLogger(Pdf2ImageUtil.class);

	@SuppressWarnings({ "resource", "unchecked" })
	public static void setupAll(File pdfFile) throws IOException {
		try {
			RandomAccessFile raf = new RandomAccessFile(pdfFile, "r");
			FileChannel channel = raf.getChannel();
			ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
			PDFFile pdffile = new PDFFile(buf);
			int numPgs = pdffile.getNumPages();
			for (int i = 0; i < numPgs; i++) {
				PDFPage page = pdffile.getPage(i);
				Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(), (int) page.getBBox().getHeight());
				Image img = page.getImage(rect.width, rect.height, rect, null, true, true);
				BufferedImage bImg = toBufferedImage(img);
				File yourImageFile = new File(pdfFile.getParent() + File.separator + pdfFile.getName() + "_" + i
						+ ".png");
				ImageIO.write(bImg, "png", yourImageFile);
			}
		} catch (Exception e) {
			logger.error("convert all page to image error:", e);
			PDDocument doc = PDDocument.load(pdfFile);
			List<PDPage> pages = doc.getDocumentCatalog().getAllPages();
			int i = 0;
			for (PDPage page : pages) {
				BufferedImage image = page.convertToImage();
				File outputfile = new File(pdfFile.getParent() + File.separator + pdfFile.getName() + "_" + i + ".png");
				ImageIO.write(image, "png", outputfile);
				doc.close();
				i++;
			}
		}
	}

	@SuppressWarnings({ "resource", "unchecked" })
	public static File setupOne(File pdfFile) throws IOException {
		try {
			RandomAccessFile raf = new RandomAccessFile(pdfFile, "r");
			FileChannel channel = raf.getChannel();
			ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
			PDFFile pdffile = new PDFFile(buf);
			PDFPage page = pdffile.getPage(0);
			Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(), (int) page.getBBox().getHeight());
			Image img = page.getImage(rect.width, rect.height, rect, null, true, true);
			BufferedImage bImg = toBufferedImage(img);
			File yourImageFile = new File(pdfFile.getParent() + File.separator + pdfFile.getName() + ".png");
			ImageIO.write(bImg, "png", yourImageFile);
			return yourImageFile;
		} catch (Exception e) {
			logger.error("convert one page to image error:", e);
			PDDocument doc = PDDocument.load(pdfFile);
			List<PDPage> pages = doc.getDocumentCatalog().getAllPages();
			PDPage page = pages.get(0);
			BufferedImage image = page.convertToImage();
			File outputfile = new File(pdfFile.getParent() + File.separator + pdfFile.getName() + ".png");
			ImageIO.write(image, "png", outputfile);
			doc.close();
			return outputfile;
		}
	}

	public static BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}
		image = new ImageIcon(image).getImage();
		boolean hasAlpha = hasAlpha(image);
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			int transparency = Transparency.OPAQUE;
			if (hasAlpha) {
				transparency = Transparency.BITMASK;
			}
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
		} catch (HeadlessException e) {
			logger.error("pdf 2 image error:", e);
		}
		if (bimage == null) {
			int type = BufferedImage.TYPE_INT_RGB;
			if (hasAlpha) {
				type = BufferedImage.TYPE_INT_ARGB;
			}
			bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
		}
		Graphics g = bimage.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return bimage;
	}

	public static boolean hasAlpha(Image image) {
		if (image instanceof BufferedImage) {
			BufferedImage bimage = (BufferedImage) image;
			return bimage.getColorModel().hasAlpha();
		}
		PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			logger.error("pdf 2 image error:", e);
		}
		ColorModel cm = pg.getColorModel();
		return cm.hasAlpha();
	}
}