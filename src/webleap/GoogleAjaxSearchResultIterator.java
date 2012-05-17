package webleap;

public class GoogleAjaxSearchResultIterator 
implements webleap.Iterator
{
    public int index;

    public GoogleAjaxSearchDriver driver;

    public GoogleAjaxSearchResultIterator(GoogleAjaxSearchDriver driver){
        this.driver=driver;
        this.index=0;
    	
    }
    
	public Object next() {
		// TODO 自動生成されたメソッド・スタブ
        SearchResultElement r=driver.getResultElementAt(index);
        index++;
        return r;
	}

	public boolean hasNext() {
		// TODO 自動生成されたメソッド・スタブ
        // This method is derived from interface webleap.Iterator
        // to do: code goes here
        if(driver.getResultElementAt(index)==null) return false;
        return true;
	}

}
