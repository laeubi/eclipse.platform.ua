/*******************************************************************************
 * Copyright (c) 2018 Remain Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     wim.jongman@remainsoftware.com - initial API and implementation
 *******************************************************************************/
package org.eclipse.tips.json.internal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.tips.core.IHtmlTip;
import org.eclipse.tips.core.Tip;
import org.eclipse.tips.core.TipImage;
import org.eclipse.tips.json.IJsonTip;

import com.google.gson.JsonObject;

/**
 * Internal implementation for a Json generated {@link IHtmlTip}.
 *
 */
public class JsonHTMLTip extends Tip implements IJsonTip, IHtmlTip {

	private String fSubject;
	private Date fDate;
	private String fHtml;
	private TipImage fTipImage;
	private String fJsonObject;

	/**
	 * Creates the tip out od the passed Json object.
	 *
	 * @param jsonObject
	 *            the json object
	 * @param provider
	 *            the provider
	 * @throws ParseException
	 *             when the json object could not be parsed.
	 */
	public JsonHTMLTip(String providerId, JsonObject jsonObject) throws ParseException {
		super(providerId);
		fJsonObject = jsonObject.toString();
		fSubject = Util.getValueOrDefault(jsonObject, JsonConstants.T_SUBJECT, "Not set");
		fDate = getDate(jsonObject);
		fHtml = Util.getValueOrDefault(jsonObject, JsonConstants.T_HTML, null);
		String base64Img = Util.getValueOrDefault(jsonObject, JsonConstants.T_IMAGE, null);
		if (base64Img != null) {
			fTipImage = new TipImage(base64Img);
			double ratio = Util.getValueOrDefault(jsonObject, JsonConstants.T_RATIO, 0d);
			if (ratio > 0) {
				fTipImage.setAspectRatio(ratio);
			}
			int max = Util.getValueOrDefault(jsonObject, JsonConstants.T_MAXWIDTH, 0);
			if (max > 0) {
				fTipImage.setMaxWidth(max);
			}
			max = Util.getValueOrDefault(jsonObject, JsonConstants.T_MAXHEIGHT, 0);
			if (max > 0) {
				fTipImage.setMaxHeight(max);
			}
		}
	}

	private static Date getDate(JsonObject jsonObject) throws ParseException {
		String date = Util.getValueOrDefault(jsonObject, JsonConstants.T_DATE, "1970-01-01");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.parse(date);
	}

	@Override
	public String getJsonObject() {
		return fJsonObject;
	}

	@Override
	public Date getCreationDate() {
		return fDate;
	}

	@Override
	public String getHTML() {
		return fHtml;
	}

	@Override
	public TipImage getImage() {
		return fTipImage;
	}

	@Override
	public String getSubject() {
		return fSubject;
	}
}