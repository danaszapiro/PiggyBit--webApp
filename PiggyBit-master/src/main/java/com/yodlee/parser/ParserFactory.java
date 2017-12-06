/*
* Copyright (c) 2015 Yodlee, Inc. All Rights Reserved.
*
* This software is the confidential and proprietary information of Yodlee, Inc.
* Use is subject to license terms.
*/
package com.yodlee.parser;


public class ParserFactory 
{
	private static final String fqcn=ParserFactory.class.getName();
	
	public static Parser getParser(Class<?> T)
	{
		String mn = "getParser(" + T.getName()+ ")";
		//System.out.println(fqcn + " :: " + mn);
		Parser parser = null;
		if(T.getCanonicalName().equals("com.yodlee.beans.CobrandContext"))
		{
			parser = (CobrandContextParser) new CobrandContextParser();
		}
		if(T.getCanonicalName().equals("com.yodlee.beans.UserContext"))
		{
			parser = (UserContextParser) new UserContextParser();
		}
		
		if(T.getCanonicalName().equals("com.yodlee.beans.AccessToken"))
		{
			parser = (AccessTokenParser) new AccessTokenParser();
		}
		if(T.getCanonicalName().equals("com.yodlee.beans.ProviderAccountRefreshStatus"))
		{
			parser = (ProviderAccRefreshStatusParser) new ProviderAccRefreshStatusParser();
		}
		//System.out.println(fqcn + " :: " + mn+": Created Parser : " + parser.getClass().getName());
		return parser;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
