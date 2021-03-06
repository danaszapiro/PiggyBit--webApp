/*
* Copyright (c) 2015 Yodlee, Inc. All Rights Reserved.
*
* This software is the confidential and proprietary information of Yodlee, Inc.
* Use is subject to license terms.
*/
package com.yodlee.parser;

import java.io.IOException;


public class GSONParser 
{
	private static final String fqcn = GSONParser.class.getName();
	
	public static <T> Object handleJson(String json, Class<?> T) throws IOException
	{
		System.out.println("In GSONParser");
		System.out.println(json);
		System.out.println(T.getCanonicalName());
		String mn = "handleJson(" + json + ", " + T.getCanonicalName()+" )";
		//System.out.println(fqcn + " :: " + mn );
		return ParserFactory.getParser(T).parseJSON(json);
	}
	
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub

	}

}
