public interface IDatabase
{
    public String getID();
    public boolean exists(String key);
    public String get(String key);
}

