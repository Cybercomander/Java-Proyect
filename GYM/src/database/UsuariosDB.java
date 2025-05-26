package database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;

public class UsuariosDB implements Serializable
{
	private static final String ARCHIVO_USUARIOS = "Usuarios.dat";
	private static final int SIZE_USUARIO = 20; // caracteres
	private static final int SIZE_PASSWORD = 20; // caracteres
	private static final int SIZE_REGISTRO = SIZE_USUARIO * 2 + SIZE_PASSWORD * 2; // 2 bytes por char

	private static UsuariosDB instancia;

	public static UsuariosDB getInstancia()
	{
		if (instancia == null)
			instancia = new UsuariosDB();

		return instancia;
	}

	private UsuariosDB() {}

	public boolean agregarUsuario(String usuario, String password) throws IOException
	{
		// Validaciones de usuario y contraseña
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

	private void escribirCadenaFija(RandomAccessFile archivo, String string, int size) throws IOException
	{
		StringBuilder stringBuilder = new StringBuilder(string);
		stringBuilder.setLength(size);
		archivo.writeChars(stringBuilder.toString());
	}

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