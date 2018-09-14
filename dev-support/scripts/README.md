# 启动数据库
```bash
docker-compose up -d
```

# 创建数据库
```bash
docker exec -it mssqldev /opt/mssql-tools/bin/sqlcmd \
   -S localhost -U SA -P 'yourStrong(!)Password' \
   -Q 'CREATE DATABASE admin COLLATE Chinese_PRC_CI_AS'
```

# 查询数据库
```bash
docker exec -it mssqldev /opt/mssql-tools/bin/sqlcmd \
   -S localhost -U SA -P 'yourStrong(!)Password' \
   -Q 'SELECT Name from sys.Databases'
```
# 删除数据库
```bash
docker exec -it mssqldev /opt/mssql-tools/bin/sqlcmd \
   -S localhost -U SA -P 'yourStrong(!)Password' \
   -Q 'DROP DATABASE admin'
```