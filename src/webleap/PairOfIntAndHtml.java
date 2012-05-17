package webleap;

public class PairOfIntAndHtml 
{
    public String url;

    public String html;
    public int frequency;
    public PairOfIntAndHtml(int fq, String html,String url)
    {
        frequency=fq;
        this.html=html;
        this.url=url;
    }
}