package no.ovitas.compass2.config.settings;


/**
 * @author gyalai
 * 
 */
public class FullTextSearchPlugin implements Plugin {

	// Attributes
	private FullTextSearch fullTextSearch;
	private ContentHandlerPlugin contentHandlerPlugin;
	private Indexer indexer;
	private QBuilder queryBuilder;


	// Methods
	
	public String dumpOut(String indent) {
		String ind = indent + " ";
		String toDumpOut = ind + "FullTextSearch:\n";
		toDumpOut += ind + fullTextSearch.dumpOut(ind) + "\n";
		toDumpOut += ind + contentHandlerPlugin.dumpOut(ind) + "\n";
		toDumpOut += ind + indexer.dumpOut(ind) + "\n";
		toDumpOut += ind + queryBuilder.dumpOut(ind) + "\n";
		
		return toDumpOut;
	}


	@Override
	public PluginType getPluginType() {
		return PluginType.FULL_TEXT_SEARCH_MAIN_PLUGIN;
	}


	/**
	 * This is a getter method for fullTextSearch.
	 * @return the fullTextSearch
	 */
	public FullTextSearch getFullTextSearch() {
		return fullTextSearch;
	}


	/**
	 * This is a setter method for fullTextSearch.
	 * @param fullTextSearch the fullTextSearch to set
	 */
	public void setFullTextSearch(FullTextSearch fullTextSearch) {
		this.fullTextSearch = fullTextSearch;
	}


	/**
	 * This is a getter method for contentHandlerPlugin.
	 * @return the contentHandlerPlugin
	 */
	public ContentHandlerPlugin getContentHandlerPlugin() {
		return contentHandlerPlugin;
	}


	/**
	 * This is a setter method for contentHandlerPlugin.
	 * @param contentHandlerplugin the contentHandlerPlugin to set
	 */
	public void setContentHandlerPlugin(ContentHandlerPlugin contentHandler) {
		this.contentHandlerPlugin = contentHandler;
	}


	/**
	 * This is a getter method for indexer.
	 * @return the indexer
	 */
	public Indexer getIndexer() {
		return indexer;
	}


	/**
	 * This is a setter method for indexer.
	 * @param indexer the indexer to set
	 */
	public void setIndexer(Indexer indexer) {
		this.indexer = indexer;
	}


	/**
	 * This is a getter method for queryBuilder.
	 * @return the queryBuilder
	 */
	public QBuilder getQueryBuilder() {
		return queryBuilder;
	}


	/**
	 * This is a setter method for queryBuilder.
	 * @param queryBuilder the queryBuilder to set
	 */
	public void setQueryBuilder(QBuilder queryBuilder) {
		this.queryBuilder = queryBuilder;
	}
	
	
}
