package webleap;
import java.util.Hashtable;
public interface KwicTable
{
    void setAggregate(Aggregate x);

    void setIterator(Iterator x);

    void setDataExtractor(DataExtractor de);

    void setCache(Hashtable cache);

    void showTable(String keyword);

}
