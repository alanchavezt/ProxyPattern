import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Database implements IDatabase
{
    String id;
    RandomAccessFile fileReader;

    Database(String id)
    {
        try 
        {
            this.id = id;
            this.fileReader = new RandomAccessFile(this.id, "r");
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(String.format("%s does not exists", id));
        }
    }

    @Override
    public String getID()
    {
        return this.id;
    }

    @Override
    public boolean exists(String key)
    {
        try
        {
            this.fileReader.seek(0);
            String line;
            
            while ((line = this.fileReader.readLine()) != null)
            {
                String[] keyVal = line.split(" ", 2);
                
                if (keyVal.length == 2 && keyVal[0].equals(key))
                {
                    return true;
                }
            }
        }
        catch (IOException e)
        {
        	throw new RuntimeException(e.getMessage());
        }
        
        return false;
    }

    @Override
    public String get(String key)
    {
        try
        {
            this.fileReader.seek(0);
            String line;
            
            while ((line = this.fileReader.readLine()) != null)
            {
                String[] keyVal = line.split(" ", 2);
                
                if (keyVal.length == 2 && keyVal[0].equals(key))
                {
                    return keyVal[1];
                }
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e.getMessage());
        }
        
        throw new RuntimeException(String.format("No such record : %s\n", key));
    }

    @Override
    protected void finalize() throws Throwable
    {
        if (this.fileReader != null)
        {
            this.fileReader.close();
        }
    }
}
