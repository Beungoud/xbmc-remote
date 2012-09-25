package fr.beungoud.xbmcremote.browsing.database.typed;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import fr.beungoud.xbmcremote.browsing.BrowseItemView;
import fr.beungoud.xbmcremote.browsing.database.typed.view.ViewFactory;

/**
 * Adapter for database specific items
 * @author Benoit
 *
 */
public class TypedBrowsingListAdapter extends BaseAdapter implements ListAdapter{
	
	List<AbstractDbBrowseItem> listItems = new ArrayList<AbstractDbBrowseItem>()	;
	
	public void setList(List<AbstractDbBrowseItem> list)
	{
		listItems = list;
	}

	@Override
	public int getCount() {
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		return listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return listItems.hashCode();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View retour = ViewFactory.createView(listItems.get(position), convertView, parent);
		
		return retour;
	}
	
}
