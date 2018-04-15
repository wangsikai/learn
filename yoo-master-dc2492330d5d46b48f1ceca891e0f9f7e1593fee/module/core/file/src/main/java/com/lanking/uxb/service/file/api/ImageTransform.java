package com.lanking.uxb.service.file.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.lanking.cloud.domain.base.file.api.Dimension;
import com.lanking.cloud.domain.base.file.api.ImageType;
import com.lanking.cloud.domain.base.file.api.Position;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年11月19日
 *
 */
public interface ImageTransform {

	void load(String srcPath) throws IOException;

	void load(InputStream input) throws IOException;

	void save(String destPath) throws IOException;

	void save(OutputStream out) throws IOException;

	void close();

	boolean isModified();

	Dimension getSize() throws IOException;

	void resize(int width, int height) throws IOException;

	void rotate(double rotate) throws IOException;

	void resizeWithMax(Integer maxWidth, Integer maxHeight) throws IOException;

	void resizeWithMin(Integer maxWidth, Integer maxHeight) throws IOException;

	void resizeWithMinAndCrop(Integer maxWidth, Integer maxHeight) throws IOException;

	void resizeWithMaxEdge(Integer maxWidth, Integer maxHeight) throws IOException;

	void rotateWithMax(double rotate, Integer maxWidth, Integer maxHeight) throws IOException;

	void crop(int left, int top, Integer width, Integer height) throws IOException;

	void resizeAndcrop(int width, int height, int cropHeight) throws IOException;

	void pressImage(InputStream pressInputStream, Position position, float alpha) throws IOException;

	InputStream getTransformed() throws IOException;

	ImageType getImageType();

	void compressImageWithSave(String destPath, String formatName, float quality);

}
