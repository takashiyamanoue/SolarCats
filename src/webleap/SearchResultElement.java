package webleap;

public class SearchResultElement
{
    String title;
    String url;
    String snippet;
    
    public SearchResultElement(String title, String url, String snippet)
    {
        this.title=title;
        this.url=url;
        this.snippet=snippet;
    }
    
    public String getSnippet(){
        return this.snippet;
    }

    public String getUrl(){
        return this.url;
    }
    
    public String getTitle(){ return this.title; }
}