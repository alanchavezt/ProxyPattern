import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheDB implements IDatabase
{
    private IDatabase database;
    private int SIZE = 5;
    private int currentCacheIndex = 0;
    private Map<String, String> cache = new HashMap<String, String>();
    private Map<Integer, String> cacheHitOrder = new HashMap<Integer, String>();

    public CacheDB(IDatabase database)
    {
        this.database = database;
    }

    @Override
    public String getID()
    {
        return this.database.getID();
    }

    @Override
    public boolean exists(String key)
    {
        if (cache.keySet().contains(key))
        {
            System.out.println(String.format("found key \"%s\" in cache", key));
            
            return true;
        }
        else
        {
            return this.database.exists(key);
        }
    }

    @Override
    public String get(String key)
    {
        if (cache.keySet().contains(key))
        {
            for (int tempKey : cacheHitOrder.keySet())
            {
                if (cacheHitOrder.get(tempKey).equals(key))
                {
                    cacheHitOrder.remove(tempKey);
                    currentCacheIndex++;
                    cacheHitOrder.put(currentCacheIndex, key);
                    break;
                }
            }
            
            System.out.println(String.format("found key \"%s\" in cache", key));
            return cache.get(key);
        }
        else
        {
            try
            {
                String value = this.database.get(key);
                currentCacheIndex++;
                
                if (cacheHitOrder.size() == SIZE && cache.size() == SIZE)
                { 
                    int maxMRUCount = Collections.max(cacheHitOrder.keySet());
                    String mrUKey = cacheHitOrder.get(maxMRUCount);
                    cacheHitOrder.remove(maxMRUCount);
                    cache.remove(mrUKey);
                }
                
                cacheHitOrder.put(currentCacheIndex, key);
                cache.put(key, value);
                return value;
            }
            catch (Exception e)
            {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    public String inspect()
    {
        List<Integer> sortedList = new ArrayList<Integer>(this.cacheHitOrder.keySet());
        Collections.sort(sortedList,Collections.reverseOrder());
        String out="";
        
        for (int i:sortedList)
        {
            if (out.equals(""))
            {
                out = String.format("[%s],", this.cacheHitOrder.get(i));
            }
            else
            {
                out = String.format("%s [%s],",out,this.cacheHitOrder.get(i));
            }
        }
        
        return out;
    }
}