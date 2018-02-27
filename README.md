Cynomys
===============

Cynomys is a high performance data synchronization tool, which recovery sql statement from MySQL binlog, it can make data synchronization from MySQL to any database which support sql statement.

### note

For MySQL 5.6.6 users, binlog_checksum system variable is NOT supported by Cynomys at the moment, and set binlog_format='MIXED' please set it to NONE.FOR MariaDB ColumnStore, constraint is NOT supported, so Cynomys may throw exceptions if any constraint exsist in CREATE TABLE statement.

### releases

1.0.0

    release date: 2017-12-18
    
### usage
```
final Cynomys cy = new Cynomys();
cy.setUser("root");
cy.setPassword("123456");
cy.setHost("localhost");
cy.setPort(3306);
cy.setServerId(6789);
cy.setBinlogPosition(4);
cy.setBinlogFileName("mysql_bin.000001");
cy.setBinlogEventListener(new BinlogEventListener() {
    public void onEvents(BinlogEventV4 event) {
        // your code goes here
    }
});
cy.start();

System.out.println("press 'q' to stop");
final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
for(String line = br.readLine(); line != null; line = br.readLine()) {
    if(line.equals("q")) {
        or.stop();
        break;
    }
}
```
