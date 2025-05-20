package base_datos;

import java.io.*;
import java.util.ArrayList;

public class UsuariosDB {
    private static final String ARCHIVO_USUARIOS = "Usuarios.dat";
    private static final int TAM_USUARIO = 20; // caracteres
    private static final int TAM_CONTRA = 20; // caracteres
    private static final int TAM_REGISTRO = TAM_USUARIO * 2 + TAM_CONTRA * 2; // 2 bytes por char

    private static UsuariosDB instancia;

    public static UsuariosDB getInstancia() {
        if (instancia == null) instancia = new UsuariosDB();
        return instancia;
    }

    private UsuariosDB() {}

    public boolean agregarUsuario(String usuario, String contrasena) throws IOException {
        // Validaciones de usuario y contraseña
        if (usuario == null || usuario.trim().isEmpty() || usuario.contains(" ")) return false;
        if (contrasena == null || contrasena.isEmpty()) return false;
        if (usuario.length() > TAM_USUARIO) usuario = usuario.substring(0, TAM_USUARIO);
        if (contrasena.length() > TAM_CONTRA) contrasena = contrasena.substring(0, TAM_CONTRA);
        if (existeUsuario(usuario)) return false;
        try (RandomAccessFile raf = new RandomAccessFile(ARCHIVO_USUARIOS, "rw")) {
            raf.seek(raf.length());
            escribirCadenaFija(raf, usuario, TAM_USUARIO);
            escribirCadenaFija(raf, contrasena, TAM_CONTRA);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean validarUsuario(String usuario, String contrasena) throws IOException {
        if (usuario == null || usuario.trim().isEmpty() || contrasena == null) return false;
        try (RandomAccessFile raf = new RandomAccessFile(ARCHIVO_USUARIOS, "r")) {
            long registros = raf.length() / TAM_REGISTRO;
            for (int i = 0; i < registros; i++) {
                String u = leerCadenaFija(raf, TAM_USUARIO);
                String c = leerCadenaFija(raf, TAM_CONTRA);
                if (u.trim().equals(usuario) && c.trim().equals(contrasena)) {
                    return true;
                }
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    public boolean existeUsuario(String usuario) throws IOException {
        if (usuario == null || usuario.trim().isEmpty()) return false;
        try (RandomAccessFile raf = new RandomAccessFile(ARCHIVO_USUARIOS, "r")) {
            long registros = raf.length() / TAM_REGISTRO;
            for (int i = 0; i < registros; i++) {
                String u = leerCadenaFija(raf, TAM_USUARIO);
                raf.skipBytes(TAM_CONTRA * 2);
                if (u.trim().equals(usuario)) return true;
            }
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    public void mostrarUsuariosEnConsola() {
        try (RandomAccessFile raf = new RandomAccessFile(ARCHIVO_USUARIOS, "r")) {
            long registros = raf.length() / TAM_REGISTRO;
            System.out.println("Usuarios registrados en el sistema:");
            for (int i = 0; i < registros; i++) {
                String u = leerCadenaFija(raf, TAM_USUARIO).trim();
                String c = leerCadenaFija(raf, TAM_CONTRA).trim();
                System.out.println("Usuario: " + u + " | Contraseña: " + c);
            }
        } catch (IOException e) {
            System.out.println("No hay usuarios registrados o no se pudo leer el archivo.");
        }
    }

    private void escribirCadenaFija(RandomAccessFile raf, String s, int tam) throws IOException {
        StringBuilder sb = new StringBuilder(s);
        sb.setLength(tam);
        raf.writeChars(sb.toString());
    }

    private String leerCadenaFija(RandomAccessFile raf, int tam) throws IOException {
        char[] chars = new char[tam];
        for (int i = 0; i < tam; i++) {
            chars[i] = raf.readChar();
        }
        return new String(chars);
    }
}
