package app.adie.reservation.entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SqliteHelper extends SQLiteOpenHelper {
	private static final String nama_database		="db_kraka";
	private static final int versi_database			=1;
	private static final String query_buat_tabel_user	= "CREATE TABLE IF NOT EXISTS user"
													+ "(id INTEGER PRIMARY KEY AUTOINCREMENT,"
													+ "nama TEXT)";
	private static final String query_hapus_tabel	= "DROP TABLE IF EXISTS user";
	
	public static final int POSISIID = 0;
	private Context crudContext;
	private SQLiteDatabase crudDatabase;
	private SqliteHelper crudHelper;
	public SqliteHelper(Context context){
		super(context, nama_database, null, versi_database);
		crudContext = context;
	}




	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase){

		sqLiteDatabase.execSQL(query_buat_tabel_user);


	}
	
	@Override
	public void onUpgrade(SQLiteDatabase database, int versi_lama, int versi_baru){

		onCreate(database);
	}

	public void BuatTabel()  {
		SQLiteDatabase database = this.getWritableDatabase();
		database.execSQL(query_buat_tabel_user);;
	}

	public void bukaKoneksi() throws SQLException {
		crudHelper = new SqliteHelper(crudContext);
		crudDatabase = crudHelper.getWritableDatabase();
	}
	public void HapusTabel()  {
		SQLiteDatabase database = this.getWritableDatabase();
		database.execSQL(query_hapus_tabel);;
	}
 

	
	public void tambahdatauser(String nama){
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values	= new ContentValues();
		values.put("nama", nama);
		database.insert("user", null, values);
		database.close();
	}
	

	public void hapus_data(String id){
		SQLiteDatabase database = this.getWritableDatabase();
		database.execSQL("DELETE FROM user WHERE id='" + id + "'");
		database.close();
	}

	public ArrayList<Penumpang> getAllPenumpangs() {
		ArrayList<Penumpang> PenumpangList = new ArrayList<Penumpang>();
		// Select All Query
		String selectQuery = "SELECT  * FROM user";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Penumpang Penumpang = new Penumpang();
				Penumpang.setId(cursor.getInt(0));
				Penumpang.setName(cursor.getString(1).split(" ")[0]);
				PenumpangList.add(Penumpang);
			} while (cursor.moveToNext());
		}


		return PenumpangList;
	}
	
	
}
