import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/* This file contains the java code that reads and filters the data available over the API
 * */


/*The following functions can be broken up into three main categories:
 *1 - QUERY FUNCTIONS - 
 *'query()' is used to search the website directory, and pulls data from online to return a List<String>, with each
 *element containing an entry from the site that fits the specifications.  
 *
 * 2 - FILTERING FUNCTIONS -
 * The filtering functions take the output of 'query()' and allows the user to further narrow down the search space
 * 
 * 3 - HELPER/MISC FUNCTIONS -
 * The rest of the functions are those with limited functionality, but may be useful in a small
 * set of cases, or may be useful for further development in the future
 * */

public class API {

	private static String base_url = "https://my.api.mockaroo.com/test_api.json?key=e6ac1da0";
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		List<String> countries = new ArrayList<String>();
		countries.add("China");
		query(countries);
	}
	
	/* QUERY FUNCTIONS
	 * 
	 * The 'query()' function is overloaded so that the user can choose which 
	 *factors to search the website.  One limitation of this is that if the user specifies a given feature, they must also specify all other features
	 *that come before it.  For example, in order to specify 'make', the user must also specify country, and model. 'id' is ignored
	 *since it is most likely not a useful feature for making stakeholder decisions. If necessary, the subsequent functions can be used to 
	 *specify 'id' if further processing is necessary*/
	
	/*search by all features*/
	public static List<String[]> query(List<String> countries, List<String> models, List<String> makes, String soldBy, int maxSalePrice) throws IOException{
		List<String[]> ret = new ArrayList<String[]>();
		
		/*construct url*/
		String query_url = base_url;
		if(countries.size() > 0) query_url += "&countries=";
		for(int i=0; i<countries.size(); i++){
			query_url += countries.get(i);
			if(i < countries.size()-1) query_url += ',';
		}
		/*access website through URL and scanner*/
		URL url = new URL(query_url);
		Scanner s = new Scanner(url.openStream());
		while(s.hasNext()){
			String[] entry = s.nextLine().split(",");
			if((countries==null || countries.contains(entry[1])) && 
					(models==null || models.contains(entry[2])) && 
					(makes==null || makes.contains(entry[3])) && 
					(soldBy==null || soldBy == entry[4]) && 
					(maxSalePrice == -1 || maxSalePrice >= Integer.parseInt(entry[5]))){
				ret.add(entry); //add entry
			}
			System.out.println(s.nextLine());
		}
		System.out.println("done");
		return ret;
	}
	/*search by country, model, make, price*/
	public static List<String[]> query(List<String> countries, List<String> models, List<String> makes, String soldBy) throws IOException{
		return query(countries, models, makes, soldBy, -1);
	}
	/*search by country, model, make*/
	public static List<String[]> query(List<String> countries, List<String> models, List<String> makes) throws IOException{
		return query(countries, models, makes, null, -1);
	}
	/*search by country, model*/
	public static List<String[]> query(List<String> countries, List<String> models) throws IOException{
		return query(countries, models, null, null, -1);
	}
	/*search by country*/
	public static List<String[]> query(List<String> countries) throws IOException{
		return query(countries, null, null, null, -1);
	}
	/*search with no constraints - aka pull everything from site*/
	public static List<String[]> query() throws IOException{
		return query(null, null, null, null, -1);
	}
	
	
	/* FILTERING FUNCTIONS
	 * The following functions takes a List<String[]>, presumably returned from 'query()', and removes elements that do not meet
	 * the specified constraint.  
	 * They all are of return type 'void', and modify the original list based on whatever filter that function applies
	 * */
	
	/*filters out all entries not from specified country*/
	public static void fromCountry(List<String[]> query, String country){
		for(Iterator<String[]> itr = query.iterator(); itr.hasNext();){
			String[] entry = itr.next();
			if(!entry[1].equals(country)) itr.remove();
		}
	}
	/*filters out all entries not of specified model*/
	public static void isModel(List<String[]> query, String model){
		for(Iterator<String[]> itr = query.iterator(); itr.hasNext();){
			String[] entry = itr.next();
			if(!entry[2].equals(model)) itr.remove();
		}
	}
	/*filters out all entries not of specified make*/
	public static void isMake(List<String[]> query, String make){
		for(Iterator<String[]> itr = query.iterator(); itr.hasNext();){
			String[] entry = itr.next();
			if(!entry[3].equals(make)) itr.remove();
		}
	}
	/*filters out all entries not from specified seller*/
	public static void bySeller(List<String[]> query, String seller){
		for(Iterator<String[]> itr = query.iterator(); itr.hasNext();){
			String[] entry = itr.next();
			if(!entry[4].equals(seller)) itr.remove();
		}
	}
	/*filters out all entries whose price is above maxPrice*/
	public static void maxPrice(List<String[]> query, int maxPrice){
		for(Iterator<String[]> itr = query.iterator(); itr.hasNext();){
			String[] entry = itr.next();
			if(!entry[5].equals("") && Integer.parseInt(entry[5]) > maxPrice) itr.remove();
		}
	}
	/*filters out all entries whose price is below minPrice*/
	public static void minPrice(List<String[]> query, int minPrice){
		for(Iterator<String[]> itr = query.iterator(); itr.hasNext();){
			String[] entry = itr.next();
			if(!entry[5].equals("") && Integer.parseInt(entry[5]) < minPrice) itr.remove();
		}
	}
	
	
	/*HELPER FUNCTIONS/MISC
	 * The following functions are most likely will not be used much, but
	 * may be of use for future development or ideas
	 * 
	 * */
	/*This function pulls from the website a String[] corresponding to the entry with specified int ID
	 * If not found, it returns an empty array*/
	public static String[] getID(int ID) throws IOException{
		String[] ret = new String[6];
		String query_url = base_url;
		URL url = new URL(query_url);
		Scanner s = new Scanner(url.openStream());
		
		/*step through ID-1 times to reach corresponding entry*/
		for(int i=0; i<ID && s.hasNext(); i++) s.next();
		if(s.hasNext()) ret = s.nextLine().split(",");
		
		System.out.println("done");
		return ret;	
	}


	
}

