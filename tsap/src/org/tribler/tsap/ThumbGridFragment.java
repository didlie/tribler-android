package org.tribler.tsap;

import java.util.ArrayList;

import org.tribler.tsap.thumbgrid.ThumbAdapter;
import org.tribler.tsap.thumbgrid.ThumbItem;

import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView.OnQueryTextListener;

public class ThumbGridFragment extends Fragment implements OnQueryTextListener {

	//stores the menu handler to remove the search item in onPause()
	private Menu menu;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
    	
    	ArrayList<ThumbItem> gridArray = new ArrayList<ThumbItem>();
    	
    	View v = inflater.inflate(R.layout.fragment_thumb_grid, container, false);
    		
    	GridView gridView = (GridView) v.findViewById(R.id.ThumbsGrid);
        
    	for(int i = 0; i < 3; i++)
    	{
    		gridArray.add(new ThumbItem("Sintel", R.drawable.sintel, ThumbItem.TORRENT_HEALTH.GREEN, 500));
    		gridArray.add(new ThumbItem("Dracula", R.drawable.dracula, ThumbItem.TORRENT_HEALTH.YELLOW, 4321));
    		gridArray.add(new ThumbItem("King Kong", R.drawable.kingkong, ThumbItem.TORRENT_HEALTH.UNKNOWN, 12353));
    		gridArray.add(new ThumbItem("Tears of Steal", R.drawable.tos, ThumbItem.TORRENT_HEALTH.RED, 423));
    		gridArray.add(new ThumbItem("To The Last Man", R.drawable.lastman, ThumbItem.TORRENT_HEALTH.UNKNOWN, 12353));
    		gridArray.add(new ThumbItem("Attack of the 50 ft woman", R.drawable.fiftyft, ThumbItem.TORRENT_HEALTH.UNKNOWN, 12353));
    		gridArray.add(new ThumbItem("Draculas Daughter", R.drawable.dracula_daughter, ThumbItem.TORRENT_HEALTH.RED, 423));
    		gridArray.add(new ThumbItem("Lusty Men", R.drawable.lustymen, ThumbItem.TORRENT_HEALTH.RED, 423));
    		gridArray.add(new ThumbItem("Mantis", R.drawable.mantis, ThumbItem.TORRENT_HEALTH.RED, 423));
    		gridArray.add(new ThumbItem("Son of Frankenstein", R.drawable.sof, ThumbItem.TORRENT_HEALTH.RED, 423));
    	}
    	
		ThumbAdapter customThumbs = new ThumbAdapter(container.getContext(), R.layout.thumb_grid_item, gridArray);
		gridView.setAdapter(customThumbs);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id){
				VideoInfoFragment vidFrag = new VideoInfoFragment();
				Bundle args = new Bundle();
				args.putInt("torrentID", (int) id);
				vidFrag.setArguments(args);
				
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(R.id.container,vidFrag);
				transaction.addToBackStack(null);
				transaction.commit();
			}
		});
       
        return v;
    }
    
    /**
     * Removes the search menu item so that the app doesn't crash when selecting the channel list fragment from the navigation drawer.
     */
    @Override
    public void onPause()
    {
    	menu.removeItem(R.id.action_search_thumbgrid);
    	super.onPause();
    }
        
    /**
	 * Adds thumb grid fragment specific options to the options menu and stores the menu.
	 * In this case, the search action is added and enabled.
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		this.menu = menu;
		inflater.inflate(R.menu.thumbgrid_fragment, menu);
		MenuItem searchMenuItem = menu.findItem(R.id.action_search_thumbgrid);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
		searchView.setOnQueryTextListener(this);
		searchView.setQueryHint("Search videos");	
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_search_thumbgrid:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
    
	public boolean onQueryTextChange(String query)
	{
        // Called when the action bar search text has changed.  Update
        // the search filter, and restart the loader to do a new query
        // with this filter.
		GridView gridView = (GridView) this.getView().findViewById(R.id.ThumbsGrid);
		((ThumbAdapter) gridView.getAdapter()).getFilter().filter(query);
        return true;
    }
	
	@Override
	public boolean onQueryTextSubmit(String query)
	{
		GridView gridView = (GridView) this.getView().findViewById(R.id.ThumbsGrid);
		((ThumbAdapter) gridView.getAdapter()).getFilter().filter(query);
		Toast.makeText(getActivity(), query, Toast.LENGTH_SHORT).show();
        // Don't care about this.
        return true;
    }
    
}