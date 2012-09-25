package fr.beungoud.xbmcremote.browsing.database;


/**
 * Classe de description d'un query
 * @author Benoit
 *
 */
public class QueryDesc {
	
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public int getReturnCount() {
		return returnCount;
	}

	public void setReturnCount(int returnCount) {
		this.returnCount = returnCount;
	}

	public int getUsefullItem() {
		return usefullItem;
	}

	public void setUsefullItem(int usefullItem) {
		this.usefullItem = usefullItem;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}


	String query;
	int returnCount;
	int usefullItem = 0;
	String title;
	String subTitle;
	String subPath;
	String addFromDbMedia;
	public String getAddFromDbMedia() {
		return addFromDbMedia;
	}

	public String getAddFromDbQuery() {
		return addFromDbQuery;
	}


	String addFromDbQuery;
	
	public void setAddFromDb(String media, String query)
	{
		addFromDbMedia = media;
		addFromDbQuery = query;
	}

	public String getSubPath() {
		return subPath;
	}

	public void setSubPath(String subPath) {
		this.subPath = subPath;
	}

	public QueryDesc(String query, int returnCount) {
		this.query = query;
		this.returnCount = returnCount;
	}

	public QueryDesc(String query, int returnCount, String title, String subTitle) {
		this.query = query;
		this.returnCount = returnCount;
		this.title = title;
		this.subTitle = subTitle;
		this.subPath = title;
	}
	
	}