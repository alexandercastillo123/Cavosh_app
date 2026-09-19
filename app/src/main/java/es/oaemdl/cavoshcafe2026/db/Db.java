package es.oaemdl.cavoshcafe2026.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Db extends SQLiteOpenHelper {
    SQLiteDatabase db;
    String sql;

    public Db(@Nullable Context context) {
        super(context, "cavoshcafe.db", null, 1);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Usuario(id integer, Nombres text, Correo text, Passwordd text, Login integer)");
        db.execSQL("create table Producto(id integer, Detalle text, Descripcion text, Precio real, Categoria integer, Nuevo integer)");
        db.execSQL("create table Local(id integer, RazonSocial text, Direccion text, idDistrito integer, Horario text, Latitud text, Longitud text)");
        db.execSQL("create table Distrito(id integer, Detalle text)");

        db.execSQL("create table Pedido(id integer, idUsuario integer, idLocal integer, FechaVenta text, Total real, Items integer )");
        db.execSQL("create table PedidoDetalle(idPedido integer, idProducto integer, Cantidad integer, Size integer, Milk integer, Cream integer, Caffeine integer, Total real )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) { }

    public void Sentencia(String sql) {
        this.sql = sql;
    }

    public Cursor getCursor() {
        return db.rawQuery( sql , null);
    }

    public int Update(String sTabla, ContentValues values, String sCondicion) {
        return db.update( sTabla, values, sCondicion, null );
    }

    public int Insert(String sTabla, ContentValues values) {
        return (int) db.insert(sTabla, null, values);
    }

}
