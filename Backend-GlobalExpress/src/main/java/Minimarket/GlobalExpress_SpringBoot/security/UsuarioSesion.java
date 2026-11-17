package Minimarket.GlobalExpress_SpringBoot.security;

/** Datos del usuario que hizo la petición (se obtienen del token, no de lo que envía el React). */
public record UsuarioSesion(Integer idUsuario, String username, String nombre, String rol) {
}
