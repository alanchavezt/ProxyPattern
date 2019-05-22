import java.util.Scanner;

public class SecureDB implements IDatabase
{
    IDatabase db,secureDB;
    boolean isAuthenticated = false;
    
    public SecureDB(IDatabase db, IDatabase secureDB)
    {
        this.db = db;
        this.secureDB = secureDB;
    }

    @Override
    public String getID()
    {
        if(isAuthenticated)
        {
            return db.getID();
        }
        else
        {
            throw new RuntimeException("Authentication failed");
        }
    }

    @Override
    public boolean exists(String key) 
    {
        if(isAuthenticated || authenticate())
        {
            return db.exists(key);
        }
        else
        {
            throw new RuntimeException("Authentication failed");
        }
    }

    @Override
    public String get(String key)
    {
        if(isAuthenticated || authenticate())
        {
            return db.get(key);
        }
        else
        {
            throw new RuntimeException("Authentication failed");
        }

    }

    public boolean authenticate()
    {
        Scanner userScanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String userName = userScanner.nextLine();
        System.out.print("Enter password: ");
        String password = userScanner.nextLine();
        
        if(secureDB.exists(userName) && secureDB.get(userName).equals(password))
        {
            isAuthenticated = true;
            
            return true;
        }
        
        return false;
    }
}
