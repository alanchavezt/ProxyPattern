import java.io.IOException;

public class ProxyTestDriver
{
	public static void main(String[] args) throws IOException
	{

        Database db = new Database("db.dat");
        test(db);

        Database userdb=new Database("userdb.dat");
        SecureDB sdb= new SecureDB(db, userdb);
        test(sdb);

        CacheDB cdb = new CacheDB(sdb);
        test(cdb);

        System.out.println("Cache contents: " + cdb.inspect());

        try
        {
            Database db2 = new Database("noname.dat");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    public static void test(IDatabase db)
    {
        try
        {
            System.out.println(db.get("one"));
            System.out.println(db.get("two"));
            System.out.println(db.exists("two"));
            System.out.println(db.get("three"));
            System.out.println(db.get("four"));
            System.out.println(db.get("four"));
            System.out.println(db.get("five"));
            System.out.println(db.get("six"));
            System.out.println(db.get("one"));
            System.out.println(db.get("seven"));
        }
        catch (Exception e) 
        {
            System.out.println(e.getMessage());
        }
    }
}
