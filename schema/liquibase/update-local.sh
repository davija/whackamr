#echo Updating to use local database
#ln -sf liquibase.mine.properties liquibase.properties

echo Performing update
./liquibase update
