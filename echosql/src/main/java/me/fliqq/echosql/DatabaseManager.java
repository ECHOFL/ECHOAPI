package me.fliqq.echosql;


public enum DatabaseManager {

    //1

    ECHO_PRISON(new DbCredentials(EchoSql.getInstance().getConfig().getString("table1.host"), EchoSql.getInstance().getConfig().getString("table1.user"), EchoSql.getInstance().getConfig().getString("table1.pass"), EchoSql.getInstance().getConfig().getString("table1.dbName"), EchoSql.getInstance().getConfig().getInt("table1.port")));
    //ECHO_PRISON(new DbCredentials("localhost", "EchoPrison","4@d@-/.bGCeou9o(","EchoPrison",3306));

    private final DatabaseAccess databaseAccess;

    DatabaseManager(DbCredentials credentials) {
        this.databaseAccess = new DatabaseAccess(credentials);
    }

    public DatabaseAccess getDatabaseAccess() {
        return databaseAccess;
    }

    public static void initAllDatabaseConnections() {
        for (DatabaseManager databaseManager : values())
            databaseManager.databaseAccess.initPool();
    }

    public static void closeAllDatabaseConnections() {
        for (DatabaseManager databaseManager : values())
            databaseManager.databaseAccess.closePool();

    }
}
