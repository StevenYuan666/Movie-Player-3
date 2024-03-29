
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a movie library, with individual movie titles and watch lists.
 */
public class Library {
	
	private Set<Movie> aMovies = new HashSet<>();
	private Set<WatchList> aWatchLists = new HashSet<>();
	private Set<Episode> aEpisodes = new HashSet<>();
	private Set<TVShow> aTVShows = new HashSet<>();
	
	//Add two fields for Library
	private String name;
	private Optional<String> emailID;
	
	private static Library aLibrary = new Library();
	
	//A private constructor to help implement singleton pattern
	private Library() {
		//Set the name as Default initially, and the users are able to change the name later
		this.name = "Default";
		this.emailID = Optional.empty();
	}
	
	/**
	 * Adds a movie to the library. Duplicate movies aren't added twice.
	 * 
	 * @param pMovie
	 *            the movie to add
	 * @pre pMovie!=null
	 */
	public void addMovie(Movie pMovie) {
		assert pMovie != null;
		aMovies.add(pMovie);
	}
	
	/**
	 * Adds a watchlist to the library. All movies from the list are also added as individual movies to the library.
	 * 
	 * @param pList
	 *            the watchlist to add
	 * @pre pList!=null;
	 */
	public void addWatchList(WatchList pList) {
		assert pList != null;
		aWatchLists.add(pList);
		for (Watchable movie : pList) {
			addMovie((Movie) movie);
		}
	}
	
	/**
	 * Adds a TVShow to the library. All Episodes from the list are also added as individual episodes to the library.
	 *
	 * @param pTVShow
	 *            the TVShow to add
	 * @pre pTVShow!=null;
	 */
	public void addTVShow(TVShow pTVShow) {
		assert pTVShow != null;
		aTVShows.add(pTVShow);
		for (Episode episode : pTVShow) {
			aEpisodes.add(episode);
		}
	}
	
	/**
	 * Method to generate a new watchlist based on some filtering mechanism
	 * 
	 * @param pName
	 *            the name of the watchlist to create
	 * @param pGenerationParameters
	 *            the generation parameters
	 * @pre pName!=null && pFilter!=null;
	 */
	public WatchList generateWatchList(String pName, WatchListGenerationInfo pGenerationParameters) {
		assert (pName != null) && (pGenerationParameters != null);
		List<Watchable> items = new ArrayList<>();
		for (TVShow show : aTVShows) {
			if (pGenerationParameters.filter(show)) {
				for (Episode episode : show) {
					if (pGenerationParameters.filter(episode)) {
						items.add(episode);
					}
				}
			}
		}
		for (Movie movie : aMovies) {
			if (pGenerationParameters.filter(movie)) {
				items.add(movie);
			}
		}
		Collections.sort(items, pGenerationParameters);
		WatchList watchlist = new WatchList(pName);
		for (Watchable item : items) {
			watchlist.addWatchable(item);
		}
		return watchlist;
	}
	
	//Using a static method to implementing singleton pattern, Factory method
	public static Library getLibrary() {
		return Library.aLibrary;
	}
	
	//Getters and Setters for name and email ID
	public String getName() {
		return this.name;
	}
	public void setName(String newName) {
		this.name = newName;
	}
	public String getEmailID() {
		if(this.emailID.isPresent()) {
			return this.emailID.get();
		}
		else {
			//Return an empty String if there is no Email ID
			return "";
		}
	}
	//A helper method to check if an email address is valid or not
	private static boolean check(String address) {
		String regex = "^(.+)@(.+)$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(address);
		return matcher.matches();
	}
	
	public void setEmailID(String newEmailID) {
		if(Library.check(newEmailID)) {
			this.emailID = Optional.of(newEmailID);
		}
		else {
			throw new AssertionError("Your input Email Address is not valid");
		}
	}
}
