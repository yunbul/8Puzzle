package algorithm;
import java.util.Comparator;

public class MyComparator implements Comparator<AlgoTable>
{
    @Override
    public int compare(AlgoTable x, AlgoTable y)
    {
        if (x.getHeuristic() < y.getHeuristic())
        {
            return -1;
        }
        if (x.getHeuristic() > y.getHeuristic())
        {
            return 1;
        }
        return 0;
    }
}