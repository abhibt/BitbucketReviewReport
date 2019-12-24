package com.bbi.BitBucketConnector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Base64.Encoder;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/*
 * RUN Configuration
 * PULLREQUEST
 * OR
 * REVIEWCOMMENTS
 * OR
 * BRANCHES
 */


public class BitBucketConnector {

	private static int nSerialNo = 0;
	enum eVal {Author,Reviewer};
	enum eOption {FILE,CS,CI};
	enum eOperation {PULLREQUEST,BRANCHES,REVIEWCOMMENTS};

	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		int OPTIONS = 3;
		String szOptions[] = {"PULLREQUEST", "REVIEWCOMMENTS","BRANCHES"};
		
		 HttpURLConnection urlConnection = null;
		 eOption bReadFile = eOption.CS; //read from file and do the parsing.
		 eOperation eOps =  eOperation.PULLREQUEST;

			int j =0;
			for (j =0; j< OPTIONS; j++)
			{
				if (szOptions[j].compareTo(args[0])==0)
				{
					break;
				}
			}
			System.out.print(szOptions[j]);
			switch(j)
			{
			case 0:eOps = eOperation.PULLREQUEST;break;
			case 1:eOps = eOperation.REVIEWCOMMENTS;break;
			case 2:eOps = eOperation.BRANCHES;break;
			default: break;
			}
		 
		 ListClass objList = new ListClass();
		    String url;
		 //   String data = json;
		    String result = null;
		    String szProjects[] = null;
/*		    String szProjects[] = {"contrail-agent",
		    		"contrail-common",
		    		"contrail-devops-tools",
		    		"contrail-dsc-ldap",
		    		"contrail-dsc-ontap",
		    		"contrail-gateway",
		    		"contrail-helm",
		    		"contrail-qa-automation",
		    		"service-agent-data-receiver",
		    		"service-community-detector",
		    		"service-community-resolver",
		    		"service-config-mgmt",
		    		"service-elasticsearch-clusters-maintenance",
		    		"service-flink-job-manager",
		    		"service-forensic-apis",
		    		"service-notifications",
		    		"service-raw-data-processor",
		    		"service-tenants",
		    		"service-webui"};
//	        String username ="abhit";*/
		    
		    //Password to be provided for working of the application
	        String CSpassword = "*******";  
	        

		    
		    String szProjects1[] = {"algorithm"
		    			};
	        String CIpassword = "abhibt";
	        String password = "*******";
	        Object urlBasePath = "https://bitbucket.com/rest/api/1.0/projects/<>/repos/";

	        urlBasePath = args[1].toString();
	        String szToken = args[2].toString(); //NDIyMjIxNTAxNDM3Oj4PnC3xVvzMFkzdQeAWIzu5KiMJ
	        
		    try {
		        String username ="abhit";
		        		   

		        String auth =new String(username + ":" + password);
		        System.out.println(auth);
		        byte[] data1 = auth.getBytes();
		        Encoder EnCode =  Base64.getEncoder();
		        String base64 = EnCode.encodeToString(data1);
				//Connect
//		        int i = szProjects.length-1; //required when bReadFile == true;
//		        i = (bReadFile == eOption.FILE)? i:0;
		        
		        //Get all the repos
		        result = ConnectAndGetData(urlBasePath,szToken);
		        base64 = szToken;
		        long nNoOfRepos = (long) GetRepoSize(result);
		        int nIgnoreRepos = args.length - 3;
		        System.out.println(nNoOfRepos-nIgnoreRepos);
		        szProjects = new String[(int)nNoOfRepos-nIgnoreRepos];
		        szProjects = ParseRepoResponse(result,szProjects,args);

		        int i = szProjects.length-1; //required when bReadFile == true;
		        i = (bReadFile == eOption.FILE)? i:0;

		        for ( nSerialNo = 0; i < szProjects.length; i++)
				        {
		        			//Get pull requests
		        			switch (eOps)
		        			{
			        			case PULLREQUEST:
			        			{
						        	if (bReadFile != eOption.FILE)
						        	{
						        		System.out.println(i);
						        		System.out.println(szProjects[i]);
						        		result = ConnectAndGetData(urlBasePath + szProjects[i] + "/pull-requests",base64);
						        	}
						        	else
						        	{
						        		result= readFileAsString("C:\\JavaLibs\\test.txt");
						        	}
						        	if (result.length() != 0)
							        {
							        	ParseResponse(i,szProjects[i],result,objList);
							           	PrintReviewsAndAuthors(objList);
							        }
						        	break;
			        			}
			        			case BRANCHES:
			        			{
						        	//Get branches
						        	if (bReadFile != eOption.FILE)
						        	{
						        		result = ConnectAndGetData(urlBasePath + szProjects[i] + "/branches",base64);
						        	}
						        	else
						        	{
						        		result= readFileAsString("C:\\JavaLibs\\Branches.txt");
						        	}
						        	if (result.length() != 0)
							        {
						        		ParseBranchResponse(i,szProjects[i],result,objList);
							        }
						        	break;
			        			}
			        			case REVIEWCOMMENTS:
			        			{
						        	//Get branches
			        				//if (i ==0)
			        				String szFileName = "";
			        				String szHref ="";
						        	if (bReadFile != eOption.FILE)
						        	{
						        		result = ConnectAndGetData(urlBasePath + szProjects[i] + "/pull-requests",base64);
						        	}
						        	else
						        	{
						        		result = readFileAsString("C:\\JavaLibs\\test.txt");
						        	}
						        	if (result.length() != 0)
							        {
						        		ArrayList<String> aszHrefs = ParsePRResponse(i,szProjects[i],result,objList);
						        		int nSize = aszHrefs.size();
						        		for( int k = 0; k <nSize; k++)
						        		{
								        	if (bReadFile != eOption.FILE)
								        	{
								        		szHref = (String)aszHrefs.get(k);
								        		//System.out.println("<p>" + szHref  + "</p>");
								        		int nLastIndex = szHref.lastIndexOf('/');
								        		szFileName = szHref.substring(nLastIndex+1, szHref.length());
								        		szFileName = szFileName + ".html";
								        		result = ConnectAndGetData((String)szHref+ "/activities",base64);
								        	}
								        	else
								        	{
								        		result= readFileAsString("C:\\JavaLibs\\comments.txt");
								        	}
				        					//CreateCommentsHTMLTable();
						        			ParseActivitiesResponse(result,szFileName,szHref);
						        			//CloseHTMLTable();
						        		}
							        }
						        	break;
			        			}			        				
			        			default: break;
		        		   }//switch
				        }//for loop
		        		//CloseHTMLTable();
		    } catch (UnsupportedEncodingException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
	}

	private static long GetRepoSize(String szResult) throws IOException, ParseException {
		// TODO Auto-generated method stub
        StringReader reader = new StringReader(szResult);
        
        JSONParser parser = new JSONParser();
        org.json.simple.JSONObject json = (org.json.simple.JSONObject) parser.parse(reader);

        long nSize = (long) json.get("size");

 		return nSize;
	}

	static void ParseActivitiesResponse(String szResult,String szFileName,String szHref) throws IOException, ParseException
	{
		  //System.out.println(szResult);
	      StringReader reader = new StringReader(szResult);
	       boolean bParentCommentFound = false; //special case, there can be comments without no comment anchor.
	       long lLineNumber = 0;
	       String szLineNumber = "";
			String szPath = "";
            File file = new File(szFileName);
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            output.write("<html><body><table border=\"1\"><thead bgcolor=\"Grey\">" +
        	        "<h1><tr><td>Serial Number</td><td>Date::LineNo</td><td>path</td><td>Comments</td></tr></h1></thead><tbody>");
     
            output.write("<p>" + szHref  + "</p>");
            
	        JSONParser parser = new JSONParser();
	        org.json.simple.JSONObject json = (org.json.simple.JSONObject) parser.parse(reader);
	        //System.out.println( szResult);
	        JSONArray jsonArray = (JSONArray) json.get("values");
			String szAuthor = null,szReviewers = null;
	        if (jsonArray != null)
	        {
		        Iterator<String> iterator = jsonArray.iterator();
		        while (iterator.hasNext()) {
		            //System.out.println(iterator.next());

		            Object slide = iterator.next();
		            
		            //Pull Request Name:
		            org.json.simple.JSONObject jsonObject2 = (org.json.simple.JSONObject) slide;
		            org.json.simple.JSONObject jsonObject3 = (org.json.simple.JSONObject) jsonObject2.get("comment");
		            if (jsonObject3 !=null)
		            {
		                output.write("<tr><td>" + Integer.toString(nSerialNo) + "</td>");
		                //System.out.print(Integer.toString(nSerialNo));
			            String szText = (String) jsonObject3.get("text");
			            org.json.simple.JSONObject jsonObject4 = (org.json.simple.JSONObject) jsonObject3.get("author");
			            szAuthor = (String) jsonObject4.get("name");
			            System.out.print(szText);
						//Created Date:
						
						//jsonObject2 = (org.json.simple.JSONObject) slide;
			            long lDate = (long) jsonObject3.get("updatedDate");
			            
			            Date date = new Date(lDate);
			            DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			            format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
			            String formatted = format.format(date);
			            //System.out.println(formatted);
			            format.setTimeZone(TimeZone.getTimeZone("India/Delhi"));
			            formatted = format.format(date);
			            //System.out.println(formatted);
			            
						jsonObject4 = (org.json.simple.JSONObject) jsonObject2.get("commentAnchor");
						if( jsonObject4 != null)
						{
				        	nSerialNo++;
				        	if( jsonObject4.containsKey("line")== true)
				        	{
				        		lLineNumber= (long) jsonObject4.get("line"); //I saw in some cases line # was not there.
				        		szLineNumber = String.valueOf(lLineNumber);
				        		szPath = (String) jsonObject4.get("path");
				        	}
						}
							//System.out.println("<td>Date:" + formatted +"Line Number:" + szLineNumber + "</td>");
						output.write("<td>"+"Line Number:" + szLineNumber + "</td>");
						output.write("<td>" + szPath + "</td>");
						output.write("<td>"+ szAuthor +"::" + szText);
						GetComments(jsonObject3.get("comments"),"<br>->",output );
						output.write("</td>");
						szLineNumber = "";
						szPath = "";
		            }
		        }

	        }
	        output.write("</tbody></table></body></html>");
	        output.close();
	        
	 }
	
	private static String GetComments(Object object,String szDash,BufferedWriter output ) throws IOException {
		// TODO Auto-generated method stub
		String szReturnedString = "";
		//org.json.simple.JSONObject jsonComments = object;
		String szReviewComment,szReviewerName = null;
		JSONArray jsonArrayReviewers = ((JSONArray) object);
        if (jsonArrayReviewers != null)
        {
	        Iterator<String> iteratorReviewers = jsonArrayReviewers.iterator();
	        while (iteratorReviewers.hasNext()) {
	            //System.out.println(iterator.next());
				Object slide1 = iteratorReviewers.next();
	            
	            //Pull Request Name:
	            org.json.simple.JSONObject jsonObject5 = (org.json.simple.JSONObject) slide1;
	            szReviewComment = (String) jsonObject5.get("text");
	            org.json.simple.JSONObject jsonObject6 = (JSONObject) jsonObject5.get("author");
	            szReviewerName = (String) jsonObject6.get("name");
	            szReturnedString = szDash+ szReviewerName + "::" + szReviewComment;
	            output.write(szReturnedString);     
		        JSONArray jsonObject7 = (JSONArray) jsonObject5.get("comments");
		        GetComments(jsonObject7,szDash + "->",output);
	        }
        }
		return szReturnedString;
	}
	
	static void ParseResponse (int nProjNo,String szProjectName, String szResult,ListClass objList) throws ParseException, IOException
	{
        StringReader reader = new StringReader(szResult);
        
        System.out.println(szResult);
        JSONParser parser = new JSONParser();
        org.json.simple.JSONObject json = (org.json.simple.JSONObject) parser.parse(reader);

        JSONArray jsonArray = (JSONArray) json.get("values");
        String szFileName = "PR.html";
    	File file = new File(szFileName);
    	BufferedWriter output;
        if (nProjNo == 0)
        {
        	output = new BufferedWriter(new FileWriter(file));
            output.write("<html><body><table border=\"1\"><thead bgcolor=\"Grey\">" +
        	        "<h1><tr><td>Serial Number</td><td>Project-Name</td><td>Title</td><td>Created Date</td>" +
        	        "<td>Author</td><td>Reviewer:Review Status</td></tr></h1></thead><tbody>");
        }
        else
        {
        	output = new BufferedWriter(new FileWriter(file,true));
        }
        
   	
        if (jsonArray != null)
        {
	        Iterator<String> iterator = jsonArray.iterator();
	        while (iterator.hasNext()) {
	            //System.out.println(iterator.next());

	            Object slide = iterator.next();
	            
	            //Pull Request Name:
	            org.json.simple.JSONObject jsonObject2 = (org.json.simple.JSONObject) slide;
	            long lID = (long) jsonObject2.get("id");
	            System.out.println(lID);
	            String szID = Long.toString(lID);
	            szID = szID + ".html";

	            output.write("<tr><td>" + Integer.toString(nSerialNo));
	            output.write("</td><td>" + "<a href=" + szID + ">" + szProjectName + "</a>");
	        	nSerialNo++;
	        		            
	            String szTitle = (String) jsonObject2.get("title");
	            output.write("</td><td>" + szTitle);
				
				//Created Date:
				
				jsonObject2 = (org.json.simple.JSONObject) slide;
	            long lDate = (long) jsonObject2.get("createdDate");
	            
	            Date date = new Date(lDate);
	            DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	            format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
	            String formatted = format.format(date);
	            //System.out.println(formatted);
	            format.setTimeZone(TimeZone.getTimeZone("India/Delhi"));
	            formatted = format.format(date);
	            //System.out.println(formatted);
	            
	            // If date is more than 5 days old then highlight in red.
		            long lCurrentDate = System.currentTimeMillis();
		            long diffInMillies = (lCurrentDate - lDate);
		            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		            if (diff >5 )
		            {
		            	output.write("</td><td bgcolor=\"red\">" + formatted);
		            }
		            else
		            {
		            	output.write("</td><td>" + formatted);
		            }
	            
				//Author:
				String szAuthor = null,szReviewers = null;
				org.json.simple.JSONObject jsonObject3 = (JSONObject) jsonObject2.get("author");
				org.json.simple.JSONObject jsonObject4 = (JSONObject) jsonObject3.get("user");
				szAuthor = (String) jsonObject4.get("name");
				output.write("</td><td>" + szAuthor + "</td><td>");
				objList.AddOrUpdate(szAuthor,eVal.Author,null);
				
				JSONArray jsonArrayReviewers = (JSONArray) jsonObject2.get("reviewers");
		        if (jsonArrayReviewers != null)
		        {
			        Iterator<String> iteratorReviewers = jsonArrayReviewers.iterator();
			        while (iteratorReviewers.hasNext()) {
			            //System.out.println(iterator.next());
						
			            Object slide1 = iteratorReviewers.next();
			            
			            //Pull Request Name:
			            org.json.simple.JSONObject jsonObject5 = (org.json.simple.JSONObject) slide1;
			            org.json.simple.JSONObject jsonObject6 = (JSONObject) jsonObject5.get("user");
			            szReviewers = (String) jsonObject6.get("name");
			            String szStatus = (String) jsonObject5.get("status");
			            output.write(szReviewers + ":" + szStatus  + ",");
						objList.AddOrUpdate(szReviewers,eVal.Reviewer,szStatus);
			        }
			        output.write("</td></tr>");
		        }
				
	        }
      
        }
        //output.write("</tbody></table></body></html>");
        output.close();
	}

	static String[] ParseRepoResponse (String szResult,String[] szProjects, String[] args) throws ParseException, IOException
	{
        StringReader reader = new StringReader(szResult);
        int n = 0;
        boolean bIgnore = false;
 /*       JSONParser parser = new JSONParser();
        org.json.simple.JSONObject json = (org.json.simple.JSONObject) parser.parse(reader);

        JSONArray jsonArray = (JSONArray) json.get("values");
        System.out.println("Args:");
        System.out.println(args.length);
        System.out.println(szResult);

        if (jsonArray != null)
        {
	        Iterator<String> iterator = jsonArray.iterator();
	        while (iterator.hasNext()) {
	        	
	            Object slide = iterator.next();
	            //Pull Request Name:
	            org.json.simple.JSONObject jsonObject2 = (org.json.simple.JSONObject) slide;
	            String szTitle = (String) jsonObject2.get("name");
	            for ( int j =1; j< args.length; j ++)
	            {
	            	if (szTitle.equals(args[j])==true) //Ignore a particular repo
	            	{
	            		bIgnore = true;
	            		break;
	            	}
	            }

	            if ( bIgnore == false)
	            {
	            	szProjects[n] = szTitle;
		            System.out.print(n);
		            System.out.println(szTitle);
	            	n++;
	            }
	            bIgnore = false;
	        }
        }
        return szProjects; */
		
     
        System.out.println(szResult);
        JSONParser parser = new JSONParser();
        org.json.simple.JSONObject json = (org.json.simple.JSONObject) parser.parse(reader);

        JSONArray jsonArray = (JSONArray) json.get("values");
        String szFileName = "PR.html";
    	File file = new File(szFileName);
    	BufferedWriter output;
         
   	
        if (jsonArray != null)
        {
	        Iterator<String> iterator = jsonArray.iterator();
	        while (iterator.hasNext()) {
	            //System.out.println(iterator.next());

	            Object slide = iterator.next();
	            
	            //Pull Request Name:
	            org.json.simple.JSONObject jsonObject2 = (org.json.simple.JSONObject) slide;
	            String szTitle = (String) jsonObject2.get("slug");
	            System.out.println(szTitle);
	            for ( int j =3; j< args.length; j ++)
	            {
	            	if (szTitle.equals(args[j])==true) //Ignore a particular repo
	            	{
	            		bIgnore = true;
	            		break;
	            	}
	            }

	            if ( bIgnore == false)
	            {
	            	szProjects[n] = szTitle;
		            System.out.print(n);
		            System.out.println(szTitle);
	            	n++;
	            }
	            bIgnore = false;

        }
        }
        return szProjects;
	}

	static ArrayList<String> ParsePRResponse (int nProjNo,String szProjectName, String szResult,ListClass objList) throws ParseException, IOException
	{
        StringReader reader = new StringReader(szResult);
        ArrayList<String> aszHrefs = new ArrayList<String>();
        
        JSONParser parser = new JSONParser();
        org.json.simple.JSONObject json = (org.json.simple.JSONObject) parser.parse(reader);

        JSONArray jsonArray = (JSONArray) json.get("values");
       

        
        if (jsonArray != null)
        {
	        Iterator<String> iterator = jsonArray.iterator();
	        while (iterator.hasNext()) {
	            //System.out.println(iterator.next());
	        	nSerialNo++;
	            //System.out.println("</tr><td>" + Integer.toString(nSerialNo));
	        	//System.out.println("</td><td>" + szProjectName);
	            Object slide = iterator.next();
	            
	            //Pull Request Name:
	            org.json.simple.JSONObject jsonObject2 = (org.json.simple.JSONObject) slide;
	            String szTitle = (String) jsonObject2.get("title");
				//System.out.println("</td><td>" + szTitle);
				
				//Created Date:
				jsonObject2 = (org.json.simple.JSONObject) slide;
	            long lDate = (long) jsonObject2.get("createdDate");
	            
	            Date date = new Date(lDate);
	            DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	            format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
	            String formatted = format.format(date);
	            //System.out.println(formatted);
	            format.setTimeZone(TimeZone.getTimeZone("India/Delhi"));
	            formatted = format.format(date);
	            //System.out.println(formatted);
	            
				//System.out.println("</td><td>" + formatted);
				
				//Author:
				String szAuthor = null,szReviewers = null;
				org.json.simple.JSONObject jsonObject3 = (JSONObject) jsonObject2.get("author");
				org.json.simple.JSONObject jsonObject4 = (JSONObject) jsonObject3.get("user");
				szAuthor = (String) jsonObject4.get("name");
				//System.out.println("</td><td>" + szAuthor + "</td><td>");
				objList.AddOrUpdate(szAuthor,eVal.Author,null);
				
				JSONArray jsonArrayReviewers = (JSONArray) jsonObject2.get("reviewers");
		        if (jsonArrayReviewers != null)
		        {
			        Iterator<String> iteratorReviewers = jsonArrayReviewers.iterator();
			        while (iteratorReviewers.hasNext()) {
			            //System.out.println(iterator.next());
						
			            Object slide1 = iteratorReviewers.next();
			            
			            //Pull Request Name:
			            org.json.simple.JSONObject jsonObject5 = (org.json.simple.JSONObject) slide1;
			            org.json.simple.JSONObject jsonObject6 = (JSONObject) jsonObject5.get("user");
			            szReviewers = (String) jsonObject6.get("name");
			            String szStatus = (String) jsonObject5.get("status");
						//System.out.println(szReviewers + ":" + szStatus  + ",");
						objList.AddOrUpdate(szReviewers,eVal.Reviewer,szStatus);
			        }
			        //System.out.println("</td></tr>");
		        }
		        org.json.simple.JSONObject jsonObject7 = (org.json.simple.JSONObject) jsonObject2.get("links");
				jsonArrayReviewers = (JSONArray) jsonObject7.get("self");
		        if (jsonArrayReviewers != null)
		        {
			        Iterator<String> iteratorReviewers = jsonArrayReviewers.iterator();
			        while (iteratorReviewers.hasNext()) {
			            //System.out.println(iterator.next());
						
			            Object slide1 = iteratorReviewers.next();
			            
			            //Pull Request Name:
			            org.json.simple.JSONObject jsonObject5 = (org.json.simple.JSONObject) slide1;
			            szReviewers = (String) jsonObject5.get("href");
			            aszHrefs.add(szReviewers);
						//System.out.println(szReviewers);
						
			        }
			        //System.out.println("</td></tr>");
		        }
				
	        }
      
        }
		return aszHrefs;
 
	}

	static void ParseBranchResponse (int nProjNo,String szProjectName, String szResult,ListClass objList) throws ParseException, IOException
	{
        StringReader reader = new StringReader(szResult);
        
        
        JSONParser parser = new JSONParser();
        org.json.simple.JSONObject json = (org.json.simple.JSONObject) parser.parse(reader);

        JSONArray jsonArray = (JSONArray) json.get("values");

        File file = new File("Branches.html");
        //BufferedWriter output = new BufferedWriter(new FileWriter(file));
    	
    	BufferedWriter output;
        if (nProjNo == 0)
        {
        	output = new BufferedWriter(new FileWriter(file));
            output.write("<html><body><table border=\"1\"><thead bgcolor=\"Grey\">" +
        	        "<h1><tr><td>Serial Number</td><td>Project-Name</td><td>Branch Name</td><td>Created Date(To Do)</td>" +
        	        "<td>Author(To Do)</td><td>Merge Status(To Do)</td></tr></h1></thead><tbody>");
        }
        else
        {
        	output = new BufferedWriter(new FileWriter(file,true));
        }
        
        if (jsonArray != null)
        {
	        Iterator<String> iterator = jsonArray.iterator();
	        while (iterator.hasNext()) {
	            //System.out.println(iterator.next());
	            Object slide = iterator.next();
	            
	            //Branch Name:
	            org.json.simple.JSONObject jsonObject2 = null;
            	jsonObject2 = (org.json.simple.JSONObject) slide;
	            String szTitle = (String) jsonObject2.get("displayId");
	            if ((szTitle.compareTo("dev")!= 0)  && (szTitle.compareTo("master")!=0))
	            {
		        	nSerialNo++;
		        	output.write("</tr><td>" + Integer.toString(nSerialNo));
		        	output.write("</td><td>" + szProjectName);
	            	//szTitle = (String) jsonObject2.get("displayId");
		        	output.write("</td><td>" + szTitle);
		        	output.write("</td></tr>");
	            }

	        }
   
        }
       // output.write("</tbody></table></body></html>");
        output.close();
 
	}

    private  static void CreateBranchesHTMLTable()
    {
    	System.out.println("<html><body><table border=\"1\"><thead bgcolor=\"Grey\">" +
    	        "<h1><tr><td>Serial Number</td><td>Project-Name</td><td>Branch Name</td></tr></h1></thead><tbody>");
    }
    
    private  static void CreateCommentsHTMLTable()
    {
    	System.out.println("<html><body><table border=\"1\"><thead bgcolor=\"Grey\">" +
    	        "<h1><tr><td>Serial Number</td><td>Date::LineNo</td><td>path</td><td>Comments</td></tr></h1></thead><tbody>");
   	
    }
	private static void PrintReviewsAndAuthors(ListClass objList) throws IOException
	{
        String szFileName = "ReviewTable.html";
        File file = new File(szFileName);
        BufferedWriter output = new BufferedWriter(new FileWriter(file));
		
        output.write("\n\n\n<html><body><table border=\"1\"><thead bgcolor=\"Grey\">" +
    	        "<h1><tr><td>Serial Number</td><td>Name</td>" +
    	        "<td>Authored</td><td>Pending Reviewes</td></tr></h1></thead><tbody>");
    	int nSize = objList.GetSize();
    	output.write("Number of Current Users: " + Integer.toString(nSize));
    	Book nTemp = null;
    	for (int n=0; n < nSize ; n++)
    	{
    		output.write("</tr><td>" + Integer.toString(n));
    		nTemp = objList.Get(n);
    		output.write("</td><td>" + nTemp.name + "</td>");
    		output.write("<td>" + Integer.toString(nTemp.nAuthored) + "</td>");
    		output.write("<td>" + Integer.toString(nTemp.nReviewer) + "</td></tr>");
    	}
    	//CloseHTMLTable();
    	output.write("</tbody></table></body></html>");
    	output.close();
	}
    private static String readFileAsString(String filePath) throws IOException {
        StringBuffer fileData = new StringBuffer();
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }
    
    private  static void CreateHTMLTable()
    {
    	System.out.println("<html><body><table border=\"1\"><thead bgcolor=\"Grey\">" +
    	        "<h1><tr><td>Serial Number</td><td>Project-Name</td><td>Title</td><td>Created Date</td>" +
    	        "<td>Author</td><td>Reviewer:Review Status</td></tr></h1></thead><tbody>");

    }
    private  static void CloseHTMLTable()
    {
    	System.out.println("</tbody></table></body></html>");
    }

    private static void GetBranches(String szProjects,String result)
    {
    	
    }
    
    
    private static String ConnectAndGetData(Object urlBasePath,String base64) throws IOException
    {
    	HttpURLConnection urlConnection = null;    	
    	String szResult = null;

        urlConnection = (HttpURLConnection) ((new URL((String) urlBasePath).openConnection()));
        urlConnection.setDoOutput(true);
        urlConnection.setRequestProperty("Content-Type", "application/json");
        //urlConnection.setRequestProperty("Authorization", "Basic "+base64);
        urlConnection.setRequestProperty("Authorization", "Bearer "+base64);
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setRequestMethod("GET");
        urlConnection.setConnectTimeout(10000);
        urlConnection.connect();
        /*JSONObject obj = new JSONObject();

        obj.put("MobileNumber", "+97333746934");
        obj.put("EmailAddress", "danish.hussain@mee.com");
        obj.put("FirstName", "Danish");
        obj.put("LastName", "Hussain");
        obj.put("Country", "BH");
        obj.put("Language", "EN");
        String data = obj.toString();
        //Write
        //OutputStream outputStream = (OutputStream) urlConnection.getOutputStream();
        //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
        //writer.write(data);
        writer.close();
        outputStream.close();*/
        int responseCode=urlConnection.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) 
        {
            //Read
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

        String line = null;
        StringBuilder sb = new StringBuilder();

        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        
        bufferedReader.close();
        szResult = sb.toString();
        urlConnection.disconnect();
        }
        //System.out.println(szResult);
    	return szResult;
    }
}


