/*import java.awt.Dimension;
import java.awt.List;
import java.util.ArrayList;
import java.util.LinkedList;

class Holder {
	  String szString; //key
	  int nAuthors;
	  int nReviews;

	public void ListClass(String szName,int noAuthors,int noReviews)
	{
		this.szString = szName;
		this.nAuthors = noAuthors;
		this.nReviews = noReviews;
	}
}

public class ListClass {
	


	static List linkedlist;
	
	
	public static void AddOrUpdate(String szName,BitBucketConnector.eVal author) {
		/* Linked List Declaration */
/*		Holder nTemp;
		if(linkedlist == null )
		{
			ArrayList<Holder> linkedlist=new ArrayList<Holder>();  
			
			//System.out.println("New List");
		}
		
		if (linkedlist != null)
		{

			int n =0;

			Holder b = null;
//			if (nSizeOfLl.height != 0 )
			{
     			for (Object b1:linkedlist )
				{
     				nTemp = ((ArrayList<Holder>) b1).get(n); 
					if (nTemp.toString() == szName)//found the element
					{
						if (author == BitBucketConnector.eVal.Author)
						{
							nTemp.nAuthors = nTemp.nAuthors + 1;
						}
						else
						{
							nTemp.nReviews = nTemp.nReviews + 1;
						}
						linkedlist.remove(n);
						linkedlist.add(nTemp);
					}
				}
    			//System.out.println("List Size " + Integer.toString(n));
			}//if nSizeOfLl !=0
			if(nSizeOfLl == n || nSizeOfLl == 0 ) //no element has been found
			{
				nTemp.string = szName;
				//System.out.println(nTemp.string);
				nTemp.nAuthors = 0;
				nTemp.nReviews = 0;
				if (author == BitBucketConnector.eVal.Author)
				{
					nTemp.nAuthors = nTemp.nAuthors + 1;
					//System.out.println(Integer.toString(nTemp.nAuthors));
				}
				else
				{
					nTemp.nReviews = nTemp.nReviews + 1;
					//System.out.println(Integer.toString(nTemp.nReviews));
				}
				linkedlist.addFirst(nTemp);
			}

		}//if linkedlist !=null

	}
	


}
*/
package com.bbi.BitBucketConnector;

import java.util.*;  
class Book {  

String name;
int nAuthored;
int nReviewer;

public Book(String name, int nAuthor, int nReviewer) {  
    this.name = name;  
    this.nAuthored = nAuthor;  
    this.nReviewer = nReviewer;  
}  
}  
public class ListClass {  
    static List<Book> list;

ListClass()
{
    list=new ArrayList<Book>(); 
}

public static void AddOrUpdate(String szName,BitBucketConnector.eVal author,String szStat)
{  
    //Creating list of Books  
 

    //Adding Books to list  
//    list.add(b1);  
//    list.add(b2);  
//    list.add(b3);  
    //Traversing list  
    boolean bNameFound = false;
    int n = 0;
    for(Book b:list)
    {  
    //System.out.println(b.name+" "+b.nAuthored+" "+b.nReviewer); 
    
		if (szName.equals(b.name))//found the element
		{
			bNameFound = true;
			if (author == BitBucketConnector.eVal.Author)
			{
				b.nAuthored = b.nAuthored + 1;
			}
			else
			{
				if(szStat.equals("UNAPPROVED")==true)
				b.nReviewer = b.nReviewer + 1;
			}
			n++;
		}
    }
	if( bNameFound == false ) //no element has been found
	{
	    Book nTemp = new Book(szName,0,0);
	    //		nTemp.name = szName;
		//System.out.println(nTemp.name);
		if (author == BitBucketConnector.eVal.Author)
		{
			nTemp.nAuthored++;
			//System.out.println(Integer.toString(nTemp.nAuthored));
		}
		else
		{
			if(szStat.equals("UNAPPROVED")==true)
			nTemp.nReviewer++;
			//System.out.println(Integer.toString(nTemp.nReviewer));
		}
		list.add(nTemp);
	}

}  
public static Book Get(int nIndex)
{
	Book nTemp = null;
	nTemp = list.get(nIndex);
	return nTemp;
}

public static int GetSize()
{
	int nTemp = 0;
	nTemp = list.size();
	return nTemp;
}
}  
