package br.com.projetousuarios.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.com.projetousuarios.jdbc.ConnectionFactory;
import br.com.projetousuarios.model.Produto;

import java.util.ArrayList;
import java.util.List;

public class ProdutoDao {
	private Connection connection;

	public ProdutoDao() throws SQLException {
		this.connection = ConnectionFactory.getConnection();
	}
	
	public void apagar(Produto produto) throws SQLException {
		String sql = "delete from produto where idproduto=?";
		PreparedStatement stmt = this.connection.prepareStatement(sql);
		stmt.setLong(1, produto.getIdProduto());
		stmt.execute();
		stmt.close();
	}
	
	
	
	public void alterar(Produto produto) throws SQLException {
		String sql = "update produto set descricao=?,  quantidade =?, preco =? where idproduto=?";
		PreparedStatement stmt = this.connection.prepareStatement(sql);
		stmt.setString(1, produto.getDescricao());
		stmt.setInt(2, produto.getQuantidade());
		stmt.setDouble(3, produto.getPreco());
		stmt.setLong(3, produto.getIdProduto());
		stmt.execute();
		stmt.close();
	}
	
	public int quantidadeProdutos() throws SQLException {
		int quantProdutos = 0;
		String sql = "select count(*) as total from produto";
		Statement stmt = this.connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		if (rs.next()) {
			quantProdutos = rs.getInt(1);
        }
		stmt.close();
		return quantProdutos;
	}
	
	public void adicionar(Produto produto) throws SQLException {				
		String sql = "insert into produto (descricao,quantidade,preco) values (?,?,?)";
		PreparedStatement stmt = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		stmt.setString(1, produto.getDescricao());		
		stmt.setInt(2, produto.getQuantidade());
		stmt.setDouble(3, produto.getPreco());
		stmt.execute();
		ResultSet rs = stmt.getGeneratedKeys();
		if (rs.next()) {
            Long idProduto = rs.getLong(1);
            produto.setIdProduto(idProduto);
        }
		stmt.close();
	}
	
	public List<Produto> paginacaoProduto(int limitePorPagina, int deslocamento) throws SQLException {
		List<Produto> produtos = new ArrayList<Produto>();
		String sql = "select * from produto order by idproduto limit ? offset ?";
		PreparedStatement stmt = this.connection.prepareStatement(sql);
		stmt.setInt(1, limitePorPagina);
		stmt.setInt(2, deslocamento);
		ResultSet resultSet = stmt.executeQuery();
		while (resultSet.next()) {			
			Long idProduto = resultSet.getLong("idproduto");
			String descricao = resultSet.getString("descricao");
			int  quantidade = resultSet.getInt("quantidade");
			double preco = resultSet.getDouble("preco");
			Produto produto = new Produto();
			produto.setIdProduto(idProduto);
			produto.setDescricao(descricao);
			produto.setQuantidade(quantidade);
			produto.setPreco(preco);
			produtos.add(produto);
		}
		stmt.close();
		return produtos;
	}
	
	public List<Produto> listarCurso() throws SQLException {
		List<Produto> produtos = new ArrayList<Produto>();
		String sql = "select * from produto";
		Statement stmt = this.connection.createStatement();
		ResultSet resultSet = stmt.executeQuery(sql);
		while (resultSet.next()) {			
			Long idProduto = resultSet.getLong("idproduto");
			String descricao = resultSet.getString("descricao");
			int  quantidade = resultSet.getInt("quantidade");
			double preco = resultSet.getDouble("preco");
			Produto produto = new Produto();
			produto.setIdProduto(idProduto);
			produto.setDescricao(descricao);
			produto.setQuantidade(quantidade);
			produto.setPreco(preco);
			produtos.add(produto);
		}
		stmt.close();
		return produtos;
	}
}
