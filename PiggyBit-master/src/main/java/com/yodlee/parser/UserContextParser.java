/*
* Copyright (c) 2015 Yodlee, Inc. All Rights Reserved.
*
* This software is the confidential and proprietary information of Yodlee, Inc.
* Use is subject to license terms.
*/
package com.yodlee.parser;

import java.io.IOException;

import com.google.gson.Gson;
import com.yodlee.beans.UserContext;

public class UserContextParser implements Parser 
{
	private String fqcn = this.getClass().getName();
	public UserContext parseJSON(String json) throws IOException 
	{
		String mn = "parseJSON(" + json + ")";
		//System.out.println(fqcn + " :: " + mn);
		Gson gson = new Gson();
		return (UserContext)gson.fromJson(json, UserContext.class);
	}

}
