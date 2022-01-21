package br.com.projetousuarios.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.projetousuarios.jdbc.ConnectionFactory;
import br.com.projetousuarios.model.Usuario;

public class UsuarioDAO {

	private Connection connection;

	public UsuarioDAO() throws SQLException {
		this.connection = ConnectionFactory.getConnection();
	}
	
	public boolean login(Usuario usuario) throws SQLException {
		boolean logado = false;
		String sql = "select * from usuario where email = ? and senha = ?";
		PreparedStatement stmt = this.connection.prepareStatement(sql);
		stmt.setString(1, usuario.getEmail());
		stmt.setString(2, usuario.getSenha());
		ResultSet resultSet = stmt.executeQuery();
		while (resultSet.next()) {			
			Long idUsuario = resultSet.getLong("id_usuario");
			String nome = resultSet.getString("nome");
			String sobreNome = resultSet.getString("sobrenome");
			usuario.setIdUsuario(idUsuario);
			usuario.setNome(nome);
			usuario.setSobreNome(sobreNome);
			logado = true;
		}
		stmt.close();
		return logado;
	}
	
	public void adicionar(Usuario usuario) throws SQLException {				
		String sql = "insert into usuario (nome,sobrenome,email,senha) values (?,?,?,?)";
		PreparedStatement stmt = this.connection.prepareStatement(sql);
		stmt.setString(1, usuario.getNome());		
		stmt.setString(2, usuario.getSobreNome());
		stmt.setString(3, usuario.getEmail());
		stmt.setString(4, usuario.getSenha());
		stmt.execute();
		stmt.close();
	}
}
