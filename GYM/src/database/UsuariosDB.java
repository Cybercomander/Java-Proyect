package database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;

/**
 * CLASE QUE GESTIONA EL ACCESO Y ALMACENAMIENTO DE USUARIOS EN EL SISTEMA
 * UTILIZA ARCHIVOS DE ACCESO ALEATORIO PARA GUARDAR USUARIOS Y CONTRASENAS
 */
public class UsuariosDB implements Serializable
{
	/** NOMBRE DEL ARCHIVO DONDE SE GUARDAN LOS USUARIOS */
	private static final String ARCHIVO_USUARIOS = "Usuarios.dat";
	/** TAMANO MAXIMO DEL NOMBRE DE USUARIO EN CARACTERES */
	private static final int SIZE_USUARIO = 20; // CARACTERES
	/** TAMANO MAXIMO DE LA CONTRASENA EN CARACTERES */
	private static final int SIZE_PASSWORD = 20; // CARACTERES
	/** TAMANO TOTAL DE UN REGISTRO DE USUARIO EN BYTES */
	private static final int SIZE_REGISTRO = SIZE_USUARIO * 2 + SIZE_PASSWORD * 2; // 2 BYTES POR CHAR

	/** INSTANCIA UNICA DE USUARIOSDB (PATRON SINGLETON) */
	private static UsuariosDB instancia;

	/**
	 * OBTIENE LA INSTANCIA UNICA DE USUARIOSDB (SINGLETON)
	 * @return INSTANCIA DE USUARIOSDB
	 */
	public static UsuariosDB getInstancia()
	{
		if (instancia == null)
			instancia = new UsuariosDB();

		return instancia;
	}

	/**
	 * CONSTRUCTOR PRIVADO PARA IMPEDIR LA CREACION DE MULTIPLES INSTANCIAS
	 */
	private UsuariosDB() {}

	/**
	 * AGREGA UN NUEVO USUARIO AL ARCHIVO DE USUARIOS
	 * @param usuario NOMBRE DE USUARIO
	 * @param password CONTRASENA DEL USUARIO
	 * @return TRUE SI SE AGREGO CORRECTAMENTE, FALSE EN CASO CONTRARIO
	 * @throws IOException SI OCURRE UN ERROR DE ESCRITURA
	 */
	public boolean agregarUsuario(String usuario, String password) throws IOException
	{
		// VALIDACIONES DE USUARIO Y CONTRASENA
		if (usuario == null || usuario.trim().isEmpty() || usuario.contains(" "))
			return false;

		if (password == null || password.isEmpty())
			return false;

		if (usuario.length() > SIZE_USUARIO)
			usuario = usuario.substring(0, SIZE_USUARIO);

		if (password.length() > SIZE_PASSWORD)
			password = password.substring(0, SIZE_PASSWORD);

		if (existeUsuario(usuario))
			return false;

		try (RandomAccessFile archivo = new RandomAccessFile(ARCHIVO_USUARIOS, "rw"))
		{
			archivo.seek(archivo.length());
			escribirCadenaFija(archivo, usuario, SIZE_USUARIO);
			escribirCadenaFija(archivo, password, SIZE_PASSWORD);
		}
		catch (IOException e)
		{
			return false;
		}
		return true;
	}

	/**
	 * VALIDA SI UN USUARIO Y CONTRASENA EXISTEN EN EL ARCHIVO
	 * @param usuario NOMBRE DE USUARIO
	 * @param password CONTRASENA
	 * @return TRUE SI EL USUARIO Y CONTRASENA SON CORRECTOS, FALSE EN CASO CONTRARIO
	 * @throws IOException SI OCURRE UN ERROR DE LECTURA
	 */
	public boolean validarUsuario(String usuario, String password) throws IOException
	{
		if (usuario == null || usuario.trim().isEmpty() || password == null)
			return false;

		try (RandomAccessFile archivo = new RandomAccessFile(ARCHIVO_USUARIOS, "r"))
		{
			long registros = archivo.length() / SIZE_REGISTRO;
			for (int i = 0; i < registros; i++)
			{
				String usuarios = leerCadenaFija(archivo, SIZE_USUARIO);
				String passwords = leerCadenaFija(archivo, SIZE_PASSWORD);
				if (usuarios.trim().equals(usuario) && passwords.trim().equals(password))
				{
					return true;
				}
			}
		}
		catch (IOException e)
		{
			return false;
		}
		return false;
	}

	/**
	 * VERIFICA SI UN USUARIO YA EXISTE EN EL ARCHIVO
	 * @param usuario NOMBRE DE USUARIO A BUSCAR
	 * @return TRUE SI EL USUARIO EXISTE, FALSE EN CASO CONTRARIO
	 * @throws IOException SI OCURRE UN ERROR DE LECTURA
	 */
	public boolean existeUsuario(String usuario) throws IOException
	{
		if (usuario == null || usuario.trim().isEmpty())
			return false;

		try (RandomAccessFile archivo = new RandomAccessFile(ARCHIVO_USUARIOS, "r"))
		{
			long registros = archivo.length() / SIZE_REGISTRO;
			for (int i = 0; i < registros; i++)
			{
				String usuarios = leerCadenaFija(archivo, SIZE_USUARIO);
				archivo.skipBytes(SIZE_PASSWORD * 2);

				if (usuarios.trim().equals(usuario))
					return true;
			}
		}
		catch (FileNotFoundException e)
		{
			return false;
		}
		catch (IOException e)
		{
			return false;
		}
		return false;
	}

	/**
	 * MUESTRA TODOS LOS USUARIOS REGISTRADOS EN CONSOLA
	 */
	public void mostrarUsuariosEnConsola()
	{
		try (RandomAccessFile archivo = new RandomAccessFile(ARCHIVO_USUARIOS, "r"))
		{
			long registros = archivo.length() / SIZE_REGISTRO;
			System.out.println("Usuarios registrados en el sistema:");
			for (int i = 0; i < registros; i++)
			{
				String usuarios = leerCadenaFija(archivo, SIZE_USUARIO).trim();
				String passwords = leerCadenaFija(archivo, SIZE_PASSWORD).trim();
				System.out.println("Usuario: " + usuarios + " | Contraseña: " + passwords);
			}
		}
		catch (IOException e)
		{
			System.out.println("No hay usuarios registrados o no se pudo leer el archivo.");
		}
	}

	/**
	 * ESCRIBE UNA CADENA DE LONGITUD FIJA EN EL ARCHIVO, RELLENANDO CON ESPACIOS SI ES NECESARIO
	 * @param archivo ARCHIVO DE ACCESO ALEATORIO
	 * @param string CADENA A ESCRIBIR
	 * @param size TAMANO FIJO DE LA CADENA
	 * @throws IOException SI OCURRE UN ERROR DE ESCRITURA
	 */
	private void escribirCadenaFija(RandomAccessFile archivo, String string, int size) throws IOException
	{
		StringBuilder stringBuilder = new StringBuilder(string);
		stringBuilder.setLength(size);
		archivo.writeChars(stringBuilder.toString());
	}

	/**
	 * LEE UNA CADENA DE LONGITUD FIJA DESDE EL ARCHIVO
	 * @param archivo ARCHIVO DE ACCESO ALEATORIO
	 * @param size TAMANO FIJO DE LA CADENA
	 * @return CADENA LEIDA DEL ARCHIVO
	 * @throws IOException SI OCURRE UN ERROR DE LECTURA
	 */
	private String leerCadenaFija(RandomAccessFile archivo, int size) throws IOException
	{
		char[] chars = new char[size];
		for (int i = 0; i < size; i++)
		{
			chars[i] = archivo.readChar();
		}
		return new String(chars);
	}
}