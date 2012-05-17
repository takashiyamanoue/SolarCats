package webleap;

class GoogleResultIterator implements webleap.Iterator
{
    public int index;

    public GoogleDriver driver;

    public GoogleResultIterator(GoogleDriver driver)
    {
        this.driver=driver;
        this.index=0;
    }

    public boolean hasNext()
    {
        // This method is derived from interface webleap.Iterator
        // to do: code goes here
        if(driver.getResultElementAt(index)==null) return false;
        return true;
    }

    public Object next()
    {
        // This method is derived from interface webleap.Iterator
        // to do: code goes here
        SearchResultElement r=driver.getResultElementAt(index);
        index++;
        return r;
    }

}