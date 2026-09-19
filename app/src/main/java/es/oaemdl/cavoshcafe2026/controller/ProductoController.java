package es.oaemdl.cavoshcafe2026.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import es.oaemdl.cavoshcafe2026.data.api.ApiClient;
import es.oaemdl.cavoshcafe2026.data.model.ApiResponse;
import es.oaemdl.cavoshcafe2026.data.model.Producto;
import es.oaemdl.cavoshcafe2026.db.Db;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class ProductoController {

    public interface ProductosCallback {
        void onProductos(List<Producto> productos);
        void onError(String mensaje);
    }

    public void obtenerNuevos(Context ctx, ProductosCallback cb) {
        Db db = new Db(ctx);

        db.Sentencia("select * from Producto where Nuevo = 1");
        List<Producto> cache = leerCursor(db.getCursor());

        if (!cache.isEmpty()) {
            cb.onProductos(cache);
            return;
        }

        ApiClient.getService().listarNuevos().enqueue(new Callback<ApiResponse<List<Producto>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Producto>>> call,
                                   @NonNull Response<ApiResponse<List<Producto>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Producto> datos = response.body().getData();
                    guardarEnSqlite(db, datos);
                    cb.onProductos(datos);
                } else {
                    cb.onError("Sin respuesta del servidor");
                }
            }
            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<Producto>>> call, @NonNull Throwable t) {
                cb.onError(t.getMessage());
            }
        });
    }
    public void obtenerFrecuentes(Context ctx, ProductosCallback cb) {
        Db db = new Db(ctx);

        db.Sentencia("select * from Producto where Frecuente = 1");
        List<Producto> cache = leerCursor(db.getCursor());

        if (!cache.isEmpty()) {
            cb.onProductos(cache);
            return;
        }

        ApiClient.getService().listarFrecuentes().enqueue(new Callback<ApiResponse<List<Producto>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Producto>>> call,
                                   @NonNull Response<ApiResponse<List<Producto>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Producto> datos = response.body().getData();
                    guardarEnSqlite(db, datos);
                    cb.onProductos(datos);
                } else {
                    cb.onError("Sin respuesta del servidor");
                }
            }
            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<Producto>>> call, @NonNull Throwable t) {
                cb.onError(t.getMessage());
            }
        });
    }

    public void obtenerTodos(Context ctx, String categoria, ProductosCallback cb) {
        Db db = new Db(ctx);

        boolean todos = (categoria == null || categoria.equalsIgnoreCase("Todos"));
        String sqlQuery = todos
                ? "select * from Producto"
                : "select * from Producto where Categoria = '" + categoria + "'";

        db.Sentencia(sqlQuery);
        List<Producto> cache = leerCursor(db.getCursor());

        if (!cache.isEmpty()) {
            cb.onProductos(cache);
            return;
        }

        String apiCategoria = todos ? null : categoria;
        ApiClient.getService().listarProductos(apiCategoria).enqueue(new Callback<ApiResponse<List<Producto>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Producto>>> call,
                                   @NonNull Response<ApiResponse<List<Producto>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Producto> datos = response.body().getData();
                    guardarEnSqlite(db, datos);
                    cb.onProductos(datos);
                } else {
                    cb.onError("Sin respuesta del servidor");
                }
            }
            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<Producto>>> call, @NonNull Throwable t) {
                cb.onError(t.getMessage());
            }
        });
    }
    public void refrescar(Context ctx, ProductosCallback cb) {
        Db db = new Db(ctx);
        db.Delete("Producto", null);

        ApiClient.getService().listarProductos(null).enqueue(new Callback<ApiResponse<List<Producto>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Producto>>> call,
                                   @NonNull Response<ApiResponse<List<Producto>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Producto> datos = response.body().getData();
                    guardarEnSqlite(db, datos);
                    cb.onProductos(datos);
                } else {
                    cb.onError("Error al refrescar");
                }
            }
            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<Producto>>> call, @NonNull Throwable t) {
                cb.onError(t.getMessage());
            }
        });
    }
    private List<Producto> leerCursor(Cursor c) {
        List<Producto> lista = new ArrayList<>();
        while (c.moveToNext()) {
            Producto p = new Producto();
            p.setIdProducto(c.getInt(c.getColumnIndexOrThrow("id")));
            p.setNombre(c.getString(c.getColumnIndexOrThrow("Detalle")));
            p.setDescripcion(c.getString(c.getColumnIndexOrThrow("Descripcion")));
            p.setPrecio(c.getDouble(c.getColumnIndexOrThrow("Precio")));
            p.setCategoria(c.getString(c.getColumnIndexOrThrow("Categoria")));
            p.setEsNuevo(c.getInt(c.getColumnIndexOrThrow("Nuevo")) == 1);
            p.setEsFrecuente(c.getInt(c.getColumnIndexOrThrow("Frecuente")) == 1);
            p.setImagenUrl(c.getString(c.getColumnIndexOrThrow("ImagenUrl")));
            p.setTamanoMl(c.getInt(c.getColumnIndexOrThrow("TamanoMl")));
            lista.add(p);
        }
        c.close();
        return lista;
    }
    private void guardarEnSqlite(Db db, List<Producto> lista) {
        if (lista == null) return;
        for (Producto p : lista) {
            ContentValues v = new ContentValues();
            v.put("id",          p.getIdProducto());
            v.put("Detalle",     p.getNombre());
            v.put("Descripcion", p.getDescripcion());
            v.put("Precio",      p.getPrecio());
            v.put("Categoria",   p.getCategoria());
            v.put("Nuevo",       p.isEsNuevo()     ? 1 : 0);
            v.put("Frecuente",   p.isEsFrecuente() ? 1 : 0);
            v.put("ImagenUrl",   p.getImagenUrl());
            v.put("TamanoMl",    p.getTamanoMl());

            int filas = db.Update("Producto", v, "id = " + p.getIdProducto());
            if (filas == 0) {
                db.Insert("Producto", v);
            }
        }
    }
}
