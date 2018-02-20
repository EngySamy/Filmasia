# SQLite 
Here, we practise using Sqlite database to save the search history for the films. <br />
To deal with that, we need first to create a class for contract specifing the tables with their columns in it, then create a helper class 
that extends from SQLiteOpenHelper class so to help creating the actual database or upgrading it when release a new version. <br />
<br />
Here also we practise using RecyclerView as our list of films, that to use it we need to create adapter class and implement its methods.<br />
We used ItemTouchHelper to help us delete an item from the list as needed.
