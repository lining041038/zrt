package antelope.beans;

import java.util.ArrayList;
import java.util.List;



public class SearchResult <A> {
	public List<A> resultData = new ArrayList<A>();
	public List<SearchColInfo> colinfos = new ArrayList<SearchColInfo>();
}
