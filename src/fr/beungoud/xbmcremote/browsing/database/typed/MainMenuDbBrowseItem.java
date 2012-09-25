package fr.beungoud.xbmcremote.browsing.database.typed;

public class MainMenuDbBrowseItem extends AbstractDbBrowseItem {
	String menuName = null;
	
	public MainMenuDbBrowseItem(String menuName)
	{
		this.menuName = menuName;
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((menuName == null) ? 0 : menuName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MainMenuDbBrowseItem other = (MainMenuDbBrowseItem) obj;
		if (menuName == null) {
			if (other.menuName != null)
				return false;
		} else if (!menuName.equals(other.menuName))
			return false;
		return true;
	}
}
