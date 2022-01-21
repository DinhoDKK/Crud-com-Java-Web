package br.com.projetousuarios.model;

public class Produto {

	private Long idProduto;
	private String descricao;
	private int quantidade;
	private double preco;

	public Produto() {

	}

	public Produto(String descricao, int quantidade, double preco, boolean onLine) {
		this.descricao = descricao;
		this.quantidade = quantidade;
		this.preco = preco;
	}

	public Produto(long idProduto, String descricao, int quantidade, double preco, boolean onLine) {
		this.idProduto = idProduto;
		this.descricao = descricao;
		this.quantidade = quantidade;
		this.preco = preco;
	}

	public Long getIdProduto() {
		return idProduto;
	}
	public void setIdProduto(Long idProduto) {
		this.idProduto = idProduto;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public double getPreco() {
		return preco;
	}

	public void setPreco(double preco) {
		this.preco = preco;

	}
	
	}


