package org.tribler.tsap.thumbgrid;

import java.util.ArrayList;
import java.util.Map;

import android.util.Log;

import org.tribler.tsap.Poller;
import org.tribler.tsap.R;
import org.tribler.tsap.XMLRPC.XMLRPCCallTask;
import org.tribler.tsap.XMLRPC.XMLRPCConnection;

/**
 * Class for receiving torrents over XMPRPC using the aXMLRPC library
 * 
 * @author Dirk Schut
 */
public class XMLRPCTorrentManager implements Poller.IPollListener{
	private ThumbAdapter mAdapter;
	XMLRPCConnection mConnection;

	/**
	 * Constructor: Makes a connection with an XMLRPC server and starts a
	 * polling loop
	 * 
	 * @param url
	 *            The url of the XMLRPC server
	 */
	public XMLRPCTorrentManager(XMLRPCConnection connection, ThumbAdapter adapter) {
		mConnection = connection;
		mAdapter = adapter;
	}

	/**
	 * Searches the remote dispersy data for torrents fitting a certain query.
	 * The results can be retrieved by calling getRemoteResults(). The amount of
	 * found results can be retrieved by calling getRemoteResultsCount().
	 * 
	 * @param keywords
	 *            The keywords that Tribler will look for in the names of the
	 *            channels
	 */
	private void searchRemote(final String keywords) {
		Log.v("XMPLRCTorrentManager", "Remote search for \"" + keywords
				+ "\" launched.");
		new XMLRPCCallTask().call("torrents.search_remote", mConnection, keywords);
	}

	/**
	 * It will send an Integer to all observers describing the amount of found
	 * results.
	 */
	private int getRemoteResultsCount() {
		return (Integer) mConnection.call("torrents.get_remote_results_count");
	}
	
	/**
	 * It will send the found torrents to the adapter.
	 */
	private void addRemoteResults() {
		Object[] arrayResult = (Object[]) mConnection.call("torrents.get_remote_results");
		ArrayList<ThumbItem> resultsList = new ArrayList<ThumbItem>();
		
		Log.v("XMPLRCTorrentManager", "Got " + arrayResult.length + " results");
		
		for (int i = 0; i < arrayResult.length; i++) {
			@SuppressWarnings("unchecked")
			ThumbItem item = convertMapToThumbItem(
					(Map<String, Object>) arrayResult[i]);
			resultsList.add(item);
		}
		mAdapter.addNew(resultsList);
	}
	
	private ThumbItem convertMapToThumbItem(Map<String, Object> map)
	{
		return new ThumbItem((String) map.get("name"),
				R.drawable.default_thumb,
				TORRENT_HEALTH.YELLOW,
				1000,
				(String)map.get("infohash"));
	}

	public void search(String keywords) {
		mAdapter.clear();
		searchRemote(keywords);
		Log.i("XMPLRCTorrentManager", "Search for \"" + keywords + "\" launched.");
	}

	@Override
	public void onPoll() {
		int foundResults = getRemoteResultsCount();
		if(foundResults > mAdapter.getCount())
			addRemoteResults();
	}
}