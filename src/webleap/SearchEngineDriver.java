package webleap;
import java.util.Hashtable;

public interface SearchEngineDriver
{
    void setProxy(String name, int port, String pkind, String password);

    void setMessageReceiver2(MessageReceiver receiver);

    SearchEngineDriver copyThis();


    void setKwicTable(KwicTable table);

    void setSettings(Settings settings);

    void setMessageReceiver(MessageReceiver receiver);

    void setQueryOptions(String options);

    KwicTable getKWICtable();

    void setCache(Hashtable cache);

    void searchTheEngine(String keyword);

    int getFrequency();

}