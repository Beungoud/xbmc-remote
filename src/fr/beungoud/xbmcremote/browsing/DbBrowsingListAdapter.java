package fr.beungoud.xbmcremote.browsing;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

/**
 * Adapter for database specific items
 * @author Benoit
 *
 */
public class DbBrowsingListAdapter extends BaseAdapter implements ListAdapter{
	
	List<DbBrowseItem> listItems = new ArrayList<DbBrowseItem>()	;
	
	public void setList(List<DbBrowseItem> list)
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
		return listItems.get(position).getPath().hashCode();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BrowseItemView TV = (BrowseItemView)convertView;
		if (TV== null)
		{
			TV = new BrowseItemView(parent.getContext());
		}
		DbBrowseItem item = listItems.get(position);
		TV.setName(item.getName());
		TV.setMediaType(item.getMediaType());
		TV.setPath(item.getPath());
		TV.setFolder(item.isFolder());
		TV.setSousTitre(item.getComment());
		TV.setSourceItem(item);
		//TV.setText(item.getName());
		return TV;
	}
	
}
