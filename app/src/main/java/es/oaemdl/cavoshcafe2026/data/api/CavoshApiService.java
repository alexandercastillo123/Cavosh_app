package es.oaemdl.cavoshcafe2026.data.api;

import es.oaemdl.cavoshcafe2026.data.model.*;
import retrofit2.Call;
import retrofit2.http.*;
import java.util.List;

public interface CavoshApiService {

    // Auth & Perfil
    @POST("api/auth/login")
    Call<ApiResponse<Usuario>> login(@Body LoginRequest request);

    @POST("api/auth/registrar")
    Call<ApiResponse<Usuario>> registrar(@Body RegisterRequest request);

    @GET("api/auth/perfil/{idUsuario}")
    Call<ApiResponse<Usuario>> obtenerPerfil(@Path("idUsuario") int idUsuario);

    @PUT("api/auth/perfil/{idUsuario}/preferencias")
    Call<ApiResponse<Usuario>> actualizarPreferencias(
            @Path("idUsuario") int idUsuario,
            @Query("notificaciones") Boolean notificaciones,
            @Query("ubicacion") Boolean ubicacion
    );

    // Productos
    @GET("api/productos")
    Call<ApiResponse<List<Producto>>> listarProductos(@Query("categoria") String categoria);

    @GET("api/productos/nuevos")
    Call<ApiResponse<List<Producto>>> listarNuevos();

    @GET("api/productos/frecuentes")
    Call<ApiResponse<List<Producto>>> listarFrecuentes();

    @GET("api/productos/buscar")
    Call<ApiResponse<List<Producto>>> buscarProductos(@Query("q") String query);

    @GET("api/productos/{id}")
    Call<ApiResponse<Producto>> obtenerProducto(@Path("id") int id);

    // Sucursales
    @GET("api/sucursales")
    Call<ApiResponse<List<Sucursal>>> listarSucursales();

    @GET("api/sucursales/ciudad")
    Call<ApiResponse<List<Sucursal>>> buscarSucursalesPorCiudad(@Query("ciudad") String ciudad);

    // Favoritos
    @GET("api/favoritos/{idUsuario}")
    Call<ApiResponse<List<Producto>>> listarFavoritos(@Path("idUsuario") int idUsuario);

    @POST("api/favoritos/toggle")
    Call<ApiResponse<Boolean>> toggleFavorito(
            @Query("idUsuario") int idUsuario,
            @Query("idProducto") int idProducto
    );

    @GET("api/favoritos/check")
    Call<ApiResponse<Boolean>> esFavorito(
            @Query("idUsuario") int idUsuario,
            @Query("idProducto") int idProducto
    );

    // Carrito
    @POST("api/carrito")
    Call<ApiResponse<CarritoItem>> agregarAlCarrito(@Body CarritoItemRequest request);

    @GET("api/carrito/{idUsuario}")
    Call<ApiResponse<CarritoResumen>> obtenerCarrito(@Path("idUsuario") int idUsuario);

    @PUT("api/carrito/{idCarrito}/usuario/{idUsuario}")
    Call<ApiResponse<CarritoItem>> actualizarCantidadCarrito(
            @Path("idCarrito") int idCarrito,
            @Path("idUsuario") int idUsuario,
            @Query("cantidad") int cantidad
    );

    @DELETE("api/carrito/{idCarrito}/usuario/{idUsuario}")
    Call<ApiResponse<Void>> eliminarDelCarrito(
            @Path("idCarrito") int idCarrito,
            @Path("idUsuario") int idUsuario
    );

    @DELETE("api/carrito/vaciar/{idUsuario}")
    Call<ApiResponse<Void>> vaciarCarrito(@Path("idUsuario") int idUsuario);

    // Cupones
    @POST("api/cupones/validar")
    Call<ApiResponse<CuponResponse>> validarCupon(@Body CuponValidateRequest request);

    // Pedidos
    @POST("api/pedidos")
    Call<ApiResponse<Pedido>> crearPedido(@Body PedidoCreateRequest request);

    @GET("api/pedidos/{id}")
    Call<ApiResponse<Pedido>> obtenerPedido(@Path("id") int id);

    @GET("api/pedidos/tracking/{numeroPedido}")
    Call<ApiResponse<Pedido>> obtenerPedidoTracking(@Path("numeroPedido") String numeroPedido);

    @GET("api/pedidos/usuario/{idUsuario}")
    Call<ApiResponse<List<Pedido>>> listarPedidosUsuario(@Path("idUsuario") int idUsuario);
}
