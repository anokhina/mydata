
## Установка Mongodb локально

Можно запустить и настроить mongodb локально.

Архивы для разных операционных систем находятся
<https://www.mongodb.com/download-center/community/releases/archive>

## Установка MongoDB в Linux

Скачать архив <https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-ubuntu2004-4.4.14.tgz>

Распаковать его в директорию, где будет база, например,

```
/home/ivanov/mongodb 
```

Переименовать получившуюся директорию `mongodb-linux-x86_64-ubuntu2004-4.4.14` в более короткую, например `mongodb_4_4_14`.
Зайти в `mongodb_4_4_14\bin` и создать там 
запуска `startDb.sh` следующего содержания

```
#!/bin/bash

CREATE_REPLICASET='no'

if [ -d ./db ] ; then
    echo "ReplicaSet already exists"
else
    echo "Create DataDir"
    mkdir ./db
    CREATE_REPLICASET='yes'
fi

./mongod --dbpath ./db --directoryperdb --replSet myReplicaSet --bind_ip localhost --maxConns 32 --fork --logpath ./mongo.log

if [ "$CREATE_REPLICASET" == "yes" ] ; then
    echo "Do create ReplicaSet"
    ./mongo --eval "rs.initiate({ _id: 'myReplicaSet', version: 1, members: [ {_id: 0, host: 'localhost'}  ] })"
fi
```

Запустить его. База создана, реплика инициализирована. Можно подключаться в MongoDb Compass к localhost:27017.


### Установка MongoDB в Windows

Скачать архив <https://fastdl.mongodb.org/windows/mongodb-windows-x86_64-4.4.14.zip>

Распаковать его в директорию, где будет база, например,

```
C:\Users\ivanov\mongodb 
```

Переименовать получившуюся директорию `mongodb-win32-x86_64-windows-4.4.14` в более короткую, например `mongodb_4_4_14`.
Зайти в `mongodb_4_4_14\bin` и создать там директорию `db` и
файл запуска `startDb.bat` следующего содержания


```
c:
cd C:\Users\ivanov\mongodb\mongodb_4_4_14\bin 

mongod --dbpath db --directoryperdb --replSet myReplicaSet --bind_ip localhost --maxConns 32 --logpath mongo.log

```

Запустить его. В cmd запустится выполнение и будет выведено:

```
C:\Users\ivanov\mongodb\mongodb_4_4_14\bin>c:

C:\Users\ivanov\mongodb\mongodb_4_4_14\bin>cd C:\Users\ivanov\mongodb\mongodb_4_4_14\bin

C:\Users\ivanov\mongodb\mongodb_4_4_14\bin>mongod --dbpath db --directoryperdb --replSet myReplicaSet 
--bind_ip localhost --maxConns 32 --logpath mongo.log
{"t":{"$date":"2023-02-22T09:58:13.739Z"},"s":"I",  "c":"CONTROL",  "id":20697,   
"ctx":"main","msg":"Renamed existing log file",
"attr":{"oldLogPath":"C:\\Users\\ivanov\\mongodb\\mongodb_4_4_14\\bin\\mongo.log",
"newLogPath":"C:\\Users\\ivanov\\mongodb\\mongodb_4_4_14\\bin\\mongo.log.2023-02-22T09-58-13"}}
```

Для работы транзакций инициализируем реплику. Это надо делать только при первом запуске. 
Для этого в новом `cmd` надо выполнить

```
cd C:\Users\ivanov\mongodb\mongodb_4_4_14\bin 

mongo --eval "rs.initiate({ _id: 'myReplicaSet', version: 1, members: [ {_id: 0, host: 'localhost'}  ] })"
```

На экран будет выведено похожее сообщение

```
MongoDB shell version v4.4.14
connecting to: mongodb://127.0.0.1:27017/?compressors=disabled&gssapiServiceName=mongodb
Implicit session: session { "id" : UUID("a2c28195-61e9-42df-88cc-b0da53bc88ed") }
MongoDB server version: 4.4.14
{ "ok" : 1 }

```

